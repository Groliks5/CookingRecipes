package com.groliks.cookingrecipes.data.filters.remotedata

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.filters.remotedata.retrofit.FiltersApiService
import javax.inject.Inject

class RemoteFiltersDataSourceImpl @Inject constructor(
    private val filtersApiService: FiltersApiService,
) : RemoteFiltersDataSource {
    override suspend fun getAvailableFilters(): List<Filter> {
        val remoteFilters =
            filtersApiService.getAvailableCategories().execute()
                .body()!!.filters
        val filters = mutableListOf<Filter>()
        for (remoteFilter in remoteFilters) {
            val filter = Filter(
                type = Filter.Type.CATEGORY,
                name = remoteFilter["strCategory"]!!
            )
            filters.add(filter)
        }
        return filters
    }
}