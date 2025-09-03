package kr.co.architecture.core.network.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.network.BuildConfig
import kr.co.architecture.core.network.PicsumApi
import kr.co.architecture.core.network.PicsumApiImpl
import kr.co.architecture.core.network.httpClient.RawHttp11Client
import kr.co.architecture.core.network.interceptor.CustomHttpLogger
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Provides
  @Singleton
  fun provideHttpLogger() = CustomHttpLogger()

  @Provides
  @Singleton
  fun provideRawHttp11Client(
    httpLogger: CustomHttpLogger
  ): RawHttp11Client {
    return RawHttp11Client(
      userAgent = "RawHttp11/0.1",
      readTimeoutMs = 60_000,
      maxRedirects = 5,
      httpLogger = httpLogger
    )
  }

  @Provides
  @Singleton
  fun providePicsumApi(
    rawHttp11Client: RawHttp11Client
  ): PicsumApi {
    return PicsumApiImpl(
      rawHttp11Client = rawHttp11Client,
      url = BuildConfig.API_URL
    )
  }
}