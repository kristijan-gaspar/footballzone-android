package hr.algebra.footballzone.api.dto.match

import com.google.gson.annotations.SerializedName

data class GoalsDto (

    @SerializedName("home") val home : Int,
    @SerializedName("away") val away : Int
)
