package com.groliks.cookingrecipes.view.localrecipeslist

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.util.DataSource
import com.groliks.cookingrecipes.data.util.LoadingStatus
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
            ::onFavouriteRecipe,
        )
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupNewRecipeButton()
        setupDeleteRecipe()
    }

    private fun setupDeleteRecipe() {
        setFragmentResultListener(DeleteRecipeDialog.DIALOG_KEY) { _, bundle ->
            bundle.getLong(DeleteRecipeDialog.RESULT_KEY).also {
                viewModel.deleteRecipe(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.deletingRecipeState.collect { deletingStatus ->
                when (deletingStatus) {
                    is LoadingStatus.None -> {}
                    is LoadingStatus.Loading -> {
                        val message = requireContext().resources.getString(R.string.deleting_recipe)
                        Toast.makeText(
                            requireContext(),
                            "$message: ${deletingStatus.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is LoadingStatus.Success -> {
                        val message = requireContext().resources.getString(R.string.recipe_deleted)
                        Toast.makeText(
                            requireContext(),
                            "$message: ${deletingStatus.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.updateRecipesList()
                    }
                    is LoadingStatus.Error -> {
                        val errorMessage =
                            requireContext().resources.getString(R.string.failed_to_delete_recipe)
                        Toast.makeText(
                            requireContext(),
                            "$errorMessage: ${deletingStatus.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setupNewRecipeButton() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.newRecipeId.collect { recipeId ->
                if (recipeId != null) {
                    val action =
                        LocalRecipesListFragmentDirections.actionLocalRecipesListFragmentToEditRecipeFragment(
                            recipeId
                        )
                    findNavController().navigate(action)
                }
            }
        }

        binding.addRecipe.setOnClickListener {
            viewModel.createRecipe()
        }
    }

    private fun onSelectRecipe(recipe: RecipeInfo) {
        val action =
            LocalRecipesListFragmentDirections.actionLocalRecipesListFragmentToLocalRecipeViewFragment(
                recipe.id
            )
        findNavController().navigate(action)
    }

    private fun onEditRecipe(recipe: RecipeInfo) {
        val action =
            LocalRecipesListFragmentDirections.actionLocalRecipesListFragmentToEditRecipeFragment(
                recipe.id
            )
        findNavController().navigate(action)
    }

    private fun onDeleteRecipe(recipe: RecipeInfo) {
        val action =
            LocalRecipesListFragmentDirections.actionLocalRecipesListFragmentToDeleteRecipeDialog(
                recipe.id,
                recipe.name
            )
        findNavController().navigate(action)
    }

    private fun onFavouriteRecipe(recipe: RecipeInfo) {
        viewModel.setFavouriteRecipe(recipe.id, recipe.isFavourite)
    }

    override fun onSelectFilters() {
        val action =
            LocalRecipesListFragmentDirections.actionLocalRecipesListFragmentToSelectFilters(
                viewModel.filters.value.toTypedArray(),
                DataSource.LOCAL
            )
        findNavController().navigate(action)
    }
}