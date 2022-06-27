package com.fimbleenterprises.sportsdb.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fimbleenterprises.sportsdb.MyBillingWrapper
import com.fimbleenterprises.sportsdb.domain.usecase.*

/**
 * Since our NewsViewModel class has constructors we need to create this factory class in order to
 * create one as viewmodels that contain constructors cannot be directly instantiated (this is just
 * one of the quirks of Android's ViewModel framework).  To create the NewsViewModel you will create
 * it like so:
 * ```
 * viewModel = ViewModelProvider(this,factory).get(NewsViewModel::class.java)
 */
class SportsdbViewModelFactory(
    private val app:Application,
    private val getTeamsFromAPIUseCase: GetTeamsFromAPIUseCase,
    private val getNextFiveEventsFromAPIUseCase: GetNextFiveEventsFromAPIUseCase,
    private val insertTeamInDatabaseUseCase: InsertTeamInDatabaseUseCase,
    private val getSavedTeamsFromDatabaseUseCase: GetSavedTeamsFromDatabaseUseCase,
    private val deleteSavedTeamsUseCase: DeleteSavedTeamsUseCase,
    private val getLastFiveEventsFromAPIUseCase: GetLastFiveEventsFromAPIUseCase,
    private val searchTeamToFollowUseCase: SearchTeamToFollowUseCase,
    private val getAllLeaguesFromAPIUseCase: GetLeaguesFromAPIUseCase,
    private val myBillingInstance: MyBillingWrapper
):ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SportsdbViewModel(
            app,
            getTeamsFromAPIUseCase,
            getNextFiveEventsFromAPIUseCase,
            insertTeamInDatabaseUseCase,
            getSavedTeamsFromDatabaseUseCase,
            deleteSavedTeamsUseCase,
            getLastFiveEventsFromAPIUseCase,
            searchTeamToFollowUseCase,
            getAllLeaguesFromAPIUseCase
        ) as T
    }

    companion object {
        private const val TAG = "FIMTOWN|SportsdbViewModelFactory"
    }

    init {
        Log.i(TAG, "Initialized:SportsdbViewModelFactory")
    }
}









