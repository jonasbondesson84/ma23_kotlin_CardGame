package com.example.cardgame

class deckOfCard {

     val deckOfCard = mutableListOf<Card>()

    init {
        createDeck()
       // shuffleCards()
    }


    fun createDeck() {
        var suite: String
        for(i in 1..4) {
            when (i) {
                1 -> {
                     suite = "Hearts"
                }
                2 -> {
                    suite = "Diamonds"
                }
                3 -> {
                    suite = "Clubs"
                }
                else -> {
                    suite = "Spades"
                }
            }
            createCards(suite)

        }
    }

    fun createCards(suite: String) {
        for (i in 2 .. 14) {
            deckOfCard.add(Card(suite, i))
        }
    }


    fun getDeckSize(): Int {
        return deckOfCard.size
    }

    fun shuffleCards() {
        deckOfCard.shuffle()
    }

    fun getNewCard(currentCardIndex: Int): Card {
        return deckOfCard[currentCardIndex]
    }
}