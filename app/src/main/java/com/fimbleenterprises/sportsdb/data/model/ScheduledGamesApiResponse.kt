package com.fimbleenterprises.sportsdb.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ScheduledGamesApiResponse(
    @SerializedName("events")
    val scheduledGames: List<ScheduledGame>?
) : Serializable