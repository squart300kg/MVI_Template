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
  val versionName = context
    .packageManager
    .getPackageInfo(context.packageName, 0)
    .versionName
  // TODO: UserAgent 주입방식 고민하기
  val client = remember {
    RawHttp11Client(
      userAgent = "GalleryApp-${versionName}-RawHttp11/0.1",
      httpLogger = CustomHttpLogger()
    )
  }
  val diskCache = remember { ImageDiskCache.create(context, maxBytes = 64L * 1024 * 1024) }
  val imageState by produceState<ImageBitmap?>(initialValue = null, url) {
    // 1) 메모리 캐시
    MemoryImageCache[url]?.let { value = it; return@produceState }

    // 2) 디스크 캐시
    val diskBytes = withContext(Dispatchers.IO) { diskCache.getBytes(url) }
    if (diskBytes != null) {
      val bitmap = BitmapFactory.decodeByteArray(diskBytes, 0, diskBytes.size)
      if (bitmap != null) {
        val imageBitmap = bitmap.asImageBitmap()
        MemoryImageCache.put(url, imageBitmap)
        value = imageBitmap
        return@produceState
      }
    }

    // 3) 네트워크 로드 + 디코딩 + 캐시 저장
    client.callApi(
      method = "GET",
      url = url,
      onResponseSuccess = {
        val bitmap = BitmapFactory.decodeByteArray(body, 0, body.size)
        if (bitmap != null) {
          val imageBitmap = bitmap.asImageBitmap()
          value = imageBitmap
          withContext(Dispatchers.IO) {
            MemoryImageCache.put(url, imageBitmap)
            diskCache.putBytes(url, body)
          }
        } else value = null
      },
      onResponseError = { value = null },
      onResponseException = { value = null }
    )
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
  } ?: run {
    placeholderContent()
  }
}