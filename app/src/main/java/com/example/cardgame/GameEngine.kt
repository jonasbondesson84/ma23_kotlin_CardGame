package com.example.cardgame

object GameEngine {

    var gameLevels = listOf(
        Level(1, 500, 0, 0, R.drawable.beetroot),
        Level(2, 1000, 0, 0, R.drawable.beachball),
        Level(3, 1500, 0, 0, R.drawable.carrot),
        Level(4, 2000, 0, 0, R.drawable.tomato),
        Level(5, 100, 0, 1, R.drawable.tree),
        Level(6, 200, 0, 1, R.drawable.carrot),
        Level(7, 400, 0, 1, R.drawable.bush),
        Level(8, 100, 0, 2, R.drawable.beachball),
        Level(9, 120, 0, 2, R.drawable.tomato),
        Level(10, 140, 0, 2, R.drawable.fountain)
    )
    var currentLevel = 0





}