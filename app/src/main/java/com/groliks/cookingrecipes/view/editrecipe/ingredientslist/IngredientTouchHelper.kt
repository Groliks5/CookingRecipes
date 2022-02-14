package com.groliks.cookingrecipes.view.editrecipe.ingredientslist

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class IngredientTouchHelper(
    private val adapter: IngredientTouchHelperAdapter,
) : ItemTouchHelper.Callback() {
    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder is IngredientsAdapter.IngredientViewHolder) {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.RIGHT
            makeMovementFlags(dragFlags, swipeFlags)
        } else {
            0
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        adapter.onMove(viewHolder.adapterPosition, target.adapterPosition)

        return true
    }

    override fun canDropOver(
        recyclerView: RecyclerView,
        current: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return target.adapterPosition <= adapter.getLastIngredientPosition()
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val newDy = if (viewHolder.adapterPosition == adapter.getLastIngredientPosition() && dY > 0
            || viewHolder.adapterPosition == 0 && dY < 0
        ) {
            0.0f
        } else {
            dY
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, newDy, actionState, isCurrentlyActive)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (direction == ItemTouchHelper.RIGHT) {
            adapter.onSwipe(viewHolder.adapterPosition)
        }
    }
}