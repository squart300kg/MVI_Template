package kr.co.architecture.custom.image.loader.domain

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kr.co.architecture.custom.http.client.HttpStatusCode.NOT_MODIFIED
import kr.co.architecture.custom.http.client.HttpStatusCode.SUCCESS
import kr.co.architecture.custom.image.loader.network.HttpClient
import java.util.Locale

class ImageEngine(
  private val imageMemoryCache: ImageMemoryCache?,
  private val imageDiskCache: ImageDiskCache?,
  private val httpClient: HttpClient
) {

  /** 캐시 상태에 맞춰 1~2번 이미지를 흘려보냄 (ex. SWR이면 stale → fresh 순서로 2회 emit) */
  fun imageFlow(url: String): Flow<ImageBitmap?> =
    flow {
      val now = System.currentTimeMillis()

      // 1) 메모리 히트
      imageMemoryCache?.get(url)?.let { emit(it); return@flow }

      // 2) 디스크 히트 (fresh or SWR)
      val cachedDiskEntry = imageDiskCache?.getEntry(url)
      if (cachedDiskEntry != null) {
        val imageBitmap = BitmapFactory.decodeByteArray(cachedDiskEntry.bytes, 0, cachedDiskEntry.bytes.size)?.asImageBitmap()
        if (imageBitmap != null) {
          if (cachedDiskEntry.meta.isFresh(now)) {
            imageMemoryCache?.put(url, imageBitmap)
            emit(imageBitmap)
            return@flow
          }

          // MaxAge ~ SWR 확인.
          //  true -> 캐싱된 이미지를 메모리 캐싱 후, 다운스트림. 그 후, SWR 재검증
          //  false -> 네트워크 호출
          if (cachedDiskEntry.meta.canServeStaleWhileRevalidate(now)) {
            imageMemoryCache?.put(url, imageBitmap)
            emit(imageBitmap)
            val revalidatedImageBytes = revalidateWhenSWR(url, cachedDiskEntry.meta)
            if (revalidatedImageBytes != null) {
              val imageBitmap = BitmapFactory.decodeByteArray(revalidatedImageBytes, 0, revalidatedImageBytes.size)?.asImageBitmap()
              if (imageBitmap != null) {
                imageMemoryCache?.put(url, imageBitmap)
                emit(imageBitmap)
              }
            }
            return@flow
          }
        }
      }

      // 3) 네트워크(조건부 요청) – 디스크 메타가 있으면 etag/lastModified 붙임
      val requestHeader = mutableMapOf<String, String>()
      cachedDiskEntry?.meta?.etag?.let { requestHeader["If-None-Match"] = it }
      cachedDiskEntry?.meta?.lastModified?.let { requestHeader["If-Modified-Since"] = it }

      val apiResponse = httpClient.get(
        url = url,
        header = requestHeader
      )

      when {
        // 200: 본문 저장 → 디코딩 후 emit
        apiResponse.code == SUCCESS && apiResponse.body != null -> {
          val imageBitmap = BitmapFactory.decodeByteArray(apiResponse.body, 0, apiResponse.body.size)?.asImageBitmap() ?: return@flow
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
          return@flow
        }
        // 304: 메타만 갱신하고, 캐시 본문 디코드 후 emit
        apiResponse.code == NOT_MODIFIED && cachedDiskEntry != null -> {
          val imageBitmap = BitmapFactory.decodeByteArray(cachedDiskEntry.bytes, 0, cachedDiskEntry.bytes.size)?.asImageBitmap() ?: return@flow
          imageMemoryCache?.put(url, imageBitmap)
          imageDiskCache.updateMetaOn304(
            url = url,
            header = apiResponse.header
          )
          emit(imageBitmap)
          return@flow
        }
        // 에러: stale-if-error 허용되면 스테일 폴백
        cachedDiskEntry?.meta?.canServeStaleOnError() == true -> {
          val imageBitmap = BitmapFactory.decodeByteArray(cachedDiskEntry.bytes, 0, cachedDiskEntry.bytes.size)?.asImageBitmap() ?: return@flow
          imageMemoryCache?.put(url, imageBitmap)
          emit(imageBitmap)
        }
        else -> emit(null)
      }
    }

  /** SWR 재검증: 200이면 디스크/메모리 갱신용 바디 반환, 304면 null */
  private suspend fun revalidateWhenSWR(url: String, meta: Meta): ByteArray? {
    val header = buildMap {
      meta.etag?.let { put("If-None-Match", it) }
      meta.lastModified?.let { put("If-Modified-Since", it) }
    }
    val apiResponse = httpClient.get(url = url, header = header)
    return when {
      apiResponse.code == SUCCESS && apiResponse.body != null -> {
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
      apiResponse.code == NOT_MODIFIED -> {
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
