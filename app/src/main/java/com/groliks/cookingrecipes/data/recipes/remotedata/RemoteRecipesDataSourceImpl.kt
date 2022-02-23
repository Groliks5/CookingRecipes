package com.groliks.cookingrecipes.data.recipes.remotedata

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeList
import com.groliks.cookingrecipes.data.recipes.remotedata.retrofit.RecipesApiService
import com.groliks.cookingrecipes.data.recipes.remotedata.util.RemoteRecipeToRecipeConverter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRecipesDataSourceImpl @Inject constructor(
    private val recipesApiService: RecipesApiService,
) : RemoteRecipesDataSource {
    override suspend fun getRecipes(filters: List<Filter>): RecipeList {
        val requestResult = recipesApiService.getRecipes().execute().body()!!
        return RemoteRecipeToRecipeConverter.convertRemoteRecipesInfo(requestResult)
    }

    override suspend fun getRecipe(recipeId: Long): Recipe {
        val requestResult =
            recipesApiService.getRecipe(recipeId.toInt()).execute().body()!!.recipes[0]
        return RemoteRecipeToRecipeConverter.convertRemoteRecipe(requestResult)
    }
}