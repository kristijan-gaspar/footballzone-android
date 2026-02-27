package hr.algebra.footballzone.api.dto.leagueDto

import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("name") val name: String,
    @SerializedName("code") val code: String,
    @SerializedName("flag") val flag: String

)
