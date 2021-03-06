package com.groliks.cookingrecipes.data.recipes.remotedata.util

import com.groliks.cookingrecipes.data.recipes.model.Ingredient
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.remotedata.retrofit.RemoteRecipeInfo

private const val REMOTE_RECIPE_DESCRIPTION = "Recipe from the meal DB"

class RemoteRecipeToRecipeConverter {
    companion object {
        fun convertRemoteRecipesInfo(remoteRecipesInfo: List<RemoteRecipeInfo>): List<RecipeInfo> {
            val recipes = mutableListOf<RecipeInfo>()
            for (recipe in remoteRecipesInfo) {
                val recipeInfo = RecipeInfo(
                    name = recipe.name,
                    photoUri = recipe.photoUri,
                    description = REMOTE_RECIPE_DESCRIPTION,
                    id = recipe.id
                )
                recipes.add(recipeInfo)
            }
            return recipes
        }

        fun convertRemoteRecipe(remoteRecipe: Map<String, String?>): Recipe {
            val recipeId = remoteRecipe["idMeal"]!!.toLong()
            val recipeName = remoteRecipe["strMeal"]!!
            val recipeInstruction = remoteRecipe["strInstructions"]!!
            val recipePhotoUri = remoteRecipe["strMealThumb"]!!
            val recipeDescription = REMOTE_RECIPE_DESCRIPTION
            val recipeCategory = remoteRecipe["strCategory"]!!
            val ingredientsNames = getIngredientValues(remoteRecipe, "strIngredient")
            val ingredientsMeasures = getIngredientValues(remoteRecipe, "strMeasure")

            val recipeInfo = RecipeInfo(
                id = recipeId,
                name = recipeName,
                description = recipeDescription,
                photoUri = recipePhotoUri,
                instruction = recipeInstruction,
                category = recipeCategory,
            )
            val ingredients = ingredientsNames.mapIndexed { index, s ->
                Ingredient(
                    recipeId = recipeId,
                    name = s,
                    measure = ingredientsMeasures[index],
                    position = index
                )
            }

            return Recipe(recipeInfo, ingredients)
        }

        private fun getIngredientValues(
            remoteRecipeInfo: Map<String, String?>,
            key: String
        ): List<String> {
            return remoteRecipeInfo.filterKeys {
                it.commonPrefixWith(key) == key
            }
                .values
                .filterNotNull()
                .filter { it != "" }
        }
    }
}