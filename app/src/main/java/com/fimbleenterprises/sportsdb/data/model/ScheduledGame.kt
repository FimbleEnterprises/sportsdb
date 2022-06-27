package com.fimbleenterprises.sportsdb.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName =  "events"
)
data class ScheduledGame(
    @SerializedName("rowid")
    @PrimaryKey(autoGenerate = true)
    val rowid:Int?,
    @SerializedName("idEvent")
    val idEvent: String?,
    @SerializedName("dateEvent")
    val dateEvent: String?,
    @SerializedName("dateEventLocal")
    val dateEventLocal: Any?,
    @SerializedName("idAPIfootball")
    val idAPIfootball: String?,
    @SerializedName("idAwayTeam")
    val idAwayTeam: String?,
    @SerializedName("idHomeTeam")
    val idHomeTeam: String?,
    @SerializedName("idLeague")
    val idLeague: String?,
    @SerializedName("idSoccerXML")
    val idSoccerXML: Any?,
    @SerializedName("intAwayScore")
    val intAwayScore: Any?,
    @SerializedName("intHomeScore")
    val intHomeScore: Any?,
    @SerializedName("intRound")
    val intRound: String?,
    @SerializedName("intScore")
    val intScore: Any?,
    @SerializedName("intScoreVotes")
    val intScoreVotes: Any?,
    @SerializedName("intSpectators")
    val intSpectators: Any?,
    @SerializedName("strAwayTeam")
    val strAwayTeam: String?,
    @SerializedName("strBanner")
    val strBanner: Any?,
    @SerializedName("strCity")
    val strCity: Any?,
    @SerializedName("strCountry")
    val strCountry: String?,
    @SerializedName("strDescriptionEN")
    val strDescriptionEN: Any?,
    @SerializedName("strEvent")
    val strEvent: String?,
    @SerializedName("strEventAlternate")
    val strEventAlternate: String?,
    @SerializedName("strFanart")
    val strFanart: Any?,
    @SerializedName("strFilename")
    val strFilename: String?,
    @SerializedName("strHomeTeam")
    val strHomeTeam: String?,
    @SerializedName("strLeague")
    val strLeague: String?,
    @SerializedName("strLocked")
    val strLocked: String?,
    @SerializedName("strMap")
    val strMap: Any?,
    @SerializedName("strOfficial")
    val strOfficial: Any?,
    @SerializedName("strPoster")
    val strPoster: Any?,
    @SerializedName("strPostponed")
    val strPostponed: String?,
    @SerializedName("strResult")
    val strResult: Any?,
    @SerializedName("strSeason")
    val strSeason: String?,
    @SerializedName("strSport")
    val strSport: String?,
    @SerializedName("strSquare")
    val strSquare: Any?,
    @SerializedName("strStatus")
    val strStatus: String?,
    @SerializedName("strTVStation")
    val strTVStation: Any?,
    @SerializedName("strThumb")
    val strThumb: Any?,
    @SerializedName("strTime")
    val strTime: String?,
    @SerializedName("strTimeLocal")
    val strTimeLocal: Any?,
    @SerializedName("strTimestamp")
    val strTimestamp: String?,
    @SerializedName("strTweet1")
    val strTweet1: Any?,
    @SerializedName("strTweet2")
    val strTweet2: Any?,
    @SerializedName("strTweet3")
    val strTweet3: Any?,
    @SerializedName("strVenue")
    val strVenue: Any?,
    @SerializedName("strVideo")
    val strVideo: Any?
): Serializable