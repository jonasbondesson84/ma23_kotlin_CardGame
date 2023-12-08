package com.example.cardgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "score"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameDoneFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameDoneFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: Int? = null
    private var param2: String? = null

    private lateinit var tvScore: TextView
    private lateinit var imStar1: ImageView
    private lateinit var imStar2: ImageView
    private lateinit var imStar3: ImageView
    private lateinit var tvBestScore: TextView
    private lateinit var imNextGame: ImageView
    private lateinit var tvNextGame: TextView

    private var timerScope = CoroutineScope(Dispatchers.Main)
   // private val gameIntroFragment = gameIntroFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
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
        val view = inflater.inflate(R.layout.fragment_game_done, container, false)

        tvScore = view.findViewById(R.id.tvScoreDone)
        imStar1 = view.findViewById(R.id.imStar1)
        imStar2 = view.findViewById(R.id.imStar2)
        imStar3 = view.findViewById(R.id.imStar3)
        tvBestScore = view.findViewById(R.id.tvBestScore)
        imNextGame = view.findViewById(R.id.imNextGame)
        tvNextGame = view.findViewById(R.id.tvNextGame)
        val imReplayGame = view.findViewById<ImageView>(R.id.imReplayGame)


        val levelScore = getScore()
        val bestScore = setBestScore(levelScore)

        tvBestScore.text = getString(R.string.bestScore, bestScore.toString())
        hideNextButton(bestScore)
        (activity as GameScreen).saveData()

        imNextGame.setOnClickListener {
            GameEngine.currentLevel++
            goToProgressTree()
        }

        imReplayGame.setOnClickListener {
            goToNextGame()
        }

        return view
    }

    private fun getScore():Int {
        val levelScore: Int
        if(param2 != null) {
            levelScore = SaveData.saveDataList[GameEngine.currentLevel].bestScore
            setStars(levelScore)
            tvScore.text = levelScore.toString()
        } else {
            levelScore = GameEngine.gameLevels[GameEngine.currentLevel].score
            animateScore(levelScore)
        }
        return levelScore
    }

    private fun animateScore(levelScore: Int) {
        timerScope.launch {
            withContext(Dispatchers.Main) {
                for(i in 0 .. levelScore) {
                    tvScore.text = i.toString()
                    setStars(i)
                    delay(1L)
                }
            }
        }
    }

    private fun setBestScore(levelScore: Int): Int {
        if(levelScore > SaveData.saveDataList[GameEngine.currentLevel].bestScore) {
            SaveData.saveDataList[GameEngine.currentLevel].bestScore = levelScore
        }
        return SaveData.saveDataList[GameEngine.currentLevel].bestScore

    }

    private fun hideNextButton(bestScore: Int) {
        if(bestScore < GameEngine.gameLevels[GameEngine.currentLevel].scoreNeeded) {
            tvNextGame.visibility = View.INVISIBLE
            imNextGame.visibility = View.INVISIBLE
        } else {
            SaveData.saveDataList[GameEngine.currentLevel].done = true
        }
    }

    private fun setStars(levelScore: Int) {
        imStar1.setImageResource(getStarsImages(levelScore, GameEngine.gameLevels[GameEngine.currentLevel].scoreNeeded))
        imStar2.setImageResource(getStarsImages(levelScore, (GameEngine.gameLevels[GameEngine.currentLevel].scoreNeeded * 1.5).toInt()))
        imStar3.setImageResource(getStarsImages(levelScore, (GameEngine.gameLevels[GameEngine.currentLevel].scoreNeeded * 2)))
    }

    private fun getStarsImages(levelScore: Int, scoreForStar: Int): Int {
        return if(levelScore >= scoreForStar) {
            R.drawable.icon_large_star_whiteoutline
        } else {
            R.drawable.icon_large_starsrey_seethroughoutline
        }
    }
    private fun goToNextGame() {
        (activity as? GameScreen)?.switchFragment(null, GameIntroFragment())
    }
    private fun goToProgressTree() {
        (activity as GameScreen).switchFragment(null, ProgressFragment())
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param score Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment gameDoneFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(score: Int, param2: String) =
            GameDoneFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, score)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



}