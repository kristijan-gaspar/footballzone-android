package hr.algebra.footballzone.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.footballzone.R
import hr.algebra.footballzone.ui.model.StandingUiModel

class StandingsAdapter(
    private var standings: List<StandingUiModel>
) : RecyclerView.Adapter<StandingsAdapter.StandingsViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StandingsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_standing, parent, false)
        return StandingsViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: StandingsViewHolder,
        position: Int
    ) {
        holder.bind(standings[position])
    }

    override fun getItemCount() = standings.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newStandings: List<StandingUiModel>) {
        standings = newStandings
        notifyDataSetChanged()
    }

    inner class StandingsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvRank = itemView.findViewById<TextView>(R.id.tvRank)
        private val tvTeamName = itemView.findViewById<TextView>(R.id.tvTeamName)
        private val ivTeamLogo = itemView.findViewById<ImageView>(R.id.ivTeamLogo)
        private val tvPoints = itemView.findViewById<TextView>(R.id.tvPoints)
        private val tvGoalsDiff = itemView.findViewById<TextView>(R.id.tvGoalsDiff)

        fun bind(standing: StandingUiModel) {
            tvTeamName.text = standing.teamName
            ivTeamLogo.setImageURI(standing.teamLogoPath.toUri())
            tvRank.text = standing.rank.toString()
            tvPoints.text = standing.points.toString()
            tvGoalsDiff.text = standing.goalsDiff.toString()

        }

    }
}


