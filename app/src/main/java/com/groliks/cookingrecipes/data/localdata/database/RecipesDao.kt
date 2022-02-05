package com.groliks.cookingrecipes.data.localdata.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.groliks.cookingrecipes.data.model.Recipe
import com.groliks.cookingrecipes.data.model.RecipeInfo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RecipesDao {
    @Transaction
    @Query("SELECT * FROM recipes")
    abstract fun getRecipes(): Flow<List<Recipe>>

    @Insert
    abstract suspend fun addRecipeInfo(recipeInfo: RecipeInfo): Long
}