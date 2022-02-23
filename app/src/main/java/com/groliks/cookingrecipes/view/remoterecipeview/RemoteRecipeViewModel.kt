package com.groliks.cookingrecipes.view.remoterecipeview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.DataSource
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
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
    private val _recipe = MutableStateFlow<LoadingStatus>(LoadingStatus.Loading)
    override val recipe = _recipe.asStateFlow()

    init {
        viewModelScope.launch {
            val recipe = recipesRepository.getRecipe(DataSource.REMOTE, recipeId)
            _recipe.emit(LoadingStatus.Success(recipe))
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