package com.groliks.cookingrecipes.data.recipes.repository

import com.groliks.cookingrecipes.data.DataSource
import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.localdata.LocalRecipesDataSource
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepositoryImpl @Inject constructor(
    private val localRecipesDataSource: LocalRecipesDataSource
) : RecipesRepository {
    override suspend fun getRecipes(
        dataSource: DataSource,
        recipesFilter: List<Filter>
    ): List<Recipe> {
        return localRecipesDataSource.getRecipes(recipesFilter)
    }

    override suspend fun addRecipe(recipe: Recipe): Long = withContext(Dispatchers.IO) {
        localRecipesDataSource.addRecipe(recipe)
    }

    override suspend fun getRecipe(dataSource: DataSource, recipeId: Long): Recipe =
        withContext(Dispatchers.IO) {
            localRecipesDataSource.getRecipe(recipeId)
        }

    override suspend fun updateRecipe(recipe: Recipe) = withContext(Dispatchers.IO) {
        localRecipesDataSource.updateRecipe(recipe)
    }
}