package kr.co.architecture.core.ui.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.ui.GlobalUiBus
import kr.co.architecture.core.ui.GlobalUiBusImpl
import javax.inject.Singleton

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class UiModules {

  @Binds
  @ActivityRetainedScoped
  abstract fun provideGlobalUiBus(
    globalUiBus: GlobalUiBusImpl
  ): GlobalUiBus

}