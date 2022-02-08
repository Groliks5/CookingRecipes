package com.groliks.cookingrecipes.view.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.groliks.cookingrecipes.databinding.FragmentRecipesListBinding
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter
import kotlinx.coroutines.flow.collect

abstract class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    protected val binding get() = _binding!!
    protected abstract val viewModel: RecipesListViewModel
    protected abstract val adapter: RecipesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recipes.layoutManager = LinearLayoutManager(requireContext())
        binding.recipes.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipesList.collect { recipes ->
                adapter.submitList(recipes)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}