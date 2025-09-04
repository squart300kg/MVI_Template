package kr.co.architecture.custom.image.loader

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.co.architecture.custom.http.client.RawHttp11Client
import kr.co.architecture.custom.http.client.interceptor.CustomHttpLogger

@Composable
fun AsyncImage(
  modifier: Modifier = Modifier,
  placeholderContent: @Composable () -> Unit = {},
  url: String,
  contentScale: ContentScale = ContentScale.Crop,
  context: Context = LocalContext.current
) {
  // TODO: 의존주입 방법 고민해보기
  // 실제 앱에선 DI/CompositionLocal로 제공 권장
  val client = remember {
    RawHttp11Client(
      userAgent = "Custom-Image-Loader-RawHttp11",
      httpLogger = CustomHttpLogger()
    )
  }
  val diskCache = remember { ImageDiskCache.create(context, maxBytes = 64L * 1024 * 1024) }

  val imageState by produceState<ImageBitmap?>(initialValue = null, key1 = url) {
    // 1) 메모리 캐시
    MemoryImageCache[url]?.let { value = it; return@produceState }

    // 2) 디스크 캐시
    val cached = withContext(Dispatchers.IO) { diskCache.getEntry(url) }
    val now = System.currentTimeMillis()
    if (cached != null) {
      val bitmap = BitmapFactory.decodeByteArray(cached.bytes, 0, cached.bytes.size)
      if (bitmap != null) {
        val imageBitmap = bitmap.asImageBitmap()

        // (a) fresh → 바로 표시 후 종료
        if (cached.meta.isFresh(now)) {
          MemoryImageCache.put(url, imageBitmap)
          value = imageBitmap
          return@produceState
        }

        // (b) SWR 구간 → UI 먼저 표시하고 백그라운드 재검증
        if (cached.meta.canServeStaleWhileRevalidate(now)) {
          MemoryImageCache.put(url, imageBitmap)
          value = imageBitmap
          withContext(Dispatchers.IO) {
            revalidateIfNeeded(
              client = client,
              url = url,
              diskCache = diskCache,
              meta = cached.meta,
              onUpdated = { newBytes ->
                val bmp2 = BitmapFactory.decodeByteArray(newBytes, 0, newBytes.size)
                if (bmp2 != null) {
                  val img2 = bmp2.asImageBitmap()
                  MemoryImageCache.put(url, img2)
                  value = img2
                }
              }
            )
          }
          return@produceState
        }

        // (c) SWR 밖 → 스테일을 UI에 그리지 않고 네트워크로 진행
      }
    }

    // 3) 네트워크 로드(조건부 요청)
    withContext(Dispatchers.IO) {
      val headers = mutableMapOf<String, String>()
      cached?.meta?.etag?.let { headers["If-None-Match"] = it }
      cached?.meta?.lastModified?.let { headers["If-Modified-Since"] = it }

      client.callApi(
        method = "GET",
        url = url,
        headers = headers,
        onResponseSuccess = {
          when (code) {
            200 -> {
              val bitmap = BitmapFactory.decodeByteArray(body, 0, body.size)
              if (bitmap != null) {
                val imageBitmap = bitmap.asImageBitmap()
                MemoryImageCache.put(url, imageBitmap)
                value = imageBitmap
                diskCache.putHttpResponse(url, body, headers = headers.mapKeys { it.key.lowercase() } + this.headers)
              } else value = null
            }
            304 -> {
              diskCache.updateMetaOn304(url, headers = this.headers)
              val bitmap = cached?.bytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
              if (bitmap != null) {
                val imageBitmap = bitmap.asImageBitmap()
                MemoryImageCache.put(url, imageBitmap)
                value = imageBitmap
              }
            }
            else -> value = null
          }
        },
        onResponseError = {
          // 에러 시에만 stale-if-error 허용 범위면 스테일로 폴백
          if (cached?.meta?.canServeStaleOnError(now) == true) {
            val bmp = cached.bytes.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
            if (bmp != null) {
              val img = bmp.asImageBitmap()
              MemoryImageCache.put(url, img)
              value = img
            }
          } else value = null
        },
        onResponseException = {
          if (cached?.meta?.canServeStaleOnError(now) == true) {
            val bmp = cached.bytes.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
            if (bmp != null) {
              val img = bmp.asImageBitmap()
              MemoryImageCache.put(url, img)
              value = img
            }
          } else value = null
        }
      )
    }
  }

  // TODO: 로딩상태 분기
  //  1. 로딩 전
  //  2. 로딩 완료
  //  3. 로딩 실패
  imageState?.let { img ->
    Image(
      modifier = modifier,
      bitmap = img,
      contentDescription = null,
      contentScale = contentScale
    )
  } ?: placeholderContent()
}

private suspend fun revalidateIfNeeded(
  client: RawHttp11Client,
  url: String,
  diskCache: ImageDiskCache,
  meta: Meta,
  onUpdated: (ByteArray) -> Unit
) {
  val headers = mutableMapOf<String, String>()
  meta.etag?.let { headers["If-None-Match"] = it }
  meta.lastModified?.let { headers["If-Modified-Since"] = it }

  client.callApi(
    method = "GET",
    url = url,
    headers = headers,
    onResponseSuccess = {
      when (code) {
        200 -> {
          diskCache.putHttpResponse(url, body, headers = this.headers)
          onUpdated(body)
        }
        304 -> diskCache.updateMetaOn304(url, headers = this.headers)
      }
    },
    onResponseError = { /* 무시: stale-while-revalidate 상황 */ },
    onResponseException = { /* 무시 */ }
  )
}
