package com.fimbleenterprises.sportsdb.presentation.di

import android.util.Log
import com.fimbleenterprises.sportsdb.domain.repository.MainSportsdbRepository
import com.fimbleenterprises.sportsdb.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

   companion object {
       private const val TAG = "FIMTOWN|UseCaseModule"
   }

   init {
       Log.i(TAG, "Initialized:UseCaseModule")
   }

    @Singleton
    @Provides
    fun provideGetTeamsFromAPIUseCase(
        mainSportsdbRepository: MainSportsdbRepository
    ):GetTeamsFromAPIUseCase{
        return GetTeamsFromAPIUseCase(mainSportsdbRepository)
    }

    @Singleton
    @Provides
    fun provideGetLeaguesFromAPIUseCase(
        mainSportsdbRepository: MainSportsdbRepository
    ):GetLeaguesFromAPIUseCase{
        return GetLeaguesFromAPIUseCase(mainSportsdbRepository)
    }

    @Singleton
    @Provides
    fun provideSearchTeamToFollowUseCase(
        mainSportsdbRepository: MainSportsdbRepository
    ):SearchTeamToFollowUseCase{
        return SearchTeamToFollowUseCase(mainSportsdbRepository)
    }

    @Singleton
    @Provides
    fun provideInsertTeamInDatabaseAPIUseCase(
        mainSportsdbRepository: MainSportsdbRepository
    ):InsertTeamInDatabaseUseCase{
        return InsertTeamInDatabaseUseCase(mainSportsdbRepository)
    }

    @Singleton
    @Provides
    fun provideGetSavedTeamsFromDatabaseUseCase(
        mainSportsdbRepository: MainSportsdbRepository
    ):GetSavedTeamsFromDatabaseUseCase{
        return GetSavedTeamsFromDatabaseUseCase(mainSportsdbRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteSavedTeamsUseCase(
        mainSportsdbRepository: MainSportsdbRepository
    ):DeleteSavedTeamsUseCase{
        return DeleteSavedTeamsUseCase(mainSportsdbRepository)
    }

    @Singleton
    @Provides
    fun provideGetNextFiveEventsFromAPIUseCase(
        mainSportsdbRepository: MainSportsdbRepository
    ):GetNextFiveEventsFromAPIUseCase{
        return GetNextFiveEventsFromAPIUseCase(mainSportsdbRepository)
    }

    @Singleton
    @Provides
    fun provideGetLastFiveEventsFromAPIUseCase(
        mainSportsdbRepository: MainSportsdbRepository
    ):GetLastFiveEventsFromAPIUseCase{
        return GetLastFiveEventsFromAPIUseCase(mainSportsdbRepository)
    }
}


















