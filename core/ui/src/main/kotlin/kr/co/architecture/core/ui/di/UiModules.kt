package kr.co.architecture.core.ui.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kr.co.architecture.core.ui.GlobalUiBus
import kr.co.architecture.core.ui.GlobalUiBusImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class UiModules {

  @Binds
  @ActivityRetainedScoped
  abstract fun provideGlobalUiBus(
    globalUiBus: GlobalUiBusImpl
  ): GlobalUiBus

}
