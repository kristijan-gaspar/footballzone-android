package hr.algebra.footballzone.mapper

import android.content.ContentValues
import hr.algebra.footballzone.model.Favorite
import hr.algebra.footballzone.model.League
import hr.algebra.footballzone.model.Match
import hr.algebra.footballzone.model.Standing
import hr.algebra.footballzone.model.Team

fun League.toContentValues(): ContentValues =
    ContentValues().apply {
        put(League::_id.name, _id)
        put(League::name.name, name)
        put(League::country.name, country)
        put(League::logoPath.name, logoPath)
        put(League::season.name, season)
    }

fun Team.toContentValues(): ContentValues =
    ContentValues().apply {
        put(Team::_id.name, _id)
        put(Team::name.name, name)
        put(Team::logoPath.name, logoPath)
        put(Team::code.name, code)
    }

fun Standing.toContentValues(): ContentValues =
    ContentValues().apply {
        put(Standing::teamId.name, teamId)
        put(Standing::leagueId.name, leagueId)
        put(Standing::rank.name, rank)
        put(Standing::points.name, points)
        put(Standing::goalsDiff.name, goalsDiff)
    }

fun Match.toContentValues(): ContentValues =
    ContentValues().apply {
        put(Match::_id.name, _id)
        put(Match::leagueId.name, leagueId)
        put(Match::homeTeamId.name, homeTeamId)
        put(Match::awayTeamId.name, awayTeamId)
        put(Match::homeTeamScore.name, homeTeamScore)
        put(Match::awayTeamScore.name, awayTeamScore)
        put(Match::round.name, round)
        put(Match::date.name, date)
        put(Match::status.name, status)
        put(Match::venue.name, venue)
        put(Match::city.name, city)
        put(Match::referee.name, referee)
    }


fun Favorite.toContentValues(): ContentValues =
    ContentValues().apply {
        put(Favorite::matchId.name, matchId)
    }