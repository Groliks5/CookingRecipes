package com.groliks.cookingrecipes.view.localrecipeview

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

class LocalRecipeViewModel(
    recipesRepository: RecipesRepository,
    recipeId: Long,
) : RecipeViewModel(recipesRepository, recipeId, DataSource.LOCAL) {
    private val _favouriteState = MutableStateFlow(false)
    val favouriteState = _favouriteState.asStateFlow()

    private val _deletingRecipeState = MutableStateFlow<LoadingStatus<Unit>>(LoadingStatus.None())
    val deletingRecipeState = _deletingRecipeState.asStateFlow()

    fun setFavouriteState(isFavourite: Boolean) {
        _favouriteState.tryEmit(isFavourite)
    }

    fun changeFavouriteState(isFavourite: Boolean) {
        viewModelScope.launch {
            (recipe.value as? LoadingStatus.Success)?.data?.also { recipe ->
                recipesRepository.setFavouriteRecipe(recipeId, !isFavourite)
                recipe.info.isFavourite = !isFavourite
                _favouriteState.emit(!isFavourite)
            }
        }
    }

    fun deleteRecipe() {
        viewModelScope.launch {
            (recipe.value as? LoadingStatus.Success)?.data?.also { recipe ->
                _deletingRecipeState.emit(LoadingStatus.Loading())
                val result = try {
                    recipesRepository.deleteRecipe(recipe.info)
                    LoadingStatus.Success(Unit)
                } catch (e: Exception) {
                    LoadingStatus.Error()
                }
                _deletingRecipeState.emit(result)
            }
        }
    }
}

class LocalRecipeViewModelFactory @AssistedInject constructor(
    private val recipesRepository: RecipesRepository,
    @Assisted("recipeId") private val recipeId: Long,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LocalRecipeViewModel(recipesRepository, recipeId) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("recipeId") recipeId: Long): LocalRecipeViewModelFactory
    }
}