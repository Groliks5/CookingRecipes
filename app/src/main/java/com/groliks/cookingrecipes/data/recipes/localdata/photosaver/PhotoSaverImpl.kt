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

        val newPhotoFile = File(photoDirectory, "${System.currentTimeMillis()}.jpg")
        try {
            FileOutputStream(newPhotoFile).use {
                photo.compress(Bitmap.CompressFormat.JPEG, 100, it)
            }
        } catch (e: IOException) {
            throw IOException("Failed save new photo.")
        }

        return newPhotoFile.toUri().toString()
    }

    override suspend fun rewritePhoto(oldFileName: String, photo: Bitmap): String {
        deletePhoto(oldFileName)
        return savePhoto(photo)
    }

    private fun checkAvailabilityDirectory() {
        if (photoDirectory == null) {
            throw IOException("External files directory is not available")
        }
    }

    override suspend fun deletePhoto(fileName: String) {
        checkAvailabilityDirectory()
        val photo = File(fileName)
        try {
            photo.delete()
        } catch (e: IOException) {
            throw IOException("Failed delete photo")
        }
    }
}