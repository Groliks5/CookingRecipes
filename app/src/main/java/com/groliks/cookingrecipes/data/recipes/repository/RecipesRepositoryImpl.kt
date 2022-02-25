package com.groliks.cookingrecipes.data.recipes.repository

import android.content.Context
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ImageRequest
import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.localdata.LocalRecipesDataSource
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.model.RecipesInfoList
import com.groliks.cookingrecipes.data.recipes.remotedata.RemoteRecipesDataSource
import com.groliks.cookingrecipes.data.util.DataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecipesRepositoryImpl @Inject constructor(
    private val localRecipesDataSource: LocalRecipesDataSource,
    private val remoteRecipesRecipesDataSource: RemoteRecipesDataSource,
    private val appContext: Context,
) : RecipesRepository {
    override suspend fun getRecipes(
        dataSource: DataSource,
        recipesFilter: List<Filter>
    ): RecipesInfoList = withContext(Dispatchers.IO) {
        when (dataSource) {
            DataSource.LOCAL -> localRecipesDataSource.getRecipes(recipesFilter)
            DataSource.REMOTE -> remoteRecipesRecipesDataSource.getRecipes(recipesFilter)
        }
    }

    override suspend fun addRecipe(recipe: Recipe): Long = withContext(Dispatchers.IO) {
        localRecipesDataSource.addRecipe(recipe)
    }

    override suspend fun getRecipe(dataSource: DataSource, recipeId: Long): Recipe =
        withContext(Dispatchers.IO) {
            when (dataSource) {
                DataSource.LOCAL -> localRecipesDataSource.getRecipe(recipeId)
                DataSource.REMOTE -> remoteRecipesRecipesDataSource.getRecipe(recipeId)
            }
        }

    override suspend fun updateRecipe(recipe: Recipe) = withContext(Dispatchers.IO) {
        localRecipesDataSource.updateRecipe(recipe)
    }

    override suspend fun deleteRecipe(recipe: RecipeInfo) = withContext(Dispatchers.IO) {
        localRecipesDataSource.deleteRecipe(recipe)
    }

    override suspend fun setFavouriteRecipe(recipeId: Long, isFavourite: Boolean) =
        withContext(Dispatchers.IO) {
            localRecipesDataSource.setFavouriteRecipe(recipeId, isFavourite)
        }

    override suspend fun downloadRecipe(recipeInfo: RecipeInfo): Long =
        withContext(Dispatchers.IO) {
            val recipe = remoteRecipesRecipesDataSource.getRecipe(recipeInfo.id)
            recipe.info.id = 0
            val photoRequest = ImageRequest.Builder(appContext)
                .data(recipe.info.photoUri)
                .target {
                    recipe.info.newPhoto = it.toBitmap()
                }
                .build()
            appContext.imageLoader.execute(photoRequest)
            recipe.info.photoUri = ""
            recipe.ingredients.forEach {
                it.id = 0
                it.recipeId = 0
            }
            localRecipesDataSource.addRecipe(recipe)
        }
}