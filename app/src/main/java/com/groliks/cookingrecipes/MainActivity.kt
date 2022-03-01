package com.groliks.cookingrecipes

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.groliks.cookingrecipes.databinding.ActivityMainBinding
import com.groliks.cookingrecipes.view.settings.SettingsFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import java.util.*

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {
    private val appBarConfiguration = AppBarConfiguration(
        setOf(
            R.id.localRecipesListFragment,
            R.id.remoteRecipesListFragment,
            R.id.settingsFragment
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setSupportActionBar(binding.toolbar)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController
        binding.bottomNavigation.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        setContentView(binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    override fun attachBaseContext(newBase: Context?) {
        runBlocking {
            newBase?.dataStore?.data?.take(1)?.map { settings ->
                settings[SettingsFragment.languagePreferencesKey] ?: Locale.getDefault().language
            }?.collect { language ->
                val locale = Locale(language)
                Locale.setDefault(locale)
                val config = newBase.resources.configuration
                config.setLocale(locale)
                config.setLayoutDirection(locale)
                val resourcesContext = newBase.createConfigurationContext(config)
                super.attachBaseContext(resourcesContext)
            } ?: super.attachBaseContext(newBase)
        }
    }
}