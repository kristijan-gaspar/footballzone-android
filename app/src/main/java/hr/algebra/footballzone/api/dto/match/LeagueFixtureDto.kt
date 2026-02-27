package hr.algebra.footballzone.api.dto.match

import com.google.gson.annotations.SerializedName

data class LeagueFixtureDto (

    @SerializedName("id") val id : Int,
    @SerializedName("season") val season : Int,
    @SerializedName("round") val round : String,

)

