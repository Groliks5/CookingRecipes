package com.groliks.cookingrecipes.data.recipes.remotedata.retrofit

import com.google.gson.annotations.SerializedName

data class RemoteRecipes(
    @SerializedName("meals")
    val recipes: List<Map<String, String?>>
)