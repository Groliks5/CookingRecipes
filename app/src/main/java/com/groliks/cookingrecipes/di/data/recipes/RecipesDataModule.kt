package com.groliks.cookingrecipes.di.data.recipes

import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module(includes = [LocalRecipesDataModule::class, RemoteRecipesDataModule::class])
interface RecipesDataModule {
    @Binds
    fun bindRecipesRepositoryImpl_to_RecipesRepository(
        recipesRepositoryImpl: RecipesRepositoryImpl
    ): RecipesRepository
}
