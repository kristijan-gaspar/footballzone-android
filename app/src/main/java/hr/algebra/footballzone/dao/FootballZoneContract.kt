package hr.algebra.footballzone.dao

import android.net.Uri
import android.provider.BaseColumns
import androidx.core.net.toUri

object FootballZoneContract {
    internal const val AUTHORITY = "hr.algebra.footballzone.provider"
    private val BASE_URI: Uri = "content://$AUTHORITY".toUri()

    val TEAMS_URI: Uri = "$BASE_URI/${Tables.TEAMS}".toUri()
    val LEAGUES_URI: Uri = "$BASE_URI/${Tables.LEAGUES}".toUri()
    val MATCHES_URI: Uri = "$BASE_URI/${Tables.MATCHES}".toUri()
    val STANDINGS_URI: Uri = "$BASE_URI/${Tables.STANDINGS}".toUri()
    val FAVORITES_URI: Uri = "$BASE_URI/${Tables.FAVORITES}".toUri()


    const val PATH_TEAMS = "teams"
    const val PATH_LEAGUES = "leagues"
    const val PATH_MATCHES = "matches"
    const val PATH_STANDINGS = "standings"
    const val PATH_FAVORITES = "favorites"

    object Tables {
        const val TEAMS = "teams"
        const val LEAGUES = "leagues"
        const val MATCHES = "matches"
        const val STANDINGS = "standings"
        const val FAVORITES = "favorites"
    }
}






