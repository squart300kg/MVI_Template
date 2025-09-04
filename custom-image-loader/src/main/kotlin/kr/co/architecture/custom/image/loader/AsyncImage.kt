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
      userAgent = "Custom-Image-Loader-RawHttp11/0.1",
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
        MemoryImageCache.put(url, imageBitmap)
        value = imageBitmap

        // (a) fresh → 바로 종료
        if (cached.meta.isFresh(now)) return@produceState

        // (b) stale-while-revalidate → 보여주면서 백그라운드 재검증
        if (cached.meta.canServeStaleWhileRevalidate(now)) {
          withContext(Dispatchers.IO) {
            revalidateIfNeeded(
              client = client,
              url = url,
              diskCache = diskCache,
              meta = cached.meta,
              onUpdated = { newBytes ->
                val bitmap = BitmapFactory.decodeByteArray(newBytes, 0, newBytes.size)
                if (bitmap != null) {
                  val imageBitmap = bitmap.asImageBitmap()
                  MemoryImageCache.put(url, imageBitmap)
                  value = imageBitmap
                }
              }
            )
          }
          return@produceState
        }
      }
    }

    // 3) 네트워크 로드: 조건부 요청(ETag / Last-Modified) 적용
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
              // 본문은 없지만 신선도만 갱신
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
          // 에러 시 stale-if-error 허용이면 기존 캐시를 계속 사용
          if (cached?.meta?.canServeStaleOnError(now) == true) {
            // value는 이미 캐시 이미지로 세팅됨
          } else value = null
        },
        onResponseException = {
          if (cached?.meta?.canServeStaleOnError(now) == true) {
            // 그대로 유지
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
  meta: ImageDiskCache.Meta,
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
