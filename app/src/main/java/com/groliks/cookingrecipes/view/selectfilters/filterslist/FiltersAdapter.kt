package com.groliks.cookingrecipes.view.selectfilters.filterslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.groliks.cookingrecipes.R
import com.groliks.cookingrecipes.data.filters.model.Filter
import com.groliks.cookingrecipes.databinding.ItemSelectFilterBinding

class FiltersAdapter(
    private val onFilterSelected: (Filter, Boolean) -> Unit,
) : ListAdapter<Filter, FiltersAdapter.SelectFilterViewHolder>(FilterItemDiffUtilCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FiltersAdapter.SelectFilterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSelectFilterBinding.inflate(inflater, parent, false)
        return SelectFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FiltersAdapter.SelectFilterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class SelectFilterViewHolder(private val binding: ItemSelectFilterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var filter: Filter? = null

        init {
            binding.selectFilter.setOnCheckedChangeListener { _, isChecked ->
                filter?.also {
                    onFilterSelected(it, isChecked)
                }
            }
        }

        fun bind(filter: Filter) {
            this.filter = null
            binding.selectFilter.isChecked = filter.isSelected
            val resources = binding.root.resources
            val filterCategoryName = resources.getString(filter.type.nameId)
            binding.filterName.text = resources.getString(
                R.string.filter_selection_name,
                filterCategoryName,
                filter.name
            )
            this.filter = filter
        }
    }
}