package hr.algebra.footballzone.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.footballzone.R
import hr.algebra.footballzone.model.League
import androidx.core.net.toUri

class LeagueAdapter(
    private var items: List<League>,
    private val onClick: (League) -> Unit
) : RecyclerView.Adapter<LeagueAdapter.LeagueViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeagueViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_league, parent, false)
        return LeagueViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: LeagueViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(leagues: List<League>) {
        items = leagues
        notifyDataSetChanged()
    }

    inner class LeagueViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private val ivLeagueLogo: ImageView =
            itemView.findViewById(R.id.ivLeagueLogo)
        private val tvLeagueName: TextView =
            itemView.findViewById(R.id.tvLeagueName)
        private val tvLeagueMeta: TextView =
            itemView.findViewById(R.id.tvLeagueMeta)

        fun bind(league: League) {
            tvLeagueName.text = league.name
            tvLeagueMeta.text = buildString {
                append(league.country)
                append(" - ")
                append(league.season)
            }

            ivLeagueLogo.setImageURI(league.logoPath.toUri())

            itemView.setOnClickListener {
                onClick(league)
            }
        }
    }
}
