package com.groliks.cookingrecipes.view.recipeslist.filterslist

import androidx.recyclerview.widget.DiffUtil
import com.groliks.cookingrecipes.data.filters.model.Filter

class FilterDiffUtilCallback(
    private val oldList: List<Filter>,
    private val newList: List<Filter>,
    private val offset: Int,
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = offset + oldList.size

    override fun getNewListSize(): Int = offset + newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val realOldPos = oldItemPosition - offset
        val realNewPos = newItemPosition - offset
        return if (realOldPos < 0 || realNewPos < 0) true
        else {
            oldList[realOldPos] == newList[realNewPos]
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return areItemsTheSame(oldItemPosition, newItemPosition)
    }
}