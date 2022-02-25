package com.groliks.cookingrecipes.data.filters.remotedata.retrofit

import retrofit2.http.GET

interface FiltersApiService {
    @GET("list.php?c=list")
    suspend fun getAvailableCategories(): RemoteFiltersList

    @GET("list.php?a=list")
    suspend fun getAvailableAreas(): RemoteFiltersList
}