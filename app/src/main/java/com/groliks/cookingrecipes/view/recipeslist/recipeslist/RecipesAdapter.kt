package com.groliks.cookingrecipes.view.recipeslist.recipeslist

import android.view.View
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.clear
import coil.load
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.databinding.RecipeInfoBinding

abstract class RecipesAdapter :
    ListAdapter<RecipeInfo, RecipesAdapter.RecipeViewHolder>(RecipeDiffUtilCallback()) {

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    open inner class RecipeViewHolder(private val binding: RecipeInfoBinding, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        protected var recipe: RecipeInfo? = null

        open fun bind(recipe: RecipeInfo) {
            binding.recipePhotoLoadingBar.show()
            this.recipe = recipe
            binding.recipeName.text = recipe.name
            binding.recipeDescription.text = recipe.description
            binding.recipePhoto.clear()
            binding.recipePhoto.load(recipe.photoUri) {
                error(R.drawable.ic_error_image_loading)
                listener(
                    onCancel = {
                        binding.recipePhotoLoadingBar.hide()
                    },
                    onError = { _, _ ->
                        binding.recipePhotoLoadingBar.hide()
                    },
                    onSuccess = { _, _ ->
                        binding.recipePhotoLoadingBar.hide()
                    }
                )
            }
        }
    }
}