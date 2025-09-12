package kr.co.architecture.yeo.core.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.yeo.core.domain.usecase.ObserveBookmarkedBooksUseCase
import kr.co.architecture.yeo.core.domain.usecase.ObserveBookmarkedBooksUseCaseImpl
import kr.co.architecture.yeo.core.domain.usecase.ObserveFilteredBookmarksUseCase
import kr.co.architecture.yeo.core.domain.usecase.ObserveFilteredBookmarksUseCaseImpl
import kr.co.architecture.yeo.core.domain.usecase.SearchBookUseCase
import kr.co.architecture.yeo.core.domain.usecase.SearchBookUseCaseImpl
import kr.co.architecture.yeo.core.domain.usecase.SearchBooksUseCase
import kr.co.architecture.yeo.core.domain.usecase.SearchBooksUseCaseImpl
import kr.co.architecture.yeo.core.domain.usecase.ToggleBookmarkUseCase
import kr.co.architecture.yeo.core.domain.usecase.ToggleBookmarkUseCaseImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModules {

  @Singleton
  @Binds
  fun bindsObserveBookmarkedBooksUseCase(
    useCase: ObserveBookmarkedBooksUseCaseImpl
  ): ObserveBookmarkedBooksUseCase

  @Singleton
  @Binds
  fun bindsObserveFilteredBookmarksUseCase(
    useCase: ObserveFilteredBookmarksUseCaseImpl
  ): ObserveFilteredBookmarksUseCase

  @Singleton
  @Binds
  fun bindsSearchBooksUseCase(
    useCase: SearchBooksUseCaseImpl
  ): SearchBooksUseCase

  @Singleton
  @Binds
  fun bindsSearchBookUseCase(
    useCase: SearchBookUseCaseImpl
  ): SearchBookUseCase

  @Singleton
  @Binds
  fun bindsToggleBookmarkUseCase(
    useCase: ToggleBookmarkUseCaseImpl
  ): ToggleBookmarkUseCase

}