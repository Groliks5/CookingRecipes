package com.groliks.cookingrecipes.data.filters.localdata

import com.groliks.cookingrecipes.data.filters.localdata.database.FiltersDao
import com.groliks.cookingrecipes.data.filters.model.Filter
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class LocalFiltersDataSourceImpl @Inject constructor(
    private val filtersDao: FiltersDao,
    @Named("favourite_filter_name") private val favouriteFilterName: String,
) : LocalFiltersDataSource {
    override suspend fun getAvailableFilters(): List<Filter> {
        val availableCategories = filtersDao.getAvailableCategories()
            .distinct()
            .filter { it.isNotBlank() }
        val filters =
            availableCategories.map {
                Filter(Filter.Type.CATEGORY, it, false)
            }.toMutableList()
        val onlyFavouritesFilter = Filter(Filter.Type.FAVOURITE, favouriteFilterName)
        filters.add(onlyFavouritesFilter)
        return filters
    }
}