package com.groliks.cookingrecipes.data.localdata

import com.groliks.cookingrecipes.data.model.Recipe
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getRecipes(): Flow<List<Recipe>>
    suspend fun addRecipe(recipe: Recipe): Long
    suspend fun getRecipe(recipeId: Long): Recipe
    suspend fun updateRecipe(recipe: Recipe)
}