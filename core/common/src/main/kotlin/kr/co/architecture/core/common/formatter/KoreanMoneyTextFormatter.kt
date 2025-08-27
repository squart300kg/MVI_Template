package kr.co.architecture.core.common.formatter

import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

class KoreanMoneyTextFormatter @Inject constructor(): MoneyTextFormatter {
    private val formatter = NumberFormat.getNumberInstance(Locale.KOREA)

    override operator fun invoke(amount: Int): String {
        return formatter.format(amount)
    }
}