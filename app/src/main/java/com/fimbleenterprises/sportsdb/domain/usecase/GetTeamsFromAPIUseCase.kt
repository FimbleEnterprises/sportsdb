package com.fimbleenterprises.sportsdb.domain.usecase

import android.util.Log
import com.fimbleenterprises.sportsdb.data.model.AllTeamsAPIResponse
import com.fimbleenterprises.sportsdb.util.Resource
import com.fimbleenterprises.sportsdb.domain.repository.MainSportsdbRepository

class GetTeamsFromAPIUseCase(private val mainSportsdbRepository: MainSportsdbRepository) {

    suspend fun execute(l : String): Resource<AllTeamsAPIResponse>{
        return mainSportsdbRepository.getAllTeamsFromWebservice(l)
    }

    companion object {
        private const val TAG = "FIMTOWN|GetTeamsFromWebserviceUseCase"
    }

    init {
        Log.i(TAG, "Initialized:GetTeamsFromWebserviceUseCase")
    }

}