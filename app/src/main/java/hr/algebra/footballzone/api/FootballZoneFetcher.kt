package hr.algebra.footballzone.api

import android.content.Context
import android.util.Log
import hr.algebra.footballzone.handler.downloadImageAndStore
import hr.algebra.footballzone.R
import hr.algebra.footballzone.api.dto.leagueDto.LeaguesApiResponseDto
import hr.algebra.footballzone.api.dto.match.FixturesApiResponseDto
import hr.algebra.footballzone.api.dto.standing.StandingsApiResponseDto
import hr.algebra.footballzone.api.dto.team.TeamsApiResponseDto
import hr.algebra.footballzone.dao.FootballZoneContract
import hr.algebra.footballzone.mapper.toContentValues
import hr.algebra.footballzone.model.League
import hr.algebra.footballzone.model.Match
import hr.algebra.footballzone.model.Standing
import hr.algebra.footballzone.model.Team
import kotlinx.coroutines.delay


class FootballZoneFetcher(private val context: Context) {

    private val api: FootballZoneApi = ApiConfig.footballApi

    suspend fun fetchData() {
        val leagueIds = context.resources.getIntArray(R.array.league_ids).toList()
        val season = context.getString(R.string.season).toInt()

        leagueIds.forEach { leagueId ->
            val leagueResponse = api.getLeague(leagueId, season)
            val league = mapLeagueData(leagueResponse, season) ?: return@forEach

            delay(200)

            val teamsResponse = api.getTeams(leagueId, season)
            val teams = mapTeamsData(teamsResponse)

            delay(200)

            val standingsResponse = api.getStandings(leagueId, season)
            val standings = mapStandingsData(standingsResponse)

            delay(200)


            val fixturesResponse = api.getFixtures(leagueId, season)
            val matches = mapFixturesData(fixturesResponse)

            Log.i("API", "League fetched: $leagueId")

            saveLeague(league)
            if (teams.isNotEmpty()) saveTeams(teams)
            if (standings.isNotEmpty()) saveStandings(standings)
            if (matches.isNotEmpty()) saveMatches(matches)

            delay(200)
            Log.i("API", "League saved: ${league.name}")
        }
    }


    private fun mapLeagueData(response: LeaguesApiResponseDto, season: Int): League? {
        if (response.response.isEmpty()) {
            Log.e("API", "EMPTY league response")
            return null
        }
        val leagueResponseDto = response.response.first()
        val league = League(
            _id = leagueResponseDto.league.id,
            name = leagueResponseDto.league.name,
            country = leagueResponseDto.country.name,
            logoPath = leagueResponseDto.league.logo,
            season = season
        )
        return league
    }

    private fun mapTeamsData(response: TeamsApiResponseDto): List<Team> {
        if (response.response.isEmpty()) {
            Log.e("API", "EMPTY teams response")
            return emptyList()
        }
        val teams = mutableListOf<Team>()
        response.response.forEach { teamDto ->
            val team = Team(
                _id = teamDto.team.id,
                name = teamDto.team.name,
                logoPath = teamDto.team.logo,
                code = teamDto.team.code
            )
            teams.add(team)
        }
        return teams
    }


    private fun mapStandingsData(response: StandingsApiResponseDto): List<Standing> {
        if (response.response.isEmpty()) {
            Log.e("API", "EMPTY standings response")
            return emptyList()
        }
        val league = response.response.first().league
        val rows = league.standings.firstOrNull() ?: return emptyList()

        return rows.map { row ->
            Standing(
                _id = null,
                teamId = row.team.id,
                leagueId = league.id,
                rank = row.rank,
                points = row.points,
                goalsDiff = row.goalsDiff
            )
        }
    }

    private fun mapFixturesData(response: FixturesApiResponseDto): List<Match> {
        if (response.response.isEmpty()) {
            Log.e("API", "EMPTY matches response")
            return emptyList()
        }
        val matches = mutableListOf<Match>()
        response.response.forEach { fixtureWrapperDto ->
            val match = Match(
                _id = fixtureWrapperDto.fixture.id,
                leagueId = fixtureWrapperDto.league.id,
                homeTeamId = fixtureWrapperDto.teams.home.id,
                awayTeamId = fixtureWrapperDto.teams.away.id,
                homeTeamScore = fixtureWrapperDto.goals.home,
                awayTeamScore = fixtureWrapperDto.goals.away,
                round = fixtureWrapperDto.league.round.filter { it.isDigit() }.toIntOrNull() ?: 0,
                date = fixtureWrapperDto.fixture.date,
                status = fixtureWrapperDto.fixture.status.short,
                venue = fixtureWrapperDto.fixture.venue.name,
                city = fixtureWrapperDto.fixture.venue.city,
                referee = fixtureWrapperDto.fixture.referee
            )
            matches.add(match)
        }
        return matches
    }

    private fun saveLeague(league: League) {
        context.contentResolver.insert(
            FootballZoneContract.LEAGUES_URI,
            league.toContentValues()
        )
    }

    private fun saveTeams(teams: List<Team>) {
        context.contentResolver.bulkInsert(
            FootballZoneContract.TEAMS_URI,
            teams.map { it.toContentValues() }.toTypedArray()
        )
    }

    private fun saveStandings(standings: List<Standing>) {
        context.contentResolver.bulkInsert(
            FootballZoneContract.STANDINGS_URI,
            standings.map { it.toContentValues() }.toTypedArray()
        )
    }

    private fun saveMatches(matches: List<Match>) {
        context.contentResolver.bulkInsert(
            FootballZoneContract.MATCHES_URI,
            matches.map { it.toContentValues() }.toTypedArray()
        )
    }
}
