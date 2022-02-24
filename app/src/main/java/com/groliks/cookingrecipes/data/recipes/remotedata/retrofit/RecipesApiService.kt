package com.groliks.cookingrecipes.data.recipes.remotedata.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesApiService {
    @GET("filter.php?")
    fun getRecipesWithCategory(@Query("c") category: String): Call<RemoteRecipeInfoList>

    @GET("lookup.php?")
    fun getRecipe(@Query("i") id: Int): Call<RemoteRecipes>
}