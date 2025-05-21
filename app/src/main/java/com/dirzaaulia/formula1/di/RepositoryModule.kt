package com.dirzaaulia.formula1.di

import com.dirzaaulia.formula1.repository.NetworkRepository
import com.dirzaaulia.formula1.repository.NetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import io.ktor.client.HttpClient

@InstallIn(ViewModelComponent::class)
@Module
class RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideNetworkRepository(
        ktorClient: HttpClient
    ): NetworkRepository {
        return NetworkRepositoryImpl(ktorClient)
    }
}