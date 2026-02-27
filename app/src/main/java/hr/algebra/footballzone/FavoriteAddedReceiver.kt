package hr.algebra.footballzone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import hr.algebra.footballzone.notifications.ACTION_FAVORITE_ADDED
import hr.algebra.footballzone.notifications.EXTRA_AWAY_LOGO
import hr.algebra.footballzone.notifications.EXTRA_HOME_LOGO
import hr.algebra.footballzone.notifications.EXTRA_MATCH_INFO
import hr.algebra.footballzone.notifications.NotificationHelper

class FavoriteAddedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_FAVORITE_ADDED) return

        val matchInfo = intent.getStringExtra(EXTRA_MATCH_INFO)
        val homeLogo = intent.getStringExtra(EXTRA_HOME_LOGO) ?: ""
        val awayLogo = intent.getStringExtra(EXTRA_AWAY_LOGO) ?: ""
        if (matchInfo == null ) return

        NotificationHelper.showFavoriteAdded(context, matchInfo, homeLogo, awayLogo)

    }
}