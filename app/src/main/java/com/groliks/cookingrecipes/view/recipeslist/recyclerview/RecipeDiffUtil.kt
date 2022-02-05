package com.groliks.cookingrecipes.view.recipeslist.recyclerview

import androidx.recyclerview.widget.DiffUtil
import com.groliks.cookingrecipes.data.model.Recipe

class RecipeDiffUtilCallback : DiffUtil.ItemCallback<Recipe>() {
    override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem.info.id == newItem.info.id
    }

    override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
        return oldItem == newItem
    }
}