package kr.co.architecture.core.datastore.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.datastore.LocalApi
import kr.co.architecture.core.datastore.LocalApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LocalApiModule {
  @Singleton
  @Binds
  fun bindsLocalApi(
    repository: LocalApiImpl
  ): LocalApi
}