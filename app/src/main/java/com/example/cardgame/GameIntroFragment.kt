package com.example.cardgame

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameIntroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GameIntroFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

//    private val gameMode0Fragment = GameMode0Fragment()
//    private val gameMode1Fragment = GameMode1Fragment()
//    private val gameMode2Fragment = GameMode2Fragment()
    private lateinit var tvRules: TextView
    private lateinit var tvScoreNeeded: TextView
    private lateinit var tvRulesTop: TextView



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
        val view = inflater.inflate(R.layout.fragment_game_intro, container, false)
        val btnNext: ImageView = view.findViewById(R.id.imStartGame)

        val rules = resources.getStringArray(R.array.Rules)
        val rulesText: String
        val gameNameList = resources.getStringArray(R.array.gameName)
        val gameName: String
        val currentGame = GameEngine.gameLevels[GameEngine.currentLevel]

        tvRules = view.findViewById(R.id.tvRules)
        tvScoreNeeded = view.findViewById(R.id.tvScoreNeeded)
        tvRulesTop = view.findViewById(R.id.tvRulesTop)


        tvRules.movementMethod = ScrollingMovementMethod()
        when(currentGame.level) {
            1,2,3,4 -> {
                gameName = gameNameList[0]
                rulesText = rules[0]
            }
            5,6,7 -> {
                gameName = gameNameList[1]
                rulesText = rules[1]
            }
            else -> {
                gameName = gameNameList[2]
                rulesText = rules[2]
            }
        }
        tvRules.text = rulesText
        tvScoreNeeded.text = resources.getString(R.string.scoreNeeded, currentGame.scoreNeeded.toString())
        tvRulesTop.text =  resources.getString(R.string.rulesForGame, currentGame.level.toString(), gameName)


        btnNext.setOnClickListener {
            when(currentGame.gameMode) {
                0 -> {
                    (activity as GameScreen).switchFragment(view, GameMode0Fragment())
                }
                1 -> {
                    (activity as GameScreen).switchFragment(view, GameMode1Fragment())
                }
                else -> {
                    (activity as GameScreen).switchFragment(view, GameMode2Fragment())
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
         * @return A new instance of fragment gameIntro.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GameIntroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}