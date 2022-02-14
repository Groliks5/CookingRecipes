package com.groliks.cookingrecipes.view.editrecipe.ingredientslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.groliks.cookingrecipes.data.model.Ingredient
import com.groliks.cookingrecipes.databinding.ItemAddIngredientBinding
import com.groliks.cookingrecipes.databinding.ItemEditIngredientBinding

private const val INGREDIENT_VIEW_TYPE = 1
private const val ADD_INGREDIENT_VIEW_TYPE = 2

private const val ADD_INGREDIENT_BUTTON_SIZE = 1

class IngredientsAdapter(
    private val onIngredientNameChange: (Int, String) -> Unit,
    private val onIngredientMeasureChange: (Int, String) -> Unit,
    private val onAddIngredient: () -> Unit,
    private val onIngredientMove: (Int, Int) -> Unit,
    private val onDeleteIngredient: (Int) -> Unit,
    private val coroutineScope: LifecycleCoroutineScope,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), IngredientTouchHelperAdapter {
    private var ingredients: List<Ingredient> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == INGREDIENT_VIEW_TYPE) {
            val binding = ItemEditIngredientBinding.inflate(inflater, parent, false)
            IngredientViewHolder(binding)
        } else {
            val binding = ItemAddIngredientBinding.inflate(inflater, parent, false)
            AddIngredientViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IngredientViewHolder) {
            holder.bind(ingredients[position])
        }
    }

    override fun getItemCount(): Int = ingredients.size + ADD_INGREDIENT_BUTTON_SIZE

    override fun getLastIngredientPosition(): Int = ingredients.lastIndex

    override fun getItemViewType(position: Int): Int {
        return if (position < ingredients.size) {
            INGREDIENT_VIEW_TYPE
        } else {
            ADD_INGREDIENT_VIEW_TYPE
        }
    }

    fun submitIngredients(newIngredients: List<Ingredient>) {
        coroutineScope.launchWhenStarted {
            val diffCallback = IngredientDiffUtilCallback(ingredients, newIngredients)
            val diff = DiffUtil.calculateDiff(diffCallback)
            ingredients = newIngredients
            diff.dispatchUpdatesTo(this@IngredientsAdapter)
        }
    }

    override fun onMove(oldPosition: Int, newPosition: Int) =
        onIngredientMove(oldPosition, newPosition)

    override fun onSwipe(position: Int) = onDeleteIngredient(position)

    private fun addIngredient() = onAddIngredient()

    inner class IngredientViewHolder(
        private val binding: ItemEditIngredientBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        private var ingredient: Ingredient? = null

        init {
            binding.ingredientName.doOnUpdateIngredientText(onIngredientNameChange)
            binding.ingredientMeasure.doOnUpdateIngredientText(onIngredientMeasureChange)
        }

        fun bind(ingredient: Ingredient) {
            this.ingredient = null
            binding.ingredientName.setText(ingredient.name)
            binding.ingredientMeasure.setText(ingredient.measure)
            this.ingredient = ingredient
        }

        private fun EditText.doOnUpdateIngredientText(callback: (Int, String) -> Unit) {
            doAfterTextChanged { newText ->
                ingredient?.also { ingredient ->
                    callback(ingredient.position, newText.toString())
                }
            }
        }
    }

    inner class AddIngredientViewHolder(
        binding: ItemAddIngredientBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.addIngredient.setOnClickListener { addIngredient() }
        }
    }
}