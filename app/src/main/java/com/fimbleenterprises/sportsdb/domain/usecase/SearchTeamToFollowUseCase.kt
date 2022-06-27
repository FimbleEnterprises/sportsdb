package com.fimbleenterprises.sportsdb.domain.usecase

import android.util.Log
import com.fimbleenterprises.sportsdb.data.model.AllTeamsAPIResponse
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import com.fimbleenterprises.sportsdb.domain.repository.MainSportsdbRepository
import com.fimbleenterprises.sportsdb.util.Resource

/**
 * I do not think using the API to perform this search is the best method since the API's search
 * is janky at best.  Moreover, we can easily retrieve all teams in a league without any problems.
 * But the code challenge asked for searching the API so implement it we will!
 */
class SearchTeamToFollowUseCase(private val sportsdbRepository: MainSportsdbRepository) {

    suspend fun execute(searchquery: String) : Resource<AllTeamsAPIResponse> = sportsdbRepository.searchTeamToFollow(searchquery)

    companion object {
        private const val TAG = "FIMTOWN|SearchTeamToFollowUseCase"
    }

    init {
        Log.i(TAG, "Initialized:SearchTeamToFollowUseCase")
    }
}