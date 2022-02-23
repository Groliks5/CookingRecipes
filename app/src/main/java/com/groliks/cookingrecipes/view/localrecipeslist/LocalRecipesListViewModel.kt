package com.groliks.cookingrecipes.view.localrecipeslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.DataSource
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.model.RecipeList
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.view.recipeslist.RecipesListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalRecipesListViewModel(
    private val repository: RecipesRepository
) : RecipesListViewModel() {
    private val _recipesList = MutableStateFlow<LoadingStatus>(LoadingStatus.Loading())
    override val recipesList = _recipesList.asStateFlow()

    init {
        viewModelScope.launch {
            updateRecipesList()
        }
    }

    override suspend fun updateRecipesList() {
        val result = try {
            val recipes = repository.getRecipes(DataSource.LOCAL, filters.value)
            LoadingStatus.Success(recipes)
        } catch (e: Exception) {
            LoadingStatus.Error(e.message.toString())
        }
        _recipesList.emit(result)
    }

    fun createRecipe(): SharedFlow<Long?> {
        val idState = MutableStateFlow<Long?>(null)
        viewModelScope.launch {
            val newRecipe = Recipe(RecipeInfo(), mutableListOf())
            val id = repository.addRecipe(newRecipe)
            idState.emit(id)
        }
        return idState
    }

    fun deleteRecipe(recipeId: Long) {
        viewModelScope.launch {
            (recipesList.value as? LoadingStatus.Success)?.also {
                val recipesList = it.data as RecipeList
                val recipe = recipesList.recipes.find { it.id == recipeId }
                recipe?.also {
                    repository.deleteRecipe(it)
                    updateRecipesList()
                }
            }
        }
    }

    fun setFavouriteRecipe(recipeId: Long, isFavourite: Boolean) {
        viewModelScope.launch {
            repository.setFavouriteRecipe(recipeId, isFavourite)
        }
    }

    class Factory @Inject constructor(private val repository: RecipesRepository) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LocalRecipesListViewModel(repository) as T
        }
    }
}