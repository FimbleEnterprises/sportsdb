package com.fimbleenterprises.sportsdb.data.model


import android.util.Log
import com.fimbleenterprises.sportsdb.util.Helpers
import com.google.gson.annotations.SerializedName
import org.joda.time.DateTime
import java.io.Serializable

data class GameResult (
    @SerializedName("genericvalue")
    val genericValue: String? = null,
    @SerializedName("dateEvent")
    val dateEvent: String? = null,
    @SerializedName("dateEventLocal")
    val dateEventLocal: String? = null,
    @SerializedName("idAPIfootball")
    val idAPIfootball: String? = null,
    @SerializedName("idAwayTeam")
    val idAwayTeam: String? = null,
    @SerializedName("idEvent")
    val idEvent: String? = null,
    @SerializedName("idHomeTeam")
    val idHomeTeam: String? = null,
    @SerializedName("idLeague")
    val idLeague: String? = null,
    @SerializedName("idSoccerXML")
    val idSoccerXML: Any? = null,
    @SerializedName("intAwayScore")
    val intAwayScore: String? = null,
    @SerializedName("intHomeScore")
    val intHomeScore: String? = null,
    @SerializedName("intRound")
    val intRound: String? = null,
    @SerializedName("intScore")
    val intScore: Any? = null,
    @SerializedName("intScoreVotes")
    val intScoreVotes: Any? = null,
    @SerializedName("intSpectators")
    val intSpectators: Any? = null,
    @SerializedName("strAwayTeam")
    val strAwayTeam: String? = null,
    @SerializedName("strBanner")
    val strBanner: Any? = null,
    @SerializedName("strCity")
    val strCity: String? = null,
    @SerializedName("strCountry")
    val strCountry: String? = null,
    @SerializedName("strDescriptionEN")
    val strDescriptionEN: String? = null,
    @SerializedName("strEvent")
    val strEvent: String? = null,
    @SerializedName("strEventAlternate")
    val strEventAlternate: String? = null,
    @SerializedName("strFanart")
    val strFanart: Any? = null,
    @SerializedName("strFilename")
    val strFilename: String? = null,
    @SerializedName("strHomeTeam")
    val strHomeTeam: String? = null,
    @SerializedName("strLeague")
    val strLeague: String? = null,
    @SerializedName("strLocked")
    val strLocked: String? = null,
    @SerializedName("strMap")
    val strMap: Any? = null,
    @SerializedName("strOfficial")
    val strOfficial: Any? = null,
    @SerializedName("strPoster")
    val strPoster: Any? = null,
    @SerializedName("strPostponed")
    val strPostponed: String? = null,
    @SerializedName("strResult")
    val strResult: String? = null,
    @SerializedName("strSeason")
    val strSeason: String? = null,
    @SerializedName("strSport")
    val strSport: String? = null,
    @SerializedName("strSquare")
    val strSquare: Any? = null,
    @SerializedName("strStatus")
    val strStatus: String? = null,
    @SerializedName("strTVStation")
    val strTVStation: Any? = null,
    @SerializedName("strThumb")
    val strThumb: String? = null,
    @SerializedName("strTime")
    val strTime: String? = null,
    @SerializedName("strTimeLocal")
    val strTimeLocal: String? = null,
    @SerializedName("strTimestamp")
    val strTimestamp: String? = null,
    @SerializedName("strVenue")
    val strVenue: String? = null,
    @SerializedName("strVideo")
    val strVideo: String? = null
) : Serializable {

    /**
     * Attempts to convert the date and time to a DateTime object.
     */
    private fun toDateTime() : DateTime {

        return if (this.dateEvent != null && this.strTime != null) {
            if (strTime.startsWith("00:")) { // Noon seems to be returned as 00 so this hacky fix may work
                val correctedTime = strTime.replaceRange(0,3, "12:")
                DateTime.parse(dateEvent + "T" + correctedTime)
            } else {
                DateTime.parse(dateEvent + "T" + strTime)
            }
        } else if (this.dateEvent != null) {
            DateTime.parse(this.dateEvent)
        } else {
            DateTime(1900, 1, 1, 1, 1)
        }
    }

    fun toPrettyDateTime() : String {
        return Helpers.DatesAndTimes.getPrettyDateAndTime(toDateTime())
    }

    companion object {
        private const val TAG = "FIMTOWN|GameResult"
    }

    init {
        Log.i(TAG, "Initialized:GameResult")
    }

}