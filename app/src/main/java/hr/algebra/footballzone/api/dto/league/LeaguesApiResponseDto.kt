package hr.algebra.footballzone.api.dto.leagueDto

import com.google.gson.annotations.SerializedName

data class LeaguesApiResponseDto(
    @SerializedName("response") val response: List<LeagueResponseDto>
)