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
        var imCard = itemView.findViewById<ImageView>(R.id.imCard1)
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
        hideAISymbolsOnCards(holder)
        holder.tvCardTopLeft.text = card.showNumberOnCard(card.number)
        holder.tvCardBottomRight.text = card.showNumberOnCard(card.number)
        holder.imCardCenter.setImageResource(card.showSuiteOnCard(card.suite))
        holder.imCardBottomRight.setImageResource(card.showSuiteOnCard(card.suite))
        holder.imCardTopLeft.setImageResource(card.showSuiteOnCard(card.suite))

        holder.cardPosition = position
        holder.itemView.setOnClickListener() {
            onCardClick?.invoke(card)
        }


    }

    fun hideAISymbolsOnCards(holder: ViewHolder) {
        holder.imCardCenter.visibility = View.INVISIBLE
        holder.imCardTopLeft.visibility = View.INVISIBLE
        holder.imCardBottomRight.visibility = View.INVISIBLE
        holder.tvCardTopLeft.visibility = View.INVISIBLE
        holder.tvCardBottomRight.visibility = View.INVISIBLE
        holder.imCard.setImageResource(R.drawable.card_backside)
    }
}