package com.groliks.cookingrecipes.data.recipes.localdata.photosaver

import android.graphics.Bitmap
import androidx.core.net.toUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotoSaverImpl @Inject constructor(
    private val photoDirectory: File?
) : PhotoSaver {
    override suspend fun savePhoto(photo: Bitmap): String {
        checkAvailabilityDirectory()
        val newPhotoName = File(photoDirectory, "${System.currentTimeMillis()}.jpg")
        try {
            val outputStream = FileOutputStream(newPhotoName)
            photo.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        } catch (e: IOException) {
            throw IOException("Failed save new photo.")
        }
        return newPhotoName.toUri().toString()
    }

    override suspend fun rewritePhoto(oldFileName: String, photo: Bitmap): String {
        checkAvailabilityDirectory()
        val oldPhoto = File(oldFileName)
        try {
            oldPhoto.delete()
        } catch (e: IOException) {
            throw IOException("Failed delete old photo.")
        }
        return savePhoto(photo)
    }

    private fun checkAvailabilityDirectory() {
        if (photoDirectory == null) {
            throw IOException("External files directory is not available")
        }
    }
}