package com.groliks.cookingrecipes.data.localdata.photosaver

import android.graphics.Bitmap

interface PhotoSaver {
    suspend fun savePhoto(photo: Bitmap): String
    suspend fun rewritePhoto(oldFileName: String, photo: Bitmap): String
}