package com.groliks.cookingrecipes.view.localrecipeview

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.data.recipes.model.Recipe
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.view.dialogs.DeleteRecipeDialog
import com.groliks.cookingrecipes.view.recipeview.RecipeViewFragment
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class LocalRecipeViewFragment : RecipeViewFragment() {
    private val navArgs: LocalRecipeViewFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: LocalRecipeViewModelFactory.Factory
    override val viewModel: LocalRecipeViewModel by viewModels { viewModelFactory.create(navArgs.recipeId) }

    private var isRecipeFavourite = false
    private var recipe: Recipe? = null

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        setupSetFavouriteFunction()
        setupDeletingDialog()
    }

    private fun setupDeletingDialog() {
        setFragmentResultListener(DeleteRecipeDialog.DIALOG_KEY) { _, bundle ->
            bundle.getLong(DeleteRecipeDialog.RESULT_KEY).also {
                viewModel.deleteRecipe()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.deletingRecipeState.collect { deletingStatus ->
                when (deletingStatus) {
                    is LoadingStatus.None -> {}
                    is LoadingStatus.Loading -> {
                        val message = requireContext().resources.getString(R.string.deleting_recipe)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is LoadingStatus.Success -> {
                        val message = requireContext().resources.getString(R.string.recipe_deleted)
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT)
                            .show()
                        findNavController().popBackStack(R.id.localRecipeViewFragment, true)
                    }
                    is LoadingStatus.Error -> {
                        val errorMessage =
                            requireContext().resources.getString(R.string.failed_to_delete_recipe)
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun setupSetFavouriteFunction() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipe.collect { loadingStatus ->
                if (loadingStatus is LoadingStatus.Success) {
                    viewModel.setFavouriteState(loadingStatus.data.info.isFavourite)
                    recipe = loadingStatus.data
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.favouriteState.collect { isFavourite ->
                isRecipeFavourite = isFavourite
                requireActivity().invalidateOptionsMenu()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_local_recipe_view, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val resources = requireContext().resources
        val setFavouriteButtonText = if (isRecipeFavourite) {
            resources.getString(R.string.delete_from_favourite)
        } else {
            resources.getString(R.string.add_to_favourite)
        }
        menu.findItem(R.id.set_favourite)?.title = setFavouriteButtonText
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.set_favourite -> {
                viewModel.changeFavouriteState(isRecipeFavourite)
                true
            }
            R.id.editRecipeFragment -> {
                recipe?.also {
                    val action =
                        LocalRecipeViewFragmentDirections.actionLocalRecipeViewFragmentToEditRecipeFragment(
                            it.info.id
                        )
                    findNavController().navigate(action)
                }
                true
            }
            R.id.deleteRecipeDialog -> {
                recipe?.also {
                    val action =
                        LocalRecipeViewFragmentDirections.actionLocalRecipeViewFragmentToDeleteRecipeDialog(
                            it.info.id,
                            it.info.name
                        )
                    findNavController().navigate(action)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}