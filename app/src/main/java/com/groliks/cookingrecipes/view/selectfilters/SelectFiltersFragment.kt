package com.groliks.cookingrecipes.view.selectfilters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.appComponent
import com.groliks.cookingrecipes.data.util.DataSource
import com.groliks.cookingrecipes.data.util.LoadingStatus
import com.groliks.cookingrecipes.databinding.FragmentSelectFiltersBinding
import com.groliks.cookingrecipes.view.selectfilters.filterslist.FiltersAdapter
import com.groliks.cookingrecipes.view.util.TextLoadingAnimator
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class SelectFiltersFragment : Fragment() {
    private val navArgs: SelectFiltersFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: SelectFilterViewModelFactory.Factory
    private val viewModel: SelectFiltersViewModel by viewModels {
        viewModelFactory.create(navArgs.selectedFilters, navArgs.dataSource)
    }

    private var loadingAnimator: TextLoadingAnimator? = null

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

        setupFiltersList(binding)
        setupCloseButton(binding)
        setupOkButton(binding)

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            onBackButtonPressed()
        }

        return binding.root
    }

    private fun setupOkButton(binding: FragmentSelectFiltersBinding) {
        binding.okButton.setOnClickListener {
            val selectedFilters = viewModel.getSelectedFilters()
            setFragmentResult(SELECT_FILTERS_KEY, bundleOf(SELECTED_FILTERS_KEY to selectedFilters))
            close()
        }
    }

    private fun setupCloseButton(binding: FragmentSelectFiltersBinding) {
        binding.cancelButton.setOnClickListener {
            if (navArgs.dataSource == DataSource.LOCAL || navArgs.selectedFilters.isNotEmpty()) {
                close()
            } else {
                Toast.makeText(
                    requireContext(),
                    R.string.no_selected_filters_error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setupFiltersList(binding: FragmentSelectFiltersBinding) {
        binding.filters.layoutManager = LinearLayoutManager(requireContext())
        val adapter = FiltersAdapter(viewModel::changeFilterSelection)
        binding.filters.adapter = adapter

        loadingAnimator = TextLoadingAnimator(binding.loadingText)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.filters.collect { loadingStatus ->
                when (loadingStatus) {
                    is LoadingStatus.None -> {}
                    is LoadingStatus.Loading -> {
                        binding.filters.isInvisible = true
                        binding.loadingText.isVisible = true
                        loadingAnimator?.start()
                    }
                    is LoadingStatus.Success -> {
                        binding.loadingText.isInvisible = true
                        loadingAnimator?.stop()
                        binding.filters.isVisible = true
                        val filters = loadingStatus.data
                        adapter.submitList(filters)
                        binding.filters.adapter = adapter
                    }
                    is LoadingStatus.Error -> {
                        binding.loadingText.isInvisible = true
                        loadingAnimator?.stop()
                        binding.filters.isVisible = true
                        Toast.makeText(
                            requireContext(),
                            R.string.failed_to_load_filters,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun close() {
        if (navArgs.dataSource == DataSource.REMOTE && viewModel.getSelectedFilters().isEmpty()) {
            Toast.makeText(requireContext(), R.string.no_selected_filters_error, Toast.LENGTH_SHORT)
                .show()
            return
        }
        findNavController().popBackStack(R.id.selectFiltersFragment, true)
    }

    private fun onBackButtonPressed() {
        close()
    }

    override fun onDestroyView() {
        loadingAnimator?.stop()
        loadingAnimator = null
        super.onDestroyView()
    }

    companion object {
        const val SELECT_FILTERS_KEY = "select_filters_key"
        const val SELECTED_FILTERS_KEY = "selected_filters_key"
    }
}