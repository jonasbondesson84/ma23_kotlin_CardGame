package com.example.cardgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class IconAdapter(val context: Context, val iconList: MutableList<Int>): RecyclerView.Adapter<IconAdapter.ViewHolder>() {

    var layoutInflater = LayoutInflater.from(context)
    var onIconClick: ((Int) -> Unit)? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var imIcon = itemView.findViewById<ImageView>(R.id.imIconSelector)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.iconselector_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return iconList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imIcon.setImageResource(iconList[position])
        val iconSelected = iconList[position]

        holder.itemView.setOnClickListener {
            onIconClick?.invoke(iconSelected)
        }
    }
}