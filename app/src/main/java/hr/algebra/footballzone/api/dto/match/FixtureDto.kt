package hr.algebra.footballzone.api.dto.match

import com.google.gson.annotations.SerializedName

data class FixtureDto (
    @SerializedName("id") val id : Int,
    @SerializedName("referee") val referee : String,
    @SerializedName("timezone") val timezone : String,
    @SerializedName("date") val date : String,
    @SerializedName("timestamp") val timestamp : Int,
    @SerializedName("venue") val venue : VenueDto,
    @SerializedName("status") val status : StatusDto
)
