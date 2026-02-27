package hr.algebra.footballzone.api.dto.leagueDto

import com.google.gson.annotations.SerializedName

data class LeagueDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("logo") val logo: String

)
