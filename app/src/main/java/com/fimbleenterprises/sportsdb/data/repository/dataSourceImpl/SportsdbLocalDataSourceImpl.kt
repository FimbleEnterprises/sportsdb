package com.fimbleenterprises.sportsdb.data.repository.dataSourceImpl

import com.fimbleenterprises.sportsdb.data.db.TeamsDAO
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import com.fimbleenterprises.sportsdb.data.repository.dataSource.SportsdbLocalDataSource
import kotlinx.coroutines.flow.Flow

/**
 * This class does the heavy lifting by interacting with the Room DB.  This class must have a related
 * interface specifically for being implemented here..
 */
class SportsdbLocalDataSourceImpl
    constructor(
        private val teamDAO: TeamsDAO
    )
: SportsdbLocalDataSource {

    /**
     * This can be named whatever you want.  Room will know what to do based on the overridden func
     * you call here from the DAO you implemented at the class level above.
     */
    override suspend fun saveTeamsToDB(sportsTeams: List<SportsTeam>) {
        teamDAO.insertTeams(sportsTeams)
    }

    override suspend fun saveTeamToDB(sportsTeam: SportsTeam): Long {
        return teamDAO.insertTeam(sportsTeam)
    }

    override fun getSavedTeamsFromDB(): Flow<List<SportsTeam>> {
        return teamDAO.getSavedTeams()
    }

    override fun getSavedTeamFromDB(idTeam: String): Flow<SportsTeam> {
        return teamDAO.getSavedTeam(idTeam)
    }

    override suspend fun deleteSavedTeams(): Int {
        return teamDAO.deleteAll()
    }

    override suspend fun deleteSavedTeam(sportsTeam: SportsTeam): Int {
        return teamDAO.deleteTeam(sportsTeam)
    }

    override fun getFollowedTeamsFromDB(): Flow<List<SportsTeam>> {
        return teamDAO.getFollowedTeams()
    }

    override suspend fun updateTeam(sportsTeam: SportsTeam): Int {
        return teamDAO.updateTeam(sportsTeam)
    }

    override suspend fun followTeam(sportsTeam: SportsTeam): Int {
        sportsTeam.following = true
        return teamDAO.updateTeam(sportsTeam)
    }

}