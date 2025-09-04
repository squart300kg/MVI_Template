package kr.co.architecture.core.network.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.network.BuildConfig
import kr.co.architecture.core.network.PicsumApi
import kr.co.architecture.core.network.PicsumApiImpl
import kr.co.architecture.custom.http.client.RawHttp11Client
import kr.co.architecture.custom.http.client.interceptor.CustomHttpLogger
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
    @ApplicationContext context: Context,
    httpLogger: CustomHttpLogger
  ): RawHttp11Client {
    return RawHttp11Client(
      userAgent = "GalleryApp-RawHttp11/0.1",
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