package com.groliks.cookingrecipes.view.recipeview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.databinding.FragmentRecipeDetailsBinding
import com.groliks.cookingrecipes.view.recipeview.ingredientslist.ViewIngredientsAdapter
import kotlinx.coroutines.flow.collect

abstract class RecipeViewFragment : Fragment() {
    private var _binding: FragmentRecipeDetailsBinding? = null
    protected val binding get() = _binding!!

    protected abstract val viewModel: RecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeDetailsBinding.inflate(inflater, container, false)

        setupRecipeView()
        setupViewOnlyLayoutMode()

        return binding.root
    }

    private fun setupViewOnlyLayoutMode() {
        binding.recipeName.disableInput()
        binding.recipeCategory.disableInput()
        binding.recipeDescription.disableInput()
        binding.recipeInstruction.disableInput()
        binding.recipePhoto.isClickable = false
    }

    private fun setupRecipeView() {
        binding.ingredients.layoutManager = LinearLayoutManager(requireContext())
        val ingredientsAdapter = ViewIngredientsAdapter()
        binding.ingredients.adapter = ingredientsAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipe.collect { loadingStatus ->
                when (loadingStatus) {
                    is LoadingStatus.None -> {}
                    is LoadingStatus.Loading -> {
                        binding.loadingBar.isVisible = true
                        binding.recipeView.isInvisible = true
                    }
                    is LoadingStatus.Success -> {
                        val recipe = loadingStatus.data
                        binding.recipeName.setText(recipe.info.name)
                        val categoryText = requireContext().resources.getString(
                            R.string.recipe_category_view,
                            recipe.info.category
                        )
                        binding.recipeCategory.setText(categoryText)
                        binding.recipeDescription.setText(recipe.info.description)
                        binding.recipeInstruction.setText(recipe.info.instruction)
                        binding.recipePhoto.load(recipe.info.photoUri) {
                            error(R.drawable.ic_error_image_loading)
                        }
                        ingredientsAdapter.submitList(recipe.ingredients)
                        binding.loadingBar.isInvisible = true
                        binding.recipeView.isVisible = true
                        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title =
                            recipe.info.name
                    }
                    is LoadingStatus.Error -> {
                        binding.loadingBar.isInvisible = true
                        Toast.makeText(requireContext(), loadingStatus.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateRecipe()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

private fun EditText.disableInput() {
    setBackgroundResource(android.R.color.transparent)
    hint = ""
    inputType += EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
    isFocusableInTouchMode = false
    clearFocus()
}