package com.example.cardgame

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson

private const val SAVED_DATA = "SaveData"

class GameScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)

        switchFragment(null, ProgressFragment())
    }

    fun switchFragment(view: View?, nextFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fmGameScreen, nextFragment, "gameIntroFragment").commit()
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
    }

    private fun saveGameProgress(): String {
        val gson = Gson()
        val json = gson.toJson(SaveData.saveDataList)
        return json
    }

}