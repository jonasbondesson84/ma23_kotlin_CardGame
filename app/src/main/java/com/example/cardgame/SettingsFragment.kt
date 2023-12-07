package com.example.cardgame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val SAVED_DATA = "SaveData"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var language: String = "en"
    private var name: String = ""
    private var icon = 0
    private var iconList = mutableListOf<Int>()

    private lateinit var rvIcons: RecyclerView
    private lateinit var etvName: EditText
    private lateinit var imIconSelected: ImageView
    private lateinit var swResetData: Switch

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
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        val imCancel = view.findViewById<ImageView>(R.id.imCancel)
        val imSave = view.findViewById<ImageView>(R.id.imSave)
        val imBritish = view.findViewById<ImageView>(R.id.imBritish)
        val imSwedish = view.findViewById<ImageView>(R.id.imSwedish)
        etvName = view.findViewById(R.id.etvName)
        imIconSelected = view.findViewById(R.id.imChosenIcon)
        swResetData = view.findViewById(R.id.swResetData)
        createIconList()
        rvIcons = view.findViewById(R.id.rvIconSelecter)
        rvIcons.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL, false)
        val adapterIcons = IconAdapter(view.context, iconList)
        rvIcons.adapter = adapterIcons

        name = SaveData.name
        language = SaveData.language
        icon = SaveData.icon
       // if(icon == 0 or icon == null) {
       //     icon = R.drawable.characters_0001
      //  }
        etvName.setText(name)
        imIconSelected.setImageResource(icon)

        if (language == "en") {
            imBritish.setImageResource(R.drawable.flag_brittish2)
            imSwedish.setImageResource(R.drawable.flag_swedish_inactive)
        } else if (language == "sv"){
            imBritish.setImageResource(R.drawable.flag_british_inactive)
            imSwedish.setImageResource(R.drawable.flag_swedish2)
        } else {
            Log.d("!!!", "Fel på språk")
        }

        adapterIcons?.onIconClick = { icon = it
            imIconSelected.setImageResource(icon)

        }



        imSave.setOnClickListener() {

            saveData()
            restartActivity()
            goToMainMenu()
        }

        imCancel.setOnClickListener() {
            goToMainMenu()
        }

        imBritish.setOnClickListener {

            setLocationForLanguage("en")
            imBritish.setImageResource(R.drawable.flag_brittish2)
            imSwedish.setImageResource(R.drawable.flag_swedish_inactive)
        }
        imSwedish.setOnClickListener {

            setLocationForLanguage("sv")
            imBritish.setImageResource(R.drawable.flag_british_inactive)
            imSwedish.setImageResource(R.drawable.flag_swedish2)
        }


        return view
    }

    fun createIconList() {
        iconList.clear()
        iconList.add(R.drawable.characters_0001)
        iconList.add(R.drawable.characters_0002)
        iconList.add(R.drawable.characters_0003)
        iconList.add(R.drawable.characters_0004)
        iconList.add(R.drawable.characters_0005)
        iconList.add(R.drawable.characters_0006)
        iconList.add(R.drawable.characters_0007)

    }

    fun goToMainMenu() {
        (activity as? MainActivity)?.switchFragment(view, MainMenyFragment())
    }

    fun saveData() {
        val savedData = (activity as MainActivity).getSharedPreferences(
            SAVED_DATA,
            AppCompatActivity.MODE_PRIVATE
        )
        val saveDataEditor = savedData.edit()
        saveDataEditor.putString("name", etvName.text.toString() ?: "unknown")
        saveDataEditor.putString("language", language)
        saveDataEditor.putInt("icon", icon)
        if (swResetData.isChecked) {
            saveDataEditor.putString("gameProgress", resetGameProgress())
        }
        Log.d("!!!", "" + icon + " " + R.drawable.characters_0002)
        saveDataEditor.apply()
        loadSavedSettings()
    }
    private fun setLocationForLanguage(languageChange: String) {
        language = languageChange
        val locale = Locale(languageChange)
        Locale.setDefault(locale)
        val resources = context?.resources
        val config = resources?.configuration
        config?.locale = locale
        resources?.updateConfiguration(config, resources.displayMetrics)
    }

    fun restartActivity() {
        val intent = requireActivity().intent
        requireActivity().finish()
        startActivity(intent)
    }

    fun resetGameProgress(): String {
        SaveData.resetData()
        val gson = Gson()
        val json = gson.toJson(SaveData.saveDataList)
        Log.d("!!!", "String to save" + json)
        return json
    }

    fun loadSavedSettings() {
        val getData = (activity as MainActivity).getSharedPreferences(SAVED_DATA, 0)
        SaveData.name = getData.getString("name", "") ?: ""
        SaveData.language = getData.getString("language", "en") ?: "en"
        SaveData.icon = getData.getInt("icon", R.drawable.characters_0001)
        SaveData.saveDataList = (activity as MainActivity).loadGameProgress("SaveData")
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}