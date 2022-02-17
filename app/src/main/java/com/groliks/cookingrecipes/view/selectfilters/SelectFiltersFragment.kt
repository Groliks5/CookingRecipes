package com.groliks.cookingrecipes.view.selectfilters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.databinding.FragmentSelectFiltersBinding
import com.groliks.cookingrecipes.view.selectfilters.filterslist.FiltersAdapter
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SelectFiltersFragment : Fragment() {
    private val navArgs: SelectFiltersFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: SelectFilterViewModelFactory.Factory
    private val viewModel: SelectFiltersViewModel by viewModels {
        viewModelFactory.create(navArgs.selectedFilters, navArgs.dataSource)
    }

    override fun onAttach(context: Context) {
        context.appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSelectFiltersBinding.inflate(inflater, container, false)

        binding.filters.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.filters.collect {
                it?.also { filters ->
                    val adapter = FiltersAdapter(
                        viewModel::changeFilterSelection,
                        filters
                    )
                    binding.filters.adapter = adapter
                }
            }
        }

        binding.cancelButton.setOnClickListener {
            close()
        }

        binding.confirmFiltersButton.setOnClickListener {
            val selectedFilters = viewModel.getSelectedFilters()
            setFragmentResult(SELECT_FILTERS_KEY, bundleOf(SELECTED_FILTERS_KEY to selectedFilters))
            close()
        }

        return binding.root
    }

    private fun close() {
        findNavController().popBackStack(R.id.selectFiltersFragment, true)
    }

    companion object {
        const val SELECT_FILTERS_KEY = "select_filters_key"
        const val SELECTED_FILTERS_KEY = "selected_filters_key"
    }
}