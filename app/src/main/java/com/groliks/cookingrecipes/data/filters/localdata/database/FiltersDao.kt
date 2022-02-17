package com.groliks.cookingrecipes.data.filters.localdata.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface FiltersDao {
    @Query("SELECT category FROM recipes")
    suspend fun getAvailableCategories(): List<String>
}