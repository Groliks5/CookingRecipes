package com.groliks.cookingrecipes.data.localdata.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.groliks.cookingrecipes.data.model.Ingredient
import com.groliks.cookingrecipes.data.model.RecipeInfo

@Database(entities = [RecipeInfo::class, Ingredient::class], version = 1)
abstract class RecipesDatabase : RoomDatabase() {
    abstract fun recipesDao(): RecipesDao
}