package com.groliks.cookingrecipes.di.data

import android.content.Context
import androidx.room.Room
import com.groliks.cookingrecipes.data.database.RecipesDatabase
import com.groliks.cookingrecipes.di.data.filters.FiltersDataModule
import com.groliks.cookingrecipes.di.data.recipes.RecipesDataModule
import dagger.Module
import dagger.Provides

@Module(includes = [RecipesDataModule::class, FiltersDataModule::class])
class DataModule {
    @Provides
    fun provideRecipesDatabase(context: Context): RecipesDatabase {
        return Room.databaseBuilder(
            context,
            RecipesDatabase::class.java,
            "recipes_db"
        ).build()
    }
}