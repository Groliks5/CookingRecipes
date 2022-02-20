package com.groliks.cookingrecipes.view.localrecipeslist.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.databinding.ItemLocalRecipeBinding
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter

class LocalRecipesAdapter(
    private val onSelectRecipe: (RecipeInfo) -> Unit,
    private val onEditRecipe: (RecipeInfo) -> Unit,
    private val onDeleteRecipe: (RecipeInfo) -> Unit,
) : RecipesAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocalRecipeBinding.inflate(inflater, parent, false)
        return LocalRecipeViewHolder(binding)
    }

    inner class LocalRecipeViewHolder(private val binding: ItemLocalRecipeBinding) :
        RecipeViewHolder(binding.recipeInfo, binding.root) {

        init {
            binding.root.setOnClickListener {
                recipe?.also { recipe -> onSelectRecipe(recipe) }
            }
            binding.editRecipe.setOnClickListener {
                recipe?.also { recipe -> onEditRecipe(recipe) }
            }
            binding.deleteRecipe.setOnClickListener {
                recipe?.also { recipe -> onDeleteRecipe(recipe) }
            }
        }
    }
}