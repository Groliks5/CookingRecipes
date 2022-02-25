package com.groliks.cookingrecipes.view.remoterecipeview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.data.util.DataSource
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.view.recipeview.RecipeViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RemoteRecipeViewModel(
    private val recipesRepository: RecipesRepository,
    private val recipeId: Long,
) : RecipeViewModel() {
    private val _recipe = MutableStateFlow<LoadingStatus>(LoadingStatus.Loading())
    override val recipe = _recipe.asStateFlow()

    private val _downloadingRecipeStatus = MutableStateFlow<LoadingStatus>(LoadingStatus.None)
    val downloadingRecipeStatus = _downloadingRecipeStatus.asStateFlow()

    init {
        viewModelScope.launch {
            val recipe = recipesRepository.getRecipe(DataSource.REMOTE, recipeId)
            _recipe.emit(LoadingStatus.Success(recipe))
        }
    }

    fun downloadRecipe() {
        viewModelScope.launch {
            val recipeLoadingStatus = recipe.value
            if (recipeLoadingStatus is LoadingStatus.Success) {
                val recipeInfo = (recipeLoadingStatus.data as Recipe).info
                _downloadingRecipeStatus.emit(LoadingStatus.Loading("Downloading recipe: ${recipeInfo.name}"))
                try {
                    val recipeId = recipesRepository.downloadRecipe(recipeInfo)
                    _downloadingRecipeStatus.emit(
                        LoadingStatus.Success(
                            data = recipeId,
                            message = "Recipe ${recipeInfo.name} downloaded"
                        )
                    )
                } catch (e: Exception) {
                    _downloadingRecipeStatus.emit(LoadingStatus.Error(e.message.toString()))
                }
                _downloadingRecipeStatus.emit(LoadingStatus.None)
            }
        }
    }
}

class RemoteRecipeViewModelFactory @AssistedInject constructor(
    private val recipesRepository: RecipesRepository,
    @Assisted("recipeId") private val recipeId: Long,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RemoteRecipeViewModel(recipesRepository, recipeId) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("recipeId") recipeId: Long): RemoteRecipeViewModelFactory
    }
}