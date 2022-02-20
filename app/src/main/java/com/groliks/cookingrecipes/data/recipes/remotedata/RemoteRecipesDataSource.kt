package com.groliks.cookingrecipes.data.recipes.remotedata

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.model.RecipeList

interface RemoteRecipesDataSource {
    suspend fun getRecipes(filters: List<Filter>): RecipeList
}