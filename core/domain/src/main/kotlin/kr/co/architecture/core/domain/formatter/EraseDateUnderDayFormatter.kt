package kr.co.architecture.core.domain.formatter

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class EraseDateUnderDayFormatter @Inject constructor() {

  private val locale = Locale.KOREA

  private val inputFormatters: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yy년 M월 d일 H시 m분", locale)

  private val outputFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("yy년 M월 d일", locale)

  /**
   * 예)
   *  - "25년 8월 27일 15시 23분"  -> "25년 8월 27일"
   *  - "25년 8월 27일 15시 23분 22초" -> "25년 8월 27일"
   */
  operator fun invoke(text: String): String {
    // 1) 파싱 시도
    runCatching {
      val ldt = LocalDateTime.parse(text, inputFormatters)
      return outputFormatter.format(ldt)
    }

    // 2) 파싱 실패 시, "일"까지 자르는 안전한 폴백
    val idx = text.indexOf('일')
    if (idx >= 0) return text.substring(0, idx + 1)

    // 3) 그래도 못 자르면 원문 반환
    return text
  }
}
