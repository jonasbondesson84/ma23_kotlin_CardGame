package com.example.cardgame

object SaveData {
    class SaveDataLevels(var bestScore: Int, var done: Boolean)
    var name = ""
     var language: String = "en"
     var icon: Int = R.drawable.characters_0003
    var saveDataList = mutableListOf<SaveDataLevels>()
    var levelList = listOf(
        R.id.imLevel1,
        R.id.imLevel2,
        R.id.imLevel3,
        R.id.imLevel4,
        R.id.imLevel5,
        R.id.imLevel6,
        R.id.imLevel7,
        R.id.imLevel8,
        R.id.imLevel9,
        R.id.imLevel10

    )



    fun resetData() {
        saveDataList.clear()
        saveDataList.add(SaveDataLevels( 0, false))
        saveDataList.add(SaveDataLevels( 0, false))
        saveDataList.add(SaveDataLevels( 0, false))
        saveDataList.add(SaveDataLevels( 0, false))
        saveDataList.add(SaveDataLevels( 0, false))
        saveDataList.add(SaveDataLevels( 0, false))
        saveDataList.add(SaveDataLevels( 0, false))
        saveDataList.add(SaveDataLevels( 0, false))
        saveDataList.add(SaveDataLevels( 0, false))
        saveDataList.add(SaveDataLevels( 0, false))
    }
    init {
        resetData()
    }


}