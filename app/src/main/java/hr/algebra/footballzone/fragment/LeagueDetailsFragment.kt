package hr.algebra.footballzone.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.launch
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import hr.algebra.footballzone.R
import hr.algebra.footballzone.adapter.LeagueDetailsPagerAdapter
import hr.algebra.footballzone.dao.FootballZoneContract
import hr.algebra.footballzone.databinding.FragmentLeagueDetailsBinding
import hr.algebra.footballzone.databinding.FragmentSplashBinding
import hr.algebra.footballzone.mapper.toLeague
import hr.algebra.footballzone.model.League
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LeagueDetailsFragment : Fragment(R.layout.fragment_league_details) {

    private var leagueId: Int = 0
    private var _binding: FragmentLeagueDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        readArguments()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLeagueDetailsBinding.bind(view)
        loadLeagueData()
    }

    private fun readArguments() {
        leagueId = arguments?.getInt("leagueId") ?: 0
        if (leagueId == 0) {
            throw IllegalArgumentException("Missing leagueId argument")
        }
    }

    private fun loadLeagueData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val league = fetchLeagueFromDb()

            withContext(Dispatchers.Main) {
                if (league != null) {
                    bindLeague(league)
                    setupTabs(league._id)
                } else
                    Toast.makeText(requireContext(), "League not found", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun fetchLeagueFromDb(): League? {
        return requireContext().contentResolver.query(
            FootballZoneContract.LEAGUES_URI,
            null,
            "${League::_id.name} = ?",
            arrayOf(leagueId.toString()),
            null
        )?.use { cursor ->
            cursor.toLeague()
        }
    }

    private fun bindLeague(league: League) {
        binding.tvLeagueName.text = league.name
        binding.ivLeagueLogo.setImageURI(league.logoPath.toUri())
    }


    private fun setupTabs(leagueId: Int) {
        val adapter = LeagueDetailsPagerAdapter(this, leagueId)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.standings)
                1 -> getString(R.string.matches)
                else -> ""
            }
        }.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}