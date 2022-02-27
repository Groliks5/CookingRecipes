package com.groliks.cookingrecipes.view.editrecipe

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.recipes.model.Ingredient
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.recipes.repository.RecipesRepository
import com.groliks.cookingrecipes.data.util.DataSource
import com.groliks.cookingrecipes.data.util.LoadingStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class EditRecipeViewModel(
    private val repository: RecipesRepository,
    private val recipeId: Long
) : ViewModel() {
    private val _recipeInfo = MutableStateFlow<RecipeInfo?>(null)
    val recipeInfo = _recipeInfo.asStateFlow()
    private val _ingredients = MutableStateFlow<List<Ingredient>?>(null)
    val ingredients = _ingredients.asStateFlow()

    var isRecipeUpdated = false
        private set

    private val _savingStatus = MutableStateFlow<LoadingStatus<Unit>>(LoadingStatus.None())
    val savingStatus = _savingStatus.asStateFlow()

    private var savingJob: Job? = null

    init {
        viewModelScope.launch {
            val recipe = repository.getRecipe(DataSource.LOCAL, recipeId)
            val sortedIngredients = recipe.ingredients.sortedBy { it.position }
            _recipeInfo.emit(recipe.info)
            _ingredients.emit(sortedIngredients)
        }
    }

    private inline fun updateRecipeInfo(updateLambda: RecipeInfo.() -> Unit) {
        recipeInfo.value?.apply { updateLambda() }
        isRecipeUpdated = true
    }

    fun updateRecipeName(newName: String) = updateRecipeInfo { name = newName }

    fun updateRecipeCategory(newCategory: String) = updateRecipeInfo { category = newCategory }

    fun updateRecipeDescription(newDescription: String) {
        updateRecipeInfo { description = newDescription }
    }

    fun updateRecipeInstruction(newInstruction: String) {
        updateRecipeInfo { instruction = newInstruction }
    }

    fun updateRecipePhoto(newPhoto: Bitmap) = updateRecipeInfo { this.newPhoto = newPhoto }

    private fun updateIngredients(updateLambda: MutableList<Ingredient>.() -> Unit) {
        ingredients.value?.toMutableList()?.apply { updateLambda() }
        isRecipeUpdated = true
    }

    fun updateIngredientName(position: Int, newName: String) {
        updateIngredients { this[position].name = newName }
    }

    fun updateIngredientMeasure(position: Int, newMeasure: String) {
        updateIngredients { this[position].measure = newMeasure }
    }

    fun addIngredient() {
        updateIngredients {
            val recipeId = recipeInfo.value?.id
                ?: throw IllegalStateException("recipeInfo in EditRecipeViewModel not found")
            val newIngredient = Ingredient(recipeId = recipeId, position = size)
            add(newIngredient)
            _ingredients.tryEmit(this)
        }
    }

    fun swapIngredients(position1: Int, position2: Int) {
        updateIngredients {
            this[position1] = this[position2].also {
                this[position2] = this[position1]
                this[position1].position = position1
                this[position2].position = position2
            }
            _ingredients.tryEmit(this)
        }
    }

    fun deleteIngredient(position: Int) {
        updateIngredients {
            removeAt(position)
            for (i in position..lastIndex) {
                this[i].position--
            }
            _ingredients.tryEmit(this)
        }
    }

    fun saveRecipe() {
        val recipeInfo = recipeInfo.value
        val ingredients = ingredients.value
        if (recipeInfo != null && ingredients != null) {
            savingJob = viewModelScope.launch {
                _savingStatus.emit(LoadingStatus.Loading("Saving recipe"))
                val result = try {
                    val recipe = Recipe(recipeInfo, ingredients)
                    repository.updateRecipe(recipe)
                    LoadingStatus.Success(Unit, "Recipe saved")
                } catch (exception: IOException) {
                    LoadingStatus.Error("Failed to save recipe")
                } catch (exception: CancellationException) {
                    LoadingStatus.Error("The save operation has been canceled")
                }
                recipeInfo.newPhoto = null
                isRecipeUpdated = false
                _savingStatus.emit(result)
            }
        }
    }

    fun saveResultReceived() {
        _savingStatus.tryEmit(LoadingStatus.None())
    }

    fun cancelSaving() {
        savingJob?.cancel()
        savingJob = null
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