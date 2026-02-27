package hr.algebra.footballzone.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import hr.algebra.footballzone.R
import androidx.core.graphics.scale
import androidx.core.graphics.createBitmap


private const val CHANNEL_ID = "favorites_channel"

object NotificationHelper {

    fun showFavoriteAdded(context: Context, matchInfo: String, homeLogo: String, awayLogo: String) {

        if (!notificationsEnabled(context)) return

        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return


        createChannel(context)

        val homeBitmap = BitmapFactory.decodeFile(homeLogo)
        val awayBitmap = BitmapFactory.decodeFile(awayLogo)

        if (homeBitmap == null || awayBitmap == null) return

        val combinedLogo = createCombinedLogo(homeBitmap, awayBitmap)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_ball)
            .setLargeIcon(combinedLogo)
            .setContentText(matchInfo)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(System.currentTimeMillis().toInt(), notification)
    }

    private fun createChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Favorites",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    fun createCombinedLogo(
        home: Bitmap,
        away: Bitmap
    ): Bitmap {

        val size = 128
        val result = createBitmap(size, size)

        val canvas = Canvas(result)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val logoSize = 80

        val homeScaled = home.scale(logoSize, logoSize)
        val awayScaled = away.scale(logoSize, logoSize)

        canvas.drawBitmap(
            homeScaled,
            0f,
            (size - logoSize).toFloat(),
            paint
        )

        canvas.drawBitmap(
            awayScaled,
            (size - logoSize).toFloat(),
            (size - logoSize).toFloat(),
            paint
        )

        return result
    }


    private fun notificationsEnabled(context: Context): Boolean {
        return PreferenceManager
            .getDefaultSharedPreferences(context)
            .getBoolean(
                context.getString(R.string.notifications),
                true
            )
    }
}