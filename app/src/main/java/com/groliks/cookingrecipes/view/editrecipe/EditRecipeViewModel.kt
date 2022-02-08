package com.groliks.cookingrecipes.view.editrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.model.Ingredient
import com.groliks.cookingrecipes.data.model.Recipe
import com.groliks.cookingrecipes.data.repositories.RecipesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditRecipeViewModel(private val repository: RecipesRepository, private val recipeId: Long) :
    ViewModel() {
    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe = _recipe.asStateFlow()
    private var isRecipeUpdated = false
    var isRecipeEditable = false
        private set

    init {
        viewModelScope.launch {
            loadRecipe()
        }
    }

    private suspend fun loadRecipe() {
        val recipe = repository.getRecipe(RecipesRepository.LOCAL_DATA_SOURCE, recipeId)
        _recipe.emit(recipe)
    }

    fun setRecipeEditable() {
        isRecipeEditable = true
    }

    fun updateRecipeName(newName: String) {
        recipe.value?.apply { info.name = newName }
        isRecipeUpdated = true
    }

    fun updateRecipeCategory(newCategory: String) {
        recipe.value?.apply { info.category = newCategory }
        isRecipeUpdated = true
    }

    fun updateRecipeDescription(newDescription: String) {
        recipe.value?.apply { info.description = newDescription }
        isRecipeUpdated = true
    }

    fun updateRecipeInstruction(newInstruction: String) {
        recipe.value?.apply { info.instruction = newInstruction }
        isRecipeUpdated = true
    }

    fun updateIngredientName(position: Int, newName: String) {
        recipe.value?.apply { ingredients[position].name = newName }
        isRecipeUpdated = true
    }

    fun updateIngredientMeasure(position: Int, newMeasure: String) {
        recipe.value?.apply { ingredients[position].measure = newMeasure }
        isRecipeUpdated = true
    }

    fun addIngredient(): Int {
        val ingredientPosition = recipe.value?.let {
            val newIngredient = Ingredient(recipeId = it.info.id, position = it.ingredients.size)
            it.ingredients.add(newIngredient)
            it.ingredients.size - 1
        } ?: throw IllegalStateException("No recipe found in EditRecipeVM")
        isRecipeUpdated = true
        return ingredientPosition
    }

    fun saveRecipe() {
        if (isRecipeUpdated) {
            recipe.value?.also { recipe ->
                viewModelScope.launch {
                    isRecipeEditable = false
                    repository.updateRecipe(recipe)
                    loadRecipe()
                    isRecipeUpdated = false
                }
            }
        }
    }
}

class EditRecipeViewModelFactory @AssistedInject constructor(
    @Assisted("recipeId") private val recipeId: Long,
    private val repository: RecipesRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditRecipeViewModel(repository, recipeId) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted("recipeId") recipeId: Long): EditRecipeViewModelFactory
    }
}