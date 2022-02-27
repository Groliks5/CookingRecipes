package com.groliks.cookingrecipes.data.recipes.repository

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.util.DataSource

interface RecipesRepository {
    suspend fun getRecipes(dataSource: DataSource, recipesFilter: List<Filter>): List<RecipeInfo>
    suspend fun addRecipe(recipe: Recipe): Long
    suspend fun getRecipe(dataSource: DataSource, recipeId: Long): Recipe
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: RecipeInfo)
    suspend fun setFavouriteRecipe(recipeId: Long, isFavourite: Boolean)
    suspend fun downloadRecipe(recipeInfo: RecipeInfo): Long
}