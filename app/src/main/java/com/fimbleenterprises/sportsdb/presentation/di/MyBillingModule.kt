package com.fimbleenterprises.sportsdb.presentation.di

import android.content.Context
import android.util.Log
import com.fimbleenterprises.sportsdb.MyBillingWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class MyBillingModule {

    @Singleton
    @Provides
    fun providesBillingClientWrapper(@ApplicationContext context: Context): MyBillingWrapper {
        return MyBillingWrapper(context)
    }

    companion object {
        private const val TAG = "FIMTOWN|MyBillingModule"
    }

    init {
        Log.i(TAG, "Initialized:MyBillingModule")
    }

}