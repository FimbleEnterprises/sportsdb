package com.fimbleenterprises.sportsdb.domain.usecase

import android.util.Log
import com.fimbleenterprises.sportsdb.data.model.AllTeamsAPIResponse
import com.fimbleenterprises.sportsdb.data.model.LeagueListApiResponse
import com.fimbleenterprises.sportsdb.util.Resource
import com.fimbleenterprises.sportsdb.domain.repository.MainSportsdbRepository

class GetLeaguesFromAPIUseCase(private val mainSportsdbRepository: MainSportsdbRepository) {

    suspend fun execute(): Resource<LeagueListApiResponse>{
        return mainSportsdbRepository.getAllLeaguesFromWebservice()
    }

    companion object {
        private const val TAG = "FIMTOWN|GetLeaguesFromAPIUseCase"
    }

    init {
        Log.i(TAG, "Initialized:GetLeaguesFromAPIUseCase")
    }

}