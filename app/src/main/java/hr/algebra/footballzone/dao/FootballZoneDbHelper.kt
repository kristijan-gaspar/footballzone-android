package hr.algebra.footballzone.dao

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import hr.algebra.footballzone.dao.FootballZoneContract.Tables
import hr.algebra.footballzone.model.Favorite
import hr.algebra.footballzone.model.League
import hr.algebra.footballzone.model.Match
import hr.algebra.footballzone.model.Standing
import hr.algebra.footballzone.model.Team

private const val DB_NAME = "football.db"
private const val DB_VERSION = 1

class FootballZoneDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_LEAGUES_TABLE)
        db?.execSQL(CREATE_TEAMS_TABLE)
        db?.execSQL(CREATE_MATCHES_TABLE)
        db?.execSQL(CREATE_STANDINGS_TABLE)
        db?.execSQL(CREATE_FAVORITES_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        db?.execSQL(DROP_LEAGUES_TABLE)
        db?.execSQL(DROP_TEAMS_TABLE)
        db?.execSQL(DROP_MATCHES_TABLE)
        db?.execSQL(DROP_STANDINGS_TABLE)
        db?.execSQL(DROP_FAVORITES_TABLE)
        onCreate(db)
    }
}

private val CREATE_LEAGUES_TABLE = "create table ${Tables.LEAGUES}( " +
        "${League::_id.name} integer primary key unique, " +
        "${League::name.name} text not null, " +
        "${League::country.name} text not null, " +
        "${League::logoPath.name} text not null, " +
        "${League::season.name} integer not null" +
        ")"


private val CREATE_TEAMS_TABLE = "create table ${Tables.TEAMS}( " +
        "${Team::_id.name} integer primary key unique, " +
        "${Team::name.name} text not null, " +
        "${Team::code.name} text not null, " +
        "${Team::logoPath.name} text not null" +
        ")"


private val CREATE_MATCHES_TABLE = "create table ${Tables.MATCHES}( " +
        "${Match::_id.name} integer primary key unique, " +
        "${Match::leagueId.name} integer not null, " +
        "${Match::homeTeamId.name} integer not null, " +
        "${Match::awayTeamId.name} integer not null, " +
        "${Match::homeTeamScore.name} integer not null, " +
        "${Match::awayTeamScore.name} integer not null, " +
        "${Match::round.name} integer not null, " +
        "${Match::date.name} text not null, " +
        "${Match::status.name} text not null, " +
        "${Match::venue.name} text not null, " +
        "${Match::city.name} text not null," +
        "${Match::referee.name} text not null" +
        ")"

private val CREATE_STANDINGS_TABLE = "create table ${Tables.STANDINGS}( " +
        "${Standing::_id.name} integer primary key autoincrement, " +
        "${Standing::leagueId.name} integer not null, " +
        "${Standing::teamId.name} integer not null, " +
        "${Standing::rank.name} integer not null, " +
        "${Standing::points.name} integer not null, " +
        "${Standing::goalsDiff.name} integer not null" +
        ")"

private val CREATE_FAVORITES_TABLE = "create table ${Tables.FAVORITES}( " +
        "${Favorite::_id.name} integer primary key autoincrement, " +
        "${Favorite::matchId.name} integer not null" +
        ")"

private const val DROP_LEAGUES_TABLE = "drop table ${Tables.LEAGUES}"
private const val DROP_TEAMS_TABLE = "drop table ${Tables.TEAMS}"
private const val DROP_MATCHES_TABLE = "drop table ${Tables.MATCHES}"
private const val DROP_STANDINGS_TABLE = "drop table ${Tables.STANDINGS}"
private const val DROP_FAVORITES_TABLE = "drop table ${Tables.FAVORITES}"
