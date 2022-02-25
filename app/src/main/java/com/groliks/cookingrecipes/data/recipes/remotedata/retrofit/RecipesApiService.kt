package com.groliks.cookingrecipes.data.recipes.remotedata.retrofit

import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesApiService {
    @GET("filter.php?")
    suspend fun getRecipesWithCategory(@Query("c") category: String): RemoteRecipeInfoList

    @GET("filter.php?")
    suspend fun getRecipesWithArea(@Query("a") area: String): RemoteRecipeInfoList

    @GET("lookup.php?")
    suspend fun getRecipe(@Query("i") id: Int): RemoteRecipes
}