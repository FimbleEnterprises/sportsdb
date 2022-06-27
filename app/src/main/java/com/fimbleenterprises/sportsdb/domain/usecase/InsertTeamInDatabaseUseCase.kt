package com.fimbleenterprises.sportsdb.domain.usecase

import android.util.Log
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import com.fimbleenterprises.sportsdb.domain.repository.MainSportsdbRepository

class InsertTeamInDatabaseUseCase(private val sportsdbRepository: MainSportsdbRepository) {

    suspend fun execute(sportsTeam:SportsTeam) : Long = sportsdbRepository.insertTeam(sportsTeam)
    suspend fun execute(sportsTeams:List<SportsTeam>) : List<Long> = sportsdbRepository.insertTeams(sportsTeams)

    companion object {
        private const val TAG = "FIMTOWN|SaveTeamsUseCase"
    }

    init {
        Log.i(TAG, "Initialized:SaveTeamsUseCase")
    }
}