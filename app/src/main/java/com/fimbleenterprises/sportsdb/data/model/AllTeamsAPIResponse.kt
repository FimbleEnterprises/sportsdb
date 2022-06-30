package com.fimbleenterprises.sportsdb.data.model


import com.google.gson.annotations.SerializedName

// Pretty fucking simple response when querying teams in a league.
data class AllTeamsAPIResponse(
    @SerializedName("teams")
    val sportsTeams: List<SportsTeam>?
)