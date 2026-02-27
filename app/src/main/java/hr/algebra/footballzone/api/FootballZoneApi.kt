package hr.algebra.footballzone.api

import hr.algebra.footballzone.api.dto.match.FixturesApiResponseDto
import hr.algebra.footballzone.api.dto.standing.StandingsApiResponseDto
import hr.algebra.footballzone.api.dto.leagueDto.LeaguesApiResponseDto
import hr.algebra.footballzone.api.dto.team.TeamsApiResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface FootballZoneApi {

    @GET("leagues")
    suspend fun getLeague(
        @Query("id") country: Int,
        @Query("season") season: Int
    ): LeaguesApiResponseDto

    @GET("teams")
    suspend fun getTeams(
        @Query("league") league: Int,
        @Query("season") season: Int
    ): TeamsApiResponseDto

    @GET("standings")
    suspend fun getStandings(
        @Query("league") league: Int,
        @Query("season") season: Int
    ): StandingsApiResponseDto

    @GET("fixtures")
    suspend fun getFixtures(
        @Query("league") league: Int,
        @Query("season") season: Int
    ): FixturesApiResponseDto
}