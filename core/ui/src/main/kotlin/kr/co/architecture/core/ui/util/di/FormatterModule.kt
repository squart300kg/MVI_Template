package kr.co.architecture.core.ui.util.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.ui.util.formatter.DateTextFormatter
import kr.co.architecture.core.ui.util.formatter.KoreanDateTextFormatter
import kr.co.architecture.core.ui.util.formatter.KoreanMoneyTextFormatter
import kr.co.architecture.core.ui.util.formatter.MoneyTextFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface FormatterModule {

    @Singleton
    @Binds
    fun provideDateTextFormatter(
        dateTextFormatter: KoreanDateTextFormatter
    ): DateTextFormatter

    @Singleton
    @Binds
    fun provideMoneyTextFormatter(
        dateTextFormatter: KoreanMoneyTextFormatter
    ): MoneyTextFormatter

}