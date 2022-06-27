package com.fimbleenterprises.sportsdb.domain.usecase

import android.util.Log
import com.fimbleenterprises.sportsdb.data.model.ScheduledGamesApiResponse
import com.fimbleenterprises.sportsdb.util.Resource
import com.fimbleenterprises.sportsdb.domain.repository.MainSportsdbRepository

class GetNextFiveEventsFromAPIUseCase(private val mainSportsdbRepository: MainSportsdbRepository) {

    suspend fun execute(id : String): Resource<ScheduledGamesApiResponse>{
        return mainSportsdbRepository.getNextFiveEventsFromWebservice(id)
    }

    companion object {
        private const val TAG = "FIMTOWN|GetTeamsFromWebserviceUseCase"
    }

    init {
        Log.i(TAG, "Initialized:GetTeamsFromWebserviceUseCase")
    }

}