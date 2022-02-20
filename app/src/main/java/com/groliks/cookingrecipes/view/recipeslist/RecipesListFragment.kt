package com.groliks.cookingrecipes.view.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.recipes.model.RecipeList
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.databinding.FragmentRecipesListBinding
import com.groliks.cookingrecipes.view.recipeslist.filterslist.FiltersAdapter
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter
import com.groliks.cookingrecipes.view.selectfilters.SelectFiltersFragment
import kotlinx.coroutines.flow.collect

abstract class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    protected val binding get() = _binding!!
    protected abstract val viewModel: RecipesListViewModel
    protected abstract val recipesAdapter: RecipesAdapter

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
        binding.recipes.adapter = recipesAdapter
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipesList.collect { loadingStatus ->
                when (loadingStatus) {
                    is LoadingStatus.Loading -> {}
                    is LoadingStatus.Success -> {
                        val result = loadingStatus.data
                        if (result is RecipeList) {
                            recipesAdapter.submitList(result.recipes)
                        }
                    }
                    is LoadingStatus.Error -> {}
                }
            }
        }

        binding.filters.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val filtersAdapter = FiltersAdapter(
            ::onSelectFilters,
            viewModel::deleteFilter,
            viewLifecycleOwner.lifecycleScope,
        )
        binding.filters.adapter = filtersAdapter
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.filters.collect { filters ->
                filtersAdapter.submitFilters(filters)
            }
        }

        setFragmentResultListener(SelectFiltersFragment.SELECT_FILTERS_KEY) { _, result ->
            result.getParcelableArray(SelectFiltersFragment.SELECTED_FILTERS_KEY)?.also { it ->
                val selectedFilters = it.map { it as Filter }
                viewModel.updateSelectedFilters(selectedFilters)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.updateRecipesList()
        }
    }

    protected abstract fun onSelectFilters()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}