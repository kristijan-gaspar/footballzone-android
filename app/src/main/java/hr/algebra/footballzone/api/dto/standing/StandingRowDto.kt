package hr.algebra.footballzone.api.dto.standing

import com.google.gson.annotations.SerializedName

data class StandingRowDto (

    @SerializedName("rank") val rank : Int,
    @SerializedName("team") val team : StandingTeamDto,
    @SerializedName("points") val points : Int,
    @SerializedName("goalsDiff") val goalsDiff : Int,
)