package com.example.cardgame

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val TIMER_SECONDS: Long = 30000

/**
 * A simple [Fragment] subclass.
 * Use the [GameMode0Fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameMode0Fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
//    private var gameDoneFragment = gameDoneFragment()
    private var deckOfCard = deckOfCard()
    private var currentCardIndex = 1
    private var score = 0
    private var currentCard = deckOfCard.getNewCard(0)
    private var nextCard = deckOfCard.getNewCard(1)
    private var rightAnswers = listOf(
        "Well done!",
        "Good job!",
        "That's right!",
        "You got it!",
        "Awesome!"
    )
    private var wrongAnswer = listOf(
        "Sorry!",
        "That was wrong.",
        "Sorry, try again!",
        "Better luck next time.",
        "Try harder!"
    )

    private lateinit var tvAIText: TextView
    private lateinit var tvCard: TextView
    private lateinit var tvCurrentScore: TextView
    private lateinit var tvCardTopLeft: TextView
    private lateinit var tvCardBottomRight: TextView
    private lateinit var imCardTopLeft: ImageView
    private lateinit var imCardBottomRight: ImageView
    private lateinit var imCardCenter: ImageView
    private lateinit var pbTimeLeft: ProgressBar
    private lateinit var imAI: ImageView


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

        val btnHigher: ImageButton = view.findViewById(R.id.imbHigher)
        val btnLower: ImageButton = view.findViewById(R.id.imbLower)
        tvCard = view.findViewById(R.id.tvCard)
        tvCardTopLeft = view.findViewById(R.id.tvCardTopLeft)
        tvCardBottomRight = view.findViewById(R.id.tvCardBottomRight)
        imCardTopLeft = view.findViewById(R.id.imCardTopLeft)
        imCardBottomRight = view.findViewById(R.id.imCardBottomRight)
        imCardCenter = view.findViewById(R.id.imCardCenter)
        tvAIText = view.findViewById(R.id.tvAITextGameMode0)
        tvCurrentScore = view.findViewById(R.id.tvCurrentScore)
        pbTimeLeft = view.findViewById(R.id.pbTimeLeft)
        imAI = view.findViewById(R.id.imAIGameMode0)

        showUICard()
        when(GameEngine.currentLevel) {
            0 -> {
                imAI.setImageResource(R.drawable.characters_0006)
            }
            1 -> {
                imAI.setImageResource(R.drawable.characters_0004)
            }
            else -> {
                imAI.setImageResource(R.drawable.characters_0003)
            }
        }

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

        val pbTimeLeftAnimator = ObjectAnimator.ofInt(pbTimeLeft,"progress",pbTimeLeft.max, 0)
        pbTimeLeftAnimator.duration = TIMER_SECONDS
        pbTimeLeftAnimator.start()


        pbTimeLeftAnimator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator) {
            }

            override fun onAnimationEnd(animation: Animator) {
                gameDone()
            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }
        })

    }
    fun gameDone() {
        showGameDoneFragment(null)
        GameEngine.gameLevels[GameEngine.currentLevel].score = score * 100
    }

    fun showGameDoneFragment(view: View?) {

        (activity as? GameScreen)?.switchFragment(null, gameDoneFragment(), false)


    }

    fun showNextCard() {
        if (currentCardIndex < deckOfCard.getDeckSize()-1) {

            currentCard = deckOfCard.getNewCard(currentCardIndex)
            nextCard = deckOfCard.getNewCard(currentCardIndex+1)
            currentCardIndex++
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
            tvAIText.text = rightAnswers.random()
        } else {
            tvAIText.text = wrongAnswer.random()
        }
        tvCurrentScore.text = "Score: ${score * 100}"
        showNextCard()
    }

    fun checkCardLower() {
        if(nextCard.number <= currentCard.number) {
            score++
            tvAIText.text = rightAnswers.random()
        } else {
            tvAIText.text = wrongAnswer.random()
        }
        tvCurrentScore.text = "Score: ${score * 100}"
        showNextCard()
    }


    fun showUICard() {

        tvCardBottomRight.text = currentCard.showNumberOnCard(currentCard.number)
        tvCardTopLeft.text = currentCard.showNumberOnCard(currentCard.number)
        imCardTopLeft.setImageResource(currentCard.showSuiteOnCard(currentCard.suite))
        imCardBottomRight.setImageResource(currentCard.showSuiteOnCard(currentCard.suite))
        imCardCenter.setImageResource(currentCard.showSuiteOnCard(currentCard.suite))

    }
}