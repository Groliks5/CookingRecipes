package com.groliks.cookingrecipes.data.recipes.remotedata

import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.remotedata.retrofit.RecipesApiService
import com.groliks.cookingrecipes.data.recipes.remotedata.retrofit.RemoteRecipeInfo
import com.groliks.cookingrecipes.data.recipes.remotedata.util.RemoteRecipeToRecipeConverter
import okio.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteRecipesDataSourceImpl @Inject constructor(
    private val recipesApiService: RecipesApiService,
) : RemoteRecipesDataSource {
    override suspend fun getRecipes(filters: List<Filter>): List<RecipeInfo> {
        if (filters.isEmpty()) {
            return listOf()
        }

        val remoteRecipesInfoByFilter = buildMap<String, MutableList<RemoteRecipeInfo>> {
            put(Filter.Type.CATEGORY.name, mutableListOf())
            put(Filter.Type.AREA.name, mutableListOf())
        }

        for (filter in filters) {
            val requestResult = when (filter.type) {
                Filter.Type.CATEGORY -> recipesApiService.getRecipesWithCategory(filter.name)
                Filter.Type.AREA -> recipesApiService.getRecipesWithArea(filter.name)
                else -> throw IllegalArgumentException("Unsupported filter type")
            }
            remoteRecipesInfoByFilter[filter.type.name]?.addAll(requestResult.recipes)
        }

        val remoteRecipesInfo = remoteRecipesInfoByFilter.map { remoteRecipesInfo ->
            remoteRecipesInfo.value.distinctBy { it.id }.toSet()
        }
            .filter { it.isNotEmpty() }
            .reduce { acc, cur ->
                acc.intersect(cur)
            }
            .toList()

        val recipesInfo = mutableListOf<RecipeInfo>()
        val convertedRecipes =
            RemoteRecipeToRecipeConverter.convertRemoteRecipesInfo(remoteRecipesInfo)
        recipesInfo.addAll(convertedRecipes)
        return recipesInfo
    }

    override suspend fun getRecipe(recipeId: Long): Recipe {
        val remoteRecipe =
            recipesApiService.getRecipe(recipeId.toInt()).recipes.firstOrNull()
                ?: throw IOException("Recipe with id=$recipeId is not found")
        return RemoteRecipeToRecipeConverter.convertRemoteRecipe(remoteRecipe)
    }
}