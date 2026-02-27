package hr.algebra.footballzone.model

data class Standing(
    var _id: Long?,
    val teamId: Int,
    val leagueId: Int,
    val rank: Int,
    val points: Int,
    val goalsDiff: Int

)
