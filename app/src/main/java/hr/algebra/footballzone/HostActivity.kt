package hr.algebra.footballzone

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import hr.algebra.footballzone.databinding.ActivityHostBinding
import hr.algebra.footballzone.manager.LocaleManager
import hr.algebra.footballzone.manager.ThemeManager

class HostActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHostBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        applyThemeFromPreferences()
        applyLanguageFromPreferences()

        super.onCreate(savedInstanceState)
        binding = ActivityHostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
        initToolbar()
        setupBackHandling()
    }

    private fun applyLanguageFromPreferences() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val language = prefs.getString(getString(R.string.language), "en") ?: "en"
        LocaleManager.setLocale(language)
    }

    private fun applyThemeFromPreferences() {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val darkModeEnabled =
            prefs.getBoolean(getString(R.string.dark_mode), false)
        ThemeManager.applyDarkMode(darkModeEnabled)
    }

    private fun initNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        initNavDestinationListener()
    }

    private fun initNavDestinationListener() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavVisibility(destination.id)
            updateToolbarVisibility(destination.id)
            configureToolbarForDestination(destination.id, destination.label)
        }
    }

    private fun updateBottomNavVisibility(destinationId: Int) {
        binding.bottomNav.isGone = when (destinationId) {
            R.id.splashFragment,
            R.id.aboutFragment,
            R.id.leagueDetailsFragment,
            R.id.matchDetailsFragment -> true
            else -> false
        }
    }

    private fun updateToolbarVisibility(destinationId: Int) {
        binding.toolbar.isGone = when (destinationId) {
            R.id.splashFragment -> true
            else -> false
        }
    }

    private fun configureToolbarForDestination(
        destinationId: Int,
        destinationLabel: CharSequence?
    ) {
        when (destinationId) {
            R.id.homeFragment -> {
                binding.toolbar.logo = ContextCompat.getDrawable(this, R.drawable.new_logo_)
                binding.toolbar.title = ""
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }

            else -> {
                binding.toolbar.logo = null
                binding.toolbar.title = destinationLabel
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.favoritesFragment,
                R.id.settingsFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    private fun setupBackHandling() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {

                override fun handleOnBackPressed() {
                    handleBackPressed()
                }
            }
        )
    }

    private fun handleBackPressed() {
        when (navController.currentDestination?.id) {

            R.id.homeFragment,
            R.id.favoritesFragment,
            R.id.settingsFragment -> {
                finish()
            }

            else -> {
                navController.navigateUp()
            }
        }
    }

}





