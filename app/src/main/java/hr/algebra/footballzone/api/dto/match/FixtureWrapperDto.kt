package hr.algebra.footballzone.api.dto.match

import com.google.gson.annotations.SerializedName

data class FixtureWrapperDto (

    @SerializedName("fixture") val fixture : FixtureDto,
    @SerializedName("league") val league : LeagueFixtureDto,
    @SerializedName("teams") val teams : TeamsDto,
    @SerializedName("goals") val goals : GoalsDto
)