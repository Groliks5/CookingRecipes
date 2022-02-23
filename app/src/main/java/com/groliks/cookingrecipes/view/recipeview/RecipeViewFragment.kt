package com.groliks.cookingrecipes.view.recipeview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.groliks.cookingrecipes.data.recipes.model.Recipe
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

        binding.ingredients.layoutManager = LinearLayoutManager(requireContext())
        val ingredientsAdapter = ViewIngredientsAdapter()
        binding.ingredients.adapter = ingredientsAdapter

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipe.collect { result ->
                when (result) {
                    is LoadingStatus.Loading -> {}
                    is LoadingStatus.Success -> {
                        val recipe = result.data as Recipe
                        binding.recipeName.text = recipe.info.name
                        binding.recipeCategory.text = recipe.info.category
                        binding.recipeDescription.text = recipe.info.description
                        binding.recipeInstruction.text = recipe.info.instruction
                        binding.recipePhoto.load(recipe.info.photoUri)
                        ingredientsAdapter.submitList(recipe.ingredients)
                    }
                    is LoadingStatus.Error -> {}
                }
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}