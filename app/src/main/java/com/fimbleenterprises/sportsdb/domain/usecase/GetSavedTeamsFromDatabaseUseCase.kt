package com.fimbleenterprises.sportsdb.domain.usecase

import android.util.Log
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import com.fimbleenterprises.sportsdb.domain.repository.MainSportsdbRepository
import kotlinx.coroutines.flow.Flow

/**
 * Not implemented - in case we want to allow user to save multiple teams to follow
 */
class GetSavedTeamsFromDatabaseUseCase(private val sportsdbRepository: MainSportsdbRepository) {

    fun execute(idTeam:String): Flow<SportsTeam> {
        return sportsdbRepository.getSavedTeam(idTeam)
    }

    fun execute(): Flow<List<SportsTeam>> {
        return sportsdbRepository.getAllSavedTeams()
    }

    companion object {
        private const val TAG = "FIMTOWN|GetSavedTeamsFromDatabaseUseCase"
    }

    init {
        Log.i(TAG, "Initialized:GetSavedTeamsFromDatabaseUseCase")
    }
}