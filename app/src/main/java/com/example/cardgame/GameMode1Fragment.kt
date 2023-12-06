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
    private lateinit var imCardDrawnAI: ImageView

    private var deckOfCard = deckOfCard().deckOfCard
    private var firstCard = deckOfCard.first()
    private val timerScope = CoroutineScope(Dispatchers.Default)
    private var waitForDrawCard = false
    private var aiTurnWaitForCard = false
    private var aiTurn = false
    private var timerClickDeck = false
    private var timerClickHandPick = false
    private var timerClickHandAnswer = false
    private var TEXTSIZE_SHORT = 24F
    private var TEXTSIZE_MEDIUMSHORT = 18F
    private var TEXTSIZE_MEDIUMLONG = 16F
    private var TEXTSIZE_LONG = 12F
    private var playerCanDrawCard = false
    private var waitForPlayerToGiveCard = false
    private var aiAskedCardNumber = 0
    private var waitToDrawCard = false


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

        tvAIText = view.findViewById(R.id.tvAITextGameMode1)
        //clDeck = view.findViewById(R.id.clDeck)
        imGoFishButton = view.findViewById(R.id.imGoFishButton)
        tvGoFishButton = view.findViewById(R.id.tvGoFishButton)
        tvPlayerText = view.findViewById(R.id.tvPlayerTextGameMode1)
        imAIText = view.findViewById(R.id.imAITextGameMode1)
        imPlayerText = view.findViewById(R.id.imPlayerTextGameMode1)
        imPLayerIcon = view.findViewById(R.id.imPLayerIconGameMode1)
        imCardDrawnAI = view.findViewById(R.id.imCardDrawnAI)
        val imDeckOfCard = view.findViewById<ImageView>(R.id.imDeck)


        imCardDrawnAI.visibility = View.INVISIBLE
        hideGoFish()
        imPLayerIcon.visibility = View.INVISIBLE
        createHands()

        val text = "Your turn"
        textSizeAndShowText(text, ai)
        hidePlayerText()

        imDeckOfCard.setOnClickListener() {
            timerScope.launch {
                withContext(Dispatchers.Main) {
                    if (playerCanDrawCard) {
                        playerCanDrawCard = false
                        drawCard(human)
                        if (checkForPairs(human)) {
                            showPlayerText()
                            var text = "I've got a pair!"
                            textSizeAndShowText(text, human)
                            adapterHumanPairs.updateNumberOfPairs(human.numberOfPairs)

                        }
                        delay(TIMER_TEXT)
                        hidePlayerText()
                        aiTurn = true
                        aiTurn()
                    }
                }
            }
//            if (waitForDrawCard && !timerClickDeck) {
//                aiTurn = true
//                timerClickDeck = true
////                showCardWhenHumanDraws()
//                placeDrawnCard(human)
//                drawCardFromDeck(human.deck, human.deckMap, human)
//
//            }
        }

        adapterHuman?.onCardClick = { cardValue, numberOfCards ->

            if (!aiTurn && !waitForPlayerToGiveCard && !playerCanDrawCard) {
                timerScope.launch {
                    withContext(Dispatchers.Main) {

                        showPlayerText()
                        hideAIText()
                        var text = "I want all your ${cardValue}s"
                        textSizeAndShowText(text, human)
                        delay(TIMER_TEXT)
                        hidePlayerText()

                        var aiGotCard = askForCard(ai, cardValue)
                        showAIText()
                        if (aiGotCard) {
                            text = "Here you go!"
                            exchangeCards(ai, human, cardValue)
                        } else {
                            text = "GO FISH!"
                            // drawCard(human)
                            playerCanDrawCard = true
                        }
                        textSizeAndShowText(text, ai)
                        delay(TIMER_TEXT)
                        if(aiGotCard) {
                            text = "You can go again!"
                            textSizeAndShowText(text, ai)
                        }
                        if(checkForPairs(human)) {
                            showPlayerText()
                            text = "I've got a pair!"
                            textSizeAndShowText(text, human)
                            adapterHumanPairs.updateNumberOfPairs(human.numberOfPairs)

                        }
                        delay(TIMER_TEXT)
                        hidePlayerText()

                        updateHandView()
                        if (human.deck.isEmpty()) {
                            drawCard(human)
                        }
                    }
                }

            } else if (aiTurn && waitForPlayerToGiveCard && cardValue == aiAskedCardNumber) {
                waitForPlayerToGiveCard = false
                timerScope.launch {
                    withContext(Dispatchers.Main) {
                        showPlayerText()
                        var text = "Here you go!"
                        textSizeAndShowText(text, human)
                        delay(TIMER_TEXT)
                        hidePlayerText()
                        exchangeCards(human, ai, cardValue)
                        if (checkForPairs(ai)) {
                            showAIText()
                            text = "I've got a pair"
                            textSizeAndShowText(text, ai)
                            adapterAIPairs.updateNumberOfPairs(ai.numberOfPairs)
                            delay(TIMER_TEXT)

                            hideAIText()
                        }
                        aiTurn()
                    }
                }

            }

        }

        imGoFishButton.setOnClickListener() {
            timerScope.launch {
                withContext(Dispatchers.Main) {
                    hideAIText()
                    hideGoFish()
                    showPlayerText()
                    var text = "GO FISH!"
                    textSizeAndShowText(text, human)
                    delay(500L)
                    showAIText()
                    text = "Darn' it!"
                    textSizeAndShowText(text, ai)
                    delay(TIMER_TEXT)
                    hidePlayerText()
                    hideAIText()

                    drawCard(ai)
                    if(checkForPairs(ai)) {
                        showAIText()
                        text = "I've got a pair!"
                        textSizeAndShowText(text, ai)
                        delay(TIMER_TEXT)
                        adapterAIPairs.updateNumberOfPairs(ai.numberOfPairs)
                        hideAIText()
                    }
                    aiTurn = false
                    waitForPlayerToGiveCard = false


                }
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
        recalculateMap(ai)
        recalculateMap(human)
        checkForPairs(ai)
        checkForPairs(human)
//        updateHandView()
    }

    fun recalculateMap(player: Player) {
        player.deckMap.clear()
        for (card in player.deck) {
            if (player.deckMap.containsKey(card.number)) {
                var count = player.deckMap[card.number] ?: 0
                count++
                player.deckMap[card.number] = count

            } else {
                player.deckMap[card.number] = 1
            }
        }
        //checkForPairs(player)
        updateHandView()
        printMap(player)
    }

    fun updateHandView() {
        rvHumanCards.adapter?.notifyDataSetChanged()
        rvAICards.adapter?.notifyDataSetChanged()
    }


    fun aiTurn() {
        if (aiTurn && ai.deck.isNotEmpty()) {
            timerScope.launch {
                withContext(Dispatchers.Main) {
                    aiAskedCardNumber = ai.deck.random().number
                    hideAIText()
                    delay(500L)
                    showAIText()
                    var text = "I want all your ${aiAskedCardNumber}s"
                    textSizeAndShowText(text, ai)
                    delay(TIMER_TEXT)


                    if (askForCard(human, aiAskedCardNumber)) {
                        waitForPlayerToGiveCard = true

                    } else {
                        waitForPlayerToGiveCard = true
                        aiTurn = false
                        showGoFish()

                    }
                }
            }
        }

    }

    fun askForCard(player: Player, cardValue: Int): Boolean {
        //Log.d("!!!", cardValue.toString())
        return (player.deckMap.containsKey(cardValue))
    }

    fun exchangeCards(playerFrom: Player, playerTo: Player, cardValue: Int) {
        playerFrom.deck.filter { it.number == cardValue }.forEach {
            playerTo.deck.add(it)
            removeCard(playerFrom, it)
        }
        recalculateMap(playerFrom)
        recalculateMap(playerTo)
        if (playerFrom.deck.isEmpty()) {
            drawCard(playerFrom)

        }


    }

    fun removeCard(player: Player, card: Card) {
        player.deck.remove(card)
        //recalculateMap(player)
//        updateHandView()
    }

    fun drawCard(player: Player) {
        if (deckOfCard.isNotEmpty()) {
            val drawnCard = deckOfCard.first()
            //Log.d("!!!", "card drawn: " + drawnCard.number.toString())
            deckOfCard.remove(drawnCard)
            player.deck.add(drawnCard)
            recalculateMap(player)

            timerScope.launch {
                withContext(Dispatchers.Main) {
                    if (player == human) {
                        showPlayerDrawnCard(drawnCard)
                        delay(TIMER_ACTION)
                        hidePlayerDrawnCard()
                        playerCanDrawCard = false
                        aiTurn = true
                        aiTurn()
                    } else {
                        showAIDrawnCard()
                        delay(TIMER_ACTION)
                        hideAIDrawnCard()
                    }
                }
            }
        }
        //updateHandView()
    }

    fun checkForPairs(player: Player): Boolean {

        for ((key, value) in player.deckMap) {
            //Log.d("!!!", key.toString() + " " + value.toString())
            if (value == 4) {
                player.numberOfPairs++
                player.deckMap.remove(key)

                player.deck.filter { it.number == key }.forEach {
                    removeCard(player, it)
                }
                if (player.deck.isEmpty() && deckOfCard.isNotEmpty()) {
                    drawCard(player)
                }
                recalculateMap(player)
                Log.d("!!!", "I've got a pair!")
//                updateHandView()
                if (ai.deck.isEmpty() && human.deck.isEmpty()) {
                    endGame()
                }
                return true
            }
        }
        return false

    }

    fun addPairsToList() {

    }

    fun endGame() {
        calculateScore()
        (activity as GameScreen).switchFragment(null, gameDoneFragment(), false)
    }

    fun calculateScore() {
        GameEngine.gameLevels[GameEngine.currentLevel].score =
            (human.numberOfPairs * 100) - (ai.numberOfPairs * 100)

    }

    fun printMap(player: Player) {
        for((key, value) in player.deckMap) {
            Log.d("!!!", player.name + ": " +key.toString() + " " + value.toString() )

        }
        Log.d("!!!","-------------")
    }

    fun textSizeAndShowText(text: String, player: Player) {
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

    fun hidePlayerText() {

        imPLayerIcon.visibility = View.INVISIBLE
        imPlayerText.visibility = View.INVISIBLE
        tvPlayerText.visibility = View.INVISIBLE
    }

    fun showPlayerText() {

        imPLayerIcon.visibility = View.VISIBLE
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

    fun showPlayerDrawnCard(card: Card) {
        (activity as GameScreen).showDrawnCard(null, CardFragment.newInstance(card.suite, card.number), R.id.flDrawnCardGameMode1)
    }

    fun hidePlayerDrawnCard() {
        (activity as GameScreen).hideDrawnCard(null)
    }

    fun showAIDrawnCard() {
        imCardDrawnAI.visibility = View.VISIBLE
    }
    fun hideAIDrawnCard() {
        imCardDrawnAI.visibility = View.INVISIBLE

    }

    fun showGoFish() {
        imGoFishButton.visibility = View.VISIBLE
        tvGoFishButton.visibility = View.VISIBLE
    }
    fun hideGoFish() {
        imGoFishButton.visibility = View.INVISIBLE
        tvGoFishButton.visibility = View.INVISIBLE
    }

//
//    fun drawCardFromDeck(deck: MutableList<Card>, deckMap: TreeMap<Int, Int>, player: Player) {
//        timerClickDeck = true
//        if (deckOfCard.isNotEmpty()) {
//
//            val drawnCard = deckOfCard.first()
//
//            timerScope.launch {
//                withContext(Dispatchers.Main) {
//                    if(player == human) {
//                        showAndHideDrawnCard(drawnCard)
//                    } else {
//                        imCardDrawnAI.visibility = View.VISIBLE
//                        delay(TIMER_ACTION)
//                        imCardDrawnAI.visibility = View.INVISIBLE
//                    }
//                    //showDeck(drawnCard, player)
//                    delay(TIMER_ACTION)
//                    deck.add(drawnCard)
////                    Log.d("!!!", "drawn card: " + drawnCard.number.toString())
//                    recalculateMap(deckMap, deck)
//
//
//                    deckOfCard.remove(drawnCard)
//                    sortHand(deckMap)
//
//                    checkForPairs(deckMap, deck, player)
//
//
//                    delay(TIMER_ACTION)
//                    hideDeck()
//                    if (!aiTurn && deck.isNotEmpty()) {
//                        waitForDrawCard = true
//
//                        val text = "My turn"
//                        textSizeAndShowText(text, ai)
//                        aiTurnWaitForCard = false
//                        delay(TIMER_TEXT)
//
//                        aiTurnSequence()
//                    } else if (deck.isNotEmpty()) {
//                        aiTurn = false
//                        val text = "Your turn"
//                        textSizeAndShowText(text, ai)
//
//                        delay(TIMER_TEXT)
//                        aiTurnWaitForCard = false
//                    }
//                    timerClickDeck = false
//
//
//                }
//            }
//
//        }
//    }
//
//    fun drawForLastCard(deck: MutableList<Card>, deckMap: TreeMap<Int, Int>, player: Player) {
//        timerClickDeck = true
//        if (ai.deck.isEmpty() && human.deck.isEmpty() && deckOfCard.isEmpty()) {
//            gameDone()
//        } else if (deckOfCard.isNotEmpty()) {
//            var drawnCard = deckOfCard.first()
//            timerScope.launch {
//                withContext(Dispatchers.Main) {
//                    if(player == human) {
//                        showAndHideDrawnCard(drawnCard)
//                    } else {
//                        imCardDrawnAI.visibility = View.VISIBLE
//                        delay(TIMER_ACTION)
//                        imCardDrawnAI.visibility = View.INVISIBLE
//                    }
////                    if (player == human) {
////                        showCardWhenHumanDraws()
////
////                    } else {
////                        hideCardWhenAIDraws()
////
////                    }
//                    //showDeck(drawnCard, player)
//                    delay(TIMER_ACTION)
//                    deck.add(drawnCard)
////                    Log.d("!!!", "drawn card for last: " + drawnCard.number.toString())
//                    recalculateMap(deckMap, deck)
//                    deckOfCard.remove(drawnCard)
//                    checkForPairs(deckMap, deck, player)
//                    delay(TIMER_ACTION)
//                    hideDeck()
//                    updateHandView()
//                    timerClickDeck = false
////                    Log.d("!!!", "AIturn: " + aiTurn)
//                    if (aiTurn) {
//                        aiTurnSequence()
//                    }
//
//                }
//            }
//
//        }
//
//    }
//
//
//    fun sortHand(deckMap: TreeMap<Int, Int>) {
//        //deckMap.toSortedMap()
//
//        updateHandView()
//    }
//
//    fun checkForPairs(deckMap: TreeMap<Int, Int>, deck: MutableList<Card>, player: Player) {
//
//        for ((key, value) in deckMap) {
//            if (value == 4) {
//                player.numberOfPairs++
//                Log.d("!!!", player.name)
//                deckMap.remove(key)
//                removeCards(key, deck, deckMap, player)
//                timerScope.launch {
//                    withContext(Dispatchers.Main) {
//                        if (player == ai) {
//                            val text = "I've got a pair"
//                            textSizeAndShowText(text, ai)
//                            delay(TIMER_TEXT)
//                            recalculateMap(ai.deckMap, ai.deck)
//                            //   timerClickHandAnswer = false
//                            adapterAIPairs.updateNumberOfPairs(ai.numberOfPairs)
//
//                        } else {
//                            showPlayerText()
//
//                            val text = "I've got a pair"
//                            textSizeAndShowText(text, human)
//                            delay(TIMER_TEXT)
//                            recalculateMap(human.deckMap, human.deck)
//                            hidePlayerText()
//                            // timerClickHandAnswer = false
//                            adapterHumanPairs.updateNumberOfPairs(human.numberOfPairs)
//                        }
//                    }
//                }
//                break
//            }
//
//            //timerClickHandAnswer = false
//
//        }
//    }
//
//    fun removeCards(
//        cardValue: Int,
//        deck: MutableList<Card>,
//        deckMap: TreeMap<Int, Int>,
//        player: Player
//    ) {
//        deck.filter { it.number == cardValue }.forEach { deck.remove(it) }
//        if (deck.isEmpty()) {
//            drawForLastCard(deck, deckMap, player)
//            updateHandView()
//        }
//        updateHandView()
//
//
//    }
//
//    fun exchangeCards(
//        cardValue: Int,
//        deckFrom: MutableList<Card>,
//        deckTo: MutableList<Card>,
//        deckMapTo: TreeMap<Int, Int>,
//        deckMapFrom: TreeMap<Int, Int>,
//        player: Player
//    ) {
//        deckFrom.filter { it.number == cardValue }.forEach { deckTo.add(it) }
////        Log.d("!!!", "card : " + cardValue + ", to " + player.name)
////        Log.d("!!!", "--------")
//        removeCards(cardValue, deckFrom, deckMapFrom, player)
//        recalculateMap(deckMapTo, deckTo)
//        recalculateMap(deckMapFrom, deckFrom)
//        checkForPairs(deckMapTo, deckTo, player)
//
//        if (aiTurn) {
//            aiTurnSequence()
//        }
//    }
//
//    fun recalculateMap(deckMap: TreeMap<Int, Int>, deck: MutableList<Card>) {
//        deckMap.clear()
//        for (card in deck) {
//            if (deckMap.containsKey(card.number)) {
//                var count = deckMap[card.number] ?: 0
//                count++
//                deckMap[card.number] = count
//
//            } else {
//                deckMap[card.number] = 1
//            }
//        }
//        printMap()
//    }
//
//
//    fun updateHandView() {
//        //sortHand(deck)
//        rvHumanCards.adapter?.notifyDataSetChanged()
//        rvAICards.adapter?.notifyDataSetChanged()
//    }
//
//    fun aiTurnSequence() {
//        if (ai.deck.isNotEmpty()) {
//            timerScope.launch {
//                withContext(Dispatchers.Main) {
//                    if (aiTurn) {
//
//                        val text = "I will go again"
//                        textSizeAndShowText(text, ai)
//                        delay(TIMER_TEXT)
//                    }
//                    var randomCard = ai.deck.random()
//                    randomCardNumber = randomCard.number
//                    var showCardSymbol = randomCard.showNumberOnCard(randomCardNumber)
////                    tvAIText.text = "Give me all your ${showCardSymbol}s"
//                    val text = "Give me all your ${showCardSymbol}s"
//                    textSizeAndShowText(text, ai)
//                    delay(TIMER_TEXT)
//                    aiTurnWaitForCard = true
//                    waitForDrawCard = false
//                    aiTurn = true
//                    if (human.deck.filter { it.number == randomCardNumber }.isEmpty()) {
//                        showGoFishButton()
//                    }
//                }
//            }
//        }
//    }
//
//    fun humanTurn(cardValue: Int) {
//
//        cardValue?.let { safeCardValue ->
//            showAIText()
//            timerScope.launch {
//                withContext(Dispatchers.Main) {
//                    if (ai.deckMap.containsKey(safeCardValue)) {
//                        var showCardSymbol = firstCard.showNumberOnCard(safeCardValue)
//                        var text = "Here you go."
//                        textSizeAndShowText(text, ai)
////                        tvAIText.text = "Here you go."
//                        delay(TIMER_TEXT)
//                        exchangeCards(
//                            safeCardValue,
//                            ai.deck,
//                            human.deck,
//                            human.deckMap, ai.deckMap, human
//                        )
//                        text = "You may go again"
//                        textSizeAndShowText(text, ai)
////                        tvAIText.text = "Your may go again."
//                        //delay(TIMER_TEXT)
//                        timerClickHandPick = false
//                    } else {
//                        val text = "GO FISH!"
//                        textSizeAndShowText(text, ai)
////                        tvAIText.text = "GO FISH"
//                        waitForDrawCard = true
//                        imCardDrawnAI.visibility = View.INVISIBLE
//                        clDeck.visibility = View.VISIBLE
//                        delay(TIMER_TEXT)
//                        timerClickHandPick = false
//                    }
//                }
//            }
//            sortHand(human.deckMap)
//            sortHand(ai.deckMap)
//            updateHandView()
//        }
//    }
//
//    fun askAIForCard(cardValue: Int) {
//        timerScope.launch {
//            withContext(Dispatchers.Main) {
//                hideAIText()
//                showPlayerText()
//                val text = "I want all your ${firstCard.showNumberOnCard(cardValue)}s"
//                textSizeAndShowText(text, human)
//                delay(TIMER_TEXT)
////                tvPlayerText.text =
////                    "I want all your ${firstCard.showNumberOnCard(cardValue)}s"
//                hidePlayerText()
//                humanTurn(cardValue)
//            }
//        }
//    }
//
//    fun giveAICard(cardValue: Int) {
//        if (cardValue == randomCardNumber) {
//
//            timerScope.launch {
//                withContext(Dispatchers.Main) {
//                    hideAIText()
//                    showPlayerText()
//                    var text = "Here you go"
//                    textSizeAndShowText(text, human)
////                    tvPlayerText.text = "Here you go!"
//                    exchangeCards(
//                        cardValue,
//                        human.deck,
//                        ai.deck,
//                        ai.deckMap,
//                        human.deckMap,
//                        ai
//                    )
//                    delay(TIMER_TEXT)
//                    hidePlayerText()
//                    showAIText()
////                    tvAIText.text = "Thank you"
//                    text = "Thank you"
//                    textSizeAndShowText(text, ai)
//                    delay(TIMER_TEXT)
//                    //hideAIText()
//                    aiTurnWaitForCard = false
//                    timerClickHandAnswer = false
//
//
//                }
//            }
//
//        } else {
//            timerClickHandAnswer = false
//        }
//
//    }
//
//    fun goFishAI() {
//        timerScope.launch {
//            withContext(Dispatchers.Main) {
//                showPlayerText()
////                tvPlayerText.text = "GO FISH!"
//                var text = "GO FISH!"
//                textSizeAndShowText(text, human)
//                text = "Darn' it"
//                textSizeAndShowText(text, ai)
////                tvAIText.text = "Darn' it!"
//                delay(TIMER_TEXT)
////                hideCardWhenAIDraws()
//                drawCardFromDeck(ai.deck, ai.deckMap, ai)
//                hidePlayerText()
//            }
//        }
//        hideGoFishButton()
//    }
//

//
//    fun showPlayerText() {
//        imPlayerText.visibility = View.VISIBLE
//        tvPlayerText.visibility = View.VISIBLE
//    }
//
//    fun hideAIText() {
//        imAIText.visibility = View.INVISIBLE
//        tvAIText.visibility = View.INVISIBLE
//
//    }
//
//    fun showAIText() {
//        imAIText.visibility = View.VISIBLE
//        tvAIText.visibility = View.VISIBLE
//    }
//
//    fun hideGoFishButton() {
//        imGoFishButton.visibility = View.INVISIBLE
//        tvGoFishButton.visibility = View.VISIBLE
//        imPLayerIcon.visibility = View.VISIBLE
//    }
//
//    fun showGoFishButton() {
//        imGoFishButton.visibility = View.VISIBLE
//        tvGoFishButton.visibility = View.VISIBLE
//        imPLayerIcon.visibility = View.INVISIBLE
//    }
//
////    fun showDeck(drawnCard: Card, player: Player) {
////
////        clDeck.visibility = View.VISIBLE
////        clDrawCard.visibility = View.VISIBLE
////        if (player == ai) {
////            placeDrawnCard(ai)
////        } else {
////            placeDrawnCard(human)
////            tvCardBottomRight.text = drawnCard.showNumberOnCard(drawnCard.number)
////            tvCardTopLeft.text = drawnCard.showNumberOnCard(drawnCard.number)
////            imCardCenter.setImageResource(drawnCard.showSuiteOnCard(drawnCard.suite))
////            imCardTopLeft.setImageResource(drawnCard.showSuiteOnCard(drawnCard.suite))
////            imCardBottomRight.setImageResource(drawnCard.showSuiteOnCard(drawnCard.suite))
////        }
////    }
//
//    fun hideDeck() {
////        clDrawCard.visibility = View.INVISIBLE
//        clDeck.visibility = View.INVISIBLE
//    }
//
//    fun printMap() {
//        for (card in human.deckMap) {
//            Log.d("!!!", "H: " + card.key.toString() + " " + card.value.toString())
//        }
//        for (card in ai.deckMap) {
//            Log.d("!!!", "AI: " + card.key.toString() + " " + card.value.toString())
//        }
//        Log.d("!!!", "--------------")
//    }
//
//    fun gameDone() {
//        calculateScore()
//        (activity as GameScreen).switchFragment(null, gameDoneFragment(), false)
//
//        Log.d("!!!", "game done")
//    }
//
////    fun hideCardWhenAIDraws() {
////        imCardCenter.visibility = View.INVISIBLE
////        imCardTopLeft.visibility = View.INVISIBLE
////        imCardBottomRight.visibility = View.INVISIBLE
////    }
////
////    fun showCardWhenHumanDraws() {
////        imCardCenter.visibility = View.VISIBLE
////        imCardTopLeft.visibility = View.VISIBLE
////        imCardBottomRight.visibility = View.VISIBLE
////    }
//

//fun calculateScore() {
//    GameEngine.gameLevels[GameEngine.currentLevel].score = human.numberOfPairs - ai.numberOfPairs
//}
//    fun placeDrawnCard(player: Player) {
//        if(player == ai) {
//            imCardDrawnAI.visibility = View.VISIBLE
//  //          clDrawCard.visibility = View.INVISIBLE
//        } else {
//            imCardDrawnAI.visibility = View.INVISIBLE
////            clDrawCard.visibility = View.VISIBLE
//        }
//    }
//
//    fun showAndHideDrawnCard(card: Card) {
//        timerScope.launch {
//            (activity as GameScreen).showDrawnCard(null, CardFragment.newInstance(card.suite, card.number), R.id.flDrawnCardGameMode1)
//            delay(1000L)
//            (activity as GameScreen).hideDrawnCard(null)
//        }
//    }

}