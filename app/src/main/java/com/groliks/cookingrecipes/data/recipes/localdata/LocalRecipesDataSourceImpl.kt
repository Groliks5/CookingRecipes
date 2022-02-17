package com.groliks.cookingrecipes.data.recipes.localdata

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.localdata.database.RecipesDao
import com.groliks.cookingrecipes.data.recipes.localdata.photosaver.PhotoSaver
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRecipesDataSourceImpl @Inject constructor(
    private val recipesDao: RecipesDao,
    private val photoSaver: PhotoSaver,
) : LocalRecipesDataSource {
    override suspend fun getRecipes(recipesFilter: List<Filter>): List<Recipe> {
        var categories = recipesFilter.filter { it.type == Filter.Type.CATEGORY }
            .map { it.name }
        if (categories.isEmpty()) {
            categories = listOf("%")
        }
        val isOnlyFavourite =
            recipesFilter.find { it.type == Filter.Type.FAVOUTRITE }?.let { true } ?: false
        return recipesDao.getRecipes(categories, isOnlyFavourite)
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