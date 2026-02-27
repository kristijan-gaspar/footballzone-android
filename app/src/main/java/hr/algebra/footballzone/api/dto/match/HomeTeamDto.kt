package hr.algebra.footballzone.api.dto.match

import com.google.gson.annotations.SerializedName

data class HomeTeamDto (
    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("logo") val logo : String,
    @SerializedName("winner") val winner : Boolean
)
