package com.groliks.cookingrecipes.view.selectfilters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.data.filters.repository.FiltersRepository
import com.groliks.cookingrecipes.data.util.DataSource
import com.groliks.cookingrecipes.data.util.LoadingStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectFiltersViewModel(
    private val filtersRepository: FiltersRepository,
    private val dataSource: DataSource,
    private val selectedFilters: Array<Filter>,
) : ViewModel() {
    private val _filters = MutableStateFlow<LoadingStatus<List<Filter>>>(LoadingStatus.None())
    val filters = _filters.asStateFlow()

    init {
        viewModelScope.launch {
            _filters.emit(LoadingStatus.Loading("Loading filters"))
            val result = try {
                val availableFilters = filtersRepository.getAvailableFilters(dataSource)
                selectedFilters.forEach { selectedFilter ->
                    availableFilters.find {
                        it.type == selectedFilter.type && it.name == selectedFilter.name
                    }?.also { filter ->
                        filter.isSelected = true
                    }
                }
                LoadingStatus.Success(availableFilters, "Filters loaded")
            } catch (e: Exception) {
                LoadingStatus.Error("Failed to load filters")
            }
            _filters.emit(result)
        }
    }

    fun changeFilterSelection(filter: Filter, isSelected: Boolean) {
        (filters.value as? LoadingStatus.Success)?.data?.also { filters ->
            filters.find {
                it.type == filter.type && it.name == filter.name
            }?.apply {
                this.isSelected = isSelected
            }
        }
    }

    fun getSelectedFilters(): Array<Filter> {
        return (filters.value as? LoadingStatus.Success)?.data?.filter {
            it.isSelected
        }?.toTypedArray() ?: selectedFilters
    }
}

class SelectFilterViewModelFactory @AssistedInject constructor(
    private val filtersRepository: FiltersRepository,
    @Assisted("selected_filters") private val selectedFilters: Array<Filter>,
    @Assisted("data_source") private val dataSource: DataSource,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SelectFiltersViewModel(filtersRepository, dataSource, selectedFilters) as T
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("selected_filters") selectedFilters: Array<Filter>,
            @Assisted("data_source") dataSource: DataSource
        ): SelectFilterViewModelFactory
    }
}