package com.example.cardgame

class Card(var suite: String, var number: Int) {


    fun showSuiteOnCard(cardSuite: String): Int {
        var imageID = 0
        when(cardSuite) {
            "Hearts" -> {
                imageID = R.drawable.characters_0006
            }

            "Diamonds" -> {
                imageID = R.drawable.characters_0001
            }

            "Clubs" -> {
                imageID = R.drawable.characters_0003
            }

            "Spades" -> {
                imageID = R.drawable.characters_0005
            }
            else ->
                imageID = R.drawable.box_orange_rounded
        }
            return imageID
        }

    fun showNumberOnCard(cardValue: Int): String {

            var numberSymbol = ""
            when(cardValue) {
                14 -> {
                    numberSymbol = "A"
                }
                13 -> {
                    numberSymbol = "K"
                }
                12 -> {
                    numberSymbol = "Q"
                }
                11 -> {
                    numberSymbol = "J"
                }
                else -> {
                    numberSymbol = cardValue.toString()
                }
            }
            return numberSymbol
    }

}