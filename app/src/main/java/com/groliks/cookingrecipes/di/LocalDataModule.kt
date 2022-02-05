package com.groliks.cookingrecipes.di

import android.content.Context
import androidx.room.Room
import com.groliks.cookingrecipes.data.localdata.LocalDataSource
import com.groliks.cookingrecipes.data.localdata.LocalDataSourceImpl
import com.groliks.cookingrecipes.data.localdata.database.RecipesDao
import com.groliks.cookingrecipes.data.localdata.database.RecipesDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [LocalDataProvideModule::class, LocalDataBindsModule::class])
class LocalDataModule

@Module
class LocalDataProvideModule {
    @Provides
    fun provideRecipesDatabase(context: Context): RecipesDatabase {
        return Room.databaseBuilder(
            context,
            RecipesDatabase::class.java,
            "recipes_db"
        ).build()
    }

    @Provides
    fun provideRecipesDao(recipesDatabase: RecipesDatabase): RecipesDao {
        return recipesDatabase.recipesDao()
    }
}

@Module
interface LocalDataBindsModule {
    @Binds
    fun bindLocalDataSourceImpl_to_LocalDataSource(localDataSourceImpl: LocalDataSourceImpl): LocalDataSource
}