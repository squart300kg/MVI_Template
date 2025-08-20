package kr.co.architecture.core.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.repository.AlimRepository
import kr.co.architecture.core.repository.AlimRepositoryImpl
import kr.co.architecture.core.repository.BuddyRepository
import kr.co.architecture.core.repository.BuddyRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModules {

  @Singleton
  @Binds
  fun bindsAlimRepository(
    repository: AlimRepositoryImpl
  ): AlimRepository

  @Singleton
  @Binds
  fun bindsBuddyRepository(
    repository: BuddyRepositoryImpl
  ): BuddyRepository

}