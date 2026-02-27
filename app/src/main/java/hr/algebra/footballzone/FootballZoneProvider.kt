package hr.algebra.footballzone

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import hr.algebra.footballzone.dao.FootballZoneContract
import hr.algebra.footballzone.dao.Repository
import hr.algebra.footballzone.dao.getRepository
import hr.algebra.footballzone.model.Favorite
import hr.algebra.footballzone.model.League
import hr.algebra.footballzone.model.Match
import hr.algebra.footballzone.model.Standing
import hr.algebra.footballzone.model.Team

private const val TEAMS = 10
private const val TEAM_ID = 11
private const val LEAGUES = 20
private const val LEAGUE_ID = 21
private const val MATCHES = 30
private const val MATCH_ID = 31
private const val STANDINGS = 40
private const val STANDING_ID = 41
private const val FAVORITES = 50
private const val FAVORITE_ID = 51

private val URI_MATCHER = UriMatcher(UriMatcher.NO_MATCH).apply {
    addURI(FootballZoneContract.AUTHORITY, FootballZoneContract.PATH_TEAMS, TEAMS)
    addURI(FootballZoneContract.AUTHORITY, "${FootballZoneContract.PATH_TEAMS}/#", TEAM_ID)

    addURI(FootballZoneContract.AUTHORITY, FootballZoneContract.PATH_LEAGUES, LEAGUES)
    addURI(FootballZoneContract.AUTHORITY, "${FootballZoneContract.PATH_LEAGUES}/#", LEAGUE_ID)

    addURI(FootballZoneContract.AUTHORITY, FootballZoneContract.PATH_MATCHES, MATCHES)
    addURI(FootballZoneContract.AUTHORITY, "${FootballZoneContract.PATH_MATCHES}/#", MATCH_ID)

    addURI(FootballZoneContract.AUTHORITY, FootballZoneContract.PATH_STANDINGS, STANDINGS)
    addURI(FootballZoneContract.AUTHORITY, "${FootballZoneContract.PATH_STANDINGS}/#", STANDING_ID)

    addURI(FootballZoneContract.AUTHORITY, FootballZoneContract.PATH_FAVORITES, FAVORITES)
    addURI(FootballZoneContract.AUTHORITY, "${FootballZoneContract.PATH_FAVORITES}/#", FAVORITE_ID)
}

class FootballZoneProvider : ContentProvider() {

    private lateinit var teamsRepo: Repository
    private lateinit var leaguesRepo: Repository
    private lateinit var matchesRepo: Repository
    private lateinit var standingsRepo: Repository
    private lateinit var favoritesRepo: Repository

    override fun onCreate(): Boolean {
        teamsRepo = getRepository(context, FootballZoneContract.Tables.TEAMS)
        leaguesRepo = getRepository(context, FootballZoneContract.Tables.LEAGUES)
        matchesRepo = getRepository(context, FootballZoneContract.Tables.MATCHES)
        standingsRepo = getRepository(context, FootballZoneContract.Tables.STANDINGS)
        favoritesRepo = getRepository(context, FootballZoneContract.Tables.FAVORITES)
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val id = when (URI_MATCHER.match(uri)) {
            TEAMS -> teamsRepo.insert(values)
            LEAGUES -> leaguesRepo.insert(values)
            MATCHES -> matchesRepo.insert(values)
            STANDINGS -> standingsRepo.insert(values)
            FAVORITES -> favoritesRepo.insert(values)
            else -> throw IllegalArgumentException("Wrong URI: $uri")
        }
        return ContentUris.withAppendedId(uri, id)
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        return when (URI_MATCHER.match(uri)) {
            TEAMS -> teamsRepo.query(projection, selection, selectionArgs, sortOrder)
            LEAGUES -> leaguesRepo.query(projection, selection, selectionArgs, sortOrder)
            MATCHES -> matchesRepo.query(projection, selection, selectionArgs, sortOrder)
            STANDINGS -> standingsRepo.query(projection, selection, selectionArgs, sortOrder)
            FAVORITES -> favoritesRepo.query(projection, selection, selectionArgs, sortOrder)
            else -> throw IllegalArgumentException("Wrong URI: $uri")
        }
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return when (URI_MATCHER.match(uri)) {
            TEAMS -> teamsRepo.update(values, selection, selectionArgs)
            TEAM_ID -> teamsRepo.update(
                values,
                "${Team::_id.name}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            LEAGUES -> leaguesRepo.update(values, selection, selectionArgs)
            LEAGUE_ID -> leaguesRepo.update(
                values,
                "${League::_id.name}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            MATCHES -> matchesRepo.update(values, selection, selectionArgs)
            MATCH_ID -> matchesRepo.update(
                values,
                "${Match::_id.name}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            STANDINGS -> standingsRepo.update(values, selection, selectionArgs)
            STANDING_ID -> standingsRepo.update(
                values,
                "${Standing::_id.name}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            FAVORITES -> favoritesRepo.update(values, selection, selectionArgs)
            FAVORITE_ID -> favoritesRepo.update(
                values,
                "${Favorite::_id.name}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            else -> throw IllegalArgumentException("Wrong URI: $uri")
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return when (URI_MATCHER.match(uri)) {
            TEAMS -> teamsRepo.delete(selection, selectionArgs)
            TEAM_ID -> teamsRepo.delete("${Team::_id.name}=?", arrayOf(uri.lastPathSegment!!))

            LEAGUES -> leaguesRepo.delete(selection, selectionArgs)
            LEAGUE_ID -> leaguesRepo.delete("${League::_id.name}=?", arrayOf(uri.lastPathSegment!!))

            MATCHES -> matchesRepo.delete(selection, selectionArgs)
            MATCH_ID -> matchesRepo.delete("${Match::_id.name}=?", arrayOf(uri.lastPathSegment!!))

            STANDINGS -> standingsRepo.delete(selection, selectionArgs)
            STANDING_ID -> standingsRepo.delete(
                "${Standing::_id.name}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            FAVORITES -> favoritesRepo.delete(selection, selectionArgs)
            FAVORITE_ID -> favoritesRepo.delete(
                "${Favorite::_id.name}=?",
                arrayOf(uri.lastPathSegment!!)
            )

            else -> throw IllegalArgumentException("Wrong URI: $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun bulkInsert(uri: Uri, values: Array<out ContentValues?>): Int {
        return when (URI_MATCHER.match(uri)) {
            TEAMS -> teamsRepo.bulkInsert(values)
            LEAGUES -> leaguesRepo.bulkInsert(values)
            MATCHES -> matchesRepo.bulkInsert(values)
            STANDINGS -> standingsRepo.bulkInsert(values)
            FAVORITES -> favoritesRepo.bulkInsert(values)
            else -> throw IllegalArgumentException("Wrong URI: $uri")
        }
    }


}