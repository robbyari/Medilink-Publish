package com.robbyari.monitoring.di

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.robbyari.monitoring.data.MonitoringRepositoryImpl
import com.robbyari.monitoring.domain.repository.MonitoringRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideMonitoringRepository(): MonitoringRepository = MonitoringRepositoryImpl(
        db = Firebase.firestore
    )
}