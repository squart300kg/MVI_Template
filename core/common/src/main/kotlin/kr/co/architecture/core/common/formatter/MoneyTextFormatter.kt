package kr.co.architecture.core.common.formatter

interface MoneyTextFormatter {

  operator fun invoke(amount: Int): String
}