package com.example.cardgame

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TIMER_SECONDS: Long = 10000

/**
 * A simple [Fragment] subclass.
 * Use the [GameMode0Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameMode0Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    val gameDoneFragment = gameDoneFragment()
    var gameMode: Int = 0
    var currentLevel: Int? = 0
    var deckOfCard = deckOfCard()
    var currentCardIndex = 1
    var score = 0
    var currentCard = deckOfCard.getNewCard(0)
    var nextCard = deckOfCard.getNewCard(1)

    lateinit var tvCard: TextView
    lateinit var tvCurrentScore: TextView
    lateinit var tvCardTopLeft: TextView
    lateinit var tvCardBottomRight: TextView
    lateinit var imCardTopLeft: ImageView
    lateinit var imCardBottomRight: ImageView
    lateinit var imCardCenter: ImageView


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
        val view = inflater.inflate(R.layout.fragment_gamemode0, container, false)
//        val args = arguments
//        var gameLevels : List<Level>? = args?.getParcelableArrayList<Level>("gameLevels")
//        //currentLevel = args?.getParcelable("currentLevel")
//
//        gameLevels?.let {
//            gameMode = gameLevels[0].gameMode
//        }
        tvCard = view.findViewById(R.id.tvCard)
        tvCardTopLeft = view.findViewById(R.id.tvCardTopLeft)
        tvCardBottomRight = view.findViewById(R.id.tvCardBottomRight)
        imCardTopLeft = view.findViewById(R.id.imCardTopLeft)
        imCardBottomRight = view.findViewById(R.id.imCardBottomRight)
        imCardCenter = view.findViewById(R.id.imCardCenter)
        showUICard()
//        val cardText = "${currentCard.suite} ${currentCard.number}"
//        tvCard.text = cardText
        val btnHigher: ImageButton = view.findViewById(R.id.imbHigher)
        val btnLower: ImageButton = view.findViewById(R.id.imbLower)
        tvCurrentScore = view.findViewById(R.id.tvCurrentScore)

        btnHigher.setOnClickListener() {
            checkCardHigher()
        }

        btnLower.setOnClickListener() {
            checkCardLower()
        }

        startTimer()

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameMode0Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun startTimer() {
        object : CountDownTimer(TIMER_SECONDS, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //tvTimer.setText(getString(R.string.timeLeft, (millisUntilFinished / 1000).toString()))

            }

            override fun onFinish() {
                //tvTimer.visibility = View.INVISIBLE
                //When time stop, game stops
          //      showGameDoneFragment(null)

            }
        }.start()
    }

    fun showGameDoneFragment(view: View?) {

        gameDoneFragment.arguments
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fmGameScreen, gameDoneFragment, "gameDoneFragment")
        transaction.commit()

    }

    fun showNextCard() {
        if (currentCardIndex < deckOfCard.getDeckSize()) {

            currentCard = deckOfCard.getNewCard(currentCardIndex)
            nextCard = deckOfCard.getNewCard(currentCardIndex+1)
            currentCardIndex++
//            var cardText = " ${currentCard.suite} ${currentCard.number} "
//            tvCard.text = cardText
            showUICard()


        } else {
            deckOfCard.shuffleCards()
            currentCardIndex = 0
            showNextCard()
        }
    }

    fun checkCardHigher() {
        if (nextCard.number >= currentCard.number) {
            score++
        }
        tvCurrentScore.text = score.toString()
        showNextCard()
    }

    fun checkCardLower() {
        if(nextCard.number <= currentCard.number) {
            score++
        }
        tvCurrentScore.text = score.toString()
        showNextCard()
    }


    fun showUICard() {
        var imageID = 0
        var numberSymbol = ""
        when(currentCard.suite) {
            "Hearts" -> {
                 imageID = R.drawable.characters_0006
            }
            "Diamonds" -> {
                imageID = R.drawable.characters_0001
            }
            "Clubs" -> {
                imageID = R.drawable.characters_0003
            } else -> {
                imageID = R.drawable.characters_0005
            }
        }
        when(currentCard.number) {
            14 -> {
                numberSymbol = "A"
            }
            13 -> {
                numberSymbol = "K"
            }
            12 -> {
                numberSymbol = "Q"
            }
            11 -> {
                numberSymbol = "J"
            }
            else -> {
                numberSymbol = currentCard.number.toString()
            }
        }
        tvCardBottomRight.text = numberSymbol
        tvCardTopLeft.text = numberSymbol
        imCardTopLeft.setImageResource(imageID)
        imCardBottomRight.setImageResource(imageID)
        imCardCenter.setImageResource(imageID)

    }
}