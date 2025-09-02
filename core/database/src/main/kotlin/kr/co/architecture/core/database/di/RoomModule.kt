package kr.co.architecture.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.database.ArchitectureSampleDB
import kr.co.architecture.core.database.dao.BookSearchDao
import javax.inject.Singleton

private const val DB_NAME = "architecture_sample.db"

@InstallIn(SingletonComponent::class)
@Module
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): ArchitectureSampleDB {
        return Room.databaseBuilder(
            appContext,
            ArchitectureSampleDB::class.java,
            DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideBookSearchDao(database: ArchitectureSampleDB): BookSearchDao {
        return database.bookSearchDao()
    }
}