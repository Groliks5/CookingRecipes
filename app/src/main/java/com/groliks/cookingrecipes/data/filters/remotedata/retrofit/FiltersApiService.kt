package com.groliks.cookingrecipes.data.filters.remotedata.retrofit

import retrofit2.Call
import retrofit2.http.GET

interface FiltersApiService {
    @GET("list.php?c=list")
    fun getAvailableCategories(): Call<RemoteFiltersList>
}