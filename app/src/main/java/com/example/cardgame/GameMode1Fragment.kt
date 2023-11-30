package com.example.cardgame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
private const val TIMER_TEXT = 1000L
private const val TIMER_ACTION = 1000L

/**
 * A simple [Fragment] subclass.
 * Use the [GameMode1Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameMode1Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var rvAICards: RecyclerView
    private lateinit var rvHumanCards: RecyclerView
    private lateinit var imCardCenter: ImageView
    private lateinit var imCardTopLeft: ImageView
    private lateinit var imCardBottomRight: ImageView
    private lateinit var tvCardTopLeft: TextView
    private lateinit var tvCardBottomRight: TextView
    private lateinit var clDrawCard: ConstraintLayout
    private lateinit var tvAIText: TextView
    private lateinit var clDeck: ConstraintLayout
    private lateinit var imGoFishButton: ImageView
    private lateinit var tvGoFishButton: TextView
    private lateinit var tvPlayerText: TextView
    private lateinit var imAIText: ImageView
    private lateinit var imPlayerText: ImageView
    private lateinit var imPLayerIcon: ImageView
    private lateinit var rvHumanPairs: RecyclerView
    private lateinit var rvAIPairs: RecyclerView
    private lateinit var adapterHumanPairs: PairsAdapter
    private lateinit var adapterAIPairs: PairsAdapter

    private var deckOfCard = deckOfCard().deckOfCard
    private var firstCard = deckOfCard.first()
    private val timerScope = CoroutineScope(Dispatchers.Default)
    private var waitForDrawCard = false
    private var aiTurnWaitForCard = false
    private var aiTurn = false
    private var timerClickDeck = false
    private var timerClickHandPick = false
    private var timerClickHandAnswer = false

    private var randomCardNumber = 0

    private val human = Player("human", mutableListOf(), TreeMap(), 0, 0)
    private val ai = Player("computer", mutableListOf(), TreeMap(), 0, 0)


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
        val view = inflater.inflate(R.layout.fragment_game_mode1, container, false)

        rvAICards = view.findViewById(R.id.rvAICards)
        rvAICards.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        val adapterAI = HandOfCardsAdapter(view.context, ai)
        rvAICards.adapter = adapterAI

        rvHumanCards = view.findViewById(R.id.rvHumanCards)
        rvHumanCards.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        val adapterHuman = HandOfCardSortedAdapter(view.context, human.deckMap, human.deck)
        rvHumanCards.adapter = adapterHuman

        rvHumanPairs = view.findViewById(R.id.rvHumanPairs)
        rvHumanPairs.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapterHumanPairs = PairsAdapter(view.context, human.numberOfPairs)
        rvHumanPairs.adapter = adapterHumanPairs

        rvAIPairs = view.findViewById(R.id.rvAIPairs)
        rvAIPairs.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapterAIPairs = PairsAdapter(view.context, ai.numberOfPairs)
        rvAIPairs.adapter = adapterAIPairs

        imCardCenter = view.findViewById(R.id.imCardCenter2)
        imCardBottomRight = view.findViewById(R.id.imCardBottomRight2)
        imCardTopLeft = view.findViewById(R.id.imCardTopLeft2)
        tvCardBottomRight = view.findViewById(R.id.tvCardBottomRight2)
        tvCardTopLeft = view.findViewById(R.id.tvCardTopLeft2)
        clDrawCard = view.findViewById(R.id.clCardPile)
        tvAIText = view.findViewById(R.id.tvAIText)
        clDeck = view.findViewById(R.id.clDeck)
        imGoFishButton = view.findViewById(R.id.imGoFishButton)
        tvGoFishButton = view.findViewById(R.id.tvGoFishButton)
        tvPlayerText = view.findViewById(R.id.tvPlayerText)
        imAIText = view.findViewById(R.id.imAIText)
        imPlayerText = view.findViewById(R.id.imPlayerText)
        imPLayerIcon = view.findViewById(R.id.imPLayerIcon)
        val imDeckOfCard = view.findViewById<ImageView>(R.id.imDeck)

        createHands()
        hideDeck()
        hideGoFishButton()
        tvAIText.text = "Your turn!"
        hidePlayerText()

        imDeckOfCard.setOnClickListener() {
            if (waitForDrawCard && !timerClickDeck) {
                timerClickDeck = true
                showCardWhenHumanDraws()
                drawCardFromDeck(human.deck, human.deckMap, human)
            }
        }

        adapterHuman?.onCardClick = { cardValue, numberOfCards ->
            Log.d("!!!", "Waitfordrawcard: $waitForDrawCard , aitTurnWaitForCard: $aiTurnWaitForCard")
            if (!waitForDrawCard && !aiTurnWaitForCard && !aiTurn) { //checks what action it is you should take
                if (!timerClickHandPick) {
                    timerClickHandPick = true  //Makes sure you cant click twice
                    askAIForCard(cardValue)
                }
            } else if (aiTurnWaitForCard) {  //Checks what action you takes
                if (!timerClickHandAnswer) { //makes sure you cant click twice
                    timerClickHandAnswer = true
                    giveAICard(cardValue)
                }
            }
        }

        imGoFishButton.setOnClickListener() {
            if (aiTurn) {
                goFishAI()
            }

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

    fun createHands() {
        for (i in 0..4) {
            ai.deck.add(deckOfCard.first())
            deckOfCard.remove(deckOfCard.first())
            human.deck.add(deckOfCard.first())
            deckOfCard.remove(deckOfCard.first())
        }
        recalculateMap(ai.deckMap, ai.deck)
        recalculateMap(human.deckMap, human.deck)
        updateHandView()
    }

    fun drawCardFromDeck(deck: MutableList<Card>, deckMap: TreeMap<Int, Int>, player: Player) {
        timerClickDeck = true
         if (deckOfCard.isNotEmpty()) {

            var drawnCard = deckOfCard.first()
            timerScope.launch {
                withContext(Dispatchers.Main) {

                    showDeck(drawnCard)
                    delay(TIMER_ACTION)
                    deck.add(drawnCard)
                    Log.d("!!!", "drawn card: " + drawnCard.number.toString())
                    recalculateMap(deckMap, deck)


                    deckOfCard.remove(drawnCard)
                    sortHand(deckMap)

                    checkForPairs(deckMap, deck, player)


                    delay(TIMER_ACTION)
                    hideDeck()
                    if (!aiTurn && deck.isNotEmpty()) {
                        waitForDrawCard = true
                        tvAIText.text = "My turn!"
                        aiTurnWaitForCard = false
                        delay(TIMER_TEXT)

                        aiTurnSequence()
                    } else if (deck.isNotEmpty()) {
                        aiTurn = false

                        tvAIText.text = " Your turn!"
                        delay(TIMER_TEXT)
                        aiTurnWaitForCard = false
                    }
                    timerClickDeck = false


                }
            }

        }
    }

    fun drawForLastCard(deck: MutableList<Card>, deckMap: TreeMap<Int, Int>, player: Player) {
        timerClickDeck = true
        if (ai.deck.isEmpty() && human.deck.isEmpty() && deckOfCard.isEmpty()) {
            gameDone()
        } else if (deckOfCard.isNotEmpty()) {
            var drawnCard = deckOfCard.first()
            timerScope.launch {
                withContext(Dispatchers.Main) {
                    if(player == human) {
                        showCardWhenHumanDraws()
                    } else {
                        hideCardWhenAIDraws()
                    }
                    showDeck(drawnCard)
                    delay(TIMER_ACTION)
                    deck.add(drawnCard)
                    Log.d("!!!", "drawn card for last: " + drawnCard.number.toString())
                    recalculateMap(deckMap, deck)
                    deckOfCard.remove(drawnCard)
                    checkForPairs(deckMap, deck, player)
                    delay(TIMER_ACTION)
                    hideDeck()
                    updateHandView()
                    timerClickDeck = false
                    Log.d("!!!", "AIturn: " + aiTurn)
                    if (aiTurn) {
                        aiTurnSequence()
                    }

                }
            }

        }

    }


    fun sortHand(deckMap: TreeMap<Int, Int>) {
        //deckMap.toSortedMap()

        updateHandView()
    }

    fun checkForPairs(deckMap: TreeMap<Int, Int>, deck: MutableList<Card>, player: Player) {

        for ((key, value) in deckMap) {
            if (value == 4) {
                player.numberOfPairs++
                Log.d("!!!", player.name)
                deckMap.remove(key)
                removeCards(key, deck, deckMap, player)
                timerScope.launch {
                    withContext(Dispatchers.Main) {
                        if (player == ai) {
                            tvAIText.text = "I've got a pair!"

                            delay(TIMER_TEXT)
                            recalculateMap(ai.deckMap, ai.deck)
                         //   timerClickHandAnswer = false
                            adapterAIPairs.updateNumberOfPairs(ai.numberOfPairs)

                        } else {
                            showPlayerText()
                            tvPlayerText.text = "I've got a pair!"

                            delay(TIMER_TEXT)
                            recalculateMap(human.deckMap, human.deck)
                            hidePlayerText()
                           // timerClickHandAnswer = false
                            adapterHumanPairs.updateNumberOfPairs(human.numberOfPairs)
                        }
                    }
                }
                break
            }

            //timerClickHandAnswer = false

        }
    }

    fun removeCards(
        cardValue: Int,
        deck: MutableList<Card>,
        deckMap: TreeMap<Int, Int>,
        player: Player
    ) {
        deck.filter { it.number == cardValue }.forEach { deck.remove(it) }
        if (deck.isEmpty()) {
            drawForLastCard(deck, deckMap, player)
            updateHandView()
        }
        updateHandView()


    }

    fun exchangeCards(
        cardValue: Int,
        deckFrom: MutableList<Card>,
        deckTo: MutableList<Card>,
        deckMapTo: TreeMap<Int, Int>,
        deckMapFrom: TreeMap<Int, Int>,
        player: Player
    ) {
        deckFrom.filter { it.number == cardValue }.forEach { deckTo.add(it) }
        Log.d("!!!", "card : " + cardValue + ", to " + player.name)
        Log.d("!!!", "--------")
        removeCards(cardValue, deckFrom, deckMapFrom, player)
        recalculateMap(deckMapTo, deckTo)
        recalculateMap(deckMapFrom, deckFrom)
        checkForPairs(deckMapTo, deckTo, player)

        if (aiTurn) {
            aiTurnSequence()
        }
    }

    fun recalculateMap(deckMap: TreeMap<Int, Int>, deck: MutableList<Card>) {
        deckMap.clear()
        for (card in deck) {
            if (deckMap.containsKey(card.number)) {
                var count = deckMap[card.number] ?: 0
                count++
                deckMap[card.number] = count

            } else {
                deckMap[card.number] = 1
            }
        }
        printMap()
    }


    fun updateHandView() {
        //sortHand(deck)
        rvHumanCards.adapter?.notifyDataSetChanged()
        rvAICards.adapter?.notifyDataSetChanged()
    }

    fun aiTurnSequence() {
        if (ai.deck.isNotEmpty()) {
            timerScope.launch {
                withContext(Dispatchers.Main) {
                    if (aiTurn) {
                        tvAIText.text = "I may go again."
                        delay(TIMER_TEXT)
                    }
                    var randomCard = ai.deck.random()
                    randomCardNumber = randomCard.number
                    var showCardSymbol = randomCard.showNumberOnCard(randomCardNumber)
                    tvAIText.text = "Give me all your ${showCardSymbol}s"
                    delay(TIMER_TEXT)
                    aiTurnWaitForCard = true
                    waitForDrawCard = false
                    aiTurn = true
                    if (human.deck.filter { it.number == randomCardNumber }.isEmpty()) {
                        showGoFishButton()
                    }
                }
            }
        }
    }

    fun humanTurn(cardValue: Int) {

        cardValue?.let { safeCardValue ->
            showAIText()
            timerScope.launch {
                withContext(Dispatchers.Main) {
                    if (ai.deckMap.containsKey(safeCardValue)) {
                        var showCardSymbol = firstCard.showNumberOnCard(safeCardValue)
                        tvAIText.text = "Here you go."
                        delay(TIMER_TEXT)
                        exchangeCards(
                            safeCardValue,
                            ai.deck,
                            human.deck,
                            human.deckMap, ai.deckMap, human
                        )
                        tvAIText.text = "Your may go again."
                        //delay(TIMER_TEXT)
                        timerClickHandPick = false
                    } else {
                        tvAIText.text = "GO FISH"
                        waitForDrawCard = true
                        clDeck.visibility = View.VISIBLE
                        delay(TIMER_TEXT)
                        timerClickHandPick = false
                    }
                }
            }
            sortHand(human.deckMap)
            sortHand(ai.deckMap)
            updateHandView()
        }
    }

    fun askAIForCard(cardValue: Int) {
        timerScope.launch {
            withContext(Dispatchers.Main) {
                hideAIText()
                showPlayerText()
                tvPlayerText.text =
                    "I want all your ${firstCard.showNumberOnCard(cardValue)}s"
                delay(TIMER_TEXT)
                hidePlayerText()
                humanTurn(cardValue)
            }
        }
    }

    fun giveAICard(cardValue: Int) {
        if (cardValue == randomCardNumber) {

            timerScope.launch {
                withContext(Dispatchers.Main) {
                    hideAIText()
                    showPlayerText()
                    tvPlayerText.text = "Here you go!"
                    exchangeCards(
                        cardValue,
                        human.deck,
                        ai.deck,
                        ai.deckMap,
                        human.deckMap,
                        ai
                    )
                    delay(TIMER_TEXT)
                    hidePlayerText()
                    showAIText()
                    tvAIText.text = "Thank you"
                    delay(TIMER_TEXT)
                    //hideAIText()
                    aiTurnWaitForCard = false
                    timerClickHandAnswer = false


                }
            }

        } else {
            timerClickHandAnswer = false
        }

    }

    fun goFishAI() {
        timerScope.launch {
            withContext(Dispatchers.Main) {
                showPlayerText()
                tvPlayerText.text = "GO FISH!"
                tvAIText.text = "Darn' it!"
                delay(TIMER_TEXT)
                hideCardWhenAIDraws()
                drawCardFromDeck(ai.deck, ai.deckMap, ai)
                hidePlayerText()
            }
        }
        hideGoFishButton()
    }

    fun hidePlayerText() {
        imPlayerText.visibility = View.INVISIBLE
        tvPlayerText.visibility = View.INVISIBLE
    }

    fun showPlayerText() {
        imPlayerText.visibility = View.VISIBLE
        tvPlayerText.visibility = View.VISIBLE
    }

    fun hideAIText() {
        imAIText.visibility = View.INVISIBLE
        tvAIText.visibility = View.INVISIBLE

    }

    fun showAIText() {
        imAIText.visibility = View.VISIBLE
        tvAIText.visibility = View.VISIBLE
    }

    fun hideGoFishButton() {
        imGoFishButton.visibility = View.INVISIBLE
        tvGoFishButton.visibility = View.VISIBLE
        imPLayerIcon.visibility = View.VISIBLE
    }

    fun showGoFishButton() {
        imGoFishButton.visibility = View.VISIBLE
        tvGoFishButton.visibility = View.VISIBLE
        imPLayerIcon.visibility = View.INVISIBLE
    }

    fun showDeck(drawnCard: Card) {
        clDeck.visibility = View.VISIBLE
        clDrawCard.visibility = View.VISIBLE
        tvCardBottomRight.text = drawnCard.showNumberOnCard(drawnCard.number)
        tvCardTopLeft.text = drawnCard.showNumberOnCard(drawnCard.number)
        imCardCenter.setImageResource(drawnCard.showSuiteOnCard(drawnCard.suite))
        imCardTopLeft.setImageResource(drawnCard.showSuiteOnCard(drawnCard.suite))
        imCardBottomRight.setImageResource(drawnCard.showSuiteOnCard(drawnCard.suite))
    }

    fun hideDeck() {
        clDrawCard.visibility = View.INVISIBLE
        clDeck.visibility = View.INVISIBLE
    }

    fun printMap() {
        for (card in human.deckMap) {
            Log.d("!!!", "H: " + card.key.toString() + " " + card.value.toString())
        }
        for (card in ai.deckMap) {
            Log.d("!!!", "AI: " + card.key.toString() + " " + card.value.toString())
        }
        Log.d("!!!", "--------------")
    }

    fun gameDone() {
        Log.d("!!!", "game done")
    }

    fun hideCardWhenAIDraws() {
        imCardCenter.visibility = View.INVISIBLE
        imCardTopLeft.visibility = View.INVISIBLE
        imCardBottomRight.visibility = View.INVISIBLE
    }

    fun showCardWhenHumanDraws() {
        imCardCenter.visibility = View.VISIBLE
        imCardTopLeft.visibility = View.VISIBLE
        imCardBottomRight.visibility = View.VISIBLE
    }

}