package com.groliks.cookingrecipes.view.remoterecipeview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
    recipesRepository: RecipesRepository,
    recipeId: Long,
) : RecipeViewModel(recipesRepository, recipeId, DataSource.REMOTE) {
    private val _downloadingRecipeStatus =
        MutableStateFlow<LoadingStatus<Unit>>(LoadingStatus.None())
    val downloadingRecipeStatus = _downloadingRecipeStatus.asStateFlow()

    fun downloadRecipe() {
        viewModelScope.launch {
            val recipeLoadingStatus = recipe.value
            if (recipeLoadingStatus is LoadingStatus.Success) {
                val recipeInfo = recipeLoadingStatus.data.info
                _downloadingRecipeStatus.emit(LoadingStatus.Loading("Downloading recipe: ${recipeInfo.name}"))
                val result = try {
                    recipesRepository.downloadRecipe(recipeInfo)
                    LoadingStatus.Success(Unit, "Recipe ${recipeInfo.name} downloaded")
                } catch (e: Exception) {
                    LoadingStatus.Error("Failed to download recipe: ${recipeInfo.name}")
                }
                _downloadingRecipeStatus.emit(result)
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