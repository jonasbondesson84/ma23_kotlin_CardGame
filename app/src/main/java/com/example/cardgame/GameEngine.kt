package com.example.cardgame

object GameEngine {

    var gameLevels = listOf(
        Level(1, "Rules for level 1: GAMEMODE HIGH - LOW", 500, 0, 0),
        Level(2, "Rules for level 2:", 1000, 0, 0),
        Level(3, "Rules for level 3:", 1500, 0, 0),
        Level(4, "Rules for level 4:", 2000, 0, 0),
        Level(5, "Rules for level 5: GAMEMODE GO FISH", 10, 0, 1),
        Level(6, "Rules for level 6:", 10, 0, 1),
        Level(7, "Rules for level 7:", 10, 0, 1),
        Level(8, "Rules for level 8: GAMEMODE CRAZY 8", 10, 0, 2),
        Level(9, "Rules for level 9:", 10, 0, 2),
        Level(10, "Rules for level 10:", 10, 0, 2)
    )
    var currentLevel = 0;





}
