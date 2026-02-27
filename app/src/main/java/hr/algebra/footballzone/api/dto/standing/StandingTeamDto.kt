package hr.algebra.footballzone.api.dto.standing

import com.google.gson.annotations.SerializedName

data class StandingTeamDto(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("logo") val logo : String

)
