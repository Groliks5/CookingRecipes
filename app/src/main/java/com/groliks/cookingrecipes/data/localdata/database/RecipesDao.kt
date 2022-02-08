package com.groliks.cookingrecipes.data.localdata.database

import androidx.room.*
import com.groliks.cookingrecipes.data.model.Ingredient
import com.groliks.cookingrecipes.data.model.Recipe
import com.groliks.cookingrecipes.data.model.RecipeInfo
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RecipesDao {
    @Transaction
    @Query("SELECT * FROM recipes")
    abstract fun getRecipes(): Flow<List<Recipe>>

    @Insert
    protected abstract suspend fun addRecipeInfo(recipeInfo: RecipeInfo): Long

    @Insert
    protected abstract suspend fun addIngredients(ingredients: List<Ingredient>)

    @Transaction
    open suspend fun addRecipe(recipe: Recipe): Long {
        val recipeId = addRecipeInfo(recipe.info)
        for (ingredient in recipe.ingredients) {
            ingredient.recipeId = recipeId
        }
        addIngredients(recipe.ingredients)
        return recipeId
    }

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    abstract suspend fun getRecipe(recipeId: Long): Recipe

    @Update
    protected abstract suspend fun updateRecipeInfo(recipeInfo: RecipeInfo)

    @Update
    protected abstract suspend fun updateIngredients(ingredients: List<Ingredient>)

    @Transaction
    open suspend fun updateRecipe(recipe: Recipe) {
        updateRecipeInfo(recipe.info)
        val (newIngredients, oldIngredients) = recipe.ingredients.partition { it.id == Ingredient.NEW_INGREDIENT_ID }
        updateIngredients(oldIngredients)
        addIngredients(newIngredients)
    }
}