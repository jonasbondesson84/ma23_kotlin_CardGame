package com.example.cardgame

import android.os.Bundle
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
 * Use the [CardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: Int? = null

    private lateinit var imCenter : ImageView
    private lateinit var imTop : ImageView
    private lateinit var imBottom: ImageView
    private lateinit var tvTop: TextView
    private lateinit var tvBottom: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getInt(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card, container, false)
        val card = Card("Heart", 1)

        imCenter = view.findViewById(R.id.imCardCenterFragment)
        imTop = view.findViewById(R.id.imTopFragment)
        imBottom = view.findViewById(R.id.imBottomFragment)
        tvTop = view.findViewById(R.id.tvTopFragment)
        tvBottom = view.findViewById(R.id.tvBottomFragment)
        param1.let{suite ->
            if (suite != null) {
                imCenter.setImageResource(card.showSuiteOnCard(suite))
                imBottom.setImageResource(card.showSuiteOnCard(suite))
                imTop.setImageResource(card.showSuiteOnCard(suite))
            }
        }
        param2.let {number ->
            if (number != null) {
                tvTop.text = card.showNumberOnCard(number)
                tvBottom.text = card.showNumberOnCard(number)
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
         * @return A new instance of fragment CardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: Int) =
            CardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                }
            }
    }
}