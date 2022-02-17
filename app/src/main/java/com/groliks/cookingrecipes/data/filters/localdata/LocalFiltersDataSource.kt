package com.groliks.cookingrecipes.data.filters.localdata

import com.groliks.cookingrecipes.data.filters.model.Filter

interface LocalFiltersDataSource {
    suspend fun getAvailableFilters(): List<Filter>
}