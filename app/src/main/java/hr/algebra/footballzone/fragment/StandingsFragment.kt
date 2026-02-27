package hr.algebra.footballzone.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope // Važan import
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.footballzone.R
import hr.algebra.footballzone.adapter.StandingsAdapter
import hr.algebra.footballzone.dao.FootballZoneContract
import hr.algebra.footballzone.databinding.FragmentStandingsBinding // Import za View Binding
import hr.algebra.footballzone.mapper.toStandings
import hr.algebra.footballzone.mapper.toTeams
import hr.algebra.footballzone.model.Standing
import hr.algebra.footballzone.model.Team
import hr.algebra.footballzone.ui.model.StandingUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StandingsFragment : Fragment(R.layout.fragment_standings) {

    private var _binding: FragmentStandingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: StandingsAdapter
    private var leagueId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStandingsBinding.bind(view)
        setupRecyclerView()
        fetchAndDisplayStandings()
    }

    private fun setupRecyclerView() {
        adapter = StandingsAdapter(emptyList())
        binding.rvStandings.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStandings.adapter = adapter
    }

    private fun fetchAndDisplayStandings() {
        lifecycleScope.launch(Dispatchers.IO) {
            val standingsUiData = fetchStandingsUiData()

            withContext(Dispatchers.Main) {
                adapter.submitList(standingsUiData)
            }
        }
    }

    private fun fetchStandingsUiData(): List<StandingUiModel> {
        val standings = fetchStandingsFromDb()
        if (standings.isEmpty()) {
            return emptyList()
        }

        val teamIds = standings.map { it.teamId }.distinct().map { it.toString() }.toTypedArray()
        if (teamIds.isEmpty()) {
            return emptyList()
        }

        val teams = fetchTeamsByIds(teamIds)

        return mapToUiModel(standings, teams)
    }

    private fun fetchStandingsFromDb(): List<Standing> {
        var standings: List<Standing> = emptyList()
        requireContext().contentResolver.query(
            FootballZoneContract.STANDINGS_URI,
            null,
            "${Standing::leagueId.name} = ?",
            arrayOf(leagueId.toString()),
            null
        )?.use { cursor ->
            standings = cursor.toStandings()
        }
        return standings
    }

    private fun fetchTeamsByIds(teamIds: Array<String>): List<Team> {

        val selection = "${Team::_id.name} IN (${teamIds.joinToString(separator = ",") { "?" }})"
        requireContext().contentResolver.query(
            FootballZoneContract.TEAMS_URI,
            null,
            selection,
            teamIds,
            null
        )?.use { cursor ->
            return cursor.toTeams()
        }
        return emptyList()
    }


    private fun mapToUiModel(standings: List<Standing>, teams: List<Team>): List<StandingUiModel> {
        val teamsMap = teams.associateBy { it._id }

        return standings.mapNotNull { standing ->
            val team = teamsMap[standing.teamId]
            if (team != null) {
                StandingUiModel(
                    rank = standing.rank,
                    teamName = team.name,
                    teamLogoPath = team.logoPath,
                    points = standing.points,
                    goalsDiff = standing.goalsDiff
                )
            } else {
                null
            }
        }.sortedBy { it.rank }
    }

    private fun readArguments() {
        arguments?.let {
            leagueId = it.getInt("leagueId")
        }
        if (leagueId == 0) {
            throw IllegalArgumentException("Missing leagueId argument")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
