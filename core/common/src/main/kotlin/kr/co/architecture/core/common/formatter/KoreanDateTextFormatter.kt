package kr.co.architecture.core.common.formatter

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject

/**
 * 추후, 여러가지 format들이 생길경우, 구조화하여 사용 가능
 */
class KoreanDateTextFormatter @Inject constructor() : DateTextFormatter {
    private val fmt = DateTimeFormatter.ofPattern("yy년 M월 d일", Locale.KOREA)

    override operator fun invoke(date: Date): String {
        val localDate: LocalDate = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        return localDate.format(fmt)
    }
}