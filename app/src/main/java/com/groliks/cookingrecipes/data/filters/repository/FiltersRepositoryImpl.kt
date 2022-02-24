package com.groliks.cookingrecipes.data.filters.repository

import com.groliks.cookingrecipes.data.DataSource
import com.groliks.cookingrecipes.data.filters.localdata.LocalFiltersDataSource
import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.filters.remotedata.RemoteFiltersDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FiltersRepositoryImpl @Inject constructor(
    private val localFiltersDataSource: LocalFiltersDataSource,
    private val remoteFiltersDataSource: RemoteFiltersDataSource
) : FiltersRepository {
    override suspend fun getAvailableFilters(dataSource: DataSource): List<Filter> =
        withContext(Dispatchers.IO) {
            when (dataSource) {
                DataSource.LOCAL -> localFiltersDataSource.getAvailableFilters()
                DataSource.REMOTE -> remoteFiltersDataSource.getAvailableFilters()
            }
        }
}