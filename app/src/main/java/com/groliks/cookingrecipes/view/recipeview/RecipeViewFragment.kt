package com.groliks.cookingrecipes.view.recipeview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.databinding.FragmentRecipeViewBinding
import com.groliks.cookingrecipes.view.recipeview.ingredientslist.ViewIngredientsAdapter
import kotlinx.coroutines.flow.collect

abstract class RecipeViewFragment : Fragment() {
    private var _binding: FragmentRecipeViewBinding? = null
    protected val binding get() = _binding!!

    protected abstract val viewModel: RecipeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeViewBinding.inflate(inflater, container, false)

        setupRecipeView()

        return binding.root
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
                        binding.recipeName.text = recipe.info.name
                        binding.recipeCategory.text = recipe.info.category
                        binding.recipeDescription.text = recipe.info.description
                        binding.recipeInstruction.text = recipe.info.instruction
                        binding.recipePhoto.load(recipe.info.photoUri)
                        ingredientsAdapter.submitList(recipe.ingredients)
                        binding.loadingBar.isInvisible = true
                        binding.recipeView.isVisible = true
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

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}