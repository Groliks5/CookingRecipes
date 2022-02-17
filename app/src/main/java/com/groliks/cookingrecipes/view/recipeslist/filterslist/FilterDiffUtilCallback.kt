package com.groliks.cookingrecipes.view.recipeslist.filterslist

import androidx.recyclerview.widget.DiffUtil
import com.groliks.cookingrecipes.data.filters.model.Filter

class FilterDiffUtilCallback(
    private val oldList: List<Filter>,
    private val newList: List<Filter>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}