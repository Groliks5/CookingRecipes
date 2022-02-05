package com.groliks.cookingrecipes.view.recipeslist

import com.groliks.cookingrecipes.data.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipesListViewModel {
    val recipesList: Flow<List<Recipe>>
}