package com.groliks.cookingrecipes.data.recipes.localdata.database

import androidx.room.*
import com.groliks.cookingrecipes.data.recipes.model.Ingredient
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo

@Dao
abstract class RecipesDao {
    @Transaction
    @Query("SELECT * FROM recipes WHERE category LIKE (:categories) AND (isFavourite = 1 OR isFavourite = :isOnlyFavourite)")
    abstract suspend fun getRecipes(
        categories: List<String>,
        isOnlyFavourite: Boolean
    ): List<RecipeInfo>

    @Insert
    protected abstract suspend fun addRecipeInfo(recipeInfo: RecipeInfo): Long

    @Insert
    protected abstract suspend fun addIngredients(ingredients: List<Ingredient>): List<Long>

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

    @Query("DELETE FROM ingredients WHERE id NOT IN (:ingredientsIds)")
    protected abstract suspend fun deleteOldIngredients(ingredientsIds: List<Long>)

    @Transaction
    open suspend fun updateRecipe(recipe: Recipe) {
        updateRecipeInfo(recipe.info)
        val (newIngredients, oldIngredients) = recipe.ingredients.partition { it.id == Ingredient.NEW_INGREDIENT_ID }
        updateIngredients(oldIngredients)
        val newIngredientsIds = addIngredients(newIngredients)
        for (i in newIngredients.indices) {
            newIngredients[i].id = newIngredientsIds[i]
        }
        val ingredientsIds = recipe.ingredients.map { it.id }
        deleteOldIngredients(ingredientsIds)
    }

    @Delete
    abstract suspend fun deleteRecipe(recipeInfo: RecipeInfo)

    @Query("UPDATE recipes SET isFavourite = :isFavourite WHERE id = :recipeId")
    abstract suspend fun setFavouriteRecipe(recipeId: Long, isFavourite: Boolean)
}