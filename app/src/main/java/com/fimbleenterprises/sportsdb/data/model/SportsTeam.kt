package com.fimbleenterprises.sportsdb.data.model


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import javax.annotation.Nullable

@Entity(
    tableName =  "teams"
)
data class SportsTeam (
    @SerializedName("idTeam")
    @PrimaryKey(autoGenerate = false)
    val idTeam: String,
    @SerializedName("following")
    var following: Boolean,
    @SerializedName("idAPIfootball")
    val idAPIfootball: String?,
    @SerializedName("idLeague")
    val idLeague: String?,
    @SerializedName("idLeague2")
    val idLeague2: Any?,
    @SerializedName("idLeague3")
    val idLeague3: Any?,
    @SerializedName("idLeague4")
    val idLeague4: Any?,
    @SerializedName("idLeague5")
    val idLeague5: Any?,
    @SerializedName("idLeague6")
    val idLeague6: Any?,
    @SerializedName("idLeague7")
    val idLeague7: Any?,
    @SerializedName("idSoccerXML")
    val idSoccerXML: Any?,
    @SerializedName("intFormedYear")
    val intFormedYear: String?,
    @SerializedName("intLoved")
    val intLoved: Any?,
    @SerializedName("intStadiumCapacity")
    val intStadiumCapacity: String?,
    @SerializedName("strAlternate")
    val strAlternate: String?,
    @SerializedName("strCountry")
    val strCountry: String?,
    @SerializedName("strDescriptionCN")
    val strDescriptionCN: Any?,
    @SerializedName("strDescriptionDE")
    val strDescriptionDE: Any?,
    @SerializedName("strDescriptionEN")
    val strDescriptionEN: String?,
    @SerializedName("strDescriptionES")
    val strDescriptionES: Any?,
    @SerializedName("strDescriptionFR")
    val strDescriptionFR: Any?,
    @SerializedName("strDescriptionHU")
    val strDescriptionHU: Any?,
    @SerializedName("strDescriptionIL")
    val strDescriptionIL: Any?,
    @SerializedName("strDescriptionIT")
    val strDescriptionIT: Any?,
    @SerializedName("strDescriptionJP")
    val strDescriptionJP: Any?,
    @SerializedName("strDescriptionNL")
    val strDescriptionNL: Any?,
    @SerializedName("strDescriptionNO")
    val strDescriptionNO: Any?,
    @SerializedName("strDescriptionPL")
    val strDescriptionPL: Any?,
    @SerializedName("strDescriptionPT")
    val strDescriptionPT: Any?,
    @SerializedName("strDescriptionRU")
    val strDescriptionRU: Any?,
    @SerializedName("strDescriptionSE")
    val strDescriptionSE: Any?,
    @SerializedName("strDivision")
    val strDivision: Any?,
    @SerializedName("strFacebook")
    val strFacebook: String?,
    @SerializedName("strGender")
    val strGender: String?,
    @SerializedName("strInstagram")
    val strInstagram: String?,
    @SerializedName("strKeywords")
    val strKeywords: String?,
    @SerializedName("strKitColour1")
    val strKitColour1: Any?,
    @SerializedName("strKitColour2")
    val strKitColour2: Any?,
    @SerializedName("strKitColour3")
    val strKitColour3: Any?,
    @SerializedName("strLeague")
    val strLeague: String?,
    @SerializedName("strLeague2")
    val strLeague2: String?,
    @SerializedName("strLeague3")
    val strLeague3: String?,
    @SerializedName("strLeague4")
    val strLeague4: String?,
    @SerializedName("strLeague5")
    val strLeague5: String?,
    @SerializedName("strLeague6")
    val strLeague6: String?,
    @SerializedName("strLeague7")
    val strLeague7: String?,
    @SerializedName("strLocked")
    val strLocked: String?,
    @SerializedName("strManager")
    val strManager: String?,
    @SerializedName("strRSS")
    val strRSS: String?,
    @SerializedName("strSport")
    val strSport: String?,
    @SerializedName("strStadium")
    val strStadium: String?,
    @SerializedName("strStadiumDescription")
    val strStadiumDescription: String?,
    @SerializedName("strStadiumLocation")
    val strStadiumLocation: String?,
    @SerializedName("strStadiumThumb")
    val strStadiumThumb: String?,
    @SerializedName("strTeam")
    var strTeam: String?,
    @SerializedName("strTeamBadge")
    val strTeamBadge: String?,
    @SerializedName("strTeamBanner")
    val strTeamBanner: String?,
    @SerializedName("strTeamFanart1")
    val strTeamFanart1: String?,
    @SerializedName("strTeamFanart2")
    val strTeamFanart2: String?,
    @SerializedName("strTeamFanart3")
    val strTeamFanart3: String?,
    @SerializedName("strTeamFanart4")
    val strTeamFanart4: String?,
    @SerializedName("strTeamJersey")
    val strTeamJersey: String?,
    @SerializedName("strTeamLogo")
    val strTeamLogo: String?,
    @SerializedName("strTeamShort")
    val strTeamShort: String?,
    @SerializedName("strTwitter")
    val strTwitter: String?,
    @SerializedName("strWebsite")
    val strWebsite: String?,
    @SerializedName("strYoutube")
    val strYoutube: String?
) : Serializable {
    override fun toString(): String {
        return strTeam.toString()
    }
}