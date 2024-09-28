package kr.co.architecture.model.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.parseToFormmatedDate(): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.getDefault())

    val dateTime = inputFormat.parse(this)
    return outputFormat.format(dateTime)
}

fun getToday(): String {
    val dateFormat = SimpleDateFormat("yyyy. MM. dd", Locale.getDefault())
    val currentDate = Date()
    return dateFormat.format(currentDate)
}