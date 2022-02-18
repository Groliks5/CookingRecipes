package com.groliks.cookingrecipes.view.util

import android.app.Dialog

fun Dialog.hideBackground() {
    window?.setBackgroundDrawableResource(android.R.color.transparent)
}