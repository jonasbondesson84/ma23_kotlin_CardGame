package com.example.cardgame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
private const val SAVED_DATA = "SaveData"
class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlay: ImageView = findViewById(R.id.imbPlay)
        val gameIntent = Intent(this, GameScreen::class.java)
        btnPlay.setOnClickListener() {
            startActivity(gameIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        val savedData = getSharedPreferences(SAVED_DATA, MODE_PRIVATE)
        val saveDataEditor = savedData.edit()

        if (!savedData.contains("Name")) {
            saveDataEditor.putString("Name", "unknown")
            saveDataEditor.putInt("Language", 0)
            saveDataEditor.putString("gameProgress", saveGameProgress())
            saveDataEditor.apply()
        }
        SaveData.saveDataList = loadGameProgress("SaveData")

    }

    fun saveGameProgress(): String {
        val gson = Gson()
        val json = gson.toJson(SaveData.saveDataList)
        Log.d("!!!", "String to save" +json)
        return json

    }

    fun loadGameProgress(key: String): MutableList<SaveData.saveDataLevels> {
        val savedData = getSharedPreferences(SAVED_DATA, MODE_PRIVATE)
        val gson = Gson()
        val json = savedData.getString("gameProgress", "")
        Log.d("!!!", "Loaded string: " + json)
        val type = object : TypeToken<MutableList<SaveData.saveDataLevels>>() {}.type

        Log.d("!!!", "Hämtat: " + gson.fromJson<Any?>(json, type) ?: "").toString()
        return gson.fromJson(json, type) ?: mutableListOf()
    }
}