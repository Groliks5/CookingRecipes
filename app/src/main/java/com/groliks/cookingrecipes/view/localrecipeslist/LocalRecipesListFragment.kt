package com.groliks.cookingrecipes.view.localrecipeslist

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.data.DataSource
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.view.dialogs.DeleteRecipeDialog
import com.groliks.cookingrecipes.view.localrecipeslist.recipeslist.LocalRecipesAdapter
import com.groliks.cookingrecipes.view.recipeslist.RecipesListFragment
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LocalRecipesListFragment : RecipesListFragment() {
    @Inject
    lateinit var viewModelFactory: LocalRecipesListViewModel.Factory
    override val viewModel: LocalRecipesListViewModel by viewModels { viewModelFactory }
    override val recipesAdapter: RecipesAdapter by lazy {
        LocalRecipesAdapter(
            ::onSelectRecipe,
            ::onEditRecipe,
            ::onDeleteRecipe,
        )
    }

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

        setFragmentResultListener(DeleteRecipeDialog.DIALOG_KEY) { _, result ->
            result.getLong(DeleteRecipeDialog.RESULT_KEY).also {
                viewModel.deleteRecipe(it)
            }
        }
    }

    private fun onSelectRecipe(recipe: RecipeInfo) {
        val action = LocalRecipesListFragmentDirections.viewRecipe(recipe.id)
        findNavController().navigate(action)
    }

    private fun onEditRecipe(recipe: RecipeInfo) {
        val action = LocalRecipesListFragmentDirections.editRecipe(recipe.id)
        findNavController().navigate(action)
    }

    private fun onDeleteRecipe(recipe: RecipeInfo) {
        val action = LocalRecipesListFragmentDirections.deleteRecipe(recipe.id, recipe.name)
        findNavController().navigate(action)
    }

    override fun onSelectFilters() {
        val action = LocalRecipesListFragmentDirections.actionLocalRecipesToSelectFiltersFragment(
            viewModel.filters.value.toTypedArray(),
            DataSource.LOCAL
        )
        findNavController().navigate(action)
    }
}