package com.groliks.cookingrecipes

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.groliks.cookingrecipes.di.AppComponent
import com.groliks.cookingrecipes.di.DaggerAppComponent
import com.groliks.cookingrecipes.view.settings.SettingsFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

class MainApp : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        runBlocking {
            dataStore.data.take(1).map { settings ->
                settings[SettingsFragment.themePreferencesKey]
                    ?: AppCompatDelegate.getDefaultNightMode()
            }.collect { theme ->
                AppCompatDelegate.setDefaultNightMode(theme)
            }
        }

        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is MainApp -> appComponent
        else -> applicationContext.appComponent
    }