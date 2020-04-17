package com.dihanov.catsearch.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dihanov.base_data.data.local.entity.Cat
import com.dihanov.catsearch.R
import com.dihanov.dogsearch.ui.main.adapter.DogViewHolder
import org.koin.core.KoinComponent

class SearchAdapter(private var items: List<Cat> = listOf()) :
    com.dihanov.base_ui.ui.ClickableAdapter<RecyclerView.ViewHolder, String>(), KoinComponent {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return DogViewHolder(view)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as DogViewHolder).bind(items[position].url, onRecyclerViewSelectedListener)
    }

    fun clear() {
        items = listOf()
        notifyDataSetChanged()
    }

    fun add(list: List<Cat>) {
        val oldPos = items.count()
        val toAdd = this.items.toMutableList()
        toAdd.addAll(list)
        items = toAdd
        notifyItemRangeInserted(oldPos, list.size)
    }
}
