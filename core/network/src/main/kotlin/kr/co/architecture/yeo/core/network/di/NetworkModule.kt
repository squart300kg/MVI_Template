package kr.co.architecture.yeo.core.network.di

import android.util.Log
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.yeo.core.network.BuildConfig
import kr.co.architecture.yeo.core.network.RemoteApi
import kr.co.architecture.yeo.core.network.constants.ApiConstants.AUTHORIZATION
import kr.co.architecture.yeo.core.network.constants.ApiConstants.KAKAO_AK
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val COMMON_TIME_OUT = 10L

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Singleton
  @Provides
  fun provideDebugInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor { message ->
      Log.d("API", message)
    }.apply {
      level = HttpLoggingInterceptor.Level.BODY
    }
  }

  @Provides
  @Singleton
  fun provideBaseInterceptor(): Interceptor = Interceptor { chain ->
    chain.proceed(
      chain.request()
        .newBuilder()
        .addHeader(AUTHORIZATION, "$KAKAO_AK ${BuildConfig.API_KEY}")
        .url(chain.request().url)
        .build()
    )
  }

  @Provides
  @Singleton
  fun provideOkHttpClient(
    baseInterceptor: Interceptor,
    loggingInterceptor: HttpLoggingInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
      .connectTimeout(COMMON_TIME_OUT, TimeUnit.SECONDS)
      .readTimeout(COMMON_TIME_OUT, TimeUnit.SECONDS)
      .writeTimeout(COMMON_TIME_OUT, TimeUnit.SECONDS)
      .callTimeout(COMMON_TIME_OUT, TimeUnit.SECONDS)
      .retryOnConnectionFailure(false)
      .addInterceptor(baseInterceptor)
      .addInterceptor(loggingInterceptor)
      .build()
  }

  @Provides
  @Singleton
  fun provideGsonConverter(): GsonConverterFactory {
    return GsonConverterFactory.create()
  }

  @Provides
  @Singleton
  fun provideRemoteApi(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory
  ): RemoteApi {
    return Retrofit.Builder()
      .baseUrl(BuildConfig.API_URL)
      .client(okHttpClient)
      .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
      .addConverterFactory(gsonConverterFactory)
      .build()
      .create(RemoteApi::class.java)
  }

}