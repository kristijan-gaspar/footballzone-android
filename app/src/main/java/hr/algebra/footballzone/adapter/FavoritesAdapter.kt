package hr.algebra.footballzone.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import hr.algebra.footballzone.R
import hr.algebra.footballzone.ui.model.FavoriteMatchUiModel
import hr.algebra.footballzone.ui.model.MatchUiModel

class FavoritesAdapter(
    private var items: List<FavoriteMatchUiModel>,
    private val onRemoveClick: (Int) -> Unit,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_match, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<FavoriteMatchUiModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val ivHomeLogo = view.findViewById<ImageView>(R.id.ivHomeLogo)
        private val ivAwayLogo = view.findViewById<ImageView>(R.id.ivAwayLogo)
        private val tvScore = view.findViewById<TextView>(R.id.tvScore)
        private val tvHomeTeamCode = view.findViewById<TextView>(R.id.tvHomeTeamCode)
        private val tvAwayTeamCode = view.findViewById<TextView>(R.id.tvAwayTeamCode)


        fun bind(item: FavoriteMatchUiModel) {
            itemView.setOnClickListener {
                onItemClick(item.matchId)
            }

            itemView.findViewById<ImageButton>(R.id.btnRemove)
                .setOnClickListener {
                    onRemoveClick(item.matchId)
                }


            tvScore.text = item.score
            tvHomeTeamCode.text = item.homeTeamCode
            tvAwayTeamCode.text = item.awayTeamCode
            ivHomeLogo.setImageURI(item.homeLogoPath.toUri())
            ivAwayLogo.setImageURI(item.awayLogoPath.toUri())
        }
    }
}
