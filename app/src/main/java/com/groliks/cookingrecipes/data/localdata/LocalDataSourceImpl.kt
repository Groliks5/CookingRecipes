package com.groliks.cookingrecipes.data.localdata

import com.groliks.cookingrecipes.data.localdata.database.RecipesDao
import com.groliks.cookingrecipes.data.localdata.photosaver.PhotoSaver
import com.groliks.cookingrecipes.data.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl @Inject constructor(
    private val recipesDao: RecipesDao,
    private val photoSaver: PhotoSaver,
) : LocalDataSource {
    override fun getRecipes(): Flow<List<Recipe>> {
        return recipesDao.getRecipes()
    }

    override suspend fun addRecipe(recipe: Recipe): Long {
        return recipesDao.addRecipe(recipe)
    }

    override suspend fun getRecipe(recipeId: Long): Recipe {
        return recipesDao.getRecipe(recipeId)
    }

    override suspend fun updateRecipe(recipe: Recipe) {
        val newPhoto = recipe.info.newPhoto
        if (newPhoto != null) {
            val oldFileName = recipe.info.photoUri
            val newPhotoUri = if (oldFileName.isNotBlank()) {
                photoSaver.rewritePhoto(oldFileName, newPhoto)
            } else {
                photoSaver.savePhoto(newPhoto)
            }
            recipe.info.photoUri = newPhotoUri
        }
        recipesDao.updateRecipe(recipe)
    }
}