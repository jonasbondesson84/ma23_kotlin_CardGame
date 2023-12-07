package com.example.cardgame

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [gameIntroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class gameIntroFragment : Fragment() {
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
        val btnNext: ImageButton = view.findViewById(R.id.imLowerGamoeMode0)
        val gameLevels = GameEngine.gameLevels

        val rules = gameLevels[GameEngine.currentLevel].rules

        tvRules = view.findViewById(R.id.tvRules)
        tvScoreNeeded = view.findViewById(R.id.tvScoreNeeded)
        tvRulesTop = view.findViewById(R.id.tvRulesTop)

        tvRules.text = rules
        tvRules.movementMethod = ScrollingMovementMethod()
        tvScoreNeeded.text = "Score needed to win: ${GameEngine.gameLevels[GameEngine.currentLevel].scoreNeeded}"
        tvRulesTop.text = "Rules for game ${GameEngine.gameLevels[GameEngine.currentLevel].level}: \n${GameEngine.gameLevels[GameEngine.currentLevel].gameName}"






        btnNext.setOnClickListener() {
            when(gameLevels[GameEngine.currentLevel].gameMode) {
                0 -> {
                    (activity as GameScreen).switchFragment(view, GameMode0Fragment(), false)
                }

                1 -> {
                    (activity as GameScreen).switchFragment(view, GameMode1Fragment(), false)
                }
                else -> {
                    (activity as GameScreen).switchFragment(view, GameMode2Fragment(), false)
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
            gameIntroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}