package com.groliks.cookingrecipes.view.localrecipeslist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.groliks.cookingrecipes.MainApp
import com.groliks.cookingrecipes.view.localrecipeslist.recyclerview.LocalRecipesAdapter
import com.groliks.cookingrecipes.view.recipeslist.RecipesListFragment
import com.groliks.cookingrecipes.view.recipeslist.recyclerview.RecipesAdapter
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LocalRecipesListFragment : RecipesListFragment() {
    @Inject
    lateinit var viewModelFactory: LocalRecipesListViewModel.Factory
    override val viewModel: LocalRecipesListViewModel by viewModels { viewModelFactory }
    override val adapter: RecipesAdapter by lazy { LocalRecipesAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (requireActivity().application as MainApp).appComponent.inject(this)
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