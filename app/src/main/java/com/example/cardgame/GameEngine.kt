package com.example.cardgame

object GameEngine {

    var gameLevels = listOf(
        Level(1, "Rules for level 1: GAMEMODE HIGH - LOW", 500, 0, 0, R.drawable.beetroot),
        Level(2, "Rules for level 2:", 1000, 0, 0, R.drawable.beachball),
        Level(3, "Rules for level 3:", 1500, 0, 0, R.drawable.carrot),
        Level(4, "Rules for level 4:", 2000, 0, 0, R.drawable.tomato),
        Level(5, "Rules for level 5: GAMEMODE GO FISH", 1, 0, 1, R.drawable.tree),
        Level(6, "Rules for level 6:", 3, 0, 1, R.drawable.carrot),
        Level(7, "Rules for level 7:", 5, 0, 1, R.drawable.bush),
        Level(8, "Rules for level 8: GAMEMODE CRAZY 8", 10, 0, 2, R.drawable.beachball),
        Level(9, "Rules for level 9:", 10, 0, 2, R.drawable.tomato),
        Level(10, "Rules for level 10:", 10, 0, 2, R.drawable.fountain)
    )
    var currentLevel = 0;





}
