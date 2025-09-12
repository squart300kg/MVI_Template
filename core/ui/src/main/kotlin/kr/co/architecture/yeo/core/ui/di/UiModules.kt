package kr.co.architecture.yeo.core.ui.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.yeo.core.ui.GlobalUiBus
import kr.co.architecture.yeo.core.ui.GlobalUiBusImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UiModules {

  @Singleton
  @Binds
  abstract fun provideGlobalUiBus(
    globalUiBus: GlobalUiBusImpl
  ): GlobalUiBus

}