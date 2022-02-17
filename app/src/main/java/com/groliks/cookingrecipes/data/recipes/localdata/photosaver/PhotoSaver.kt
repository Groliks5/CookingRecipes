package com.groliks.cookingrecipes.data.recipes.localdata.photosaver

import android.graphics.Bitmap

interface PhotoSaver {
    suspend fun savePhoto(photo: Bitmap): String
    suspend fun rewritePhoto(oldFileName: String, photo: Bitmap): String
}