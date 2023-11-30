package com.example.cardgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.TreeMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameMode2Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameMode2Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var adapterHumanCards: HandOfCardsAdapter
    private lateinit var adapterAICards: HandOfCardsAdapter

    private lateinit var rvAICards: RecyclerView
    private lateinit var rvHumanCards: RecyclerView
    private var deckOfCard = deckOfCard().deckOfCard


    private val human = Player("human", mutableListOf(), TreeMap(), 0, 0)
    private val ai = Player("computer", mutableListOf(), TreeMap(), 0, 0)

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
        val view =  inflater.inflate(R.layout.fragment_game_mode2, container, false)
        rvAICards = view.findViewById(R.id.rvAICardsGAMEMODE2)
        rvHumanCards = view.findViewById(R.id.rvHumanCardsGAMEMODE2)

        rvHumanCards.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapterHumanCards = HandOfCardsAdapter(view.context, human.deck)
        rvHumanCards.adapter = adapterHumanCards

        rvAICards.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapterAICards = HandOfCardsAdapter(view.context, ai.deck)
        rvAICards.adapter = adapterAICards

        
        createHands()
        updateHandView()


        return view
    }

    fun createHands() {
        for (i in 0..4) {
            ai.deck.add(deckOfCard.first())
            deckOfCard.remove(deckOfCard.first())
            human.deck.add(deckOfCard.first())
            deckOfCard.remove(deckOfCard.first())
        }
    }
    fun updateHandView() {

        rvHumanCards.adapter?.notifyDataSetChanged()
        rvAICards.adapter?.notifyDataSetChanged()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameMode2Fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameMode2Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}