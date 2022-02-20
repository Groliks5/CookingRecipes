package com.groliks.cookingrecipes.view.recipeslist.recipeslist

import androidx.recyclerview.widget.DiffUtil
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo

class RecipeDiffUtilCallback : DiffUtil.ItemCallback<RecipeInfo>() {
    override fun areItemsTheSame(oldItem: RecipeInfo, newItem: RecipeInfo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RecipeInfo, newItem: RecipeInfo): Boolean {
        return oldItem == newItem
    }
}