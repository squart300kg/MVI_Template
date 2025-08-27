package kr.co.architecture.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import kr.co.architecture.core.database.entity.BookEntity

@Dao
interface BookSearchDao {

    @Query("SELECT * FROM BookEntity")
    fun observeBookmarkedBooks(): Flow<List<BookEntity>>

    @Query("DELETE FROM BookEntity WHERE characterId = :id")
    fun deleteCharacter(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(vararg bookEntity: BookEntity)

}