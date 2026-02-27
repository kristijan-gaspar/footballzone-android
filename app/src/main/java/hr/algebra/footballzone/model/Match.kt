package hr.algebra.footballzone.model

data class Match(
    var _id: Int,
    val leagueId: Int,
    val homeTeamId: Int,
    val awayTeamId: Int,
    val homeTeamScore: Int,
    val awayTeamScore: Int,
    val round: Int,
    val date: String,
    val status: String,
    val venue: String,
    val city: String,
    val referee: String
){
    val score: String
        get() = "$homeTeamScore : $awayTeamScore"
}
