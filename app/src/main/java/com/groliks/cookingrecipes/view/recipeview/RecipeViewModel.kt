package com.groliks.cookingrecipes.view.recipeview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.data.util.DataSource
import com.groliks.cookingrecipes.data.util.LoadingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class RecipeViewModel(
    protected val recipesRepository: RecipesRepository,
    protected val recipeId: Long,
    protected val dataSource: DataSource
) : ViewModel() {
    private val _recipe = MutableStateFlow<LoadingStatus<Recipe>>(LoadingStatus.None())
    val recipe = _recipe.asStateFlow()

    init {
        updateRecipe()
    }

    fun updateRecipe() {
        viewModelScope.launch {
            _recipe.emit(LoadingStatus.Loading("Loading recipe with id $recipeId"))
            val result = try {
                val recipe = recipesRepository.getRecipe(dataSource, recipeId)
                LoadingStatus.Success(recipe, "Recipe with id $recipeId loaded")
            } catch (e: Exception) {
                LoadingStatus.Error("Failed to load recipe with id $recipeId")
            }
            _recipe.emit(result)
        }
    }
}