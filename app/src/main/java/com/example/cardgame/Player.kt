package com.example.cardgame

class Player(
    var name: String,
    var deck: MutableList<Card>,
    var deckMap: MutableMap<Int, Int>,
    var numberOfPairs: Int,
    var score: Int
) {}