package com.example.cardgame

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
private const val SAVED_DATA = "SaveData"
class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchFragment(null, MainMenyFragment())


    }

    override fun onResume() {
        super.onResume()
        val savedData = getSharedPreferences(SAVED_DATA, MODE_PRIVATE)
        val saveDataEditor = savedData.edit()

        if (!savedData.contains("name")) {
            saveDataEditor.putString("name", "unknown")
            saveDataEditor.putInt("language", 0)
            saveDataEditor.putInt("icon", R.drawable.characters_0001)
            saveDataEditor.putString("gameProgress", saveGameProgress())
            saveDataEditor.apply()
        }
      //  loadSavedSettings()

    }

    fun saveGameProgress(): String {
        val gson = Gson()
        val json = gson.toJson(SaveData.saveDataList)
        Log.d("!!!", "String to save" +json)
        return json

    }
    fun loadSavedSettings() {
        val getData = getSharedPreferences(SAVED_DATA, 0)
        SaveData.name = getData.getString("name", "") ?: ""
        SaveData.language = getData.getInt("language", 0 )
        SaveData.icon = getData.getInt("icon", R.drawable.characters_0001)
        SaveData.saveDataList = loadGameProgress("SaveData")
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

    fun switchFragment(view: View?, nextFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fmMainActivity, nextFragment, "gameIntroFragment")
        transaction.commit()

    }
}