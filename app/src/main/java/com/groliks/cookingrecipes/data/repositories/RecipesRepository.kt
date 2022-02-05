package com.groliks.cookingrecipes.data.repositories

import com.groliks.cookingrecipes.data.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipesRepository {
    fun getRecipes(dataSource: Int): Flow<List<Recipe>>

    companion object {
        const val LOCAL_DATA_SOURCE = 0
        const val REMOTE_DATA_SOURCE = 1
    }
}