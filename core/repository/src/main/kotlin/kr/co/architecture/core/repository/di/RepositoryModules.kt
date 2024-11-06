package kr.co.architecture.core.repository.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.co.architecture.core.repository.Repository
import kr.co.architecture.core.repository.RepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModules {

    @Singleton
    @Binds
    fun bindsNewsRepository(
        repository: RepositoryImpl
    ): Repository

}