package hr.algebra.footballzone.fragment

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import hr.algebra.footballzone.R
import hr.algebra.footballzone.framework.navigate
import hr.algebra.footballzone.manager.LocaleManager
import hr.algebra.footballzone.manager.ThemeManager

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var preferences: SharedPreferences

    override fun onCreatePreferences(
        savedInstanceState: Bundle?,
        rootKey: String?
    ) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        initPreferences()
        setupAboutPreference()
    }


    private fun initPreferences() {
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
    }

    private fun setupAboutPreference() {
        val aboutPreference = findPreference<Preference>(getString(R.string.about))
        aboutPreference?.setOnPreferenceClickListener {
            navigate(R.id.aboutFragment)
            true
        }
    }

    override fun onResume() {
        super.onResume()
        preferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(
        sharedPreferences: SharedPreferences?,
        key: String?
    ) {
        when (key) {
            getString(R.string.dark_mode) -> {
                val enabled = sharedPreferences?.getBoolean(key, false) ?: false
                ThemeManager.applyDarkMode(enabled)
            }

            getString(R.string.language) -> {
                val language = sharedPreferences?.getString(key, "en") ?: "en"
                LocaleManager.setLocale(language)
            }
        }
    }
}

