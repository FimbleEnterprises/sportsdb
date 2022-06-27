package com.fimbleenterprises.sportsdb.data.repository.dataSource

import com.fimbleenterprises.sportsdb.data.model.AllTeamsAPIResponse
import com.fimbleenterprises.sportsdb.data.model.GameResultsApiResponse
import com.fimbleenterprises.sportsdb.data.model.LeagueListApiResponse
import com.fimbleenterprises.sportsdb.data.model.ScheduledGamesApiResponse
import retrofit2.Response

interface SportsdbRemoteDataSource {

    /**
     * l == League (e.g. NHL)
     */
    suspend fun getAllTeams(l : String):Response<AllTeamsAPIResponse>

    suspend fun getAllLeagues():Response<LeagueListApiResponse>

    /**
     * query == team name (note: the search sucks ass in this API)
     */
    suspend fun searchTeams(query: String):Response<AllTeamsAPIResponse>

    /**
     * id == team id in sportsdb's system
     */
    suspend fun getNextFiveEvents(id : String): Response<ScheduledGamesApiResponse>

    /**
     * id == team id in sportsdb's system
     */
    suspend fun getLastFiveEvents(id: String): Response<GameResultsApiResponse>

}