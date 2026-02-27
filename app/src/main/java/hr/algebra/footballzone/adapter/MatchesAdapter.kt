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
import hr.algebra.footballzone.model.League
import hr.algebra.footballzone.ui.model.MatchUiModel

class MatchesAdapter(
    private var matches: List<MatchUiModel>,
    private val onItemClick: (MatchUiModel) -> Unit
) : RecyclerView.Adapter<MatchesAdapter.MatchViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MatchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_match, parent, false)
        return MatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: MatchViewHolder, position: Int) {
        holder.bind(matches[position])
    }

    override fun getItemCount() = matches.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newMatches: List<MatchUiModel>) {
        matches = newMatches
        notifyDataSetChanged()
    }

    inner class MatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvRound: TextView = itemView.findViewById(R.id.tvRound)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        private val tvHomeTeam: TextView = itemView.findViewById(R.id.tvHomeTeam)
        private val tvAwayTeam: TextView = itemView.findViewById(R.id.tvAwayTeam)
        private val tvScore: TextView = itemView.findViewById(R.id.tvScore)
        private val ivHomeLogo: ImageView = itemView.findViewById(R.id.ivHomeLogo)
        private val ivAwayLogo: ImageView = itemView.findViewById(R.id.ivAwayLogo)

        fun bind(item: MatchUiModel) {

            tvRound.text = buildString {
                append("R")
                append(item.round)
            }
            tvDate.text = extractDate(item.date)
            tvStatus.text = item.status
            tvHomeTeam.text = item.homeCode
            tvAwayTeam.text = item.awayCode
            tvScore.text = item.score
            ivHomeLogo.setImageURI(item.homeLogoPath.toUri())
            ivAwayLogo.setImageURI(item.awayLogoPath.toUri())

            itemView.setOnClickListener {
                onItemClick(item)
            }
        }

        private fun extractDate(raw: String) = raw.substringBefore("T")
    }
}