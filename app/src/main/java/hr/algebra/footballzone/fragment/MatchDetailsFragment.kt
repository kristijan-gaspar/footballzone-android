package hr.algebra.footballzone.fragment

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import hr.algebra.footballzone.FavoriteAddedReceiver
import hr.algebra.footballzone.R
import hr.algebra.footballzone.dao.FootballZoneContract
import hr.algebra.footballzone.databinding.FragmentMatchDetailsBinding
import hr.algebra.footballzone.mapper.toMatch
import hr.algebra.footballzone.mapper.toTeams
import hr.algebra.footballzone.model.Favorite
import hr.algebra.footballzone.model.Match
import hr.algebra.footballzone.model.Team
import hr.algebra.footballzone.notifications.ACTION_FAVORITE_ADDED
import hr.algebra.footballzone.notifications.EXTRA_AWAY_LOGO
import hr.algebra.footballzone.notifications.EXTRA_HOME_LOGO
import hr.algebra.footballzone.notifications.EXTRA_MATCH_INFO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchDetailsFragment : Fragment(R.layout.fragment_match_details) {
    private var matchId: Int = 0
    private var isFavorite: Boolean = false
    private var _binding: FragmentMatchDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var message: String
    private lateinit var homeLogoPath: String
    private lateinit var awayLogoPath: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readArguments()
    }

    private fun readArguments() {
        matchId = arguments?.getInt("matchId") ?: 0
        if (matchId == 0) {
            throw IllegalArgumentException("Missing matchId argument")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentMatchDetailsBinding.bind(view)
        checkIfFavorite()
        setupFavoriteButton()
        loadAndDisplayMatchData()
    }

    private fun checkIfFavorite() {
        lifecycleScope.launch(Dispatchers.IO) {
            requireContext().contentResolver.query(
                FootballZoneContract.FAVORITES_URI,
                null,
                "matchId = ?",
                arrayOf(matchId.toString()),
                null
            )?.use { cursor ->
                isFavorite = cursor.count > 0
            }

            withContext(Dispatchers.Main) {
                updateFavoriteButton()
            }
        }
    }

    private fun updateFavoriteButton() {
        binding.btnFavorite.text =
            if (isFavorite)
                getString(R.string.remove_from_favorites)
            else
                getString(R.string.add_to_favorites)
    }


    private fun loadAndDisplayMatchData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val match = fetchMatchFromDb()
            if (match == null) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Match not found", Toast.LENGTH_SHORT).show()
                }
            }

            val teams = fetchTeamFromDb(match!!)

            withContext(Dispatchers.Main) {
                bindMatchData(match, teams)
            }
        }
    }

    private fun fetchMatchFromDb(): Match? {
        return requireContext().contentResolver.query(
            FootballZoneContract.MATCHES_URI,
            null,
            "${Match::_id.name} = ?",
            arrayOf(matchId.toString()),
            null
        )?.use { cursor ->
            cursor.toMatch()
        }
    }

    private fun fetchTeamFromDb(match: Match): Map<Int, Team> {
        val teamIds = arrayOf(
            match.homeTeamId.toString(),
            match.awayTeamId.toString()
        )
        val selection = "${Team::_id.name} IN (${teamIds.joinToString(separator = ",") { "?" }})"
        return requireContext().contentResolver.query(
            FootballZoneContract.TEAMS_URI,
            null,
            selection,
            teamIds,
            null
        )?.use { cursor ->
            cursor.toTeams().associateBy { it._id }
        } ?: emptyMap()
    }


    private fun bindMatchData(match: Match, teams: Map<Int, Team>) {
        binding.tvHomeName.text = teams[match.homeTeamId]?.name
        binding.tvAwayName.text = teams[match.awayTeamId]?.name
        binding.tvScore.text = match.score
        binding.tvVenue.text = match.venue
        binding.tvCity.text = match.city
        binding.tvReferee.text = match.referee
        binding.tvStatus.text = match.status
        binding.tvDate.text = extractDate(match.date)
        binding.ivHomeLogo.setImageURI(teams[match.homeTeamId]?.logoPath?.toUri())
        binding.ivAwayLogo.setImageURI(teams[match.awayTeamId]?.logoPath?.toUri())

        message = "${teams[match.homeTeamId]?.name} - ${teams[match.awayTeamId]?.name}"
        homeLogoPath = teams[match.homeTeamId]?.logoPath ?: ""
        awayLogoPath = teams[match.awayTeamId]?.logoPath ?: ""

    }

    private fun setupFavoriteButton() {
        binding.btnFavorite.setOnClickListener {
            if (isFavorite) {
                removeFromFavorites()
            } else {
                addToFavorites()
            }
        }
    }

    private fun addToFavorites() {

        if (!askForPermission()) return

        lifecycleScope.launch(Dispatchers.IO) {

            val values = ContentValues().apply {
                put("matchId", matchId)
            }

            val uri = requireContext().contentResolver.insert(
                FootballZoneContract.FAVORITES_URI,
                values
            )

            if (uri != null) {
                withContext(Dispatchers.Main) {
                    isFavorite = true
                    updateFavoriteButton()

                    val intent = Intent(requireContext(), FavoriteAddedReceiver::class.java).apply {
                        action = ACTION_FAVORITE_ADDED
                        putExtra(EXTRA_MATCH_INFO, message)
                        putExtra(EXTRA_HOME_LOGO, homeLogoPath)
                        putExtra(EXTRA_AWAY_LOGO, awayLogoPath)
                    }
                    requireContext().sendBroadcast(intent)
                }
            }
        }
    }


    private fun removeFromFavorites() {
        lifecycleScope.launch(Dispatchers.IO) {
            requireContext().contentResolver.delete(
                FootballZoneContract.FAVORITES_URI,
                "${Favorite::matchId.name} = ?",
                arrayOf(matchId.toString())
            )

            isFavorite = false

            withContext(Dispatchers.Main) {
                updateFavoriteButton()
            }
        }
    }

    private fun extractDate(raw: String) = raw.substringBefore("T")

    private fun askForPermission(): Boolean {
        if (Build.VERSION.SDK_INT < 33) return true

        return if (
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
            false
        }
    }


    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.notification_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}