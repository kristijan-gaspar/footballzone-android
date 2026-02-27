package hr.algebra.footballzone.api.dto.team

import com.google.gson.annotations.SerializedName

data class TeamResponseDto(
    @SerializedName("team") val team : TeamDto,
)
