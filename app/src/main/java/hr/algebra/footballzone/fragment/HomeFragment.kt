package hr.algebra.footballzone.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import hr.algebra.footballzone.R
import hr.algebra.footballzone.dao.FootballZoneContract
import hr.algebra.footballzone.databinding.FragmentHomeBinding
import hr.algebra.footballzone.framework.navigate
import hr.algebra.footballzone.model.League
import hr.algebra.footballzone.adapter.LeagueAdapter
import hr.algebra.footballzone.mapper.toLeagues
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LeagueAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        initAdapter()
        setupRecyclerView()
        fetchDataFromDatabase()
    }

    private fun initAdapter() {
        adapter = LeagueAdapter(emptyList()) { clickedLeague ->
            val bundle = Bundle().apply {
                putInt("leagueId", clickedLeague._id)
            }
            navigate(R.id.leagueDetailsFragment, bundle)
        }
    }

    private fun setupRecyclerView() {
        binding.rvLeagues.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLeagues.adapter = adapter
    }

    private fun fetchDataFromDatabase() {
        lifecycleScope.launch(Dispatchers.IO) {
            val leagues = fetchLeaguesFromDb()

            withContext(Dispatchers.Main) {
                adapter.submitList(leagues)
            }
        }
    }

    private fun fetchLeaguesFromDb(): List<League> {
        requireContext().contentResolver.query(
            FootballZoneContract.LEAGUES_URI,
            null,
            null,
            null,
            "${League::name.name} ASC"
        )?.use { cursor ->
            return cursor.toLeagues()
        }
        return emptyList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
