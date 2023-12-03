package com.example.cardgame

object SaveData {
    class saveDataLevels(var level: Int, var bestScore: Int, var done: Boolean)
     var name: String = ""
     var language: Int = 0
     var icon: Int = 0
    var saveDataList = mutableListOf<saveDataLevels>()
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
        saveDataList.add(saveDataLevels(1, 0, true))
        saveDataList.add(saveDataLevels(2, 0, true))
        saveDataList.add(saveDataLevels(3, 0, false))
        saveDataList.add(saveDataLevels(4, 0, false))
        saveDataList.add(saveDataLevels(5, 0, false))
        saveDataList.add(saveDataLevels(6, 0, false))
        saveDataList.add(saveDataLevels(7, 0, false))
        saveDataList.add(saveDataLevels(8, 0, false))
        saveDataList.add(saveDataLevels(9, 0, false))
        saveDataList.add(saveDataLevels(10, 0, false))
    }
    init {
        resetData()
    }


}