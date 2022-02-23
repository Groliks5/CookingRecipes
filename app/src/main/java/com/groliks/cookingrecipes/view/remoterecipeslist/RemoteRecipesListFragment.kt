package com.groliks.cookingrecipes.view.remoterecipeslist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.view.recipeslist.RecipesListFragment
import com.groliks.cookingrecipes.view.recipeslist.RecipesListViewModel
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter
import com.groliks.cookingrecipes.view.remoterecipeslist.recipeslist.RemoteRecipesAdapter
import javax.inject.Inject

class RemoteRecipesListFragment : RecipesListFragment() {
    @Inject
    lateinit var viewModelFactory: RemoteRecipesListViewModel.Factory
    override val viewModel: RecipesListViewModel by viewModels { viewModelFactory }
    override val recipesAdapter: RecipesAdapter by lazy {
        RemoteRecipesAdapter(
            ::onSelectRecipe,
        )
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addRecipe.isGone = true
    }

    private fun onSelectRecipe(recipe: RecipeInfo) {
        val action = RemoteRecipesListFragmentDirections.viewRecipe(recipe.id)
        findNavController().navigate(action)
    }

    override fun onSelectFilters() {
        TODO("Not yet implemented")
    }
}