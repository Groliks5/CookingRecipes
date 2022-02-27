package com.groliks.cookingrecipes.data.recipes.model

import androidx.room.Embedded
import androidx.room.Relation

data class Recipe(
    @Embedded
    val info: RecipeInfo = RecipeInfo(),
    @Relation(
        parentColumn = "id",
        entityColumn = "recipe_id"
    )
    val ingredients: List<Ingredient> = listOf(),
)