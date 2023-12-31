package com.example.cardgame

import android.os.Bundle
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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.TreeMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val CRAZY_EIGHT = 8
private const val TIMER_TEXT = 1000L
private const val TIMER_ACTION = 1000L
private const val TEXTSIZE_SHORT = 24F
private const val TEXTSIZE_MEDIUMSHORT = 18F
private const val TEXTSIZE_MEDIUMLONG = 16F
private const val TEXTSIZE_LONG = 12F

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
    private lateinit var tvAIText: TextView
    private lateinit var imAIText: ImageView
    private lateinit var imDeck: ImageView
    private lateinit var clSuites: ConstraintLayout
    private lateinit var imHearts: ImageView
    private lateinit var imDiamonds: ImageView
    private lateinit var imClubs: ImageView
    private lateinit var imSpades: ImageView
    private lateinit var imPassIcon: ImageView
    private lateinit var tvPassText: TextView
    private lateinit var flDrawnCard: FrameLayout
    private lateinit var imDrawnCardAI: ImageView
    private lateinit var flCardPile: FrameLayout
    private lateinit var imSuiteAI: ImageView

    private var deckOfCard = DeckOfCard().deckOfCard
    private var pileDeck = mutableListOf<Card>()
    private var aiTurn = false
    private var timerScope = CoroutineScope(Dispatchers.Main)
    private lateinit var currentCard: Card
    private var currentCardPosition: Int = 1
    private val human = Player("human", mutableListOf(), TreeMap(), 0, 0, false)
    private val ai = Player("computer", mutableListOf(), TreeMap(), 0, 0, true)
    private var currentNumbersOfPasses = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerScope.cancel()
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
        imAIText = view.findViewById(R.id.imAITextGAMEMODE2)
        tvAIText = view.findViewById(R.id.tvAITextGAMEMODE2)
        imSuiteAI = view.findViewById(R.id.imSuiteAIGameMode2)
        imDeck = view.findViewById(R.id.imDeckGAMEMODE2)
        clSuites = view.findViewById(R.id.clSuites)
        imHearts = view.findViewById(R.id.imSuiteHeartGAMEMODE2)
        imDiamonds = view.findViewById(R.id.imSuiteDiamondGAMEMODE2)
        imClubs = view.findViewById(R.id.imSuiteClubsGAMEMODE2)
        imSpades = view.findViewById(R.id.imSuiteSpadesGAMEMODE2)
        tvPassText = view.findViewById(R.id.tvPass)
        imPassIcon = view.findViewById(R.id.imPassIcon)
        flDrawnCard = view.findViewById(R.id.flDrawnCardPlayer)
        imDrawnCardAI = view.findViewById(R.id.imDrawnCardAIGameMode2)
        flCardPile = view.findViewById(R.id.flCardPileGameMode2)

        rvHumanCards.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapterHumanCards = HandOfCardsAdapter(view.context, human)
        rvHumanCards.adapter = adapterHumanCards

        rvAICards.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapterAICards = HandOfCardsAdapter(view.context, ai)
        rvAICards.adapter = adapterAICards

        imPlayerIcon.setImageResource(SaveData.icon)



        createHands()
        updateHandView()
        hidePlayerIconAndText()
        sortHand(human)
        hideSuitesList()
        hidePassIcon()
        hideSuiteAI()
        showAIText()
        val text = resources.getString(R.string.yourTurn)
        textSizeAndShowText(text, ai)
        currentCard = pileDeck.last()
        currentCardPosition = pileDeck.size - 1

        imDrawnCardAI.visibility = View.INVISIBLE

        imDeck.setOnClickListener {
            if (!aiTurn) {
                drawCardFromDeck(human)
            }
        }

        adapterHumanCards.onCardClick = { card, position ->
            if (!aiTurn) {
                hideAIText()
                hidePassIcon()
                timerScope.launch {
                    withContext(Dispatchers.Main) {
                        if (card.number == CRAZY_EIGHT) {
                            crazyEightHuman(card, position)
                        } else if (card.number == pileDeck.last().number || card.suite == pileDeck.last().suite) {
                            aiTurn = true
                            addCardToPile(card)
                            removeCardFromHand(card, human)
                            if (human.deck.isNotEmpty()) {
                                aiTurnSequence()
                            }
                        }
                    }
                }
            }
        }

        imHearts.setOnClickListener {
            chooseSuite("Hearts")
        }

        imDiamonds.setOnClickListener {
            chooseSuite("Diamonds")
        }

        imClubs.setOnClickListener {
            chooseSuite("Clubs")
        }
        imSpades.setOnClickListener {
            chooseSuite("Spades")
        }

        imPassIcon.setOnClickListener {
            if (!aiTurn) {
                currentNumbersOfPasses++
                passTo(ai)
            }
        }

        return view
    }

    private fun chooseSuite(suite: String) {
        removeCardFromHand(currentCard, human)
        currentCard.suite = suite
        addCardToPile(currentCard)
        aiTurnSequence()
    }

    private fun crazyEightHuman(card: Card, position: Int) {
        timerScope.launch {
            withContext(Dispatchers.Main) {
                aiTurn = true
                currentCard = card
                currentCardPosition = position
                showPlayerIconAndText()
                val text = resources.getString(R.string.chooseSuite)
                textSizeAndShowText(text, human)
                delay(TIMER_TEXT)
                hidePlayerIconAndText()
                delay(TIMER_ACTION / 2)
                showSuitesList()
            }
        }
    }

    private fun changeDeckImage() {
        if (deckOfCard.size == 1) {
            imDeck.scaleX = 0.8f
            imDeck.scaleY = 0.8f
            imDeck.setImageResource(R.drawable.card_backside)
        } else if (deckOfCard.isEmpty()) {
            imDeck.visibility = View.INVISIBLE
        }
    }

    private fun createHands() {
        pileDeck
        for (i in 0..4) {
            ai.deck.add(deckOfCard.first())
            deckOfCard.remove(deckOfCard.first())
            human.deck.add(deckOfCard.first())
            deckOfCard.remove(deckOfCard.first())
        }
        val firstDraw = deckOfCard.first()
        pileDeck.add(firstDraw)
        deckOfCard.remove(firstDraw)
        (activity as GameScreen).switchToNextCard(
            null, CardFragment.newInstance(firstDraw.suite, firstDraw.number),
            R.id.flCardPileGameMode2
        )
    }

    private fun updatePileCard(card: Card) {
        (activity as GameScreen).updateCardLayout(null, card)
        hideSuitesList()
    }

    private fun updateHandView() {

        adapterHumanCards.notifyDataSetChanged()
        adapterAICards.notifyDataSetChanged()
    }

    private fun aiTurnSequence() {
        timerScope.launch {
            withContext(Dispatchers.Main) {
                val crazyEight: Card
                showAIText()
                var text = resources.getString(R.string.myTurn)
                textSizeAndShowText(text, ai)
                delay(TIMER_TEXT)
                for (card in ai.deck) {
                    if (card.suite == pileDeck.last().suite || card.number == pileDeck.last().number) {
                        if (card.number == CRAZY_EIGHT) {
                            crazyEight = card
                            crazyEightAI(crazyEight)
                            return@withContext
                        }
                        addCardToPile(card)
                        removeCardFromHand(card, ai)
                        aiTurn = false
                        text = resources.getString(R.string.yourTurn)
                        textSizeAndShowText(text, ai)
                        return@withContext
                    }
                }

                drawCardFromDeck(ai)
                if (aiTurn) {
                    delay(TIMER_ACTION)
                    aiTurnSequence()
                }

            }
        }
    }

    private fun crazyEightAI(crazyEight: Card) {
        timerScope.launch {
            withContext(Dispatchers.Main) {
                var text = resources.getString(R.string.AIchangedSuite)
                textSizeAndShowText(text, ai)
                removeCardFromHand(crazyEight, ai)
                countSuitsForAI(ai)?.let {
                    crazyEight.suite = it
                }
                showSuiteAI(crazyEight)
                delay(TIMER_TEXT)
                addCardToPile(crazyEight)
                text = resources.getString(R.string.yourTurn)
                textSizeAndShowText(text, ai)
                hideSuiteAI()
                aiTurn = false
            }
        }
        return
    }

    private fun countSuitsForAI(player: Player): String? {
        val deckMap = mutableMapOf<String, Int>()
        for (card in player.deck) {
            val countSuite = deckMap.getOrDefault(card.suite, 0)
            deckMap[card.suite] = countSuite
        }
        val highestNumberOfSuite = deckMap.maxByOrNull { it.value }?.key
        return highestNumberOfSuite
    }

    private fun drawCardFromDeck(player: Player) {
        if (deckOfCard.isNotEmpty()) {
            val drawnCard = deckOfCard.first()
            player.deck.add(drawnCard)
            deckOfCard.remove(drawnCard)
            if (player == human) {
                showAndHideDrawnCard(drawnCard)
            } else {
                showAndHideDrawnCardAI()
            }
            changeDeckImage()
            sortHand(player)
        } else if (aiTurn) {
            timerScope.launch {
                withContext(Dispatchers.Main) {

                    val text = resources.getString(R.string.iPass)
                    showAIText()
                    textSizeAndShowText(text, ai)
                    currentNumbersOfPasses++
                    delay(TIMER_TEXT)
                    passTo(human)
                }
            }
        } else {
            showPassIcon()
        }

    }

    private fun showAndHideDrawnCard(card: Card) {
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

    private fun showAndHideDrawnCardAI() {
        timerScope.launch {
            imDrawnCardAI.visibility = View.VISIBLE
            delay(1000L)
            imDrawnCardAI.visibility = View.INVISIBLE
        }
    }

    private fun gameDone() {
        timerScope.launch {
            withContext(Dispatchers.Main) {
                val text = resources.getString(R.string.gameOver)
                if (human.deck.isEmpty()) {
                    hideAIText()
                    showPlayerIconAndText()
                    textSizeAndShowText(text, human)
                } else {
                    hidePlayerIconAndText()
                    showAIText()
                    textSizeAndShowText(text, ai)
                }
                countScore(ai)
                countScore(human)
                delay(TIMER_TEXT)
                GameEngine.gameLevels[GameEngine.currentLevel].score = 100 - human.score + ai.score
                (activity as GameScreen).switchFragment(null, GameDoneFragment())
            }
        }
    }

    private fun countScore(player: Player) {
        var countScore = 0
        for (card in player.deck) {
            countScore += card.number
        }
        player.score = countScore
    }

    private fun removeCardFromHand(card: Card, player: Player) {
        player.deck.remove(card)
        updateHandView()
        if (player.deck.isEmpty()) {
            gameDone()
            return
        }
    }

    private fun addCardToPile(card: Card) {
        pileDeck.add(card)
        updatePileCard(card)
        currentNumbersOfPasses = 0
    }

    private fun sortHand(player: Player) {
        player.deck.sortBy { it.number }
        player.deck.sortBy { it.suite }
        updateHandView()
    }


    private fun passTo(player: Player) {
        if (deckOfCard.isEmpty() && currentNumbersOfPasses >= 2) {
            gameDone()
        } else if (player == ai) {
            aiTurn = true
            aiTurnSequence()
        } else {
            aiTurn = false

        }
    }

    private fun showPassIcon() {
        tvPassText.visibility = View.VISIBLE
        imPassIcon.visibility = View.VISIBLE
    }

    private fun hidePassIcon() {
        tvPassText.visibility = View.INVISIBLE
        imPassIcon.visibility = View.INVISIBLE
    }

    private fun showSuitesList() {
        clSuites.visibility = View.VISIBLE
    }

    private fun hideSuitesList() {
        clSuites.visibility = View.INVISIBLE
    }

    private fun showPlayerIconAndText() {
        imPlayerIcon.visibility = View.VISIBLE
        imPlayerTextImage.visibility = View.VISIBLE
        tvPlayerText.visibility = View.VISIBLE
    }

    private fun hidePlayerIconAndText() {
        imPlayerIcon.visibility = View.INVISIBLE
        imPlayerTextImage.visibility = View.INVISIBLE
        tvPlayerText.visibility = View.INVISIBLE
    }

    private fun hideAIText() {
        imAIText.visibility = View.INVISIBLE
        tvAIText.visibility = View.INVISIBLE
    }

    private fun showAIText() {
        imAIText.visibility = View.VISIBLE
        tvAIText.visibility = View.VISIBLE
    }

    private fun showSuiteAI(card: Card) {
        imSuiteAI.setImageResource(card.showSuiteOnCard(card.suite))
        imSuiteAI.visibility = View.VISIBLE
    }

    private fun hideSuiteAI() {
        imSuiteAI.visibility = View.INVISIBLE
    }

    private fun textSizeAndShowText(text: String, player: Player) {
        if (player == ai) {
            when {
                text.length > 22 -> {
                    tvAIText.textSize = TEXTSIZE_LONG
                }

                text.length in 18..22 -> {
                    tvAIText.textSize = TEXTSIZE_MEDIUMLONG
                }

                text.length in 15..18 -> {
                    tvAIText.textSize = TEXTSIZE_MEDIUMSHORT
                }

                else -> {
                    tvAIText.textSize = TEXTSIZE_SHORT
                }
            }

            tvAIText.text = text
        } else {
            when {
                text.length > 22 -> {
                    tvPlayerText.textSize = TEXTSIZE_LONG
                }

                text.length in 18..22 -> {
                    tvPlayerText.textSize = TEXTSIZE_MEDIUMLONG
                }

                text.length in 12..18 -> {
                    tvPlayerText.textSize = TEXTSIZE_MEDIUMSHORT
                }

                else -> {
                    tvPlayerText.textSize = TEXTSIZE_SHORT
                }
            }

            tvPlayerText.text = text
        }
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