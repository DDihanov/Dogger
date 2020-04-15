package com.dihanov.dogger.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dihanov.dogger.R
import com.dihanov.dogger.data.local.db.entity.Dog
import com.dihanov.dogger.ui.base.ClickableAdapter
import org.koin.core.KoinComponent

class SearchAdapter(private var items: List<Dog> = listOf()) :
    ClickableAdapter<RecyclerView.ViewHolder, String>(), KoinComponent {

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

    fun add(list: List<Dog>) {
        val oldPos = items.count()
        val toAdd = this.items.toMutableList()
        toAdd.addAll(list)
        items = toAdd
        notifyItemRangeInserted(oldPos, list.size)
    }
}
