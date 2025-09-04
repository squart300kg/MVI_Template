package kr.co.architecture.custom.image.loader.domain.mediator

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kr.co.architecture.custom.http.client.HttpHeaderConstants
import kr.co.architecture.custom.http.client.HttpStatusCode
import kr.co.architecture.custom.image.loader.domain.model.Meta
import kr.co.architecture.custom.image.loader.network.HttpClient
import java.util.Locale

class ImageMediatorImpl(
  private val imageMemoryCache: ImageMemoryCache?,
  private val imageDiskCache: ImageDiskCache?,
  private val httpClient: HttpClient
): ImageMediator {

  /** 캐시 상태에 맞춰 1~2번 이미지를 흘려보냄 (ex. SWR이면 stale → fresh 순서로 2회 emit) */
  override fun imageFlow(url: String): Flow<ImageBitmap?> =
    flow {
      val now = System.currentTimeMillis()

      // 1) 메모리 캐시
      imageMemoryCache?.get(url)?.let { emit(it); return@flow }

      // 2) 디스크 캐시
      val cachedDiskEntry = imageDiskCache?.getEntry(url)
      if (cachedDiskEntry != null) {
        val diskCachedImageBitmap = cachedDiskEntry.bytes.decodeToImageBitmap()
        if (diskCachedImageBitmap != null) {
          if (cachedDiskEntry.meta.isFresh(now)) {
            imageMemoryCache?.put(url, diskCachedImageBitmap)
            emit(diskCachedImageBitmap)
            return@flow
          }

          // 2-1) MaxAge ~ SWR 확인.
          //        true -> 캐싱된 이미지를 메모리 캐싱 후, 다운스트림. 그 후, SWR 재검증
          //        false -> 네트워크 호출
          if (cachedDiskEntry.meta.canServeStaleWhileRevalidate(now)) {
            imageMemoryCache?.put(url, diskCachedImageBitmap)
            emit(diskCachedImageBitmap)

            val freshImageBytes = revalidateWhenSWR(url, cachedDiskEntry.meta) ?: return@flow
            val freshImageBitmap = freshImageBytes.decodeToImageBitmap() ?: return@flow
            imageMemoryCache?.put(url, freshImageBitmap)
            emit(freshImageBitmap)
            return@flow
          }
        }
      }

      // 3) 네트워크 캐시 – 디스크 메타가 있으면 etag/lastModified 붙임
      val requestHeader = mutableMapOf<String, String>()
      cachedDiskEntry?.meta?.etag?.let {
        requestHeader[HttpHeaderConstants.Property.IF_NONE_MATCH] = it
      }
      cachedDiskEntry?.meta?.lastModified?.let {
        requestHeader[HttpHeaderConstants.Property.IF_MODIFIED_SINCE] = it
      }

      val apiResponse = httpClient.get(
        url = url,
        header = requestHeader
      )
      when {
        // 200: 디코딩 -> 메모리/디스크 캐싱 -> 이미지 발행
        apiResponse.code == HttpStatusCode.SUCCESS && apiResponse.body != null -> {
          val imageBitmap = apiResponse.body.decodeToImageBitmap() ?: return@flow
          imageMemoryCache?.put(url, imageBitmap)
          imageDiskCache?.putHttpResponse(
            url = url,
            body = apiResponse.body,
            header = mergedHeader(
              requestHeader = requestHeader,
              responseHeader = apiResponse.header
            )
          )
          emit(imageBitmap)
        }
        // 304: 디코딩 -> 메모리 캐싱/디스크 캐싱(메타만 갱신) -> 이미지 발행
        apiResponse.code == HttpStatusCode.NOT_MODIFIED && cachedDiskEntry != null -> {
          val imageBitmap = cachedDiskEntry.bytes.decodeToImageBitmap() ?: return@flow
          imageMemoryCache?.put(url, imageBitmap)
          imageDiskCache.updateMetaOn304(
            url = url,
            header = apiResponse.header
          )
          emit(imageBitmap)
        }
        // 서버 에러(eg., 40x, 50x..): soe 범위 내, 메모리 캐싱 -> 이미지 발행
        cachedDiskEntry?.meta?.canServeStaleOnError() == true -> {
          val imageBitmap = cachedDiskEntry.bytes.decodeToImageBitmap() ?: return@flow
          imageMemoryCache?.put(url, imageBitmap)
          emit(imageBitmap)
        }
        else -> emit(null)
      }
    }.flowOn(Dispatchers.IO)

  private suspend fun ByteArray.decodeToImageBitmap(): ImageBitmap? =
    withContext(Dispatchers.Default) {
      BitmapFactory.decodeByteArray(this@decodeToImageBitmap, 0, size)?.asImageBitmap()
    }

  /** SWR 재검증: 200이면 디스크/메모리 갱신용 바디 반환, 304면 null */
  private suspend fun revalidateWhenSWR(url: String, meta: Meta): ByteArray? {
    val header = buildMap {
      meta.etag?.let { put(HttpHeaderConstants.Property.IF_NONE_MATCH, it) }
      meta.lastModified?.let { put(HttpHeaderConstants.Property.IF_MODIFIED_SINCE, it) }
    }
    val apiResponse = httpClient.get(url = url, header = header)
    return when {
      apiResponse.code == HttpStatusCode.SUCCESS && apiResponse.body != null -> {
        apiResponse.body.also {
          imageDiskCache?.putHttpResponse(
            url = url,
            body = apiResponse.body,
            header = mergedHeader(
              requestHeader = header,
              responseHeader = apiResponse.header
            )
          )
        }
      }
      apiResponse.code == HttpStatusCode.NOT_MODIFIED -> {
        null.also {
          imageDiskCache?.updateMetaOn304(url, apiResponse.header)
        }
      }
      else -> null
    }
  }

  private fun mergedHeader(
    requestHeader: Map<String, String>,
    responseHeader: Map<String, String>
  ): Map<String, String> =
    buildMap {
      putAll(requestHeader.mapKeys { it.key.lowercase(Locale.ROOT) })
      responseHeader.forEach { (k, v) -> put(k.lowercase(Locale.ROOT), v) }
    }
}