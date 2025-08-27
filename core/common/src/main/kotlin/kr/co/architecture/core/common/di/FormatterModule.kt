package kr.co.architecture.core.common.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.common.date.DateTextFormatter
import kr.co.architecture.core.common.date.KoreanDateTextFormatter
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