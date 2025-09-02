package kr.co.architecture.core.network.di

import android.util.Log
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.network.BuildConfig
import kr.co.architecture.core.network.RemoteApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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
  fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor
  ): OkHttpClient {
    return OkHttpClient.Builder()
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
  fun provideMarbleCharacterApi(
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