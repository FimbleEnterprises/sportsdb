package com.fimbleenterprises.sportsdb.domain.repository

import com.fimbleenterprises.sportsdb.data.model.*
import com.fimbleenterprises.sportsdb.util.Resource
import kotlinx.coroutines.flow.Flow

interface MainSportsdbRepository{

      // Web requests
      suspend fun getAllTeamsFromWebservice(l : String): Resource<AllTeamsAPIResponse>
      suspend fun getNextFiveEventsFromWebservice(id: String): Resource<ScheduledGamesApiResponse>
      suspend fun getLastFiveEventsFromWebservice(id: String): Resource<GameResultsApiResponse>
      suspend fun searchTeamToFollow(query: String): Resource<AllTeamsAPIResponse>
      suspend fun getAllLeaguesFromWebservice():Resource<LeagueListApiResponse>

      // DB ops
      fun getAllSavedTeams(): Flow<List<SportsTeam>>
      suspend fun insertTeam(sportsTeam: SportsTeam): Long
      fun getFollowedTeamsFromDB(): Flow<List<SportsTeam>>
      suspend fun updateTeam(sportsTeam: SportsTeam):Int
      suspend fun deleteSavedTeams():Int
      suspend fun deleteSavedTeam(sportsTeam: SportsTeam):Int
}