package hr.algebra.footballzone.model

data class League(
    var _id: Int,
    val name: String,
    val country: String,
    val logoPath: String,
    val season: Int
)
