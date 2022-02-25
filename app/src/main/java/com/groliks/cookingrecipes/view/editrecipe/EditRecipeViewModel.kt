package com.groliks.cookingrecipes.view.editrecipe

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.recipes.model.Ingredient
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.data.util.DataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class EditRecipeViewModel(private val repository: RecipesRepository, private val recipeId: Long) :
    ViewModel() {
    private val _recipeInfo = MutableStateFlow<RecipeInfo?>(null)
    val recipeInfo = _recipeInfo.asStateFlow()
    private val _ingredients = MutableStateFlow<List<Ingredient>?>(null)
    val ingredients = _ingredients.asStateFlow()

    var isRecipeUpdated = false
        private set
    var isRecipeInfoEditable = false
        private set

    private val _isSaveFinished =
        MutableSharedFlow<Boolean>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val isSaveFinished = _isSaveFinished.asSharedFlow()

    private var savingJob: Job? = null

    init {
        viewModelScope.launch {
            val recipe = repository.getRecipe(DataSource.LOCAL, recipeId)
            val sortedIngredients = recipe.ingredients.sortedBy { it.position }
            _recipeInfo.emit(recipe.info)
            _ingredients.emit(sortedIngredients)
        }
    }

    fun setRecipeInfoEditable() {
        isRecipeInfoEditable = true
    }

    fun updateRecipeName(newName: String) {
        recipeInfo.value?.apply { name = newName }
        isRecipeUpdated = true
    }

    fun updateRecipeCategory(newCategory: String) {
        recipeInfo.value?.apply { category = newCategory }
        isRecipeUpdated = true
    }

    fun updateRecipeDescription(newDescription: String) {
        recipeInfo.value?.apply { description = newDescription }
        isRecipeUpdated = true
    }

    fun updateRecipeInstruction(newInstruction: String) {
        recipeInfo.value?.apply { instruction = newInstruction }
        isRecipeUpdated = true
    }

    fun updateRecipePhoto(newPhoto: Bitmap) {
        recipeInfo.value?.apply { this.newPhoto = newPhoto }
        isRecipeUpdated = true
    }

    fun updateIngredientName(position: Int, newName: String) {
        ingredients.value?.apply { this[position].name = newName }
        isRecipeUpdated = true
    }

    fun updateIngredientMeasure(position: Int, newMeasure: String) {
        ingredients.value?.apply { this[position].measure = newMeasure }
        isRecipeUpdated = true
    }

    fun addIngredient() {
        ingredients.value?.toMutableList()?.apply {
            val recipeId = recipeInfo.value?.id
                ?: throw IllegalStateException("recipeInfo in EditRecipeViewModel not found")
            val newIngredient = Ingredient(recipeId = recipeId, position = size)
            add(newIngredient)
            _ingredients.tryEmit(this)
        }
        isRecipeUpdated = true
    }

    fun swapIngredients(position1: Int, position2: Int) {
        ingredients.value?.toMutableList()?.also { ingredients ->
            val tempIngredient = ingredients[position1]
            ingredients[position1] = ingredients[position2]
            ingredients[position2] = tempIngredient
            ingredients[position1].position = position1
            ingredients[position2].position = position2
            _ingredients.tryEmit(ingredients)
        }
        isRecipeUpdated = true
    }

    fun deleteIngredient(position: Int) {
        ingredients.value?.toMutableList()?.apply {
            removeAt(position)
            for (i in position..lastIndex) {
                this[i].position--
            }
            _ingredients.tryEmit(this)
        }
        isRecipeUpdated = true
    }

    fun saveRecipe() {
        recipeInfo.value?.also { recipeInfo ->
            ingredients.value?.also { ingredients ->
                savingJob = viewModelScope.launch {
                    isRecipeInfoEditable = false
                    try {
                        val recipe = Recipe(recipeInfo, ingredients)
                        repository.updateRecipe(recipe)
                    } catch (exception: IOException) {
                        Log.e("EditRecipeViewModel", exception.message.toString())
                    }
                    recipeInfo.newPhoto = null
                    isRecipeUpdated = false
                    _isSaveFinished.emit(true)
                }
            }
        }
    }

    fun cancelSaving() {
        savingJob?.cancel()
        savingJob = null
        isRecipeInfoEditable = true
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