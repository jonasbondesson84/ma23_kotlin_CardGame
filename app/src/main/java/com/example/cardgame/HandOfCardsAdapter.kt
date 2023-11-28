package com.example.cardgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HandOfCardsAdapter(val context: Context, val handOfCards: List<Card>): RecyclerView.Adapter<HandOfCardsAdapter.ViewHolder>() {

    var layoutInflater = LayoutInflater.from(context)
    var onCardClick: ((Card) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCardTopLeft = itemView.findViewById<TextView>(R.id.tvCardTopLeft)
        var tvCardBottomRight = itemView.findViewById<TextView>(R.id.tvCardBottomRight)
        var imCardCenter = itemView.findViewById<ImageView>(R.id.imCardCenter)
        var imCardTopLeft = itemView.findViewById<ImageView>(R.id.imCardTopLeft)
        var imCardBottomRight = itemView.findViewById<ImageView>(R.id.imCardBottomRight)
        var cardPosition = 0


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.hand_card_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return handOfCards.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = handOfCards[position]

        holder.tvCardTopLeft.text = card.showNumberOnCard(card)
        holder.tvCardBottomRight.text = card.showNumberOnCard(card)
        holder.imCardCenter.setImageResource(card.showSuiteOnCard(card))
        holder.imCardBottomRight.setImageResource(card.showSuiteOnCard(card))
        holder.imCardTopLeft.setImageResource(card.showSuiteOnCard(card))

        holder.cardPosition = position
        holder.itemView.setOnClickListener() {
            onCardClick?.invoke(card)
        }


    }
}