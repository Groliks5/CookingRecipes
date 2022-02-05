package com.groliks.cookingrecipes.view.recipeslist.recyclerview

import android.view.View
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.groliks.cookingrecipes.data.model.Recipe

abstract class RecipesAdapter :
    ListAdapter<Recipe, RecipesAdapter.RecipeViewHolder>(RecipeDiffUtilCallback()) {

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    abstract inner class RecipeViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        abstract fun bind(recipe: Recipe)
    }
}