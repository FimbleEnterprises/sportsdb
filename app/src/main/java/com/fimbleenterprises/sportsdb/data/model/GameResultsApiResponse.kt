package com.fimbleenterprises.sportsdb.data.model


import com.google.gson.annotations.SerializedName

data class GameResultsApiResponse(
    @SerializedName("results")
    val gameResults: List<GameResult>?
)