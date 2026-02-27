package hr.algebra.footballzone.api.dto.team

import com.google.gson.annotations.SerializedName

data class TeamsApiResponseDto(
    @SerializedName("response") val response: List<TeamResponseDto>
)
