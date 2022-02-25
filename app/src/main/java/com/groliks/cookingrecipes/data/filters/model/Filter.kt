package com.groliks.cookingrecipes.data.filters.model

import android.os.Parcelable
import com.groliks.cookingrecipes.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class Filter(
    val type: Type,
    val name: String,
    var isSelected: Boolean = false
) : Parcelable {
    enum class Type(val nameId: Int) {
        CATEGORY(R.string.category),
        FAVOURITE(R.string.favourites),
        AREA(R.string.area),
    }
}