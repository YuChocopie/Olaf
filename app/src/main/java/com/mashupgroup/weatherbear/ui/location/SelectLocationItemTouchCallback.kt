package com.mashupgroup.weatherbear.ui.location

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class SelectLocationItemTouchCallback(listener: IOnItemEditListener) : ItemTouchHelper.Callback() {
    private var onEditedListener: IOnItemEditListener = listener

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlag = ItemTouchHelper.START or ItemTouchHelper.END
        return makeMovementFlags(dragFlag, swipeFlag)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onEditedListener.onItemSwiped(viewHolder.adapterPosition)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
        onEditedListener.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return true
    }

    interface IOnItemEditListener {
        fun onItemMoved(posFrom: Int, posTo: Int)
        fun onItemSwiped(pos: Int)
    }
}