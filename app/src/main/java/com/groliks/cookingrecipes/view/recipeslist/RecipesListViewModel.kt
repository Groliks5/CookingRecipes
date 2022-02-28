package com.groliks.cookingrecipes.view.recipeslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.data.util.DataSource
import com.groliks.cookingrecipes.data.util.LoadingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class RecipesListViewModel(
    protected val recipesRepository: RecipesRepository,
    protected val dataSource: DataSource,
) : ViewModel() {
    private var _recipesList =
        MutableStateFlow<LoadingStatus<List<RecipeInfo>>>(LoadingStatus.None())
    val recipesList = _recipesList.asStateFlow()

    private val _filters = MutableStateFlow(listOf<Filter>())
    val filters = _filters.asStateFlow()

    init {
        viewModelScope.launch {
            updateRecipesList()
        }
    }

    suspend fun updateRecipesList() {
        _recipesList.emit(LoadingStatus.Loading())
        val result = try {
            val recipes = recipesRepository.getRecipes(dataSource, filters.value)
            LoadingStatus.Success(recipes)
        } catch (e: Exception) {
            LoadingStatus.Error()
        }
        _recipesList.emit(result)
    }

    fun updateSelectedFilters(selectedFilters: List<Filter>) {
        viewModelScope.launch {
            _filters.emit(selectedFilters)
            updateRecipesList()
        }
    }

    fun deleteFilter(filter: Filter) {
        viewModelScope.launch {
            filters.value.toMutableList().apply {
                remove(filter)
                _filters.emit(this)
            }
            updateRecipesList()
        }
    }
}