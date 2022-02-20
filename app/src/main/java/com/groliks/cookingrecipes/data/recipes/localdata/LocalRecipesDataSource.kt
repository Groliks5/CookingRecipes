package com.groliks.cookingrecipes.data.recipes.localdata

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.model.RecipeList

interface LocalRecipesDataSource {
    suspend fun getRecipes(recipesFilter: List<Filter>): RecipeList
    suspend fun addRecipe(recipe: Recipe): Long
    suspend fun getRecipe(recipeId: Long): Recipe
    suspend fun updateRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: RecipeInfo)
}