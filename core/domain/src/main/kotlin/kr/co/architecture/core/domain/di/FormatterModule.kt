package kr.co.architecture.core.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.domain.formatter.DateTextFormatter
import kr.co.architecture.core.domain.formatter.KoreanDateTextFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FormatterModule {
    @Singleton
    @Binds
    fun provideDateTextFormatter(
        dateTextFormatter: KoreanDateTextFormatter
    ): DateTextFormatter
}