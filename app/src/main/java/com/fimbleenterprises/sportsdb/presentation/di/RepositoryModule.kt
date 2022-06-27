package com.fimbleenterprises.sportsdb.presentation.di

import android.util.Log
import com.fimbleenterprises.sportsdb.data.repository.MainSportsdbRepositoryImpl
import com.fimbleenterprises.sportsdb.data.repository.dataSource.SportsdbRemoteDataSource
import com.fimbleenterprises.sportsdb.data.repository.dataSource.SportsdbLocalDataSource
import com.fimbleenterprises.sportsdb.domain.repository.MainSportsdbRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideSportsdbRepository(
        sportsdbRemoteDataSource: SportsdbRemoteDataSource,
        sportsdbLocalDataSource: SportsdbLocalDataSource
    ):MainSportsdbRepository{
        return MainSportsdbRepositoryImpl(sportsdbRemoteDataSource, sportsdbLocalDataSource)
    }

    companion object {
        private const val TAG = "FIMTOWN|RepositoryModule"
    }

    init {
        Log.i(TAG, "Initialized:RepositoryModule")
    }

}














