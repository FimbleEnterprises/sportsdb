package com.fimbleenterprises.sportsdb.data.db
import androidx.room.*
import com.fimbleenterprises.sportsdb.data.model.SportsTeam
import kotlinx.coroutines.flow.Flow

/**
 * This interface is what Room will use to actually perform CRUD operations in the db.
 */
@Dao // This annotation turns this interface into a magical mechanism to actually perform CRUD operations in the Room db.
interface TeamsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeam(sportsTeam: SportsTeam):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(sportsTeams: List<SportsTeam>):List<Long>

    @Update
    suspend fun updateTeam(sportsTeam: SportsTeam) : Int

    @Delete
    suspend fun deleteTeam(sportsTeam: SportsTeam) : Int

    @Query("DELETE FROM teams")
    suspend fun deleteAll() : Int

    @Query("SELECT * FROM teams")
    fun getSavedTeams(): Flow<List<SportsTeam>>

    @Query("SELECT * FROM teams WHERE idTeam = :id")
    fun getSavedTeam(id: String): Flow<SportsTeam>

    @Query("SELECT * FROM teams WHERE following = 1")
    fun getFollowedTeams(): Flow<List<SportsTeam>>

}