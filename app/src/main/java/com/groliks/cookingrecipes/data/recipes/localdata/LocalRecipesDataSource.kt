package com.groliks.cookingrecipes.data.recipes.localdata

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.model.Recipe

interface LocalRecipesDataSource {
    suspend fun getRecipes(recipesFilter: List<Filter>): List<Recipe>
    suspend fun addRecipe(recipe: Recipe): Long
    suspend fun getRecipe(recipeId: Long): Recipe
    suspend fun updateRecipe(recipe: Recipe)
}