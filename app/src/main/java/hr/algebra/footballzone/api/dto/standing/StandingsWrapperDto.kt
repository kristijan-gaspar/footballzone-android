package hr.algebra.footballzone.api.dto.standing

import com.google.gson.annotations.SerializedName
import hr.algebra.footballzone.api.dto.standing.LeagueStandingsDto

data class StandingsWrapperDto (

    @SerializedName("league") val league : LeagueStandingsDto
)