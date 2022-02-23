package com.groliks.cookingrecipes.view.recipeview

import androidx.lifecycle.ViewModel
import com.groliks.cookingrecipes.data.util.LoadingStatus
import kotlinx.coroutines.flow.StateFlow

abstract class RecipeViewModel : ViewModel() {
    abstract val recipe: StateFlow<LoadingStatus>
}