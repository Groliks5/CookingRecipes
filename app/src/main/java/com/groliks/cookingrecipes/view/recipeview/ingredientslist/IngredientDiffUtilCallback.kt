package com.groliks.cookingrecipes.view.recipeview.ingredientslist

import androidx.recyclerview.widget.DiffUtil
import com.groliks.cookingrecipes.data.recipes.model.Ingredient

class IngredientItemDiffUtilCallback : DiffUtil.ItemCallback<Ingredient>() {
    override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
        return oldItem == newItem
    }
}