package kr.co.architecture.yeo.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import kr.co.architecture.yeo.core.database.dao.BookSearchDao
import kr.co.architecture.yeo.core.database.entity.BookEntity

@Database(
    entities = [
        BookEntity::class],
    version = 1
)
abstract class ArchitectureSampleDB : RoomDatabase() {
    abstract fun bookSearchDao() : BookSearchDao

}