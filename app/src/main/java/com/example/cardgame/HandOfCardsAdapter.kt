package com.example.cardgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class HandOfCardsAdapter(val context: Context, private val player: Player /*val handOfCards: List<Card>*/): RecyclerView.Adapter<HandOfCardsAdapter.ViewHolder>() {

    private var layoutInflater = LayoutInflater.from(context)
    var onCardClick: ((Card, Int) -> Unit)? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCardTopLeft: TextView = itemView.findViewById(R.id.tvCardTopLeftHandAdapter)
        var tvCardBottomRight: TextView = itemView.findViewById(R.id.tvCardBottomRightHandAdapter)
        var imCardCenter: ImageView = itemView.findViewById(R.id.imCardCenterHandAdapter)
        var imCardTopLeft: ImageView = itemView.findViewById(R.id.imCardTopLeftHandAdapter)
        var imCardBottomRight: ImageView = itemView.findViewById(R.id.imCardBottomRightHandAdapter)
        var imCard: ImageView = itemView.findViewById(R.id.imCardHandAdapter)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.hand_card_item, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return player.deck.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = player.deck[position]
        if(player.computer) {
            hideAISymbolsOnCards(holder)
        } else {
            holder.tvCardTopLeft.text = card.showNumberOnCard(card.number)
            holder.tvCardBottomRight.text = card.showNumberOnCard(card.number)
            holder.imCardCenter.setImageResource(card.showSuiteOnCard(card.suite))
            holder.imCardBottomRight.setImageResource(card.showSuiteOnCard(card.suite))
            holder.imCardTopLeft.setImageResource(card.showSuiteOnCard(card.suite))

            holder.itemView.setOnClickListener {
                onCardClick?.invoke(card, position)
            }
        }
    }

    private fun hideAISymbolsOnCards(holder: ViewHolder) {
        holder.imCardCenter.visibility = View.INVISIBLE
        holder.imCardTopLeft.visibility = View.INVISIBLE
        holder.imCardBottomRight.visibility = View.INVISIBLE
        holder.tvCardTopLeft.visibility = View.INVISIBLE
        holder.tvCardBottomRight.visibility = View.INVISIBLE
        holder.imCard.setImageResource(R.drawable.card_backside)
    }
}