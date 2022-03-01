package com.groliks.cookingrecipes.view.remoterecipeslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.data.util.DataSource
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.view.recipeslist.RecipesListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemoteRecipesListViewModel(
    recipesRepository: RecipesRepository,
) : RecipesListViewModel(recipesRepository, DataSource.REMOTE) {
    private val _downloadingRecipeStatus =
        MutableStateFlow<LoadingStatus<Unit>>(LoadingStatus.None())
    val downloadingRecipeStatus = _downloadingRecipeStatus.asStateFlow()

    init {
        viewModelScope.launch {
            updateRecipesList()
        }
    }

    fun downloadRecipe(recipeInfo: RecipeInfo) {
        viewModelScope.launch {
            _downloadingRecipeStatus.emit(LoadingStatus.Loading(recipeInfo.name))
            val result = try {
                recipesRepository.downloadRecipe(recipeInfo)
                LoadingStatus.Success(Unit, recipeInfo.name)
            } catch (e: Exception) {
                LoadingStatus.Error(recipeInfo.name)
            }
            _downloadingRecipeStatus.emit(result)
            _downloadingRecipeStatus.emit(LoadingStatus.None())
        }
    }

    class Factory @Inject constructor(
        private val recipesRepository: RecipesRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RemoteRecipesListViewModel(recipesRepository) as T
        }
    }
}