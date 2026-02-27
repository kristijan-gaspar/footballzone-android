package hr.algebra.footballzone.manager

import androidx.appcompat.app.AppCompatDelegate
 // singleton
object ThemeManager {
    fun applyDarkMode(enabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}