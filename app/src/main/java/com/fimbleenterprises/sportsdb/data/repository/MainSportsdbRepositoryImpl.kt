package com.fimbleenterprises.sportsdb.data.repository

import android.util.Log
import com.fimbleenterprises.sportsdb.data.model.*
import com.fimbleenterprises.sportsdb.data.repository.dataSource.SportsdbLocalDataSource
import com.fimbleenterprises.sportsdb.data.repository.dataSource.SportsdbRemoteDataSource
import com.fimbleenterprises.sportsdb.util.Resource
import com.fimbleenterprises.sportsdb.domain.repository.MainSportsdbRepository
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

/**
 * This class will do the actual lifting for all things remote or local.  This class does the getting from
 * web services to saving to local databases.
 */
class MainSportsdbRepositoryImpl(
        private val sportsdbRemoteDataSource: SportsdbRemoteDataSource,
        private val sportsdbLocalDataSource: SportsdbLocalDataSource
):MainSportsdbRepository {

    override suspend fun getAllTeamsFromWebservice(l : String): Resource<AllTeamsAPIResponse> {
        return allTeamsApiResponseToResource(sportsdbRemoteDataSource.getAllTeams(l))
    }

    override suspend fun getNextFiveEventsFromWebservice(id: String): Resource<ScheduledGamesApiResponse> {
        return scheduledGamesApiResponseToResource(sportsdbRemoteDataSource.getNextFiveEvents(id))
    }

    override suspend fun getLastFiveEventsFromWebservice(id: String): Resource<GameResultsApiResponse> {
        return gameResultsApiResponseToResource(sportsdbRemoteDataSource.getLastFiveEvents(id))
    }

    override suspend fun searchTeamToFollow(query: String): Resource<AllTeamsAPIResponse> {
        return allTeamsApiResponseToResource(sportsdbRemoteDataSource.searchTeams(query))
    }

    override suspend fun getAllLeaguesFromWebservice(): Resource<LeagueListApiResponse> {
        return getLeaguesApiResponseToResource(sportsdbRemoteDataSource.getAllLeagues())
    }

    override fun getAllSavedTeams(): Flow<List<SportsTeam>> {
        return sportsdbLocalDataSource.getSavedTeamsFromDB()
    }

    override fun getSavedTeam(idTeam: String): Flow<SportsTeam> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTeams(sportsTeams: List<SportsTeam>): List<Long> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTeam(sportsTeam: SportsTeam): Long {
        return sportsdbLocalDataSource.saveTeamToDB(sportsTeam)
    }

    override fun getMySavedTeam(): Flow<SportsTeam> {
        TODO("Not yet implemented")
    }

    override fun getFollowedTeamsFromDB(): Flow<List<SportsTeam>> {
        return sportsdbLocalDataSource.getFollowedTeamsFromDB()
    }

    override suspend fun updateTeam(sportsTeam: SportsTeam): Int {
        return sportsdbLocalDataSource.updateTeam(sportsTeam)
    }

    override suspend fun deleteSavedTeams(): Int {
        return sportsdbLocalDataSource.deleteSavedTeams()
    }

    override suspend fun deleteSavedTeam(sportsTeam: SportsTeam): Int {
        return sportsdbLocalDataSource.deleteSavedTeam(sportsTeam)
    }


    // region -= Utility methods =-

    /**
     * Convert the Retrofit Response<T> object to our custom Resource<T> object
     */
    private fun allTeamsApiResponseToResource(
        response:Response<AllTeamsAPIResponse>
    ):Resource<AllTeamsAPIResponse>{

        if(response.isSuccessful){
            response.body()?.let {result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * Convert the Retrofit Response<T> object to our custom Resource<T> object
     */
    private fun scheduledGamesApiResponseToResource(
        response:Response<ScheduledGamesApiResponse>
    ):Resource<ScheduledGamesApiResponse>{

        if(response.isSuccessful){
            response.body()?.let {result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * Convert the Retrofit Response<T> object to our custom Resource<T> object
     */
    private fun gameResultsApiResponseToResource(
        response:Response<GameResultsApiResponse>
    ):Resource<GameResultsApiResponse>{

        if(response.isSuccessful){
            response.body()?.let {result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * Convert the Retrofit Response<T> object to our custom Resource<T> object
     */
    private fun getLeaguesApiResponseToResource(
        response:Response<LeagueListApiResponse>
    ):Resource<LeagueListApiResponse>{

        if(response.isSuccessful){
            response.body()?.let {result->
                return Resource.Success(result)
            }
        }
        return Resource.Error(response.message())
    }

    companion object {
        private const val TAG = "FIMTOWN|AllTeamsRepositoryImpl"
    }

    init {
        Log.i(TAG, "Initialized:AllTeamsRepositoryImpl")
    }

    // endregion
}