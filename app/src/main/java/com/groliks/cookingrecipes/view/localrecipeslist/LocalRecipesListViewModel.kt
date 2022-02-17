package com.groliks.cookingrecipes.view.localrecipeslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.DataSource
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.view.recipeslist.RecipesListViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalRecipesListViewModel(
    private val repository: RecipesRepository
) : RecipesListViewModel() {
    private val _recipesList = MutableStateFlow<List<Recipe>>(listOf())
    override val recipesList = _recipesList.asStateFlow()

    init {
        viewModelScope.launch {
            updateRecipesList()
        }
    }

    override suspend fun updateRecipesList() {
        val recipes = repository.getRecipes(DataSource.LOCAL, filters.value)
        _recipesList.emit(recipes)
    }

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