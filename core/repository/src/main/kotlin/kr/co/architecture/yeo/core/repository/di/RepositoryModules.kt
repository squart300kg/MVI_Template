package kr.co.architecture.yeo.core.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.yeo.core.domain.repository.BookRepository
import kr.co.architecture.yeo.core.repository.BookRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModules {

  @Singleton
  @Binds
  fun bindsBookRepository(
    repository: BookRepositoryImpl
  ): BookRepository

}