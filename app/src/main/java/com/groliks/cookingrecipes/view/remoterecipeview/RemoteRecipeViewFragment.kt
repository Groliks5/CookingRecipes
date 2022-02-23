package com.groliks.cookingrecipes.view.remoterecipeview

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.view.recipeview.RecipeViewFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class RemoteRecipeViewFragment : RecipeViewFragment() {
    private val navArgs: RemoteRecipeViewFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: RemoteRecipeViewModelFactory.Factory
    override val viewModel: RemoteRecipeViewModel by viewModels { viewModelFactory.create(navArgs.recipeId) }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDownloadingRecipe()
        setHasOptionsMenu(true)
    }

    private fun setupDownloadingRecipe() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.downloadingRecipeStatus.collect { loadingStatus ->
                when (loadingStatus) {
                    is LoadingStatus.None -> {}
                    is LoadingStatus.Loading, is LoadingStatus.Error -> {
                        Toast.makeText(
                            requireActivity(),
                            loadingStatus.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is LoadingStatus.Success -> {
                        val recipeId = loadingStatus.data as Long
                        val action = RemoteRecipeViewFragmentDirections.downloadRecipe(recipeId)
                        findNavController().navigate(action)

                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_remote_recipe_view, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.download_recipe) {
            viewModel.downloadRecipe()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}