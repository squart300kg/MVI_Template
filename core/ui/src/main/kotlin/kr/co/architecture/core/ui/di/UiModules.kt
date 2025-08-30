package kr.co.architecture.core.ui.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kr.co.architecture.core.ui.GlobalUiBus

@Module
@InstallIn(SingletonComponent::class)
object UiModules {

  @Provides
  @Singleton
  fun provideGlobalUiBus(): GlobalUiBus =
    GlobalUiBus()
}