package com.groliks.cookingrecipes

import android.app.Application
import android.content.Context
import com.groliks.cookingrecipes.di.AppComponent
import com.groliks.cookingrecipes.di.DaggerAppComponent

class MainApp : Application() {
    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

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