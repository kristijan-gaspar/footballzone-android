package hr.algebra.footballzone.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.footballzone.R
import hr.algebra.footballzone.adapter.MatchesAdapter
import hr.algebra.footballzone.dao.FootballZoneContract
import hr.algebra.footballzone.databinding.FragmentMatchesBinding
import hr.algebra.footballzone.framework.navigate
import hr.algebra.footballzone.mapper.toMatches
import hr.algebra.footballzone.mapper.toTeams
import hr.algebra.footballzone.model.Match
import hr.algebra.footballzone.model.Team
import hr.algebra.footballzone.ui.model.MatchUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MatchesFragment : Fragment(R.layout.fragment_matches) {
    private var _binding: FragmentMatchesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MatchesAdapter
    private var leagueId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMatchesBinding.bind(view)

        initAdapter()
        setupRecyclerView()
        fetchAndDisplayStandings()

    }

    private fun initAdapter() {
        adapter = MatchesAdapter(emptyList()) { clickedLeague ->
            val bundle = Bundle().apply {
                putInt("matchId", clickedLeague.matchId)
            }
            navigate(R.id.matchDetailsFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        binding.rvMatches.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMatches.adapter = adapter
    }


    private fun readArguments() {
        arguments?.let {
            leagueId = it.getInt("leagueId")
        }
        if (leagueId == 0) {
            throw IllegalArgumentException("Missing leagueId argument")
        }
    }

    private fun fetchAndDisplayStandings() {
        lifecycleScope.launch(Dispatchers.IO) {
            val matchesUiData = fetchMatchesUiData()

            withContext(Dispatchers.Main) {
                adapter.submitList(matchesUiData)
            }
        }
    }

    private fun fetchMatchesUiData(): List<MatchUiModel> {
        val matches = fetchMatchesFromDb()
        if (matches.isEmpty()) {
            return emptyList()
        }

        val teamIds = extractTeamIds(matches)
        if (teamIds.isEmpty()) {
            return emptyList()
        }
        val teams = fetchTeamsByIdsFromDb(teamIds)

        return mapToUiModel(matches, teams)
    }

    private fun fetchMatchesFromDb(): List<Match> {

        return requireContext().contentResolver.query(
            FootballZoneContract.MATCHES_URI,
            null,
            "${Match::leagueId.name} = ?",
            arrayOf(leagueId.toString()),
            "${Match::date.name} ASC"
        )?.use { cursor ->
            cursor.toMatches()
        } ?: emptyList()
    }

    private fun extractTeamIds(matches: List<Match>): Array<String> {
        return matches
            .flatMap { listOf(it.homeTeamId, it.awayTeamId) }
            .distinct()
            .map { it.toString() }
            .toTypedArray()
    }

    private fun fetchTeamsByIdsFromDb(teamIds: Array<String>): List<Team> {
        val selection = "${Team::_id.name} IN (${teamIds.joinToString(separator = ",") { "?" }})"
        return requireContext().contentResolver.query(
            FootballZoneContract.TEAMS_URI,
            null,
            selection,
            teamIds,
            null
        )?.use { cursor ->
            cursor.toTeams()
        } ?: emptyList()
    }

    private fun mapToUiModel(
        matches: List<Match>,
        teams: List<Team>
    ): List<MatchUiModel> {
        val teamsMap = teams.associateBy { it._id }


        return matches.map { match ->
            val homeTeam = teamsMap[match.homeTeamId]
            val awayTeam = teamsMap[match.awayTeamId]

            if (homeTeam != null && awayTeam != null) {
                MatchUiModel(
                    matchId = match._id,
                    homeCode = homeTeam.code,
                    awayCode = awayTeam.code,
                    homeScore = match.homeTeamScore,
                    awayScore = match.awayTeamScore,
                    round = match.round,
                    date = match.date,
                    status = match.status,
                    homeLogoPath = homeTeam.logoPath,
                    awayLogoPath = awayTeam.logoPath
                )
            } else {
                return emptyList()
            }
        }.sortedBy { it.date }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}