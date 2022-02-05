package com.groliks.cookingrecipes.data.localdata.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "ingredients",
    foreignKeys = [ForeignKey(
        entity = RecipeInfo::class,
        parentColumns = ["id"],
        childColumns = ["recipe_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Ingredient(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "recipe_id")
    var recipeId: Long,
    var position: Int,
    var name: String = "",
    var measure: String = "",
)