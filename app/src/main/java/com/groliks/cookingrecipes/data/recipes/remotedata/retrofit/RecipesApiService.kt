package com.groliks.cookingrecipes.data.recipes.remotedata.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface RecipesApiService {
    @GET("filter.php?c=Seafood")
    fun getRecipes(): Call<RemoteRecipeInfoList>
}