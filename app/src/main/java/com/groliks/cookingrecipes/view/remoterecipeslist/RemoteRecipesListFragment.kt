package com.groliks.cookingrecipes.view.remoterecipeslist

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import com.groliks.cookingrecipes.view.recipeslist.RecipesListFragment
import com.groliks.cookingrecipes.view.recipeslist.RecipesListViewModel
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter

class RemoteRecipesListFragment : RecipesListFragment() {
    override val viewModel: RecipesListViewModel
        get() = TODO("Not yet implemented")
    override val recipesAdapter: RecipesAdapter
        get() = TODO("Not yet implemented")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addRecipe.isGone = true
    }

    override fun onSelectFilters() {
        TODO("Not yet implemented")
    }
}