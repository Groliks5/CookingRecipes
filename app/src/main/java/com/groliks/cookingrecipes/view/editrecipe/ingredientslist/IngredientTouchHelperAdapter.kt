package com.groliks.cookingrecipes.view.editrecipe.ingredientslist

interface IngredientTouchHelperAdapter {
    fun getLastIngredientPosition(): Int
    fun onMove(oldPosition: Int, newPosition: Int)
    fun onSwipe(position: Int)
}