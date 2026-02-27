package hr.algebra.footballzone.mapper

import android.database.Cursor
import hr.algebra.footballzone.model.League
import hr.algebra.footballzone.model.Match
import hr.algebra.footballzone.model.Standing
import hr.algebra.footballzone.model.Team

fun Cursor.toLeagues(): List<League> {
    val leagues = mutableListOf<League>()
    while (moveToNext()) {
        leagues.add(
            League(
                _id = getInt(getColumnIndexOrThrow(League::_id.name)),
                name = getString(getColumnIndexOrThrow(League::name.name)),
                country = getString(getColumnIndexOrThrow(League::country.name)),
                logoPath = getString(getColumnIndexOrThrow(League::logoPath.name)),
                season = getInt(getColumnIndexOrThrow(League::season.name))
            )
        )
    }
    close()
    return leagues
}

fun Cursor.toLeague(): League? {
    if (!moveToFirst()) return null
    moveToFirst()
    return League(
        _id = getInt(getColumnIndexOrThrow(League::_id.name)),
        name = getString(getColumnIndexOrThrow(League::name.name)),
        country = getString(getColumnIndexOrThrow(League::country.name)),
        logoPath = getString(getColumnIndexOrThrow(League::logoPath.name)),
        season = getInt(getColumnIndexOrThrow(League::season.name))
    )
}


fun Cursor.toTeams(): List<Team> {
    val teams = mutableListOf<Team>()
    while (moveToNext()) {
        teams.add(
            Team(
                _id = getInt(getColumnIndexOrThrow(Team::_id.name)),
                name = getString(getColumnIndexOrThrow(Team::name.name)),
                logoPath = getString(getColumnIndexOrThrow(Team::logoPath.name)),
                code = getString(getColumnIndexOrThrow(Team::code.name))
            )
        )
    }
    close()
    return teams
}

fun Cursor.toTeam(): Team? {
    if (!moveToFirst()) return null
    return Team(
        _id = getInt(getColumnIndexOrThrow(Team::_id.name)),
        name = getString(getColumnIndexOrThrow(Team::name.name)),
        logoPath = getString(getColumnIndexOrThrow(Team::logoPath.name)),
        code = getString(getColumnIndexOrThrow(Team::code.name))
    )
}


fun Cursor.toMatches(): List<Match> {
    val matches = mutableListOf<Match>()
    while (moveToNext()) {
        matches.add(
            Match(
                _id = getInt(getColumnIndexOrThrow(Match::_id.name)),
                leagueId = getInt(getColumnIndexOrThrow(Match::leagueId.name)),
                homeTeamId = getInt(getColumnIndexOrThrow(Match::homeTeamId.name)),
                awayTeamId = getInt(getColumnIndexOrThrow(Match::awayTeamId.name)),
                homeTeamScore = getInt(getColumnIndexOrThrow(Match::homeTeamScore.name)),
                awayTeamScore = getInt(getColumnIndexOrThrow(Match::awayTeamScore.name)),
                round = getInt(getColumnIndexOrThrow(Match::round.name)),
                date = getString(getColumnIndexOrThrow(Match::date.name)),
                status = getString(getColumnIndexOrThrow(Match::status.name)),
                venue = getString(getColumnIndexOrThrow(Match::venue.name)),
                city = getString(getColumnIndexOrThrow(Match::city.name)),
                referee = getString(getColumnIndexOrThrow(Match::referee.name))
            )
        )
    }
    close()
    return matches
}

fun Cursor.toMatch(): Match? {
    if (!moveToFirst()) return null
    return Match(
        _id = getInt(getColumnIndexOrThrow(Match::_id.name)),
        leagueId = getInt(getColumnIndexOrThrow(Match::leagueId.name)),
        homeTeamId = getInt(getColumnIndexOrThrow(Match::homeTeamId.name)),
        awayTeamId = getInt(getColumnIndexOrThrow(Match::awayTeamId.name)),
        homeTeamScore = getInt(getColumnIndexOrThrow(Match::homeTeamScore.name)),
        awayTeamScore = getInt(getColumnIndexOrThrow(Match::awayTeamScore.name)),
        round = getInt(getColumnIndexOrThrow(Match::round.name)),
        date = getString(getColumnIndexOrThrow(Match::date.name)),
        status = getString(getColumnIndexOrThrow(Match::status.name)),
        venue = getString(getColumnIndexOrThrow(Match::venue.name)),
        city = getString(getColumnIndexOrThrow(Match::city.name)),
        referee = getString(getColumnIndexOrThrow(Match::referee.name))
    )
}




fun Cursor.toStandings(): List<Standing> {
    val standings = mutableListOf<Standing>()
    while (moveToNext()) {
        standings.add(
            Standing(
                _id = getLong(getColumnIndexOrThrow(Standing::_id.name)),
                teamId = getInt(getColumnIndexOrThrow(Standing::teamId.name)),
                leagueId = getInt(getColumnIndexOrThrow(Standing::leagueId.name)),
                rank = getInt(getColumnIndexOrThrow(Standing::rank.name)),
                points = getInt(getColumnIndexOrThrow(Standing::points.name)),
                goalsDiff = getInt(getColumnIndexOrThrow(Standing::goalsDiff.name))
            )
        )
    }
    close()
    return standings
}


fun Cursor.toIdLogoMap(): Map<Int, String> {
    val map = mutableMapOf<Int, String>()
    use {
        while (moveToNext()) {
            val id = getInt(getColumnIndexOrThrow("_id"))
            val logoPath = getString(getColumnIndexOrThrow("logoPath"))
            map[id] = logoPath
        }
    }
    return map
}

