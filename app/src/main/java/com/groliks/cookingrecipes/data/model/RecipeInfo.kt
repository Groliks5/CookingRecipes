package com.groliks.cookingrecipes.data.model

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeInfo(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String = "",
    var category: String = "",
    var photoUri: String = "",
    var description: String = "",
    var instruction: String = "",
    var isFavourite: Boolean = false,
    @Ignore
    var newPhoto: Bitmap? = null,
)