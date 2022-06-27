package com.fimbleenterprises.sportsdb.presentation.di

import android.content.Context
import android.util.Log
import com.fimbleenterprises.sportsdb.presentation.adapter.GameResultsAdapter
import com.fimbleenterprises.sportsdb.presentation.adapter.LeaguesAdapter
import com.fimbleenterprises.sportsdb.presentation.adapter.TeamsAdapter
import com.fimbleenterprises.sportsdb.presentation.adapter.ScheduledGamesAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AdapterModule {

    @Singleton
    @Provides
    fun provideSportsdbAdapter(): TeamsAdapter {
        return TeamsAdapter()
    }

    @Singleton
    @Provides
    fun provideNextFiveEventsAdapter(): ScheduledGamesAdapter {
        return ScheduledGamesAdapter()
    }

    @Singleton
    @Provides
    fun provideLastFiveEventsAdapter(@ApplicationContext context: Context): GameResultsAdapter {
        return GameResultsAdapter(context)
    }

    @Singleton
    @Provides
    fun provideLeaguesAdapter(): LeaguesAdapter {
        return LeaguesAdapter()
    }

    companion object {
        private const val TAG = "FIMTOWN|AdapterModule"
    }

    init {
        Log.i(TAG, "Initialized:AdapterModule")
    }
}