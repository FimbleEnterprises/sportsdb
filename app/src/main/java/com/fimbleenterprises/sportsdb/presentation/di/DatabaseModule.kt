package com.fimbleenterprises.sportsdb.presentation.di

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.fimbleenterprises.sportsdb.data.db.TeamsDAO
import com.fimbleenterprises.sportsdb.data.db.TeamsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideTeamsDb(app:Application): TeamsDatabase {
        return Room.databaseBuilder(app, TeamsDatabase::class.java, "teams_db")
            .fallbackToDestructiveMigration()
            .setQueryCallback(
                fun(sqlQuery: String, bindArgs: MutableList<Any>) {
                    println("SQL Query: $sqlQuery SQL Args: $bindArgs")
                }, Executors.newSingleThreadExecutor()
            )
            .allowMainThreadQueries()
            .build()
    }

    @Singleton
    @Provides
    fun provideTeamsDAO(teamsDatabase: TeamsDatabase): TeamsDAO {
        return teamsDatabase.getTeamsDAO()
    }


    companion object {
        private const val TAG = "FIMTOWN|DatabaseModule"
    }

    init {
        Log.i(TAG, "Initialized:DatabaseModule")
    }

}