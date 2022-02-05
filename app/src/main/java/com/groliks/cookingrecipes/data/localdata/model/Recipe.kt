package com.groliks.cookingrecipes.data.localdata.model

import androidx.room.Embedded
import androidx.room.Relation

data class Recipe(
    @Embedded
    val info: RecipeInfo,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipe_id"
    )
    val ingredients: MutableList<Ingredient>,
)