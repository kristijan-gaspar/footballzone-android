package hr.algebra.footballzone.api.dto.match

import com.google.gson.annotations.SerializedName

data class TeamsDto(
    @SerializedName("home") val home : HomeTeamDto,
    @SerializedName("away") val away : AwayTeamDto
)
