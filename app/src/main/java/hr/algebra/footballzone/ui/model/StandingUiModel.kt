package hr.algebra.footballzone.ui.model

data class StandingUiModel(
    val rank: Int,
    val teamName: String,
    val teamLogoPath: String,
    val points: Int,
    val goalsDiff: Int
)
