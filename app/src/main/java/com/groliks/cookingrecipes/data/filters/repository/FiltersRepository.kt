package com.groliks.cookingrecipes.data.filters.repository

import com.groliks.cookingrecipes.data.DataSource
import com.groliks.cookingrecipes.data.filters.model.Filter

interface FiltersRepository {
    suspend fun getAvailableFilters(dataSource: DataSource): List<Filter>
}