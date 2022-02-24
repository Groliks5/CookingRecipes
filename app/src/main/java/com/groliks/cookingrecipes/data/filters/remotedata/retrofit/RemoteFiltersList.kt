package com.groliks.cookingrecipes.data.filters.remotedata.retrofit

import com.google.gson.annotations.SerializedName

data class RemoteFiltersList(
    @SerializedName("meals")
    val filters: List<Map<String, String>>,
)