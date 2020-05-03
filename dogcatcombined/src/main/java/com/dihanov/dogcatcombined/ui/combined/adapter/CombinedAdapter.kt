package com.dihanov.dogcatcombined.ui.combined.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dihanov.base_ui.ClickableAdapter
import com.dihanov.dogcatcombined.R

class CombinedAdapter(private var items: List<String> = listOf()) :
    ClickableAdapter<RecyclerView.ViewHolder, String>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.combined_search_item, parent, false)
        return CombinedViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CombinedViewHolder).bind(items[position], onRecyclerViewSelectedListener)
    }

    fun clear() {
        items = listOf()
        notifyDataSetChanged()
    }

    fun add(list: List<String>) {
        val oldPos = items.count()
        val toAdd = this.items.toMutableList()
        toAdd.addAll(list)
        items = toAdd
        notifyItemRangeInserted(oldPos, list.size)
    }
}