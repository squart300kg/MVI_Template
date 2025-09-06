package kr.co.architecture.custom.image.loader.domain.mediator

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.IF_MODIFIED_SINCE
import kr.co.architecture.custom.http.client.HttpHeaderConstants.Property.IF_NONE_MATCH
import kr.co.architecture.custom.http.client.HttpStatusCode.NOT_MODIFIED
import kr.co.architecture.custom.http.client.HttpStatusCode.SUCCESS
import kr.co.architecture.custom.image.loader.network.HttpClient
import kr.co.architecture.custom.image.loader.util.decodeToImageBitmap
import kr.co.architecture.custom.image.loader.util.mergedHeader

class ImageMediatorImpl(
  private val imageMemoryCache: ImageMemoryCache? = null,
  private val imageDiskCache: ImageDiskCache? = null,
  private val httpClient: HttpClient
) : ImageMediator {

  /** 캐시 상태에 맞춰 1~2번 이미지를 흘려보냄 (ex. SWR이면 stale → fresh 순서로 2회 emit) */
  override fun imageFlow(url: String): Flow<ImageState> =
    flow {
      val now = System.currentTimeMillis()

      // 1) 메모리 캐시 체크
      imageMemoryCache?.get(url)?.let { emit(ImageState.Success(it)); return@flow }

      // 2) 디스크 캐시 체크
      val cachedDiskEntry = imageDiskCache?.getCachedEntry(url)
      if (cachedDiskEntry != null) {
        val diskCachedImageBitmap = cachedDiskEntry.bytes.data.decodeToImageBitmap()

        if (diskCachedImageBitmap != null) {
          // 2-1) 캐싱된 디스크 이미지 히트.
          if (cachedDiskEntry.meta.isFresh(now)) {
            imageMemoryCache?.cache(
              key = url,
              image = diskCachedImageBitmap
            )
            emit(ImageState.Success(diskCachedImageBitmap))
            return@flow
          }

          // 2-2) cache-control: max-age ~ stale-while-revalidate 확인.
          //  true -> 기존 보유 이미지, 메모리 캐싱 및 다운스트림. 그 후, SWR 검증
          //  false -> 네트워크 호출
          if (cachedDiskEntry.meta.canServeStaleWhileRevalidate(now)) {
            imageMemoryCache?.cache(
              key = url,
              image = diskCachedImageBitmap
            )
            emit(ImageState.Success(diskCachedImageBitmap))

            // SWR 재검증: 200이면 디스크/메모리 갱신용 바디 반환, 304면 null
            val header = buildMap {
              cachedDiskEntry.meta.etag?.let { put(IF_NONE_MATCH, it) }
              cachedDiskEntry.meta.lastModified?.let { put(IF_MODIFIED_SINCE, it) }
            }
            val apiResponse = httpClient.get(
              url = url,
              header = header
            )
            when {
              apiResponse.code == SUCCESS && apiResponse.body != null -> {
                val refreshedImageBitmap = apiResponse.body.data.decodeToImageBitmap() ?: return@flow
                imageMemoryCache?.cache(
                  key = url,
                  image = refreshedImageBitmap
                )
                imageDiskCache.cacheBodyAndMeta(
                  url = url,
                  body = apiResponse.body.data,
                  header = header
                )
                emit(ImageState.Success(refreshedImageBitmap))
                return@flow
              }
              apiResponse.code == NOT_MODIFIED -> {
                imageDiskCache.cacheMeta(
                  url = url,
                  header = header
                )
                return@flow
              }
              else -> return@flow
            }
          }
        }
      }

      // 3) 네트워크 호출 및 캐싱 – 디스크 메타가 있으면 etag/lastModified 붙임
      val requestHeader = mutableMapOf<String, String>()
      cachedDiskEntry?.meta?.etag?.let {
        requestHeader[IF_NONE_MATCH] = it
      }
      cachedDiskEntry?.meta?.lastModified?.let {
        requestHeader[IF_MODIFIED_SINCE] = it
      }

      val apiResponse = httpClient.get(
        url = url,
        header = requestHeader
      )
      when {
        // 200: 디코딩 -> 메모리/디스크 캐싱 -> 이미지 발행
        apiResponse.code == SUCCESS && apiResponse.body != null -> {
          val imageBitmap = apiResponse.body.data.decodeToImageBitmap()
            ?: run { emit(ImageState.Failure); return@flow }

          imageMemoryCache?.cache(
            key = url,
            image = imageBitmap
          )
          imageDiskCache?.cacheBodyAndMeta(
            url = url,
            body = apiResponse.body.data,
            header = mergedHeader(
              requestHeader = requestHeader,
              responseHeader = apiResponse.header
            )
          )
          emit(ImageState.Success(imageBitmap))
        }
        // 304: 디코딩 -> 메모리 캐싱/디스크 캐싱(메타만 갱신) -> 이미지 발행
        apiResponse.code == NOT_MODIFIED && cachedDiskEntry != null -> {
          val imageBitmap = cachedDiskEntry.bytes.data.decodeToImageBitmap()
            ?: run { emit(ImageState.Failure); return@flow }

          imageMemoryCache?.cache(
            key = url,
            image = imageBitmap
          )
          imageDiskCache.cacheMeta(
            url = url,
            header = apiResponse.header
          )
          emit(ImageState.Success(imageBitmap))
        }
        // 서버 에러(eg., 40x, 50x..): soe 범위 내, 메모리 캐싱 -> 이미지 발행
        cachedDiskEntry?.meta?.canServeStaleOnError() == true -> {
          val imageBitmap = cachedDiskEntry.bytes.data.decodeToImageBitmap()
            ?: run { emit(ImageState.Failure); return@flow }

          imageMemoryCache?.cache(
            key = url,
            image = imageBitmap
          )
          emit(ImageState.Success(imageBitmap))
        }
        else -> emit(ImageState.Failure)
      }
    }
      .flowOn(Dispatchers.IO)
      .catch { emit(ImageState.Failure) }
}