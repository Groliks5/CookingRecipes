package com.groliks.cookingrecipes.view.recipeslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.databinding.FragmentRecipesListBinding
import com.groliks.cookingrecipes.view.recipeslist.filterslist.FiltersAdapter
import com.groliks.cookingrecipes.view.recipeslist.recipeslist.RecipesAdapter
import com.groliks.cookingrecipes.view.selectfilters.SelectFiltersFragment
import com.groliks.cookingrecipes.view.util.TextLoadingAnimator
import kotlinx.coroutines.flow.collect

abstract class RecipesListFragment : Fragment() {
    private var _binding: FragmentRecipesListBinding? = null
    protected val binding get() = _binding!!
    protected abstract val viewModel: RecipesListViewModel
    protected abstract val recipesAdapter: RecipesAdapter

    private var loadingAnimator: TextLoadingAnimator? = null

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

        setupRecipesList()
        setupFiltersList()
        setupSelectFilters()
    }

    private fun setupSelectFilters() {
        setFragmentResultListener(SelectFiltersFragment.SELECT_FILTERS_KEY) { _, result ->
            result.getParcelableArray(SelectFiltersFragment.SELECTED_FILTERS_KEY)?.also { it ->
                val selectedFilters = it.map { it as Filter }
                viewModel.updateSelectedFilters(selectedFilters)
            }
        }
    }

    private fun setupFiltersList() {
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
    }

    private fun setupRecipesList() {
        loadingAnimator = TextLoadingAnimator(binding.loadingText)

        binding.recipes.layoutManager = LinearLayoutManager(requireContext())
        binding.recipes.adapter = recipesAdapter
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recipesList.collect { loadingStatus ->
                when (loadingStatus) {
                    is LoadingStatus.None -> {}
                    is LoadingStatus.Loading -> {
                        binding.recipesNotFoundText.isInvisible = true
                    }
                    is LoadingStatus.Success -> {
                        binding.loadingText.isInvisible = true
                        loadingAnimator?.stop()
                        val recipesInfo = loadingStatus.data
                        recipesAdapter.submitList(recipesInfo)
                        binding.recipes.isVisible = true
                        if (recipesInfo.isEmpty()) {
                            binding.recipesNotFoundText.isVisible = true
                        }
                    }
                    is LoadingStatus.Error -> {
                        binding.loadingText.isInvisible = true
                        loadingAnimator?.stop()
                        binding.recipesNotFoundText.isVisible = true
                        Toast.makeText(requireContext(), loadingStatus.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            binding.loadingText.isVisible = true
            binding.recipes.isInvisible = true
            loadingAnimator?.start()
            viewModel.updateRecipesList()
        }
    }

    protected abstract fun onSelectFilters()

    override fun onDestroyView() {
        _binding = null
        loadingAnimator?.stop()
        loadingAnimator = null
        super.onDestroyView()
    }
}