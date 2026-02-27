package hr.algebra.footballzone.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.footballzone.R
import hr.algebra.footballzone.adapter.FavoritesAdapter
import hr.algebra.footballzone.dao.FootballZoneContract
import hr.algebra.footballzone.databinding.FragmentFavoritesBinding

import hr.algebra.footballzone.framework.navigate
import hr.algebra.footballzone.mapper.toMatches
import hr.algebra.footballzone.mapper.toTeams
import hr.algebra.footballzone.model.Match
import hr.algebra.footballzone.model.Team
import hr.algebra.footballzone.ui.model.FavoriteMatchUiModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesFragment : Fragment(R.layout.fragment_favorites) {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FavoritesAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoritesBinding.bind(view)

        initAdapter()
        setupRecyclerView()
        fetchAndDisplayStandings()
    }

    private fun initAdapter() {
        adapter = FavoritesAdapter(
            emptyList(),
            removeItemClick,
            itemClick
        )
    }

    val itemClick: (Int) -> Unit = { matchId ->
        val bundle = Bundle().apply {
            putInt("matchId", matchId)
        }
        navigate(R.id.matchDetailsFragment, bundle)

    }

    val removeItemClick: (Int) -> Unit = { matchId ->
        removeFromFavorites(matchId)
    }


    private fun setupRecyclerView() {
        binding.rvFavoriteMatches.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteMatches.adapter = adapter
    }


    private fun fetchAndDisplayStandings() {
        lifecycleScope.launch(Dispatchers.IO) {
            val favoriteMatchUiModels = fetchFavoritesUiData()

            withContext(Dispatchers.Main) {
                if (favoriteMatchUiModels.isEmpty()) {
                    binding.rvFavoriteMatches.visibility = View.GONE
                    binding.tvEmpty.visibility = View.VISIBLE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvFavoriteMatches.visibility = View.VISIBLE
                    adapter.submitList(favoriteMatchUiModels)
                }
            }
        }
    }

    private fun fetchFavoritesUiData(): List<FavoriteMatchUiModel> {

        val favoriteIds = fetchFavoriteMatchIds()
        if (favoriteIds.isEmpty()) return emptyList()
        val favoriteMatches = fetchMatchesByIds(favoriteIds)

        val teamIds = extractTeamIds(favoriteMatches)
        if (teamIds.isEmpty()) return emptyList()

        val teams = fetchTeamsByIdsFromDb(teamIds)

        return mapToUiModel(favoriteMatches, teams)

    }

    private fun fetchFavoriteMatchIds(): List<Int> {
        val ids = mutableListOf<Int>()
        requireContext().contentResolver.query(
            FootballZoneContract.FAVORITES_URI,
            arrayOf(FavoriteMatchUiModel::matchId.name),
            null,
            null,
            null
        )?.use { cursor ->
            while (cursor.moveToNext()) {
                ids.add(cursor.getInt(0))
            }
        }
        return ids
    }


    private fun fetchMatchesByIds(ids: List<Int>): List<Match> {
        if (ids.isEmpty()) return emptyList()

        val args = ids.map { it.toString() }.toTypedArray()
        val selection = "${Match::_id.name} IN (${ids.joinToString { "?" }})"

        return requireContext().contentResolver.query(
            FootballZoneContract.MATCHES_URI,
            null,
            selection,
            args,
            null
        )?.use { it.toMatches() } ?: emptyList()
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


    private fun removeFromFavorites(matchId: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            requireContext().contentResolver.delete(
                FootballZoneContract.FAVORITES_URI,
                "${FavoriteMatchUiModel::matchId.name} = ?",
                arrayOf(matchId.toString())
            )

            withContext(Dispatchers.Main) {
                fetchAndDisplayStandings()
            }
        }
    }


    private fun mapToUiModel(
        favoriteMatches: List<Match>,
        teams: List<Team>
    ): List<FavoriteMatchUiModel> {
        val teamsMap = teams.associateBy { it._id }

        return favoriteMatches.map { match ->
            val homeTeam = teamsMap[match.homeTeamId]
            val awayTeam = teamsMap[match.awayTeamId]

            if (homeTeam != null && awayTeam != null) {
                FavoriteMatchUiModel(
                    matchId = match._id,
                    homeLogoPath = homeTeam.logoPath,
                    awayLogoPath = awayTeam.logoPath,
                    homeScore = match.homeTeamScore,
                    awayScore = match.awayTeamScore,
                    homeTeamCode = homeTeam.code,
                    awayTeamCode = awayTeam.code
                )
            } else {
                return emptyList()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
