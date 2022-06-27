package com.fimbleenterprises.sportsdb.domain.usecase

import android.util.Log
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import com.fimbleenterprises.sportsdb.domain.repository.MainSportsdbRepository

class DeleteSavedTeamsUseCase(private val sportsdbRepository: MainSportsdbRepository) {

    suspend fun execute() : Int = sportsdbRepository.deleteSavedTeams()
    suspend fun execute(sportsTeam:SportsTeam) : Int = sportsdbRepository.deleteSavedTeam(sportsTeam)

    companion object {
        private const val TAG = "FIMTOWN|DeleteSavedNewsUseCase"
    }

    init {
        Log.i(TAG, "Initialized:DeleteSavedNewsUseCase")
    }

}