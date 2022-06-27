package com.fimbleenterprises.sportsdb.data.model


import com.google.gson.annotations.SerializedName

data class LeagueListApiResponse(
    @SerializedName("leagues")
    val leagues: List<League>
)