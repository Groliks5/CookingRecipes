package com.groliks.cookingrecipes.view.localrecipeview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.data.util.DataSource
import com.groliks.cookingrecipes.view.recipeview.RecipeViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class LocalRecipeViewModel(
    recipesRepository: RecipesRepository,
    recipeId: Long,
) : RecipeViewModel(recipesRepository, recipeId, DataSource.LOCAL)

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