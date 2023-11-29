package com.example.cardgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.TreeMap

class HandOfCardSortedAdapter(val context: Context, val handOfCardsMap: TreeMap<Int, Int>, val handOfCards: MutableList<Card>): RecyclerView.Adapter<HandOfCardSortedAdapter.ViewHolder>() {

    var layoutInflater = LayoutInflater.from(context)
    var onCardClick: ((Int, Int) -> Unit)? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvCardTopLeft = itemView.findViewById<TextView>(R.id.tvCardTopLeft)
        var tvCardBottomRight = itemView.findViewById<TextView>(R.id.tvCardBottomRight)
        var imCardCenter = itemView.findViewById<ImageView>(R.id.imCardCenter)
        var imCardTopLeft = itemView.findViewById<ImageView>(R.id.imCardTopLeft)
        var imCardBottomRight = itemView.findViewById<ImageView>(R.id.imCardBottomRight)
        var imCard2 = itemView.findViewById<ImageView>(R.id.imCard2)
        var tvCard2Text = itemView.findViewById<TextView>(R.id.tvCardTopLeftCard2)
        var imCard3 = itemView.findViewById<ImageView>(R.id.imCard3)
        var tvCard3Text = itemView.findViewById<TextView>(R.id.tvCardTopLeftCard3)
        var cardPosition = 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.sorted_cards_item, parent, false)

        return  ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return handOfCardsMap.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = handOfCards[position]
        val cardMap = handOfCardsMap.entries.elementAt(position)
        val cardMapKey =  cardMap.key
        val cardMapValue =  cardMap.value

        holder.cardPosition = position
        holder.itemView.setOnClickListener() {
            onCardClick?.invoke(cardMapKey, cardMapValue)
        }
        when(cardMapValue ) {
            3 -> {
                holder.tvCard3Text.visibility = View.VISIBLE
                holder.imCard3.visibility = View.VISIBLE
                holder.tvCard3Text.text= card.showNumberOnCard(cardMapKey)

                holder.tvCard2Text.visibility = View.VISIBLE
                holder.imCard2.visibility = View.VISIBLE
                holder.tvCard2Text.text= card.showNumberOnCard(cardMapKey)

                holder.tvCardTopLeft.text = card.showNumberOnCard(cardMapKey)
                holder.tvCardBottomRight.text = card.showNumberOnCard(cardMapKey)
                //holder.imCardCenter.setImageResource(card.showSuiteOnCard(card))
                //holder.imCardBottomRight.setImageResource(card.showSuiteOnCard(card))
                //holder.imCardTopLeft.setImageResource(card.showSuiteOnCard(card))
            }
            2 -> {
                holder.tvCard3Text.visibility = View.INVISIBLE
                holder.imCard3.visibility = View.INVISIBLE

                holder.tvCard2Text.visibility = View.VISIBLE
                holder.imCard2.visibility = View.VISIBLE
                holder.tvCard2Text.text= card.showNumberOnCard(cardMapKey)

                holder.tvCardTopLeft.text = card.showNumberOnCard(cardMapKey)
                holder.tvCardBottomRight.text = card.showNumberOnCard(cardMapKey)
            }
            else -> {
                holder.tvCard3Text.visibility = View.INVISIBLE
                holder.imCard3.visibility = View.INVISIBLE

                holder.tvCard2Text.visibility = View.INVISIBLE
                holder.imCard2.visibility = View.INVISIBLE

                holder.tvCardTopLeft.text = card.showNumberOnCard(cardMapKey)
                holder.tvCardBottomRight.text = card.showNumberOnCard(cardMapKey)
            }
        }




    }
}