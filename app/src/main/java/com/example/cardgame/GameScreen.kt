package com.example.cardgame

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private const val SAVED_DATA = "SaveData"

class GameScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)
        val playAgain = intent.getBooleanExtra("playAgain", false)
        Log.d("!!!", playAgain.toString())
        switchFragment(null, ProgressFragment(), playAgain)
//        if (playAgain) {
//            switchFragment(
//                null,
//                gameDoneFragment.newInstance(
//                    SaveData.saveDataList[GameEngine.currentLevel].bestScore,
//                    ""
//                ),
//                true
//            )
//        } else {
//            switchFragment(null, gameIntroFragment(), false)
//        }

    }

    fun switchFragment(view: View?, nextFragment: Fragment, playAgain: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fmGameScreen, nextFragment, "gameIntroFragment")
        transaction.commit()

    }

    fun showDrawnCard(view: View?, fragment: Fragment, fragmentID: Int) {

        val transaction = supportFragmentManager.beginTransaction()

        transaction.add(fragmentID, fragment, "drawnCard").commit()
    }
    fun hideDrawnCard(view: View?) {
        val cardFragment = supportFragmentManager.findFragmentByTag("drawnCard")
        if(cardFragment != null)  {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.remove(cardFragment).commit()
        }
    }

    fun switchToNextCard(view: View?, fragment: Fragment, fragmentID: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(fragmentID, fragment, "nextCard") .commit()

    }

    fun updateCardLayout(view: View?, card: Card) {
        val fragment = supportFragmentManager.findFragmentByTag("nextCard") as CardFragment
        fragment.updateCard(card)
    }


    fun saveData() {
        val savedData = getSharedPreferences(SAVED_DATA, MODE_PRIVATE)
        val saveDataEditor = savedData.edit()
        saveDataEditor.putString("gameProgress", saveGameProgress())
        saveDataEditor.apply()

        loadGameProgress()


    }

    fun saveGameProgress(): String {
        val gson = Gson()
        val json = gson.toJson(SaveData.saveDataList)
        Log.d("!!!", "String to save" + json)
        return json


    }

    fun loadGameProgress() {
        val savedData = getSharedPreferences(SAVED_DATA, MODE_PRIVATE)
        val gson = Gson()
        val json = savedData.getString("gameProgress", "")
        Log.d("!!!", "Loaded string: " + json)
        val type = object : TypeToken<MutableList<SaveData.saveDataLevels>>() {}.type

        Log.d("!!!", "Hämtat: " + gson.fromJson<Any?>(json, type) ?: "").toString()
        SaveData.saveDataList = gson.fromJson(json, type) ?: mutableListOf()
    }



}