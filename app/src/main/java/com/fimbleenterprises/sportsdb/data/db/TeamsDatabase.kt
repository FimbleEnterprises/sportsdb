package com.fimbleenterprises.sportsdb.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fimbleenterprises.sportsdb.data.model.SportsTeam

// This annotation is what gets Room to actually create and maintain a single table in the database.
@Database(
    entities = [SportsTeam::class],
    version =  3,
    exportSchema = false
)
// OPTIONAL
// Converters is a custom class used to handle data type conversions for properties of your
// object that are not standard db data types.  We need conversion functions to convert those
// properties to data types that Room can work with.  This is not required if all of your properties
// for the class (Article in this case) are standard db type properties.
@TypeConverters(Converters::class)
/** Since this abstract class overrides the RoomDatabase class, Room will look to the abstract functions
 * it contains to determine what db operations to perform.  It determines this by interpreting the
 * the magic interfaces you created (naming convention: [some_name]DAO etc.) that these functions
 * must implement.  Shit's confusing for sure but pretty cool and gets cooler the more you learn it.
 */
abstract class TeamsDatabase : RoomDatabase() {
    // This function will get
    abstract fun getTeamsDAO() : TeamsDAO
}