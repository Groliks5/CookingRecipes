package com.groliks.cookingrecipes.view.remoterecipeslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.DataSource
import com.groliks.cookingrecipes.data.filters.repository.FiltersRepository
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.view.recipeslist.RecipesListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RemoteRecipesListViewModel(
    private val recipesRepository: RecipesRepository,
    private val filtersRepository: FiltersRepository,
) : RecipesListViewModel() {
    private val _recipesList = MutableStateFlow<LoadingStatus>(LoadingStatus.None)
    override val recipesList = _recipesList.asStateFlow()

    private val _downloadingRecipeStatus = MutableStateFlow<LoadingStatus>(LoadingStatus.None)
    val downloadingRecipeStatus = _downloadingRecipeStatus.asStateFlow()

    init {
        viewModelScope.launch {
            updateRecipesList()
        }
    }

    override suspend fun updateRecipesList() {
        _recipesList.emit(LoadingStatus.Loading())
        val result = try {
            val recipes = recipesRepository.getRecipes(DataSource.REMOTE, filters.value)
            LoadingStatus.Success(recipes)
        } catch (e: Exception) {
            LoadingStatus.Error(e.message.toString())
        }
        _recipesList.emit(result)
        _recipesList.emit(LoadingStatus.None)
    }

    fun downloadRecipe(recipeInfo: RecipeInfo) {
        viewModelScope.launch {
            _downloadingRecipeStatus.emit(LoadingStatus.Loading("Downloading recipe: ${recipeInfo.name}"))
            try {
                recipesRepository.downloadRecipe(recipeInfo)
                _downloadingRecipeStatus.emit(
                    LoadingStatus.Success(
                        message = "Recipe ${recipeInfo.name} downloaded"
                    )
                )
            } catch (e: Exception) {
                _downloadingRecipeStatus.emit(LoadingStatus.Error(e.message.toString()))
            }
        }
    }

    class Factory @Inject constructor(
        private val recipesRepository: RecipesRepository,
        private val filtersRepository: FiltersRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return RemoteRecipesListViewModel(recipesRepository, filtersRepository) as T
        }
    }
}