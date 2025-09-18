package kr.co.architecture.core.model

object MediaIdentity {
  /**
   * 프로퍼티 기준, ID 추출
   */
  fun idOf(
    mediaContentsType: MediaContentsTypeEnum,
    title: String,
    contents: String,
    thumbnailUrl: String
  ): String = buildString {
    append(mediaContentsType); append(':')
    append(title); append(':')
    append(contents); append(':')
    append(thumbnailUrl)
  }
}