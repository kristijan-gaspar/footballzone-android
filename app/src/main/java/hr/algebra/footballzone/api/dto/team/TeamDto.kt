package hr.algebra.footballzone.api.dto.team

import com.google.gson.annotations.SerializedName

data class TeamDto(
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("code") val code : String,
    @SerializedName("country") val country : String,
    @SerializedName("founded") val founded : Int,
    @SerializedName("national") val national : Boolean,
    @SerializedName("logo") val logo : String
)
