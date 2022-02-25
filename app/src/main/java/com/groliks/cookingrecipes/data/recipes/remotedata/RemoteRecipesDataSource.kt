package com.groliks.cookingrecipes.data.recipes.remotedata

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipesInfoList

interface RemoteRecipesDataSource {
    suspend fun getRecipes(filters: List<Filter>): RecipesInfoList
    suspend fun getRecipe(recipeId: Long): Recipe
}