package kr.co.architecture.core.domain.formatter

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * 날짜를 사용자에게 표시하기 좋은 문자열로 변환하는 포맷터.
 *
 * 예:
 * ```
 * val formatter = KoreanDateTextFormatter()
 * formatter(Date())  // "25년 8월 27일 15시 23분 22초"
 * ```
 */
class KoreanDateTextFormatter @Inject constructor() {

    private val displayFmt = DateTimeFormatter.ofPattern("yy년 M월 d일 H시 m분", Locale.KOREA)

    /**
     * 주어진 [Date] 객체를 포맷팅하여 문자열로 반환한다.
     *
     * @param raw 포맷팅할 [String]타입의 날짜
     * @return 포맷팅된 문자열 표현 (예: "25년 8월 27일 15시 23분 22초")
     */
    operator fun invoke(raw: String): String {
        val text = raw.trim()
        if (text.isEmpty()) return ""

        val instant = try {
            OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant()
        } catch (_: DateTimeParseException) {
            return ""
        }

        val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
        return displayFmt.format(localDateTime)
    }
}