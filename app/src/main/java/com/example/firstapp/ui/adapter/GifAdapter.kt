package com.example.firstapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.firstapp.R

class GifAdapter() : RecyclerView.Adapter<GifAdapter.ViewHolder>() {
    private var items = emptyList<String>()

    fun addItems(items: List<String>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView =
            view.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.gif_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val imageView = holder.imageView
        Glide.with(imageView.context).load(item).into(imageView)

    }
}