package com.groliks.cookingrecipes.data.recipes.remotedata

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.model.RecipeList
import com.groliks.cookingrecipes.data.recipes.remotedata.retrofit.RecipesApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRecipesDataSourceImpl @Inject constructor(
    private val recipesApiService: RecipesApiService,
) : RemoteRecipesDataSource {
    override suspend fun getRecipes(filters: List<Filter>): RecipeList {
        val recipes = recipesApiService.getRecipes().execute().body()!!.recipes
        val resultRecipes = mutableListOf<RecipeInfo>()
        for (recipe in recipes) {
            val recipeInfo = RecipeInfo(
                name = recipe.name,
                photoUri = recipe.photoUri,
                description = "",
                id = recipe.id
            )
            resultRecipes.add(recipeInfo)
        }
        return RecipeList(resultRecipes)
    }
}