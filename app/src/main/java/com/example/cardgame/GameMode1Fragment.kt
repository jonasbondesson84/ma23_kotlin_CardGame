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
    lateinit var tvAINumberOfPairs: TextView
    lateinit var tvPlayerNumberOfPairs: TextView
    lateinit var tvGoFishButton: TextView
    lateinit var tvPlayerText: TextView
    lateinit var imAIText: ImageView
    lateinit var imPlayerText: ImageView
    lateinit var imPLayerIcon: ImageView
   // var AIdeckOfCard = mutableListOf<Card>()
  //  var humandeckOfCard = mutableListOf<Card>()
    var deckOfCard = deckOfCard().deckOfCard
   // var numbersOfPairs = 0
   // var AIDeckNumbersOnly = mutableMapOf<Int, Int>()
 //   var humanDeckNumbersOnly = mutableMapOf<Int, Int>()
    var card: Card? = null
  //  val adapterHuman = view?.let { HandOfCardsAdapter(it.context, humandeckOfCard) }
    val timerScope = CoroutineScope(Dispatchers.Default)
    var waitForDrawCard = false
    var aiTurnAnswer = false
    var aiTurn = false
  //  var timerClickCheck = false

    var randomCardNumber = 0

    val human = Player("human", mutableListOf(), mutableMapOf(), 0 ,0)
    val ai = Player("computer", mutableListOf(), mutableMapOf(), 0, 0)


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
        val adapterHuman = HandOfCardsAdapter(view.context, human.deck)
        rvHumanCards.adapter = adapterHuman

        imCardCenter = view.findViewById(R.id.imCardCenter2)
        imCardBottomRight = view.findViewById(R.id.imCardBottomRight2)
        imCardTopLeft = view.findViewById(R.id.imCardTopLeft2)
        tvCardBottomRight = view.findViewById(R.id.tvCardBottomRight2)
        tvCardTopLeft = view.findViewById(R.id.tvCardTopLeft2)
        clDrawCard = view.findViewById(R.id.clDrawCard)
        tvAIText = view.findViewById(R.id.tvAIText)
        clDeck = view.findViewById(R.id.clDeck)
        imGoFishButton = view.findViewById(R.id.imGoFishButton)
        tvAINumberOfPairs = view.findViewById(R.id.tvAInumberOfPairs)
        tvPlayerNumberOfPairs = view.findViewById(R.id.tvPlayerNumberOfPairs)
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


        val imDeckOfCard = view.findViewById<ImageView>(R.id.imDeck)

        imDeckOfCard.setOnClickListener() {
           // if(!timerClickCheck) {
                drawCardFromDeck(human.deck, human.deckMap, human)
          //  }
        }

        adapterHuman?.onCardClick = {

//            if(!timerClickCheck) {
//                timerClickCheck = true
                if (!waitForDrawCard && !aiTurnAnswer) {
                    var pickedCard = it
                    humanTurn(pickedCard)

                } else if (aiTurnAnswer) {
                    var pickedCardForAnswer = it
                    if (pickedCardForAnswer.number == randomCardNumber) {
                        exchangeCards(
                            pickedCardForAnswer.number,
                            human.deck,
                            ai.deck,
                            ai.deckMap, human.deckMap, ai
                        )
                        aiTurnAnswer = false

                    }
                }
//            }
        }

        imGoFishButton.setOnClickListener() {
//            if(!timerClickCheck) {
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
//            }
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
        sortHand(human.deck)
        sortHand(ai.deck)
        updateHandView()
    }

    fun drawCardFromDeck(deck: MutableList<Card>, deckMap: MutableMap<Int, Int>, player: Player) {
        if (deckOfCard.size > 0) {
            var drawnCard = deckOfCard.first()
            deck.add(drawnCard)
            recalculateMap(deckMap, deck)
            clDeck.visibility = View.VISIBLE
            clDrawCard.visibility = View.VISIBLE
            tvCardBottomRight.text = drawnCard.showNumberOnCard(drawnCard)
            tvCardTopLeft.text = drawnCard.showNumberOnCard(drawnCard)
            imCardCenter.setImageResource(drawnCard.showSuiteOnCard(drawnCard))
            imCardTopLeft.setImageResource(drawnCard.showSuiteOnCard(drawnCard))
            imCardBottomRight.setImageResource(drawnCard.showSuiteOnCard(drawnCard))

            deckOfCard.remove(drawnCard)
            sortHand(deck)

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

                }
            }
        }
    }

    fun sortHand(deck: MutableList<Card>) {
        deck.sortBy { it.number }
        updateHandView()
    }

    fun checkForPairs(deckMap: MutableMap<Int, Int>, deck: MutableList<Card>, player: Player) {

        for ((key, value) in deckMap) {
            if (value == 4) {
                player.numberOfPairs++
                Log.d("!!!", player.name)
                deckMap.remove(key)
                removeCards(key, deck, deckMap, player)
                if(player == ai) {
                    timerScope.launch {
                        withContext(Dispatchers.Main) {
                            tvAIText.text = "I got a pair!"
                            tvAINumberOfPairs.text = "Number of pairs: ${ai.numberOfPairs}"
                            delay(TIMER_TEXT)
                        }
                    }

                } else {
                    timerScope.launch {
                        withContext(Dispatchers.Main) {
                            showPlayerText()
                            tvPlayerText.text = "I've got a pair!"
                            tvPlayerNumberOfPairs.text = "Number of pairs: ${human.numberOfPairs}"
                            delay(TIMER_TEXT)
                            hidePlayerText()
                        }
                    }

                }
                Log.d("!!!", player.name + player.numberOfPairs.toString())
                break
            }
        }
    }

    fun removeCards(cardValue: Int, deck: MutableList<Card>, deckMap: MutableMap<Int, Int>, player: Player) {
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
        deckMapTo: MutableMap<Int, Int>,
        deckMapFrom: MutableMap<Int, Int>,
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

    fun recalculateMap( deckMap: MutableMap<Int, Int>, deck: MutableList<Card>) {
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
                var showCardSymbol = randomCard.showNumberOnCard(randomCard)
                tvAIText.text = "Give me all your $showCardSymbol"
                aiTurnAnswer = true
                aiTurn = true
                if(human.deck.filter { it.number == randomCardNumber }.isEmpty()) {
                    showGoFishButton()
                }
            }
        }
    }

    fun humanTurn(card: Card) {

        if (card !in ai.deck) {
//                Log.d("!!!", card?.number.toString())
            card?.let { safeCard ->
                if (ai.deckMap.containsKey(safeCard.number)) {
                    var showCardSymbol = safeCard.showNumberOnCard(safeCard)
             //       Log.d("!!!", showCardSymbol)
                    tvAIText.text = "Here, have all my $showCardSymbol"
                    exchangeCards(
                        safeCard.number,
                        ai.deck,
                        human.deck,
                        human.deckMap, ai.deckMap, human
                    )
                    timerScope.launch {
                        withContext(Dispatchers.Main) {
                            delay(TIMER_TEXT)
                            tvAIText.text = "Your may go again."
//                            timerClickCheck= false
                        }

                    }
                } else {
                    tvAIText.text = "GO FISH"
                    waitForDrawCard = true
                    clDeck.visibility = View.VISIBLE
//                    timerClickCheck = false


                }

            }
        }
        sortHand(human.deck)
        sortHand(ai.deck)
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