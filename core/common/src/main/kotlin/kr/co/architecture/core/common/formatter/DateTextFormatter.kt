package kr.co.architecture.core.common.formatter

import java.util.Date

interface DateTextFormatter {
    operator fun invoke(date: Date): String
}