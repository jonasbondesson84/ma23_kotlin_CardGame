package com.example.cardgame

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    val deckOfCard =  deckOfCard()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlay: ImageView = findViewById(R.id.imbPlay)
        val gameIntent = Intent(this, GameScreen::class.java)


        btnPlay.setOnClickListener() {
            startActivity(gameIntent)
        }



    }
}