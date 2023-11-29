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
    lateinit var imCardCenter: ImageView
    lateinit var imCardTopLeft: ImageView
    lateinit var imCardBottomRight: ImageView
    lateinit var tvCardTopLeft: TextView
    lateinit var tvCardBottomRight: TextView
    lateinit var clDrawCard: ConstraintLayout
    lateinit var tvAIText: TextView
    lateinit var clDeck: ConstraintLayout
    lateinit var imGoFishButton: ImageView
    lateinit var tvGoFishButton: TextView
    lateinit var tvPlayerText: TextView
    lateinit var imAIText: ImageView
    lateinit var imPlayerText: ImageView
    lateinit var imPLayerIcon: ImageView
    lateinit var rvHumanPairs: RecyclerView
    lateinit var rvAIPairs: RecyclerView
    lateinit var adapterHumanPairs: PairsAdapter
    lateinit var adapterAIPairs: PairsAdapter

    var deckOfCard = deckOfCard().deckOfCard
    var firstCard = deckOfCard.first()
    var card: Card? = null
  //  val adapterHuman = view?.let { HandOfCardsAdapter(it.context, humandeckOfCard) }
    val timerScope = CoroutineScope(Dispatchers.Default)
    var waitForDrawCard = false
    var aiTurnAnswer = false
    var aiTurn = false
    var timerClickDeck = false
    var timerClickHandPick = false
    var timerClickHandAnswer = false

    var randomCardNumber = 0

    val human = Player("human", mutableListOf(), TreeMap(), 0 ,0)
    val ai = Player("computer", mutableListOf(), TreeMap(), 0, 0)


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
        val adapterAI = HandOfCardsAdapter(view.context, ai.deck)
        rvAICards.adapter = adapterAI

        rvHumanCards = view.findViewById(R.id.rvHumanCards)
        rvHumanCards.layoutManager =
            LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        val adapterHuman = HandOfCardSortedAdapter(view.context, human.deckMap, human.deck)
        rvHumanCards.adapter = adapterHuman

        rvHumanPairs = view.findViewById(R.id.rvHumanPairs)
        rvHumanPairs.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapterHumanPairs = PairsAdapter(view.context, human.numberOfPairs)
        rvHumanPairs.adapter = adapterHumanPairs

        rvAIPairs = view.findViewById(R.id.rvAIPairs)
        rvAIPairs.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        adapterAIPairs = PairsAdapter(view.context, ai.numberOfPairs)
        rvAIPairs.adapter = adapterAIPairs

        imCardCenter = view.findViewById(R.id.imCardCenter2)
        imCardBottomRight = view.findViewById(R.id.imCardBottomRight2)
        imCardTopLeft = view.findViewById(R.id.imCardTopLeft2)
        tvCardBottomRight = view.findViewById(R.id.tvCardBottomRight2)
        tvCardTopLeft = view.findViewById(R.id.tvCardTopLeft2)
        clDrawCard = view.findViewById(R.id.clDrawCard)
        tvAIText = view.findViewById(R.id.tvAIText)
        clDeck = view.findViewById(R.id.clDeck)
        imGoFishButton = view.findViewById(R.id.imGoFishButton)
        tvGoFishButton = view.findViewById(R.id.tvGoFishButton)
        tvPlayerText = view.findViewById(R.id.tvPlayerText)
        imAIText = view.findViewById(R.id.imAIText)
        imPlayerText = view.findViewById(R.id.imPlayerText)
        imPLayerIcon = view.findViewById(R.id.imPLayerIcon)

        createHands()
        clDrawCard.visibility = View.INVISIBLE
        clDeck.visibility = View.INVISIBLE
        hideGoFishButton()
        tvAIText.text = "Your turn!"
        hidePlayerText()
        adapterAIPairs.updateNumberOfPairs(4)
        adapterHumanPairs.updateNumberOfPairs(2)



        val imDeckOfCard = view.findViewById<ImageView>(R.id.imDeck)

        imDeckOfCard.setOnClickListener() {
            if(!timerClickDeck) {
                timerClickDeck = true
                drawCardFromDeck(human.deck, human.deckMap, human)
            }
        }

        adapterHuman?.onCardClick = { cardValue, numberOfCards ->

                if (!waitForDrawCard && !aiTurnAnswer) {

                    if(!timerClickHandPick) {
                        timerClickHandPick = true
                        timerScope.launch {
                            withContext(Dispatchers.Main) {
                                hideAIText()
                                showPlayerText()
                                tvPlayerText.text = "I want all your ${firstCard.showNumberOnCard(cardValue)}s"
                                delay(TIMER_TEXT)
                                hidePlayerText()
                                humanTurn(cardValue)
                            }
                        }
                    }


                } else if (aiTurnAnswer) {
                    //var pickedCardForAnswer = it
                    if (cardValue == randomCardNumber) {

                        timerScope.launch {
                            withContext(Dispatchers.Main) {
                                hideAIText()
                                showPlayerText()
                                tvPlayerText.text = "Here you go!"
                                delay(TIMER_TEXT)
                                hidePlayerText()
                                showAIText()
                                tvAIText.text = "Thank you"
                                delay(TIMER_TEXT)
                                //hideAIText()
                                aiTurnAnswer = false
                                exchangeCards(cardValue, human.deck, ai.deck, ai.deckMap, human.deckMap, ai)
                            }
                        }

                    }
                }

        }

        imGoFishButton.setOnClickListener() {

                if (aiTurn) {
                    showPlayerText()
                    tvPlayerText.text = "GO FISH"
                    tvAIText.text = "Darn' it!"
                    timerScope.launch {
                        withContext(Dispatchers.Main) {
                            delay(TIMER_TEXT)
                            drawCardFromDeck(ai.deck, ai.deckMap, ai)
                            hidePlayerText()

                        }
                    }
                    hideGoFishButton()

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
            recalculateMap( ai.deckMap, ai.deck)
            deckOfCard.remove(deckOfCard.first())
            human.deck.add(deckOfCard.first())
            recalculateMap( human.deckMap, human.deck)
            deckOfCard.remove(deckOfCard.first())
        }
        sortHand(human.deckMap)
        sortHand(ai.deckMap)
        updateHandView()
    }

    fun drawCardFromDeck(deck: MutableList<Card>, deckMap: TreeMap<Int, Int>, player: Player) {
        if (deckOfCard.size > 0) {
            var drawnCard = deckOfCard.first()
            deck.add(drawnCard)
            recalculateMap(deckMap, deck)
            clDeck.visibility = View.VISIBLE
            clDrawCard.visibility = View.VISIBLE
            tvCardBottomRight.text = drawnCard.showNumberOnCard(drawnCard.number)
            tvCardTopLeft.text = drawnCard.showNumberOnCard(drawnCard.number)
            imCardCenter.setImageResource(drawnCard.showSuiteOnCard(drawnCard))
            imCardTopLeft.setImageResource(drawnCard.showSuiteOnCard(drawnCard))
            imCardBottomRight.setImageResource(drawnCard.showSuiteOnCard(drawnCard))

            deckOfCard.remove(drawnCard)
            sortHand(deckMap)

            checkForPairs(deckMap, deck, player)

            timerScope.launch {
                withContext(Dispatchers.Main) {
                    delay(TIMER_TEXT)
                    clDrawCard.visibility = View.INVISIBLE
                    clDeck.visibility = View.INVISIBLE
                    if (!aiTurn) {
                        waitForDrawCard = false
                        tvAIText.text = "My turn!"
                        aiTurnSequence()
                    } else {
                        aiTurn = false
                        aiTurnAnswer = false
                        tvAIText.text = " Your turn!"
                    }
                    timerClickDeck= false


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
                if(player == ai) {
                    timerScope.launch {
                        withContext(Dispatchers.Main) {
                            tvAIText.text = "I've got a pair!"

                            delay(TIMER_TEXT)
                            timerClickHandAnswer = false
                            adapterAIPairs.updateNumberOfPairs(ai.numberOfPairs)
                        }
                    }

                } else {
                    timerScope.launch {
                        withContext(Dispatchers.Main) {
                            showPlayerText()
                            tvPlayerText.text = "I've got a pair!"

                            delay(TIMER_TEXT)
                            hidePlayerText()
                            timerClickHandAnswer = false
                            adapterHumanPairs.updateNumberOfPairs(human.numberOfPairs)

                        }
                    }

                }
                Log.d("!!!", player.name + player.numberOfPairs.toString())
                break
            }
        }
    }

    fun removeCards(cardValue: Int, deck: MutableList<Card>, deckMap: TreeMap<Int, Int>, player: Player) {
        deck.filter { it.number == cardValue }.forEach { deck.remove(it) }
        if(deck.isEmpty()) {
            drawCardFromDeck(deck, deckMap, player)
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
        removeCards(cardValue, deckFrom, deckMapFrom, player)
        recalculateMap(deckMapTo, deckTo)
        recalculateMap(deckMapFrom, deckFrom)
        checkForPairs(deckMapTo, deckTo, player)

        if (aiTurn) {
            aiTurnSequence()
        }
    }

    fun recalculateMap( deckMap: TreeMap<Int, Int>, deck: MutableList<Card>) {
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
    }



    fun updateHandView() {
        //sortHand(deck)
        rvHumanCards.adapter?.notifyDataSetChanged()
        rvAICards.adapter?.notifyDataSetChanged()
    }

    fun aiTurnSequence() {
        if(aiTurn ) {
            timerScope.launch {
                withContext(Dispatchers.Main) {
                    delay(TIMER_TEXT)
                    tvAIText.text = "I may go again."
                }

            }
        }
        //timerScope.cancel()
        timerScope.launch {
            withContext(Dispatchers.Main) {

                delay(TIMER_TEXT)
                var randomCard = ai.deck.random()
                randomCardNumber = randomCard.number
                var showCardSymbol = randomCard.showNumberOnCard(randomCardNumber)
                tvAIText.text = "Give me all your $showCardSymbol"
                aiTurnAnswer = true
                aiTurn = true
                if(human.deck.filter { it.number == randomCardNumber }.isEmpty()) {
                    showGoFishButton()
                }
            }
        }
    }

    fun humanTurn(/*card: Card*/cardValue: Int) {

//        if (card !in ai.deck) {
//                Log.d("!!!", card?.number.toString())
           cardValue?.let { safeCardValue ->
                showAIText()
                if (ai.deckMap.containsKey(safeCardValue)) {
                    var showCardSymbol = firstCard.showNumberOnCard(safeCardValue)
             //       Log.d("!!!", showCardSymbol)
                    tvAIText.text = "Here, have all my $showCardSymbol"
                    exchangeCards(
                        safeCardValue,
                        ai.deck,
                        human.deck,
                        human.deckMap, ai.deckMap, human
                    )
                    timerScope.launch {
                        withContext(Dispatchers.Main) {
                            delay(TIMER_TEXT)
                            tvAIText.text = "Your may go again."
                            timerClickHandPick = false
                        }

                    }
                } else {
                    tvAIText.text = "GO FISH"
                    waitForDrawCard = true
                    clDeck.visibility = View.VISIBLE
                    timerClickHandPick = false


                }

            }
//        }
        sortHand(human.deckMap)
        sortHand(ai.deckMap)
        updateHandView()

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
}