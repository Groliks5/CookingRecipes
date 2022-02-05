package com.groliks.cookingrecipes.view.localrecipeslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.groliks.cookingrecipes.data.model.Recipe
import com.groliks.cookingrecipes.data.repositories.RecipesRepository
import com.groliks.cookingrecipes.view.recipeslist.RecipesListViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalRecipesListViewModel(
    private val repository: RecipesRepository
) : ViewModel(), RecipesListViewModel {
    override val recipesList: Flow<List<Recipe>> =
        repository.getRecipes(RecipesRepository.LOCAL_DATA_SOURCE)

    class Factory @Inject constructor(private val repository: RecipesRepository) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LocalRecipesListViewModel(repository) as T
        }
    }
}