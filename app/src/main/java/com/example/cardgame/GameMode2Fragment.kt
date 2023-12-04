package com.example.cardgame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.TreeMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val CRAZY_EIGHT = 2

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
    private lateinit var imPlayerIcon: ImageView
    private lateinit var imPlayerTextImage: ImageView
    private lateinit var tvPlayerText: TextView

    //private lateinit var clCardDrawn: ConstraintLayout
    private lateinit var imDeck: ImageView

    //    private lateinit var imCardPileCenter: ImageView
//    private lateinit var imCardPileTopLeft: ImageView
//    private lateinit var imCardPileBottomRight: ImageView
//    private lateinit var tvCardPileTopLeft: TextView
//    private lateinit var tvCardPileBottomRight: TextView
    private lateinit var clSuites: ConstraintLayout
    private lateinit var imHearts: ImageView
    private lateinit var imDiamonds: ImageView
    private lateinit var imClubs: ImageView
    private lateinit var imSpades: ImageView
    private lateinit var imPassIcon: ImageView
    private lateinit var tvPassText: TextView
    private lateinit var flDrawnCard: FrameLayout
    private lateinit var imDrawnCardAI: ImageView

    private var deckOfCard = deckOfCard().deckOfCard
    private var pileDeck = mutableListOf<Card>()
    private var selectedSuite = "Heart"
    private var aiTurn = false
    private var timerScope = CoroutineScope(Dispatchers.Main)


    private val human = Player("human", mutableListOf(), TreeMap(), 0, 0)
    private val ai = Player("Computer", mutableListOf(), TreeMap(), 0, 0)

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
        val view = inflater.inflate(R.layout.fragment_game_mode2, container, false)
        rvAICards = view.findViewById(R.id.rvAICardsGAMEMODE2)
        rvHumanCards = view.findViewById(R.id.rvHumanCardsGAMEMODE2)
        imPlayerIcon = view.findViewById(R.id.imPLayerIconGAMEMODE2)
        imPlayerTextImage = view.findViewById(R.id.imPlayerTextGAMEMODE2)
        tvPlayerText = view.findViewById(R.id.tvPlayerTextGAMEMODE2)
        //clCardDrawn = view.findViewById(R.id.clCardDrawnGAMEMODE2)
        imDeck = view.findViewById(R.id.imDeckGAMEMODE2)
//        imCardPileCenter = view.findViewById(R.id.imCardPileCenterGAMEMODE2)
//        imCardPileTopLeft = view.findViewById(R.id.imCardPileTopLeftGAMEMODE2)
//        imCardPileBottomRight = view.findViewById(R.id.imCardPileBottomRightGAMEMODE2)
//        tvCardPileTopLeft = view.findViewById(R.id.tvCardPileTopLeftGAMEMODE2)
//        tvCardPileBottomRight = view.findViewById(R.id.tvCardPileBottomRightGAMEMODE2)
        clSuites = view.findViewById(R.id.clSuites)
        imHearts = view.findViewById(R.id.imSuiteHeartGAMEMODE2)
        imDiamonds = view.findViewById(R.id.imSuiteDiamondGAMEMODE2)
        imClubs = view.findViewById(R.id.imSuiteClubsGAMEMODE2)
        imSpades = view.findViewById(R.id.imSuiteSpadesGAMEMODE2)
        tvPassText = view.findViewById(R.id.tvPass)
        imPassIcon = view.findViewById(R.id.imPassIcon)
        flDrawnCard = view.findViewById(R.id.flDrawnCardPlayer)
        imDrawnCardAI = view.findViewById(R.id.imDrawnCardAIGameMode2)

        rvHumanCards.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapterHumanCards = HandOfCardsAdapter(view.context, human)
        rvHumanCards.adapter = adapterHumanCards

        rvAICards.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapterAICards = HandOfCardsAdapter(view.context, ai)
        rvAICards.adapter = adapterAICards



        createHands()
        updateHandView()
        hidePlayerIconAndText()
        hideCardDrawn()
        sortHand(human)
        sortHand(ai)
        printHand()
        hideSuitesList()
        hidePassIcon()
        imDrawnCardAI.visibility = View.INVISIBLE
        var currentCard: Card = pileDeck.last()
        var currentCardPosition = pileDeck.size - 1


        imDeck.setOnClickListener() {
            drawCardFromDeck(human)

        }

        adapterHumanCards?.onCardClick = { card, position -> //Click on card in hand
            if (!aiTurn) {
                hidePassIcon()
                if (card.number == CRAZY_EIGHT) {
                    aiTurn = true
                    currentCard = card
                    currentCardPosition = position
                    Log.d("!!!", currentCard.number.toString())
                    showSuitesList()

                } else if (card.number == pileDeck.last().number || card.suite == selectedSuite) {
                    aiTurn = true
                    addCardToPile(card, human)
                    removeCardFromHand(card, human, position)
                    aiTurnSequence()
                }
            }
        }

        imHearts.setOnClickListener() {

            removeCardFromHand(currentCard, human, currentCardPosition)
            currentCard.suite = "Hearts"
            //updatePileCard(currentCard, "Hearts")
            addCardToPile(currentCard, human)
            aiTurnSequence()
        }
        imDiamonds.setOnClickListener() {

            removeCardFromHand(currentCard, human, currentCardPosition)
            currentCard.suite = "Diamonds"
            //updatePileCard(currentCard, "Diamonds")
            addCardToPile(currentCard, human)
            aiTurnSequence()
        }
        imClubs.setOnClickListener() {
            removeCardFromHand(currentCard, human, currentCardPosition)
            currentCard.suite = "Clubs"
            //updatePileCard(currentCard, "Clubs")
            addCardToPile(currentCard, human)
            aiTurnSequence()
        }
        imSpades.setOnClickListener() {

            removeCardFromHand(currentCard, human, currentCardPosition)
            currentCard.suite = "Spades"
//            updatePileCard(updatedCard, "Spades")
            addCardToPile(currentCard, human)
            aiTurnSequence()
        }

        imPassIcon.setOnClickListener() {
            passTo(ai)
        }


        return view
    }

    fun createHands() {
        pileDeck
        for (i in 0..4) {
            ai.deck.add(deckOfCard.first())
            deckOfCard.remove(deckOfCard.first())
            human.deck.add(deckOfCard.first())
            deckOfCard.remove(deckOfCard.first())
        }
        var firstDraw = deckOfCard.first()
        pileDeck.add(firstDraw)
        deckOfCard.remove(firstDraw)
        updatePileCard(firstDraw, firstDraw.suite)

    }

    fun updatePileCard(card: Card, suite: String) {
        selectedSuite = suite
        (activity as GameScreen).switchToNextCard(
            null,
            CardFragment.newInstance(card.suite, card.number),
            R.id.flCardPileGameMode2
        )
//        imCardPileCenter.setImageResource(card.showSuiteOnCard(selectedSuite))
//        imCardPileTopLeft.setImageResource(card.showSuiteOnCard(selectedSuite))
//        imCardPileBottomRight.setImageResource(card.showSuiteOnCard(selectedSuite))
//        tvCardPileTopLeft.text = card.showNumberOnCard(card.number)
//        tvCardPileBottomRight.text = card.showNumberOnCard(card.number)
        hideSuitesList()
        Log.d("!!!", "Top of pile: " + card.suite + card.number.toString())
        Log.d(
            "!!!",
            "Last of pileDeck: " + pileDeck.last().suite + pileDeck.last().number.toString()
        )

    }

    fun updateHandView() {

        adapterHumanCards?.notifyDataSetChanged()
        rvAICards.adapter?.notifyDataSetChanged()
    }

    fun aiTurnSequence() {
        var crazyEight: Card? = null

        for (card in ai.deck) {
            if (card.suite == selectedSuite || card.number == pileDeck.last().number) {
                if (card.number == CRAZY_EIGHT) {
                    crazyEight = card
                    break
                }
                addCardToPile(card, ai)
                removeCardFromHand(card, ai, 0)
                aiTurn = false
                return

            }
        }
        if (crazyEight != null) {
            removeCardFromHand(crazyEight, ai, 0)
            countSuits(ai)?.let {
                crazyEight.suite = it
            }
            addCardToPile(crazyEight, ai)
            aiTurn = false
            return
        }
        drawCardFromDeck(ai)
        if (aiTurn) {
            aiTurnSequence()
        }
    }

    fun countSuits(player: Player): String? {
        var deckMap = mutableMapOf<String, Int>()
        for (card in player.deck) {
            val countSuite = deckMap.getOrDefault(card.suite, 0)
            deckMap[card.suite] = countSuite

        }
        var highestNumberOfSuite = deckMap.maxByOrNull { it.value }?.key
        Log.d("!!!", "highestnumberofsuite: " + highestNumberOfSuite)
        return highestNumberOfSuite
    }

    fun drawCardFromDeck(player: Player) {
        if (deckOfCard.isNotEmpty()) {
            val drawnCard = deckOfCard.first()
            player.deck.add(drawnCard)
            deckOfCard.remove(drawnCard)

            if (player == human) {
                showAndHideDrawnCard(drawnCard)
            } else {
                showAndHideDrawnCardAI()
            }
            sortHand(player)

        } else if (aiTurn) {
            passTo(human)
        } else {
            showPassIcon()
        }
    }

    fun showAndHideDrawnCard(card: Card) {
        timerScope.launch {
            (activity as GameScreen).showDrawnCard(
                null,
                CardFragment.newInstance(card.suite, card.number),
                R.id.flDrawnCardPlayer
            )
            delay(1000L)
            (activity as GameScreen).hideDrawnCard(null)
        }
    }

    fun showAndHideDrawnCardAI() {
        timerScope.launch {
            imDrawnCardAI.visibility = View.VISIBLE
            delay(1000L)
            imDrawnCardAI.visibility = View.INVISIBLE
        }
    }


    fun gameDone() {
        countScore(ai)
        countScore(human)
    }

    fun countScore(player: Player) {
        var countScore = 0
        for (card in player.deck) {
            countScore += card.number
        }
        player.score = countScore
        Log.d("!!!", "Playerscore: " + player.score.toString())
    }

    fun removeCardFromHand(card: Card, player: Player, position: Int) {
        player.deck.remove(card)
        if (player.deck.isEmpty()) {
            gameDone()
        }
        //adapterHumanCards.notifyItemRemoved(position)
        updateHandView()

    }

    fun addCardToPile(card: Card, player: Player) {
        selectedSuite = card.suite
        pileDeck.add(card)
        updatePileCard(card, card.suite)

    }

    fun sortHand(player: Player) {
        player.deck.sortBy { it.number }
        player.deck.sortBy { it.suite }
        updateHandView()
    }

    fun printHand() {
        for (card in human.deck) {
            Log.d("!!!", "H: " + card.suite + " " + card.number.toString())
        }
        for (card in ai.deck) {
            Log.d("!!!", "AI: " + card.suite + " " + card.number.toString())
        }
        Log.d("!!!", "--------------")
    }

    fun passTo(player: Player) {
        if (player == ai) {
            aiTurn = true
            aiTurnSequence()
        } else {
            aiTurn = false

        }
    }

    fun showPassIcon() {
        tvPassText.visibility = View.VISIBLE
        imPassIcon.visibility = View.VISIBLE
    }

    fun hidePassIcon() {
        tvPassText.visibility = View.INVISIBLE
        imPassIcon.visibility = View.INVISIBLE
    }

    fun showSuitesList() {
        clSuites.visibility = View.VISIBLE
    }

    fun hideSuitesList() {
        clSuites.visibility = View.INVISIBLE
    }

    fun showPlayerIconAndText() {
        imPlayerIcon.visibility = View.VISIBLE
        imPlayerTextImage.visibility = View.VISIBLE
        tvPlayerText.visibility = View.VISIBLE
    }

    fun hidePlayerIconAndText() {
        imPlayerIcon.visibility = View.INVISIBLE
        imPlayerTextImage.visibility = View.INVISIBLE
        tvPlayerText.visibility = View.INVISIBLE
    }

    fun showCardDrawn() {
//        clCardDrawn.visibility = View.VISIBLE
    }

    fun hideCardDrawn() {
//        clCardDrawn.visibility = View.INVISIBLE
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