package kr.co.architecture.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.network.RemoteApi
import kr.co.architecture.core.network.RemoteMockApiImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

  @Singleton
  @Binds
  fun provideRemoteApi(
    remoteMockApiImpl: RemoteMockApiImpl
  ): RemoteApi

}