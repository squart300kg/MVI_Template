package kr.co.architecture.core.domain.formatter

import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import javax.inject.Inject

/**
 * 추후, 여러가지 format들이 생길경우, 구조화하여 사용 가능
 * 한국어 날짜(예: "yy년 M월 d일 H시 m분")로 표기하는 구현.
 * */
class KoreanDateTextFormatter @Inject constructor() : DateTextFormatter {

    private val displayFmt = DateTimeFormatter.ofPattern("yy년 M월 d일 H시 m분", Locale.KOREA)

    override fun invoke(raw: String): String {
        val text = raw.trim()
        if (text.isEmpty()) return ""

        val instant = try {
            OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toInstant()
        } catch (_: DateTimeParseException) {
            return ""
        }

        // 시스템 기본 타임존 기준(예: Asia/Seoul)으로 로컬 날짜-시간 생성
        val localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime()
        return displayFmt.format(localDateTime)
    }
}