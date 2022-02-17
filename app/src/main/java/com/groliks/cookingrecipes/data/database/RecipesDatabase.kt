package com.groliks.cookingrecipes.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.groliks.cookingrecipes.data.filters.localdata.database.FiltersDao
import com.groliks.cookingrecipes.data.recipes.localdata.database.RecipesDao
import com.groliks.cookingrecipes.data.recipes.model.Ingredient
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo

@Database(entities = [RecipeInfo::class, Ingredient::class], version = 1)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
    abstract fun filtersDao(): FiltersDao
}