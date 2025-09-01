package kr.co.architecture.core.ui.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.domain.repository.BookRepository
import kr.co.architecture.core.ui.GlobalUiBus
import javax.inject.Singleton
import kr.co.architecture.core.ui.GlobalUiBusImpl

@Module
@InstallIn(SingletonComponent::class)
interface UiModules {

  @Singleton
  @Binds
  fun bindGlobalUiBus(
    globalUiBus: GlobalUiBusImpl
  ): GlobalUiBus

}