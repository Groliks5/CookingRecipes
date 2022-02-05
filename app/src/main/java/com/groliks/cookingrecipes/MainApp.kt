package com.groliks.cookingrecipes

import android.app.Application
import com.groliks.cookingrecipes.di.AppComponent
import com.groliks.cookingrecipes.di.DaggerAppComponent

class MainApp : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }
}