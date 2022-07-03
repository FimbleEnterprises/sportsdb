package com.fimbleenterprises.sportsdb.data.repository.dataSource

import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import kotlinx.coroutines.flow.Flow

interface SportsdbLocalDataSource {
    suspend fun saveTeamsToDB(sportsTeams:List<SportsTeam>)
    suspend fun saveTeamToDB(sportsTeam: SportsTeam):Long
    fun getSavedTeamsFromDB(): Flow<List<SportsTeam>>
    fun getSavedTeamFromDB(idTeam:String): Flow<SportsTeam>
    suspend fun deleteSavedTeams():Int
    suspend fun deleteSavedTeam(sportsTeam: SportsTeam):Int
    fun getFollowedTeamsFromDB(): Flow<List<SportsTeam>>
    suspend fun updateTeam(sportsTeam:SportsTeam):Int
    suspend fun followTeam(sportsTeam:SportsTeam):Int
}