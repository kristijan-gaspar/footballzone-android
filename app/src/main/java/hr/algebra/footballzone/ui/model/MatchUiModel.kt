package hr.algebra.footballzone.ui.model

data class MatchUiModel(
    val matchId: Int,
    val homeCode: String,
    val awayCode: String,
    val homeLogoPath: String,
    val awayLogoPath: String,
    val homeScore: Int,
    val awayScore: Int,
    val round: Int,
    val date: String,
    val status: String
){
    val score: String
        get() = "$homeScore : $awayScore"
}
