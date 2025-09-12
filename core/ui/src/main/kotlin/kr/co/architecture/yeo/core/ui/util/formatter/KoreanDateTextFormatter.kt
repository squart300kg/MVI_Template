package kr.co.architecture.yeo.core.ui.util.formatter

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import javax.inject.Inject

/**
 * 추후, 여러가지 format들이 생길경우, 구조화하여 사용 가능
 * 한국어 날짜(예: "yy년 M월 d일")로 표기하는 구현.
 * */
class KoreanDateTextFormatter @Inject constructor() : DateTextFormatter {

    private val displayFmt = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREA)

    override fun invoke(raw: String): String {
        val text = raw.trim()
        if (text.isEmpty()) return ""

        val instant = try {
            OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant()
        } catch (_: DateTimeParseException) {
            return ""
        }

        val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
        return displayFmt.format(localDate)
    }
}