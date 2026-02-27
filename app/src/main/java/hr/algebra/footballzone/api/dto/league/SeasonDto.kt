package hr.algebra.footballzone.api.dto.leagueDto

import com.google.gson.annotations.SerializedName

data class SeasonDto(
    @SerializedName("year") val year : Int,
)
