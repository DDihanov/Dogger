package com.dihanov.dogsearch.ui.main.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dihanov.base_ui.ClickableAdapter
import kotlinx.android.synthetic.main.search_item.view.*

class DogViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: String, listener: ClickableAdapter.RecyclerViewItemSelectedListener<String>?) {
        Glide.with(itemView.context).load(item).diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(android.R.color.white)
            .transition(DrawableTransitionOptions.withCrossFade()).into(itemView.image)

        itemView.image.setOnClickListener {
            listener?.onItemSelected(item)
        }
    }
}