package hr.algebra.footballzone.api.dto.leagueDto

import com.google.gson.annotations.SerializedName

data class LeagueResponseDto(
    @SerializedName("league") val league: LeagueDto,
    @SerializedName("country") val country: CountryDto,
    @SerializedName("seasons") val seasons: List<SeasonDto>
)
