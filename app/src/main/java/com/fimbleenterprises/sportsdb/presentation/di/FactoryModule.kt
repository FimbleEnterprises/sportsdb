package com.fimbleenterprises.sportsdb.presentation.di

import android.app.Application
import android.util.Log
import com.fimbleenterprises.sportsdb.MyBillingWrapper
import com.fimbleenterprises.sportsdb.domain.usecase.*
import com.fimbleenterprises.sportsdb.presentation.viewmodel.SportsdbViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Like most every @Module annotated classes this allows us to instantiate an instance of this class
 * by simply declaring it as a lateinit var annotated with @Inject wherever we need this class.
 *
 * e.g.
 * ```
 *  @Inject
 *  ```
    lateinit var factory: NewsViewModelFactory

 *  BAM!  You now have this class.  Note that it does not matter what you name the function (annotated
 *  with @Provides) that does the injection; it is the return type that tells dagger which function
 *  to use.
 */

@Module
@InstallIn(SingletonComponent::class)
class FactoryModule {

    @Singleton
    @Provides
    fun provideSportsdbViewModelFactory(
        application: Application,
        getTeamsFromAPIUseCase: GetTeamsFromAPIUseCase,
        getNextFiveEventsFromAPIUseCase: GetNextFiveEventsFromAPIUseCase,
        insertTeamInDatabaseUseCase: InsertTeamInDatabaseUseCase,
        getSavedTeamsFromDatabaseUseCase: GetSavedTeamsFromDatabaseUseCase,
        deleteSavedTeamsUseCase: DeleteSavedTeamsUseCase,
        getLastFiveEventsFromAPIUseCase: GetLastFiveEventsFromAPIUseCase,
        searchTeamToFollowUseCase: SearchTeamToFollowUseCase,
        getLeaguesFromAPIUseCase: GetLeaguesFromAPIUseCase,
        myMyBillingInstance: MyBillingWrapper
    ): SportsdbViewModelFactory {
        return SportsdbViewModelFactory(
            application,
            getTeamsFromAPIUseCase,
            getNextFiveEventsFromAPIUseCase,
            insertTeamInDatabaseUseCase,
            getSavedTeamsFromDatabaseUseCase,
            deleteSavedTeamsUseCase,
            getLastFiveEventsFromAPIUseCase,
            searchTeamToFollowUseCase,
            getLeaguesFromAPIUseCase,
            myMyBillingInstance
        );
    }

    companion object {
        private const val TAG = "FIMTOWN|FactoryModule"
    }

    init {
        Log.i(TAG, "Initialized:FactoryModule")
    }
}








