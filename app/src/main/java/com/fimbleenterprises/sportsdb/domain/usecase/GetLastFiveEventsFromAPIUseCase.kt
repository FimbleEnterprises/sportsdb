package com.fimbleenterprises.sportsdb.domain.usecase

import android.util.Log
import com.fimbleenterprises.sportsdb.data.model.GameResultsApiResponse
import com.fimbleenterprises.sportsdb.util.Resource
import com.fimbleenterprises.sportsdb.domain.repository.MainSportsdbRepository

class GetLastFiveEventsFromAPIUseCase(private val mainSportsdbRepository: MainSportsdbRepository) {

    suspend fun execute(id : String): Resource<GameResultsApiResponse>{
        return mainSportsdbRepository.getLastFiveEventsFromWebservice(id)
    }

    companion object {
        private const val TAG = "FIMTOWN|GetLastFiveEventsFromAPIUseCase"
    }

    init {
        Log.i(TAG, "Initialized:GetLastFiveEventsFromAPIUseCase")
    }

}