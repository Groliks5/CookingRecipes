package com.groliks.cookingrecipes.view.localrecipeslist.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.databinding.ItemLocalRecipeBinding
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter

class LocalRecipesAdapter(
    private val onSelectRecipe: (RecipeInfo) -> Unit,
    private val onEditRecipe: (RecipeInfo) -> Unit,
    private val onDeleteRecipe: (RecipeInfo) -> Unit,
    private val onFavouriteRecipe: (RecipeInfo) -> Unit,
) : RecipesAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocalRecipeBinding.inflate(inflater, parent, false)
        return LocalRecipeViewHolder(binding)
    }

    inner class LocalRecipeViewHolder(private val binding: ItemLocalRecipeBinding) :
        RecipeViewHolder(binding.recipeInfo, binding.root) {

        init {
            binding.root.setOnClickListener { recipe?.also(onSelectRecipe) }
            binding.editRecipe.setOnClickListener { recipe?.also(onEditRecipe) }
            binding.deleteRecipe.setOnClickListener { recipe?.also(onDeleteRecipe) }
            binding.favouriteRecipe.setOnClickListener {
                recipe?.also { recipe ->
                    recipe.isFavourite = !recipe.isFavourite
                    setFavouriteIcon(recipe.isFavourite)
                    onFavouriteRecipe(recipe)
                }
            }
        }

        override fun bind(recipe: RecipeInfo) {
            super.bind(recipe)
            setFavouriteIcon(recipe.isFavourite)
        }

        private fun setFavouriteIcon(isFavourite: Boolean) {
            val iconRes = if (isFavourite) {
                R.drawable.ic_favourite_recipe
            } else {
                R.drawable.ic_favourite_border_recipe
            }
            binding.favouriteRecipe.setIconResource(iconRes)
        }
    }
}