package com.fimbleenterprises.sportsdb.presentation.di

import android.app.Service
import android.os.Debug
import android.util.Log
import com.fimbleenterprises.sportsdb.BuildConfig
import com.fimbleenterprises.sportsdb.data.api.SportsdbAPIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {

        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            if (Debug.isDebuggerConnected()) {
                level = HttpLoggingInterceptor.Level.BODY
            } else {
                level = HttpLoggingInterceptor.Level.NONE
            }
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.SPORTSDB_BASE_URL)
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideSportsdbAPIService(retrofit: Retrofit):SportsdbAPIService {
        return retrofit.create(SportsdbAPIService::class.java)
    }

    companion object {
        private const val TAG = "FIMTOWN|NetModule"
    }

    init {
        Log.i(TAG, "Initialized:NetModule")
    }

}













