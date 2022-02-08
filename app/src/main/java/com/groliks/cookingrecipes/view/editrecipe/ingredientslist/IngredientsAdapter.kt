package com.groliks.cookingrecipes.view.editrecipe.ingredientslist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.groliks.cookingrecipes.data.model.Ingredient
import com.groliks.cookingrecipes.databinding.ItemAddIngredientBinding
import com.groliks.cookingrecipes.databinding.ItemEditIngredientBinding

private const val INGREDIENT_VIEW_TYPE = 1
private const val ADD_INGREDIENT_VIEW_TYPE = 2

private const val ADD_INGREDIENT_BUTTON_SIZE = 1

class IngredientsAdapter(
    private val ingredients: List<Ingredient>,
    private val onIngredientNameChange: (Int, String) -> Unit,
    private val onIngredientMeasureChange: (Int, String) -> Unit,
    private val onAddIngredient: () -> Int
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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

    override fun getItemViewType(position: Int): Int {
        return if (position < ingredients.size) {
            INGREDIENT_VIEW_TYPE
        } else {
            ADD_INGREDIENT_VIEW_TYPE
        }
    }

    private fun addIngredient() {
        val insertPosition = onAddIngredient()
        notifyItemInserted(insertPosition)
    }

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