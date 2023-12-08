package com.example.cardgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class PairsAdapter(val context: Context, private var numberOfPairs: Int): RecyclerView.Adapter<PairsAdapter.ViewHolder>() {

    private var layoutInflater = LayoutInflater.from(context)


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.pair_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return numberOfPairs
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    fun updateNumberOfPairs(newNumberOfPairs: Int) {
        numberOfPairs = newNumberOfPairs
        notifyItemInserted(numberOfPairs-1)
    }
}