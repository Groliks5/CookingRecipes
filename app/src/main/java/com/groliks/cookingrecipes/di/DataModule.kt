package com.groliks.cookingrecipes.di

import com.groliks.cookingrecipes.data.repositories.RecipesRepository
import com.groliks.cookingrecipes.data.repositories.RecipesRepositoryImpl
import dagger.Binds
import dagger.Module

@Module(includes = [LocalDataModule::class])
interface DataModule {
    @Binds
    fun bindRecipesRepositoryImpl_to_RecipesRepository(recipesRepositoryImpl: RecipesRepositoryImpl): RecipesRepository
}
