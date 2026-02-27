package hr.algebra.footballzone.api.dto.standing

import com.google.gson.annotations.SerializedName
import hr.algebra.footballzone.api.dto.standing.StandingRowDto

data class LeagueStandingsDto (

    @SerializedName("id") val id : Int,
    @SerializedName("name") val name : String,
    @SerializedName("country") val country : String,
    @SerializedName("logo") val logo : String,
    @SerializedName("flag") val flag : String,
    @SerializedName("season") val season : Int,
    @SerializedName("standings") val standings : List<List<StandingRowDto>>
)