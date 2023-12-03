package com.example.cardgame

object SaveData {
    class saveDataLevels(var level: Int, var bestScore: Int, var done: Boolean)
     var name: String = ""
     var language: Int = 0
     var icon: Int = 0
    var saveDataList = mutableListOf<saveDataLevels>()




    fun resetData() {
        saveDataList.clear()
        saveDataList.add(saveDataLevels(1, 0, false))
        saveDataList.add(saveDataLevels(2, 0, false))
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