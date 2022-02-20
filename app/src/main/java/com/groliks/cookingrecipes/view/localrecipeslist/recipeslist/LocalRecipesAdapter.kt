package com.groliks.cookingrecipes.view.localrecipeslist.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.groliks.cookingrecipes.databinding.ItemLocalRecipeBinding
import com.groliks.cookingrecipes.view.localrecipeslist.LocalRecipesListFragmentDirections
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter

class LocalRecipesAdapter : RecipesAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocalRecipeBinding.inflate(inflater, parent, false)
        return LocalRecipeViewHolder(binding)
    }

    inner class LocalRecipeViewHolder(private val binding: ItemLocalRecipeBinding) :
        RecipeViewHolder(binding.recipeInfo, binding.root) {

        init {
            binding.root.setOnClickListener {
                recipe?.also { recipe ->
                    val action = LocalRecipesListFragmentDirections.viewRecipe(recipe.id)
                    binding.root.findNavController().navigate(action)
                }
            }
            binding.editRecipe.setOnClickListener {
                recipe?.also { recipe ->
                    val action = LocalRecipesListFragmentDirections.editRecipe(recipe.id)
                    binding.root.findNavController().navigate(action)
                }
            }
        }
    }
}