package com.groliks.cookingrecipes.view.remoterecipeslist

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.data.recipes.model.RecipeInfo
import com.groliks.cookingrecipes.data.util.DataSource
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.view.recipeslist.RecipesListFragment
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter
import com.groliks.cookingrecipes.view.remoterecipeslist.recipeslist.RemoteRecipesAdapter
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class RemoteRecipesListFragment : RecipesListFragment() {
    @Inject
    lateinit var viewModelFactory: RemoteRecipesListViewModel.Factory
    override val viewModel: RemoteRecipesListViewModel by viewModels { viewModelFactory }
    override val recipesAdapter: RecipesAdapter by lazy {
        RemoteRecipesAdapter(
            ::onSelectRecipe,
            viewModel::downloadRecipe,
        )
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addRecipe.isGone = true

        setupDownloadingRecipeMessage()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.filters.collect {
                if (it.isEmpty()) {
                    onSelectFilters()
                }
            }
        }
    }

    private fun onSelectRecipe(recipe: RecipeInfo) {
        val action =
            RemoteRecipesListFragmentDirections.actionRemoteRecipesListFragmentToRemoteRecipeViewFragment(
                recipe.id
            )
        findNavController().navigate(action)
    }

    private fun setupDownloadingRecipeMessage() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.downloadingRecipeStatus.collect { downloadingStatus ->
                when (downloadingStatus) {
                    is LoadingStatus.None -> {}
                    is LoadingStatus.Loading -> {
                        val message =
                            requireContext().resources.getString(R.string.downloading_recipe)
                        Toast.makeText(
                            requireContext(),
                            "$message: ${downloadingStatus.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is LoadingStatus.Success -> {
                        val message =
                            requireContext().resources.getString(R.string.recipe_downloaded)
                        Toast.makeText(
                            requireContext(),
                            "$message: ${downloadingStatus.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        viewModel.updateRecipesList()
                    }
                    is LoadingStatus.Error -> {
                        val errorMessage =
                            requireContext().resources.getString(R.string.failed_to_download_recipe)
                        Toast.makeText(
                            requireContext(),
                            "$errorMessage: ${downloadingStatus.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onSelectFilters() {
        val action =
            RemoteRecipesListFragmentDirections.actionRemoteRecipesListFragmentToSelectFilters(
                viewModel.filters.value.toTypedArray(),
                DataSource.REMOTE
            )
        findNavController().navigate(action)
    }
}