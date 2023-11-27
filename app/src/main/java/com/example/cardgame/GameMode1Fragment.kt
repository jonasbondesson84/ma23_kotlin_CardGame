package com.example.cardgame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameMode1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameMode1Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var rvAICards: RecyclerView
    lateinit var rvHumanCards: RecyclerView
    var AIdeckOfCard = mutableListOf<Card>()
    var humandeckOfCard = mutableListOf<Card>()
    var deckOfCard = deckOfCard().deckOfCard
    var numbersOfPairs = 0
    var AIDeckNumbersOnly = mutableMapOf<Int, MutableList<Card>>()
    var humanDeckNumbersOnly = mutableMapOf<Int, Int>()
   // val adapterHuman = view?.let { HandOfCardsAdapter(it.context, humandeckOfCard) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_game_mode1, container, false)

        rvAICards = view.findViewById(R.id.rvAICards)
        rvAICards.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        val adapterAI = HandOfCardsAdapter(view.context, AIdeckOfCard)
        rvAICards.adapter = adapterAI

        rvHumanCards = view.findViewById(R.id.rvHumanCards)
        rvHumanCards.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        val adapterHuman = HandOfCardsAdapter(view.context, humandeckOfCard)
        rvHumanCards.adapter = adapterHuman

        val imDeckOfCard = view.findViewById<ImageView>(R.id.imDeck)
        imDeckOfCard.setOnClickListener() {
            addCardToHand()
        }



        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameMode1Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameMode1Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun addCardToHand() {
        if(deckOfCard.size > 0) {
            humandeckOfCard.add(deckOfCard.first())
            recalculateMap(humandeckOfCard.last())
            humandeckOfCard.sortBy { it.number }
            deckOfCard.removeAt(0)
            sortHand()
            printMap()
            checkForPairs(humandeckOfCard)
           // humanTurn()
        }
    }

    fun sortHand() {
        humandeckOfCard.sortedBy { it.number }
        rvHumanCards.adapter?.notifyDataSetChanged()
    }

    fun checkForPairs(deck: MutableList<Card>) {

        for ((key, value) in humanDeckNumbersOnly) {
            if (value == 4) {
                numbersOfPairs++
                humanDeckNumbersOnly.remove(key)
                removeCards(key)
                break
            }
        }
    }

    fun removeCards(cardValue: Int) {
        humandeckOfCard.filter {it.number == cardValue}.forEach{humandeckOfCard.remove(it)}

    }

    fun recalculateMap(card: Card) {

            if(humanDeckNumbersOnly.containsKey(card.number)) {
                var count = humanDeckNumbersOnly[card.number] ?: 0
                count++
                humanDeckNumbersOnly.put(card.number, count)

            } else {
                humanDeckNumbersOnly.put(card.number, 1)
            }
    }

    fun printMap() {
        for (card in humanDeckNumbersOnly) {
            Log.d("!!!", card.key.toString() + " " + card.value.toString())
        }
    }


    fun aiTurn() {

    }

    fun humanTurn() {

       // var card = adapterHuman.onCardClick
       // Log.d("!!!", card.toString())
        //Select card
        //AI checks for card
        //If has card - gives all of value
        //If not has card - take from deck
    }
}