package com.groliks.cookingrecipes.view.localrecipeslist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.view.localrecipeslist.recipeslist.LocalRecipesAdapter
import com.groliks.cookingrecipes.view.recipeslist.RecipesListFragment
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LocalRecipesListFragment : RecipesListFragment() {
    @Inject
    lateinit var viewModelFactory: LocalRecipesListViewModel.Factory
    override val viewModel: LocalRecipesListViewModel by viewModels { viewModelFactory }
    override val adapter: RecipesAdapter by lazy { LocalRecipesAdapter() }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addRecipe.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.createRecipe().collect { recipeId ->
                    if (recipeId != null) {
                        val action = LocalRecipesListFragmentDirections.editRecipe(recipeId)
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }
}