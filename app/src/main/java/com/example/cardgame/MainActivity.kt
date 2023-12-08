package com.example.cardgame

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Locale

private const val SAVED_DATA = "SaveData"
class MainActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val savedData = getSharedPreferences(SAVED_DATA, MODE_PRIVATE)
        val saveDataEditor = savedData.edit()
        if (!savedData.contains("name")) {
            saveDataEditor.putString("name", "unknown")
            saveDataEditor.putString("language", "en")
            saveDataEditor.putInt("icon", R.drawable.characters_0001)
            saveDataEditor.putString("gameProgress", saveGameProgress())
            saveDataEditor.apply()
            restartActivity()
        }
        loadSavedSettings()
        checkLanguage()
    }

    override fun onResume() {
        super.onResume()
        switchFragment(null, MainMenyFragment())
    }

    private fun checkLanguage() {
        if(SaveData.language != resources.configuration.locales[0].language){
            setLocationForLanguage(SaveData.language)
            restartActivity()
        }
    }
    private fun setLocationForLanguage(languageChange: String) {
        val locale = Locale(languageChange)
        Locale.setDefault(locale)
        val resources = resources
        val config = resources?.configuration
        config?.locale = locale
        resources?.updateConfiguration(config, resources.displayMetrics)
    }

    private fun restartActivity() {
        val intent = this.intent
        this.finish()
        startActivity(intent)
    }


    private fun saveGameProgress(): String {
        SaveData.resetData()
        val gson = Gson()
        val json = gson.toJson(SaveData.saveDataList)
        return json

    }
    fun loadSavedSettings() {
        val getData = getSharedPreferences(SAVED_DATA, 0)
        SaveData.name = getData.getString("name", "") ?: ""
        SaveData.language = getData.getString("language", "en" ) ?: "en"
        SaveData.icon = getData.getInt("icon", R.drawable.characters_0001)
        SaveData.saveDataList = loadGameProgress()
    }

    fun loadGameProgress(): MutableList<SaveData.SaveDataLevels> {
        val savedData = getSharedPreferences(SAVED_DATA, MODE_PRIVATE)
        val gson = Gson()
        val json = savedData.getString("gameProgress", "")
        val type = object : TypeToken<MutableList<SaveData.SaveDataLevels>>() {}.type

        return gson.fromJson(json, type) ?: mutableListOf()
    }

    fun switchFragment(view: View?, nextFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fmMainActivity, nextFragment, "gameIntroFragment")
        transaction.commit()

    }
}