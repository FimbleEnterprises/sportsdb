package com.fimbleenterprises.sportsdb.presentation.di

import android.util.Log
import com.fimbleenterprises.sportsdb.MyAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class MyAnalyticsModule {

    @Singleton
    @Provides
    fun providesMyFirebaseAnalytics(): MyAnalytics {
        return MyAnalytics(Firebase.analytics)
    }

    companion object {
        private const val TAG = "FIMTOWN|MyAnalyticsModule"
    }

    init {
        Log.i(TAG, "-= Initialized:MyAnalyticsModule =-")
    }

}