package com.groliks.cookingrecipes.view.recipeview.ingredientslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.data.recipes.model.Ingredient
import com.groliks.cookingrecipes.databinding.ItemViewIngredientBinding

class ViewIngredientsAdapter : ListAdapter<Ingredient, ViewIngredientsAdapter.IngredientViewHolder>(
    IngredientItemDiffUtilCallback()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemViewIngredientBinding.inflate(inflater, parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class IngredientViewHolder(
        private val binding: ItemViewIngredientBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient: Ingredient) {
            val resources = binding.root.resources
            val ingredientSeparator = resources.getString(R.string.ingredient_separator)
            val ingredientText = binding.root.resources.getString(
                R.string.ingredient,
                ingredient.name,
                ingredientSeparator,
                ingredient.measure
            )
            binding.ingredient.text = ingredientText
        }
    }
}