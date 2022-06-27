package com.fimbleenterprises.sportsdb.data.api

import com.fimbleenterprises.sportsdb.BuildConfig
import com.fimbleenterprises.sportsdb.data.model.AllTeamsAPIResponse
import com.fimbleenterprises.sportsdb.data.model.GameResultsApiResponse
import com.fimbleenterprises.sportsdb.data.model.LeagueListApiResponse
import com.fimbleenterprises.sportsdb.data.model.ScheduledGamesApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * We define all of the operations we want to perform against the REST API here in this interface.
 */
interface SportsdbAPIService {

    // search_all_teams.php?l=NHL
    @GET("api/v1/json/${BuildConfig.SPORTSDB_FREE_API_KEY}/search_all_teams.php")
    suspend fun getAllTeams(
        @Query("l")
        league:String
    ) : Response<AllTeamsAPIResponse>

    // all_leagues.php
    @GET("api/v1/json/${BuildConfig.SPORTSDB_FREE_API_KEY}/all_leagues.php")
    suspend fun getAllLeagues() : Response<LeagueListApiResponse>

    // eventsnext.php?id=134855
    @GET("api/v1/json/${BuildConfig.SPORTSDB_PAID_API_KEY}/eventsnext.php")
    suspend fun getNextFiveEvents(
        @Query("id")
        id:String
    ) : Response<ScheduledGamesApiResponse>

    // eventslast.php?id=134855
    @GET("api/v1/json/${BuildConfig.SPORTSDB_PAID_API_KEY}/eventslast.php")
    suspend fun getLastFiveEvents(
        @Query("id")
        id:String
    ) : Response<GameResultsApiResponse>

    // searchteams.php?t=dia
    @GET("api/v1/json/${BuildConfig.SPORTSDB_PAID_API_KEY}/searchteams.php")
    suspend fun searchTeamsInApi(
        @Query("t")
        t:String
    ) : Response<AllTeamsAPIResponse>

/*    @GET("v2/top-headlines")
    suspend fun searchTopHeadlines(
        @Query("country")
        country:String,
        @Query("q")
        searchQuery: String,
        @Query("page")
        page:Int,
        @Query("apiKey")
        apiKey:String = BuildConfig.API_KEY
    ) : Response<APIResponse>*/

}