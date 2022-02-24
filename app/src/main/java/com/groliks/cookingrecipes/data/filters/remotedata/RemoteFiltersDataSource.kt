package com.groliks.cookingrecipes.data.filters.remotedata

import com.groliks.cookingrecipes.data.filters.model.Filter

interface RemoteFiltersDataSource {
    suspend fun getAvailableFilters(): List<Filter>
}