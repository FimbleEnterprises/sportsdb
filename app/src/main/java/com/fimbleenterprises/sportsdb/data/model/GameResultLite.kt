package com.fimbleenterprises.sportsdb.data.model

import com.google.gson.annotations.SerializedName

data class GameResultLite(
    @SerializedName("youtube_url")
    val dateEvent: String?,
    @SerializedName("dateEventLocal")
    val dateEventLocal: String?,
    @SerializedName("idAPIfootball")
    val idAPIfootball: String?,
    @SerializedName("idAwayTeam")
    val idAwayTeam: String?,
    @SerializedName("idEvent")
    val idEvent: String?
)