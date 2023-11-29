package com.example.cardgame

import java.util.TreeMap

class Player(
    var name: String,
    var deck: MutableList<Card>,
    var deckMap: TreeMap<Int, Int>,
    var numberOfPairs: Int,
    var score: Int
) {}