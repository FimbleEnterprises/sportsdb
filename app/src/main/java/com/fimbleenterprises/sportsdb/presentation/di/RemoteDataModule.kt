package com.fimbleenterprises.sportsdb.presentation.di

import android.util.Log
import com.fimbleenterprises.sportsdb.data.api.SportsdbAPIService
import com.fimbleenterprises.sportsdb.data.repository.dataSource.SportsdbRemoteDataSource
import com.fimbleenterprises.sportsdb.data.repository.dataSourceImpl.SportsdbRemoteDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RemoteDataModule {

    @Singleton
    @Provides
    fun provideSportsdbRemoteDataSource(
        sportsdbAPIService: SportsdbAPIService
    ):SportsdbRemoteDataSource{
        return SportsdbRemoteDataSourceImpl(sportsdbAPIService)
    }

    companion object {
        private const val TAG = "FIMTOWN|RemoteDataModule"
    }

    init {
        Log.i(TAG, "Initialized:RemoteDataModule")
    }

}












