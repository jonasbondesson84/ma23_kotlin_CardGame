package com.example.cardgame

import android.animation.Animator
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    private var deckOfCard = deckOfCard()
    private var currentCardIndex = 1
    private var score = 0
    private var currentCard = deckOfCard.getNewCard(0)
    private var nextCard = deckOfCard.getNewCard(1)
    private var currentStreak = 0
    private lateinit var rightAnswers: Array<String>

//        listOf(
//        "Well done!",
//        "Good job!",
//        "That's right!",
//        "You got it!",
//        "Awesome!"
//    )
    private lateinit var wrongAnswer : Array<String>

//        listOf(
//        "Sorry!",
//        "That was wrong.",
//        "Sorry, try again!",
//        "Better luck next time.",
//        "Try harder!"
//    )
    private var TEXTSIZE_SHORT = 24F
    private var TEXTSIZE_MEDIUMSHORT = 18F
    private var TEXTSIZE_MEDIUMLONG = 16F
    private var TEXTSIZE_LONG = 12F


    private lateinit var tvAIText: TextView
    //    private lateinit var tvCard: TextView
    private lateinit var tvCurrentScore: TextView
    //    private lateinit var tvCardTopLeft: TextView
//    private lateinit var tvCardBottomRight: TextView
//    private lateinit var imCardTopLeft: ImageView
//    private lateinit var imCardBottomRight: ImageView
//    private lateinit var imCardCenter: ImageView
    private lateinit var pbTimeLeft: ProgressBar
    private lateinit var imPlayerIcon: ImageView
    private var timerScope = CoroutineScope(Dispatchers.Main)



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
        val view = inflater.inflate(R.layout.fragment_gamemode0, container, false)

        val btnHigher: ImageButton = view.findViewById(R.id.imHigherGamoeMode0)
        val btnLower: ImageButton = view.findViewById(R.id.imLowerGamoeMode0)
//        tvCard = view.findViewById(R.id.tvNextCardGameMode0)
//        tvCardTopLeft = view.findViewById(R.id.tvCardTopLeftGameMode0)
//        tvCardBottomRight = view.findViewById(R.id.tvCardBottomRightGameMode0)
//        imCardTopLeft = view.findViewById(R.id.imCardTopLeftGamoeMode0)
//        imCardBottomRight = view.findViewById(R.id.imCardBottomRightGamoeMode0)
//        imCardCenter = view.findViewById(R.id.imCardCenterGameMode0)
        tvAIText = view.findViewById(R.id.tvAITextGameMode0)
        tvCurrentScore = view.findViewById(R.id.tvCurrentScoreGamoeMode0)
        pbTimeLeft = view.findViewById(R.id.pbTimeLeft)
        imPlayerIcon = view.findViewById(R.id.imAIGameMode0)

        rightAnswers = resources.getStringArray(R.array.rightAnswers)
        wrongAnswer = requireContext().resources.getStringArray(R.array.wrongAnswers)

        var higherClicked = false
        var lowerClicked = false

        showUICard(currentCard)

        imPlayerIcon.setImageResource(SaveData.icon)
//        when(GameEngine.currentLevel) {
//            0 -> {
//                imPlayerIcon.setImageResource(R.drawable.characters_0006)
//            }
//            1 -> {
//                imPlayerIcon.setImageResource(R.drawable.characters_0004)
//            }
//            else -> {
//                imPlayerIcon.setImageResource(R.drawable.characters_0003)
//            }
//        }

        btnHigher.setOnClickListener() {
            if(!higherClicked) {
                higherClicked = true
                checkCardHigher()
                timerScope.launch {
                    btnHigher.setImageResource(R.drawable.buttontext_large_greyoutline_round)
                    btnLower.setImageResource(R.drawable.buttontext_large_greyoutline_round)
                    delay(1000L)
                    btnHigher.setImageResource(R.drawable.buttontext_large_orange_round)
                    btnLower.setImageResource(R.drawable.buttontext_large_orange_round)
                    higherClicked = false
                }
            }

        }

        btnLower.setOnClickListener() {
            if(!lowerClicked) {
                lowerClicked = true

                checkCardLower()
                timerScope.launch {
                    btnHigher.setImageResource(R.drawable.buttontext_large_greyoutline_round)
                    btnLower.setImageResource(R.drawable.buttontext_large_greyoutline_round)
                    delay(1000L)
                    btnHigher.setImageResource(R.drawable.buttontext_large_orange_round)
                    btnLower.setImageResource(R.drawable.buttontext_large_orange_round)
                    lowerClicked = false
                }
            }
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
        GameEngine.gameLevels[GameEngine.currentLevel].score = score
    }

    fun showGameDoneFragment(view: View?) {

        (activity as? GameScreen)?.switchFragment(null, gameDoneFragment(), false)


    }

    fun showNextCard() {
        if (currentCardIndex < deckOfCard.getDeckSize()-1) {

            currentCard = deckOfCard.getNewCard(currentCardIndex)
            nextCard = deckOfCard.getNewCard(currentCardIndex+1)
            currentCardIndex++
            showUICard(currentCard)


        } else {
            deckOfCard.shuffleCards()
            currentCardIndex = 0
            showNextCard()
        }
    }

    fun checkCardHigher() {

        if (nextCard.number >= currentCard.number) {
            score += 100
            var text = rightAnswers.random()
            textSizeAndShowText(text)
            currentStreak++
            Log.d("!!!", currentStreak.toString())
        } else {
            currentStreak = 0
            var text = wrongAnswer.random()
            textSizeAndShowText(text)
        }
        addStreakPoints()
        tvCurrentScore.text = ": ${score}"
        showNextCard()
    }

    fun checkCardLower() {

        if(nextCard.number <= currentCard.number) {
            score += 100
            currentStreak++
            var text = rightAnswers.random()
            textSizeAndShowText(text)
        } else {
            var text = wrongAnswer.random()
            textSizeAndShowText(text)
            currentStreak = 0
        }
        addStreakPoints()
        tvCurrentScore.text = ": ${score}"
        showNextCard()
    }


    fun showUICard(card: Card) {
        (activity as GameScreen).switchToNextCard(null, CardFragment.newInstance(currentCard.suite, currentCard.number), R.id.flShowCardGameMode0)

//        tvCardBottomRight.text = currentCard.showNumberOnCard(currentCard.number)
//        tvCardTopLeft.text = currentCard.showNumberOnCard(currentCard.number)
//        imCardTopLeft.setImageResource(currentCard.showSuiteOnCard(currentCard.suite))
//        imCardBottomRight.setImageResource(currentCard.showSuiteOnCard(currentCard.suite))
//        imCardCenter.setImageResource(currentCard.showSuiteOnCard(currentCard.suite))

    }

    fun addStreakPoints() {
        var streakBonus = 0
        if (currentStreak >= 10) {
            streakBonus = 200 * currentStreak
        } else if( currentStreak >= 5) {
            streakBonus = 100 * currentStreak
        } else if(currentStreak >= 3) {
            streakBonus = 50 * currentStreak
        }
        score += streakBonus
     //   val text = requireContext().resources.getString(R.string.streakBonus, streakBonus.toString())//"Streak bonus: ${streakBonus}"
        if(streakBonus > 3) {
            val text = resources.getString(R.string.streakBonus, streakBonus.toString())//"Streak bonus : $streakBonus"
            textSizeAndShowText(text)
        }
    }

    fun textSizeAndShowText(text: String){
        when {
            text.length > 22 -> {
                tvAIText.textSize = TEXTSIZE_LONG
            }
            text.length in 18..22 -> {
                tvAIText.textSize = TEXTSIZE_MEDIUMLONG
            }
            text.length in 15 .. 18 -> {
                tvAIText.textSize = TEXTSIZE_MEDIUMSHORT
            }
            else -> {
                tvAIText.textSize = TEXTSIZE_SHORT
            }
        }
        tvAIText.text = text
    }
}