package com.groliks.cookingrecipes.view.recipeslist.filterslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.databinding.ItemFilterBinding
import com.groliks.cookingrecipes.databinding.ItemSelectFiltersBinding

private const val ADD_FILTER_BUTTON_SIZE = 1

private const val ADD_FILTER_BUTTON_VIEW_TYPE = 0
private const val FILTER_VIEW_TYPE = 1

private const val FIRST_FILTER_POSITION = ADD_FILTER_BUTTON_SIZE

class FiltersAdapter(
    private val onAddFilter: () -> Unit,
    private val onDeleteFilter: (Filter) -> Unit,
    private val coroutineScope: LifecycleCoroutineScope,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var filters: List<Filter> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ADD_FILTER_BUTTON_VIEW_TYPE -> {
                val binding = ItemSelectFiltersBinding.inflate(inflater, parent, false)
                SelectFiltersViewHolder(binding)
            }
            else -> {
                val binding = ItemFilterBinding.inflate(inflater, parent, false)
                FilterViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FilterViewHolder) {
            val filterPosition = getFilterPosition(position)
            val filter = filters[filterPosition]
            holder.bind(filter)
        }
    }

    override fun getItemCount(): Int = ADD_FILTER_BUTTON_SIZE + filters.size

    override fun getItemViewType(position: Int): Int {
        return if (position < FIRST_FILTER_POSITION) {
            ADD_FILTER_BUTTON_VIEW_TYPE
        } else {
            FILTER_VIEW_TYPE
        }
    }

    private fun getFilterPosition(position: Int): Int {
        return position - ADD_FILTER_BUTTON_SIZE
    }

    fun submitFilters(newFilters: List<Filter>) {
        coroutineScope.launchWhenStarted {
            val diffUtilCallback = FilterDiffUtilCallback(filters, newFilters)
            val diffUtilResult = DiffUtil.calculateDiff(diffUtilCallback)
            filters = newFilters
            diffUtilResult.dispatchUpdatesTo(this@FiltersAdapter)
        }
    }

    inner class FilterViewHolder(private val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var filter: Filter? = null

        init {
            binding.deleteFilter.setOnClickListener {
                filter?.also { filter ->
                    onDeleteFilter(filter)
                }
            }
        }

        fun bind(filter: Filter) {
            this.filter = filter
            binding.filterName.text = filter.name
        }
    }

    inner class SelectFiltersViewHolder(binding: ItemSelectFiltersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.selectFilters.setOnClickListener {
                onAddFilter()
            }
        }
    }
}