package hr.algebra.footballzone.adapter

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import hr.algebra.footballzone.fragment.MatchesFragment
import hr.algebra.footballzone.fragment.StandingsFragment

private const val NUM_TABS = 2

class LeagueDetailsPagerAdapter(
    fragment: Fragment,
    private val leagueId: Int
) : FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> StandingsFragment().apply {
                arguments = bundleOf("leagueId" to leagueId)
            }
            1 -> MatchesFragment().apply {
                arguments = bundleOf("leagueId" to leagueId)
            }
            else -> throw IllegalStateException()
        }

    }

    override fun getItemCount() = NUM_TABS
}