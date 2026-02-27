package hr.algebra.footballzone.ui.model

data class FavoriteMatchUiModel(
    val matchId: Int,
    val homeLogoPath: String,
    val awayLogoPath: String,
    val homeTeamCode: String,
    val awayTeamCode: String,
    val homeScore: Int,
    val awayScore: Int,
) {
    val score: String
        get() = "$homeScore : $awayScore"
}
