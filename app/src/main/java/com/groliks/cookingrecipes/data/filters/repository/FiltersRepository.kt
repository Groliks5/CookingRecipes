package com.groliks.cookingrecipes.data.filters.repository

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.util.DataSource

interface FiltersRepository {
    suspend fun getAvailableFilters(dataSource: DataSource): List<Filter>
}