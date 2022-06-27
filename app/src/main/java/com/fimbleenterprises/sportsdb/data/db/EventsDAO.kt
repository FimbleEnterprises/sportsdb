package com.fimbleenterprises.sportsdb.data.db
import androidx.room.*
import com.fimbleenterprises.sportsdb.data.model.ScheduledGame
import kotlinx.coroutines.flow.Flow

/**
 * This interface is what Room will use to actually perform CRUD operations in the db.
 */
@Dao // This annotation turns this interface into a magical mechanism to actually perform CRUD operations in the Room db.
interface EventsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(scheduledGame: ScheduledGame):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(scheduledGames: List<ScheduledGame>):List<Long>

    @Update
    suspend fun updateEvent(scheduledGame: ScheduledGame) : Int

    @Query("DELETE FROM events")
    suspend fun deleteAll() : Int

    @Query("SELECT * FROM events ")
    fun getSavedEvents(): Flow<List<ScheduledGame>>

}