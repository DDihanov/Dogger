package com.dihanov.dogger.ui.main.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dihanov.dogger.ui.base.ClickableAdapter
import kotlinx.android.synthetic.main.search_item.view.*

class DogViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: String, listener: ClickableAdapter.RecyclerViewItemSelectedListener<String>?) {
        Glide.with(itemView.context).load(item).diskCacheStrategy(DiskCacheStrategy.ALL)
            .transition(DrawableTransitionOptions.withCrossFade()).into(itemView.image)

        itemView.image.setOnClickListener {
            listener?.onItemSelected(item)
        }
    }
}