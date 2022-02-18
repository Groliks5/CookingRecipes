package com.groliks.cookingrecipes.view.localrecipeslist.recipeslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import coil.clear
import coil.load
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.data.recipes.model.Recipe
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
        RecipeViewHolder(binding.root) {
        private lateinit var recipe: Recipe

        init {
            binding.root.setOnClickListener {
                val action = LocalRecipesListFragmentDirections.viewRecipe(recipe.info.id)
                binding.root.findNavController().navigate(action)
            }
            binding.editRecipe.setOnClickListener {
                val action = LocalRecipesListFragmentDirections.editRecipe(recipe.info.id)
                binding.root.findNavController().navigate(action)
            }
        }

        override fun bind(recipe: Recipe) {
            binding.recipeInfo.recipePhotoLoadingBar.show()
            this.recipe = recipe
            binding.recipeInfo.recipeName.text = recipe.info.name
            binding.recipeInfo.recipeDescription.text = recipe.info.description
            binding.recipeInfo.recipePhoto.clear()
            binding.recipeInfo.recipePhoto.load(recipe.info.photoUri) {
                error(R.drawable.ic_error_image_loading)
                listener(
                    onCancel = {
                        binding.recipeInfo.recipePhotoLoadingBar.hide()
                    },
                    onError = { _, _ ->
                        binding.recipeInfo.recipePhotoLoadingBar.hide()
                    },
                    onSuccess = { _, _ ->
                        binding.recipeInfo.recipePhotoLoadingBar.hide()
                    }
                )
            }
        }
    }
}