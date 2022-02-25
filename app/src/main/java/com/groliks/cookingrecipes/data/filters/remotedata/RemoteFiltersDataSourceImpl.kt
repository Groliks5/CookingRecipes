package com.groliks.cookingrecipes.data.filters.remotedata

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.filters.remotedata.retrofit.FiltersApiService
import javax.inject.Inject

class RemoteFiltersDataSourceImpl @Inject constructor(
    private val filtersApiService: FiltersApiService,
) : RemoteFiltersDataSource {
    override suspend fun getAvailableFilters(): List<Filter> {
        val categoryFilters = filtersApiService.getAvailableCategories().filters
        val areaFilters = filtersApiService.getAvailableAreas().filters
        val remoteFilters = categoryFilters + areaFilters

        val filters = mutableListOf<Filter>()
        for (remoteFilter in remoteFilters) {
            val filterKey = remoteFilter.keys.first()
            val filterType = when (filterKey) {
                "strCategory" -> Filter.Type.CATEGORY
                "strArea" -> Filter.Type.AREA
                else -> null
            }
            filterType?.also {
                val filter = Filter(
                    type = it,
                    name = remoteFilter[filterKey]!!
                )
                filters.add(filter)
            }
        }

        return filters
    }
}