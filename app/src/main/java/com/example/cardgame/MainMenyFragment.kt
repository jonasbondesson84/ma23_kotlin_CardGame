package com.example.cardgame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import java.time.LocalDateTime

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainMenyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainMenyFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        val view = inflater.inflate(R.layout.fragment_main_meny, container, false)

        val imPlay = view.findViewById<ImageView>(R.id.imbPlay)
        val imSettings = view.findViewById<ImageView>(R.id.imSettings)
        val imProgress = view.findViewById<ImageView>(R.id.imProgress)

        //(activity as MainActivity).loadSavedSettings()

        imPlay.setOnClickListener() {
            val gameIntent = Intent(view.context, GameScreen::class.java)
            startActivity(gameIntent)

        }

        imProgress.setOnClickListener {
            (activity as MainActivity).switchFragment(view, ProgressFragment())
        }

        imSettings.setOnClickListener() {
            Log.d("!!!", LocalDateTime.now().toString())
            (activity as? MainActivity)?.switchFragment(view, SettingsFragment())
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
         * @return A new instance of fragment MainMenyFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainMenyFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}