package com.groliks.cookingrecipes.data.filters.repository

import com.groliks.cookingrecipes.data.DataSource
import com.groliks.cookingrecipes.data.filters.localdata.LocalFiltersDataSource
import com.groliks.cookingrecipes.data.filters.model.Filter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FiltersRepositoryImpl @Inject constructor(
    private val localFiltersDataSource: LocalFiltersDataSource,
) : FiltersRepository {
    override suspend fun getAvailableFilters(dataSource: DataSource): List<Filter> {
        return localFiltersDataSource.getAvailableFilters()
    }
}