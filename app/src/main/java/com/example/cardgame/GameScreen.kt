package com.example.cardgame

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity



class GameScreen : AppCompatActivity() {

    val gameIntroFragment = gameIntroFragment()
    val gameFragment = GameMode2Fragment()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_screen)


        showIntroFragment(null)

    }

    fun showIntroFragment(view: View?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fmGameScreen, gameFragment, "gameIntroFragment")
        transaction.commit()

    }

}