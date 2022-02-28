package com.groliks.cookingrecipes.view.localrecipeslist

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.data.util.DataSource
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.view.recipeslist.RecipesListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalRecipesListViewModel(
    recipesRepository: RecipesRepository
) : RecipesListViewModel(recipesRepository, DataSource.LOCAL) {
    private val _newRecipeId = MutableStateFlow<Long?>(null)
    val newRecipeId = _newRecipeId.asStateFlow()

    private val _deletingRecipeState = MutableStateFlow<LoadingStatus<Unit>>(LoadingStatus.None())
    val deletingRecipeState = _deletingRecipeState.asStateFlow()

    init {
        viewModelScope.launch {
            updateRecipesList()
        }
    }

    fun createRecipe() {
        viewModelScope.launch {
            val newRecipe = Recipe()
            val id = recipesRepository.addRecipe(newRecipe)
            _newRecipeId.emit(id)
            _newRecipeId.emit(null)
        }
    }

    fun deleteRecipe(recipeId: Long) {
        viewModelScope.launch {
            (recipesList.value as? LoadingStatus.Success)?.data?.also { recipes ->
                val recipe = recipes.find { it.id == recipeId }
                recipe?.also {
                    _deletingRecipeState.emit(LoadingStatus.Loading(it.name))
                    val result = try {
                        recipesRepository.deleteRecipe(it)
                        LoadingStatus.Success(Unit, it.name)
                    } catch (e: Exception) {
                        LoadingStatus.Error(it.name)
                    }
                    _deletingRecipeState.emit(result)
                }
            }
        }
    }

    fun setFavouriteRecipe(recipeId: Long, isFavourite: Boolean) {
        viewModelScope.launch {
            recipesRepository.setFavouriteRecipe(recipeId, isFavourite)
        }
    }

    class Factory @Inject constructor(
        private val repository: RecipesRepository,
        private val appContext: Context
    ) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LocalRecipesListViewModel(repository) as T
        }
    }
}