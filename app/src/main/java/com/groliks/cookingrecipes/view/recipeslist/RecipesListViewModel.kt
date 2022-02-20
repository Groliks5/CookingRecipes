package com.groliks.cookingrecipes.view.recipeslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.util.LoadingStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class RecipesListViewModel : ViewModel() {
    abstract val recipesList: StateFlow<LoadingStatus>
    private val _filters = MutableStateFlow(listOf<Filter>())
    val filters = _filters.asStateFlow()

    abstract suspend fun updateRecipesList()

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