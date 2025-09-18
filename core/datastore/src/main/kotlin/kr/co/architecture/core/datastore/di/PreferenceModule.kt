package kr.co.architecture.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton
import androidx.datastore.preferences.preferencesDataStoreFile
import kr.co.architecture.core.datastore.LocalApiImpl.Companion.SHARED_PREFS_NAME

@Module
@InstallIn(SingletonComponent::class)
object PreferenceModule {
  @Provides
  @Singleton
  fun provideDataStore(
    @ApplicationContext context: Context,
  ): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
      scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    ) {
      context.preferencesDataStoreFile(SHARED_PREFS_NAME)
    }
  }
}