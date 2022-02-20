package com.groliks.cookingrecipes.view.remoterecipeslist.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.groliks.cookingrecipes.databinding.ItemRemoteRecipeBinding
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter

class RemoteRecipesAdapter : RecipesAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRemoteRecipeBinding.inflate(inflater, parent, false)
        return RemoteRecipeViewHolder(binding)
    }

    inner class RemoteRecipeViewHolder(
        private val binding: ItemRemoteRecipeBinding,
    ) : RecipeViewHolder(binding.recipeInfo, binding.root)
}