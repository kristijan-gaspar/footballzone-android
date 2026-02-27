package hr.algebra.footballzone.api

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import hr.algebra.footballzone.dao.FootballZoneContract
import hr.algebra.footballzone.handler.downloadImageAndStore
import hr.algebra.footballzone.mapper.toIdLogoMap
import hr.algebra.footballzone.model.League
import hr.algebra.footballzone.model.Team

class ImageDownloader(private val context: Context) {

    fun downloadAll() {
        downloadLeagueImages()
        downloadTeamImages()
    }

    private fun downloadTeamImages() {
        context.contentResolver.query(
            FootballZoneContract.TEAMS_URI,
            arrayOf(Team::_id.name, Team::logoPath.name),
            null,
            null,
            null
        )?.use { cursor ->
            cursor.toIdLogoMap().forEach { (id, url) ->
                if (url.startsWith("http")) {
                    val localPath = downloadImageAndStore(context, url)
                    updateTeam(id, localPath)
                }
            }
        }
    }

    private fun updateTeam(id: Int, localPath: String?) {
        if (localPath == null) return

        val values = ContentValues().apply {
            put(Team::logoPath.name, localPath)
        }

        context.contentResolver.update(
            ContentUris.withAppendedId(FootballZoneContract.TEAMS_URI,id.toLong()),
            values,
            null,
            null
        )
    }

    private fun downloadLeagueImages() {
        context.contentResolver.query(
            FootballZoneContract.LEAGUES_URI,
            arrayOf(League::_id.name, League::logoPath.name),
            null,
            null,
            null
        )?.use { cursor ->
            cursor.toIdLogoMap().forEach { (id, url) ->
                if (url.startsWith("http")) {
                    val localPath = downloadImageAndStore(context, url)
                    updateLeague(id, localPath)
                }
            }
        }
    }

    private fun updateLeague(id: Int, localPath: String?) {
        if (localPath == null) return
        val values = ContentValues().apply {
            put(League::logoPath.name, localPath)
        }
        context.contentResolver.update(
            ContentUris.withAppendedId(FootballZoneContract.LEAGUES_URI, id.toLong()),
            values,
            null,
            null
        )
    }

}
