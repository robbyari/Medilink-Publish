package com.robbyari.monitoring.di

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.robbyari.monitoring.data.MonitoringRepositoryImpl
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.userDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideMonitoringRepository(userDataStorePreferences: DataStore<Preferences>, context: Context): MonitoringRepository = MonitoringRepositoryImpl(
        storage = Firebase.storage,
        db = Firebase.firestore,
        userDataStorePreferences = userDataStorePreferences,
        context = context
    )

    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    companion object {
        @Provides
        @Singleton
        fun provideUserDataStorePreferences(
            @ApplicationContext applicationContext: Context
        ): DataStore<Preferences> {
            return applicationContext.userDataStore
        }
    }
}