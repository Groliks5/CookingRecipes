package com.groliks.cookingrecipes.di

import android.content.Context
import com.groliks.cookingrecipes.view.localrecipeslist.LocalRecipesListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DataModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(fragment: LocalRecipesListFragment)
}