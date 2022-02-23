package com.groliks.cookingrecipes.data.recipes.remotedata.util

import com.groliks.cookingrecipes.data.recipes.model.Ingredient
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.model.RecipeList
import com.groliks.cookingrecipes.data.recipes.remotedata.retrofit.RemoteRecipeInfoList

class RemoteRecipeToRecipeConverter {
    companion object {
        fun convertRemoteRecipesInfo(remoteRecipesInfo: RemoteRecipeInfoList): RecipeList {
            val recipes = mutableListOf<RecipeInfo>()
            for (recipe in remoteRecipesInfo.recipes) {
                val recipeInfo = RecipeInfo(
                    name = recipe.name,
                    photoUri = recipe.photoUri,
                    description = "",
                    id = recipe.id
                )
                recipes.add(recipeInfo)
            }
            return RecipeList(recipes)
        }

        fun convertRemoteRecipe(remoteRecipe: Map<String, String?>): Recipe {
            val recipeId = remoteRecipe["idMeal"]!!.toLong()
            val recipeName = remoteRecipe["strMeal"]!!
            val recipeInstruction = remoteRecipe["strInstructions"]!!
            val recipePhotoUri = remoteRecipe["strMealThumb"]!!
            val recipeDescription = "Recipe from the meal DB"
            val recipeCategory = remoteRecipe["strCategory"]!!
            val ingredientsNames = remoteRecipe.filterKeys {
                it.commonPrefixWith("strIngredient") == "strIngredient"
            }
                .values
                .filterNotNull()
                .filter { it != "" }
            val ingredientsMeasures = remoteRecipe.filterKeys {
                it.commonPrefixWith("strMeasure") == "strMeasure"
            }
                .values
                .filterNotNull()
                .filter { it != "" }

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
    }
}