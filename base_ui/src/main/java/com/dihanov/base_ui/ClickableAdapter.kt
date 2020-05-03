package com.dihanov.base_ui

import androidx.recyclerview.widget.RecyclerView

abstract class ClickableAdapter<T: RecyclerView.ViewHolder, I>: RecyclerView.Adapter<T>() {
    interface RecyclerViewItemSelectedListener<I> {
        fun onItemSelected(item: I)
    }

    protected var onRecyclerViewSelectedListener: RecyclerViewItemSelectedListener<I>? = null

    fun setOnRecyclerViewSelected(recyclerViewSelectedListener: RecyclerViewItemSelectedListener<I>) {
        if (this.onRecyclerViewSelectedListener != null) {
            removeOnRecyclerViewSelected()
        }

        this.onRecyclerViewSelectedListener = recyclerViewSelectedListener
    }

    fun removeOnRecyclerViewSelected() {
        this.onRecyclerViewSelectedListener = null
    }
}