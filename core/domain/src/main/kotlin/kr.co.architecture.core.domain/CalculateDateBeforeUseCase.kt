package kr.co.architecture.core.domain

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Date
import javax.inject.Inject

class CalculateDateBeforeUseCase @Inject constructor(){
  operator fun invoke(params: Params): Response {
    val createAt = LocalDateTime.ofInstant(params.date.toInstant(), ZoneId.systemDefault())
    val currentDate = LocalDateTime.now()
    return when {
      ChronoUnit.YEARS.between(createAt, currentDate) > 0 -> {
        Response(
          beforeTime = ChronoUnit.YEARS.between(createAt, currentDate).toInt(),
          beforeTimeUnit = BeforeTimeUnit.YEAR
        )
      }
      ChronoUnit.MONTHS.between(createAt, currentDate) > 0 -> {
        Response(
          beforeTime = ChronoUnit.MONTHS.between(createAt, currentDate).toInt(),
          beforeTimeUnit = BeforeTimeUnit.MONTH
        )
      }
      ChronoUnit.WEEKS.between(createAt, currentDate) > 0 -> {
        Response(
          beforeTime = ChronoUnit.WEEKS.between(createAt, currentDate).toInt(),
          beforeTimeUnit = BeforeTimeUnit.WEEK
        )
      }
      ChronoUnit.DAYS.between(createAt, currentDate) > 0 -> {
        Response(
          beforeTime = ChronoUnit.DAYS.between(createAt, currentDate).toInt(),
          beforeTimeUnit = BeforeTimeUnit.DAY
        )
      }
      ChronoUnit.HOURS.between(createAt, currentDate) > 0 -> {
        Response(
          beforeTime = ChronoUnit.HOURS.between(createAt, currentDate).toInt(),
          beforeTimeUnit = BeforeTimeUnit.HOUR
        )
      }
      ChronoUnit.MINUTES.between(createAt, currentDate) > 0 -> {
        Response(
          beforeTime = ChronoUnit.MINUTES.between(createAt, currentDate).toInt(),
          beforeTimeUnit = BeforeTimeUnit.MINUTE
        )
      }
      else -> {
        Response(
          beforeTime = -1,
          beforeTimeUnit = BeforeTimeUnit.NOW
        )
      }
    }
  }

  data class Params(val date: Date)
  data class Response(val beforeTime: Int, val beforeTimeUnit: BeforeTimeUnit)
  enum class BeforeTimeUnit {
    YEAR,
    MONTH,
    WEEK,
    DAY,
    HOUR,
    MINUTE,
    NOW
  }
}