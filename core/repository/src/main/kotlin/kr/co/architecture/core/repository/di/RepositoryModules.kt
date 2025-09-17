package kr.co.architecture.core.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.repository.ImageRepository
import kr.co.architecture.core.repository.ImageRepositoryImpl
import kr.co.architecture.core.repository.VideoRepository
import kr.co.architecture.core.repository.VideoRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModules {

  @Singleton
  @Binds
  fun bindsImageRepository(
    repository: ImageRepositoryImpl
  ): ImageRepository

  @Singleton
  @Binds
  fun bindsVideoRepository(
    repository: VideoRepositoryImpl
  ): VideoRepository

}