package kr.co.architecture.core.common.date

import java.util.Date

interface DateTextFormatter {
    operator fun invoke(date: Date): String
}