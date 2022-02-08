package com.groliks.cookingrecipes.data.repositories

import com.groliks.cookingrecipes.data.localdata.LocalDataSource
import com.groliks.cookingrecipes.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : RecipesRepository {
    override fun getRecipes(dataSource: Int): Flow<List<Recipe>> {
        return localDataSource.getRecipes()
    }

    override suspend fun addRecipe(recipe: Recipe): Long {
        return localDataSource.addRecipe(recipe)
    }

    override suspend fun getRecipe(dataSource: Int, recipeId: Long): Recipe {
        return localDataSource.getRecipe(recipeId)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        localDataSource.updateRecipe(recipe)
    }
}