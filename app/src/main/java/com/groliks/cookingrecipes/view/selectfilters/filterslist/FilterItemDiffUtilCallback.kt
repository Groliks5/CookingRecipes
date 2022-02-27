package com.groliks.cookingrecipes.view.selectfilters.filterslist

import androidx.recyclerview.widget.DiffUtil
import com.groliks.cookingrecipes.data.filters.model.Filter

class FilterItemDiffUtilCallback : DiffUtil.ItemCallback<Filter>() {
    override fun areItemsTheSame(oldItem: Filter, newItem: Filter): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Filter, newItem: Filter): Boolean {
        return oldItem == newItem
    }
}