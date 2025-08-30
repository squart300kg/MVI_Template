package kr.co.architecture.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class BookEntity(
    @PrimaryKey
    val isbn: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "authors")
    val authors: String,
    @ColumnInfo(name = "publisher")
    val publisher: String,
    @ColumnInfo(name = "dateTime")
    val dateTime: String,
    @ColumnInfo(name = "discountedPrice")
    val discountedPrice: Int,
    @ColumnInfo(name = "originPrice")
    val originPrice: Int,
    @ColumnInfo(name = "url")
    val url: String,
    @ColumnInfo(name = "thumbnail")
    val thumbnail: String,
    @ColumnInfo(name = "contents")
    val contents: String
)