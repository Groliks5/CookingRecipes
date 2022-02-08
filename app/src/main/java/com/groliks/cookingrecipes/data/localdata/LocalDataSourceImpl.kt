package com.groliks.cookingrecipes.data.localdata

import com.groliks.cookingrecipes.data.localdata.database.RecipesDao
import com.groliks.cookingrecipes.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val recipesDao: RecipesDao
) : LocalDataSource {
    override fun getRecipes(): Flow<List<Recipe>> {
        return recipesDao.getRecipes()
    }

    override suspend fun addRecipe(recipe: Recipe): Long {
        return recipesDao.addRecipe(recipe)
    }

    override suspend fun getRecipe(recipeId: Long): Recipe {
        return recipesDao.getRecipe(recipeId)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        recipesDao.updateRecipe(recipe)
    }
}