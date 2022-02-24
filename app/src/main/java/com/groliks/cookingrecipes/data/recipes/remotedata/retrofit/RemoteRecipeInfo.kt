package com.groliks.cookingrecipes.data.recipes.remotedata.retrofit

import com.google.gson.annotations.SerializedName

data class RemoteRecipeInfo(
    @SerializedName("strMeal")
    val name: String,
    @SerializedName("strMealThumb")
    val photoUri: String,
    @SerializedName("idMeal")
    val id: Long,
)

data class RemoteRecipeInfoList(
    @SerializedName("meals")
    val recipes: List<RemoteRecipeInfo> = listOf()
)