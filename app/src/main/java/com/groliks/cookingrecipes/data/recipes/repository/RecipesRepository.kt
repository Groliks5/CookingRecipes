package com.groliks.cookingrecipes.data.recipes.repository

import com.groliks.cookingrecipes.data.DataSource
import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.model.Recipe

interface RecipesRepository {
    suspend fun getRecipes(dataSource: DataSource, recipesFilter: List<Filter>): List<Recipe>
    suspend fun addRecipe(recipe: Recipe): Long
    suspend fun getRecipe(dataSource: DataSource, recipeId: Long): Recipe
    suspend fun updateRecipe(recipe: Recipe)
}