package kr.co.architecture.yeo.core.domain.enums

import kr.co.architecture.yeo.core.domain.entity.Book
import kotlin.collections.sortedBy
import kotlin.collections.sortedByDescending

enum class SortDirectionEnum {
  ASCENDING,
  DESCENDING
}

fun List<Book>.sortedByTitle(direction: SortDirectionEnum): List<Book> = when (direction) {
  SortDirectionEnum.ASCENDING  -> sortedBy { it.title }
  SortDirectionEnum.DESCENDING -> sortedByDescending { it.title }
}