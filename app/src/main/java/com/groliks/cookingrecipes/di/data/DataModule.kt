package com.groliks.cookingrecipes.di.data

import android.content.Context
import androidx.room.Room
import com.groliks.cookingrecipes.BuildConfig
import com.groliks.cookingrecipes.data.database.RecipesDatabase
import com.groliks.cookingrecipes.di.data.filters.FiltersDataModule
import com.groliks.cookingrecipes.di.data.recipes.RecipesDataModule
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    @Provides
    fun provideRecipesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.THEMEALDB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}