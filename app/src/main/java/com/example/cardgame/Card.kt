package com.example.cardgame

class Card(var suite: String, var number: Int) {


    fun showSuiteOnCard(card: Card): Int {
        var imageID = 0
        when(card.suite) {
            "Hearts" -> {
                imageID = R.drawable.characters_0006
            }

            "Diamonds" -> {
                imageID = R.drawable.characters_0001
            }

            "Clubs" -> {
                imageID = R.drawable.characters_0003
            }

            else -> {
                imageID = R.drawable.characters_0005
            }
        }
            return imageID
        }

    fun showNumberOnCard(card: Card): String {

            var numberSymbol = ""
            when(card.number) {
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
                    numberSymbol = card.number.toString()
                }
            }
            return numberSymbol
    }

}