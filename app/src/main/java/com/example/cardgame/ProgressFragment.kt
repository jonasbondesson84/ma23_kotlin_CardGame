package com.example.cardgame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProgressFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProgressFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var imLevel1: ImageView
    private lateinit var imLevel2: ImageView
    private lateinit var imLevel3: ImageView
    private lateinit var imLevel4: ImageView
    private lateinit var imLevel5: ImageView
    private lateinit var imLevel6: ImageView
    private lateinit var imLevel7: ImageView
    private lateinit var imLevel8: ImageView
    private lateinit var imLevel9: ImageView
    private lateinit var imLevel10: ImageView



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
        val view = inflater.inflate(R.layout.fragment_progress, container, false)

        imLevel1 = view.findViewById(R.id.imLevel1)
        imLevel2 = view.findViewById(R.id.imLevel2)
        imLevel3 = view.findViewById(R.id.imLevel3)
        imLevel4 = view.findViewById(R.id.imLevel4)
        imLevel5 = view.findViewById(R.id.imLevel5)
        imLevel6 = view.findViewById(R.id.imLevel6)
        imLevel7 = view.findViewById(R.id.imLevel7)
        imLevel8 = view.findViewById(R.id.imLevel8)
        imLevel9 = view.findViewById(R.id.imLevel9)
        imLevel10 = view.findViewById(R.id.imLevel10)
        val imOk = view.findViewById<ImageView>(R.id.imOkPathScreen)

        setLevelImages(view)

        // All for click on levels -------------------------------------
        imLevel1.setOnClickListener {
            GameEngine.currentLevel = 0
            if(SaveData.saveDataList[0].done) {
                (activity as GameScreen).switchFragment(null, gameDoneFragment.newInstance(SaveData.saveDataList[0].bestScore, "playAgain"), true)
            } else  {
                (activity as GameScreen).switchFragment(null, gameIntroFragment(), false)
            }
            //clickForPlay(view, 0)
//            if(SaveData.saveDataList[0].done) {
//                GameEngine.currentLevel = 0
//                val gameIntent = Intent(view.context, GameScreen::class.java)
//                gameIntent.putExtra("playAgain", true)
//                startActivity(gameIntent)
//            } else {
//                val gameIntent = Intent(view.context, GameScreen::class.java)
//                startActivity(gameIntent)
//            }
        }
        imLevel2.setOnClickListener {
            clickForPlay(view,1)
        }
        imLevel3.setOnClickListener {
            clickForPlay(view,2)
        }
        imLevel4.setOnClickListener {
            clickForPlay(view,3)
        }
        imLevel5.setOnClickListener {
            clickForPlay(view,4)
        }
        imLevel6.setOnClickListener {
            clickForPlay(view,5)
        }
        imLevel7.setOnClickListener {
            clickForPlay(view,6)
        }
        imLevel8.setOnClickListener {
            clickForPlay(view,7)
        }
        imLevel9.setOnClickListener {
            clickForPlay(view,8)
        }
        imLevel10.setOnClickListener {
            clickForPlay(view,9)
        }
        //-------------------------------------------------

        imOk.setOnClickListener {
            val currentScreen = this.javaClass.simpleName

            if(currentScreen == MainActivity::class.java.simpleName) {
                (activity as MainActivity).switchFragment(null, MainMenyFragment())
            } else {
                (activity as GameScreen).finish()
            }
        }


        return view
    }

    fun clickForPlay(view: View, chosenLevel: Int) {
        GameEngine.currentLevel = chosenLevel
        if(SaveData.saveDataList[chosenLevel].done) {
            (activity as GameScreen).switchFragment(null, gameDoneFragment.newInstance(SaveData.saveDataList[chosenLevel].bestScore, "playAgain"), true)
        } else if ( SaveData.saveDataList[chosenLevel-1].done) {
            (activity as GameScreen).switchFragment(null, gameIntroFragment(), false)
        }
//        if(SaveData.saveDataList[chosenLevel].done) {
//            (activity as GameScreen).switchFragment(null, gameIntroFragment(), false)
////            val gameIntent = Intent(view.context, GameScreen::class.java)
////            gameIntent.putExtra("playAgain", true)
////            startActivity(gameIntent)
//        } else if(SaveData.saveDataList[chosenLevel-1].done) {
////            val gameIntent = Intent(view.context, GameScreen::class.java)
////            startActivity(gameIntent)
//        }
    }

    fun setLevelImages(view: View) {
        for(i in 1..9) {
            var image = view.findViewById<ImageView>(SaveData.levelList[i])
            Log.d("!!!", "levelList: $i is " + SaveData.saveDataList[i].done.toString())

            if(SaveData.saveDataList[i].done) {
                image.setImageResource(GameEngine.gameLevels[i].imageDone)
            }
            else if(SaveData.saveDataList[i-1].done) {
                if(i == 9) {
                    image.setImageResource(R.drawable.door_golden_2_)
                } else {
                    image.setImageResource(R.drawable.door_silver)
                }
            } else {
                image.setImageResource(R.drawable.cloud_02)
            }
        }
        if(SaveData.saveDataList[0].done) {
            imLevel1.setImageResource(GameEngine.gameLevels[0].imageDone)
        }
//        } else {
//            imLevel10.setImageResource(R.drawable.door_silver)
//        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProgressFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProgressFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}