package com.example.cardgame

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


class GameScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)
        val playAgain =intent.getBooleanExtra("playAgain", false)
        Log.d("!!!", playAgain.toString())
        if(playAgain) {
            switchFragment(null, gameDoneFragment.newInstance(SaveData.saveDataList[GameEngine.currentLevel].bestScore, ""), true)
        } else {
            switchFragment(null, gameIntroFragment(),false)
        }

    }

    fun switchFragment(view: View?, nextFragment: Fragment, playAgain: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fmGameScreen, nextFragment, "gameIntroFragment")
        transaction.commit()

    }



}