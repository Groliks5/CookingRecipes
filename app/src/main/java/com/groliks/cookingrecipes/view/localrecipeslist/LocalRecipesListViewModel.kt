package com.groliks.cookingrecipes.view.localrecipeslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.model.Recipe
import com.groliks.cookingrecipes.data.model.RecipeInfo
import com.groliks.cookingrecipes.data.repositories.RecipesRepository
import com.groliks.cookingrecipes.view.recipeslist.RecipesListViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalRecipesListViewModel(
    private val repository: RecipesRepository
) : ViewModel(), RecipesListViewModel {
    override val recipesList: Flow<List<Recipe>> =
        repository.getRecipes(RecipesRepository.LOCAL_DATA_SOURCE)

    fun createRecipe(): SharedFlow<Long?> {
        val idState = MutableStateFlow<Long?>(null)
        viewModelScope.launch {
            val newRecipe = Recipe(RecipeInfo(), mutableListOf())
            val id = repository.addRecipe(newRecipe)
            idState.emit(id)
        }
        return idState
    }

    class Factory @Inject constructor(private val repository: RecipesRepository) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LocalRecipesListViewModel(repository) as T
        }
    }
}