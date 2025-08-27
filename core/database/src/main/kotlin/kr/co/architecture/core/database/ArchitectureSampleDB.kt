package kr.co.architecture.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.co.architecture.core.database.converters.DateConverter
import kr.co.architecture.core.database.dao.BookSearchDao
import kr.co.architecture.core.database.entity.BookEntity

@Database(
    entities = [
        BookEntity::class],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class ArchitectureSampleDB : RoomDatabase() {
    abstract fun bookSearchDao() : BookSearchDao

}