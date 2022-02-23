package com.groliks.cookingrecipes.di

import android.content.Context
import com.groliks.cookingrecipes.di.data.DataModule
import com.groliks.cookingrecipes.view.editrecipe.EditRecipeFragment
import com.groliks.cookingrecipes.view.localrecipeslist.LocalRecipesListFragment
import com.groliks.cookingrecipes.view.localrecipeview.LocalRecipeViewFragment
import com.groliks.cookingrecipes.view.remoterecipeslist.RemoteRecipesListFragment
import com.groliks.cookingrecipes.view.remoterecipeview.RemoteRecipeViewFragment
import com.groliks.cookingrecipes.view.selectfilters.SelectFiltersFragment
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
    fun inject(fragment: RemoteRecipesListFragment)
    fun inject(fragment: EditRecipeFragment)
    fun inject(fragment: SelectFiltersFragment)
    fun inject(fragment: LocalRecipeViewFragment)
    fun inject(fragment: RemoteRecipeViewFragment)
}