package com.fimbleenterprises.sportsdb.data.repository.dataSourceImpl

import android.util.Log
import com.fimbleenterprises.sportsdb.data.api.SportsdbAPIService
import com.fimbleenterprises.sportsdb.data.model.AllTeamsAPIResponse
import com.fimbleenterprises.sportsdb.data.model.GameResultsApiResponse
import com.fimbleenterprises.sportsdb.data.model.LeagueListApiResponse
import com.fimbleenterprises.sportsdb.data.model.ScheduledGamesApiResponse
import com.fimbleenterprises.sportsdb.data.repository.dataSource.SportsdbRemoteDataSource
import retrofit2.Response

class SportsdbRemoteDataSourceImpl(
        private val sportsdbAPIService: SportsdbAPIService
):SportsdbRemoteDataSource {

    /**
     * l == League (e.g. NHL)
     */
    override suspend fun getAllTeams(
        l : String
    ): Response<AllTeamsAPIResponse> {
          return sportsdbAPIService.getAllTeams(l)
    }

    override suspend fun getAllLeagues(): Response<LeagueListApiResponse> {
        return sportsdbAPIService.getAllLeagues()
    }

    override suspend fun searchTeams(t: String): Response<AllTeamsAPIResponse> {
        return sportsdbAPIService.searchTeamsInApi(t)
    }

    /**
     * id == team's id in their system e.g. 133602
     */
    override suspend fun getNextFiveEvents(
        id : String
    ): Response<ScheduledGamesApiResponse> {
        return sportsdbAPIService.getNextFiveEvents(id)
    }

    /**
     * id == team's id in their system e.g. 133602
     */
    override suspend fun getLastFiveEvents(
        id : String
    ): Response<GameResultsApiResponse> {
        return sportsdbAPIService.getLastFiveEvents(id)
    }

companion object {
    private const val TAG = "FIMTOWN|AllTeamsRemoteDataSourceImpl"
}

init {
    Log.i(TAG, "Initialized:AllTeamsRemoteDataSourceImpl")
}
}