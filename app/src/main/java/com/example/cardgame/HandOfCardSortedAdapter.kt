package com.example.cardgame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.TreeMap

class HandOfCardSortedAdapter(val context: Context, private val handOfCardsMap: TreeMap<Int, Int>, private val handOfCards: MutableList<Card>): RecyclerView.Adapter<HandOfCardSortedAdapter.ViewHolder>() {

    private var layoutInflater = LayoutInflater.from(context)
    var onCardClick: ((Int, Int) -> Unit)? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvCardTopLeft: TextView = itemView.findViewById(R.id.tvCardTopLeftHandAdapter)
        var tvCardBottomRight: TextView = itemView.findViewById(R.id.tvCardBottomRightHandAdapter)
        var imCardCenter: ImageView = itemView.findViewById(R.id.imCardCenterHandAdapter)
        var imCardTopLeft: ImageView = itemView.findViewById(R.id.imCardTopLeftHandAdapter)
        var imCardBottomRight: ImageView = itemView.findViewById(R.id.imCardBottomRightHandAdapter)
        var imCard2: ImageView = itemView.findViewById(R.id.imCard2)
        var tvCard2Text: TextView = itemView.findViewById(R.id.tvCardTopLeftCard2)
        var imCard3: ImageView = itemView.findViewById(R.id.imCard3)
        var tvCard3Text:TextView = itemView.findViewById(R.id.tvCardTopLeftCard3)
        var imCard4: ImageView = itemView.findViewById(R.id.imCard4)
        var tvCard4Text: TextView = itemView.findViewById(R.id.tvCardTopLeftCard4)

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
        var cardMapSuite = ""
        for(card in handOfCards) {
            if (card.number == cardMapKey) {
                cardMapSuite = card.suite
                break
            }
        }
        
        holder.itemView.setOnClickListener {
            onCardClick?.invoke(cardMapKey, cardMapValue)
        }
        //Shows number of cards based on how many you have in your cardMap
        when(cardMapValue ) {
            4 -> {
                showCard4(holder,card, cardMapKey)
                showCard3(holder, card, cardMapKey)
                showCard2(holder, card, cardMapKey)
                showCard1(holder, card, cardMapKey, cardMapSuite)
            }
            3 -> {
                hideCard4(holder)
                showCard3(holder, card, cardMapKey)
                showCard2(holder, card, cardMapKey)
                showCard1(holder, card, cardMapKey,cardMapSuite)
            }
            2 -> {
                hideCard4(holder)
                hideCard3(holder)
                showCard2(holder,card,cardMapKey)
                showCard1(holder,card,cardMapKey,cardMapSuite)
            }
            else -> {
                hideCard4(holder)
                hideCard3(holder)
                hideCard2(holder)
                showCard1(holder, card, cardMapKey, cardMapSuite)
            }
        }




    }
    private fun showCard4(holder:ViewHolder, card: Card, cardMapKey: Int) {
        holder.imCard4.visibility = View.VISIBLE
        holder.tvCard4Text.visibility = View.VISIBLE
        holder.tvCard4Text.text = card.showNumberOnCard(cardMapKey)
    }

    private fun hideCard4(holder:ViewHolder) {
        holder.imCard4.visibility = View.INVISIBLE
        holder.tvCard4Text.visibility = View.INVISIBLE
    }

    private fun showCard3(holder: ViewHolder, card: Card, cardMapKey: Int) {
        holder.tvCard3Text.visibility = View.VISIBLE
        holder.imCard3.visibility = View.VISIBLE
        holder.tvCard3Text.text= card.showNumberOnCard(cardMapKey)
    }

    private fun hideCard3(holder: ViewHolder) {
        holder.tvCard3Text.visibility = View.INVISIBLE
        holder.imCard3.visibility = View.INVISIBLE
    }

    private fun showCard2(holder: ViewHolder, card: Card, cardMapKey: Int) {
        holder.tvCard2Text.visibility = View.VISIBLE
        holder.imCard2.visibility = View.VISIBLE
        holder.tvCard2Text.text= card.showNumberOnCard(cardMapKey)
    }

    private fun hideCard2(holder: ViewHolder) {
        holder.tvCard2Text.visibility = View.INVISIBLE
        holder.imCard2.visibility = View.INVISIBLE
    }

    private fun showCard1(holder: ViewHolder, card: Card, cardMapKey: Int, cardMapSuite: String) {
        holder.tvCardTopLeft.text = card.showNumberOnCard(cardMapKey)
        holder.tvCardBottomRight.text = card.showNumberOnCard(cardMapKey)
        holder.imCardCenter.setImageResource(card.showSuiteOnCard(cardMapSuite))
        holder.imCardBottomRight.setImageResource(card.showSuiteOnCard(cardMapSuite))
        holder.imCardTopLeft.setImageResource(card.showSuiteOnCard(cardMapSuite))
    }
}