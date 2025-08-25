package kr.co.architecture.core.router.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kr.co.architecture.core.router.internal.navigator.InternalNavigator
import kr.co.architecture.core.router.internal.navigator.Navigator
import kr.co.architecture.core.router.internal.navigator.NavigatorImpl

@Module
@InstallIn(ActivityRetainedComponent::class)
internal abstract class RouterModel {

    @Binds
    @ActivityRetainedScoped
    abstract fun provideNavigator(
        navigator: NavigatorImpl
    ): Navigator

    @Binds
    @ActivityRetainedScoped
    abstract fun provideInternalNavigator(
        navigator: NavigatorImpl
    ): InternalNavigator
}
