package com.example.cardgame

object GameEngine {

    var gameLevels = listOf(
        Level(1, "The goal of the game is to guess if the next card in the deck is higher or lower than the previous card. If you get a number of guesses in a row right, you get bonus points. ", 500, 0, 0, R.drawable.beetroot, "High - Low"),
        Level(2, "The goal of the game is to guess if the next card in the deck is higher or lower than the previous card. If you get a number of guesses in a row right, you get bonus points. ", 1000, 0, 0, R.drawable.beachball, "High - Low"),
        Level(3, "The goal of the game is to guess if the next card in the deck is higher or lower than the previous card. If you get a number of guesses in a row right, you get bonus points. ", 1500, 0, 0, R.drawable.carrot, "High - Low"),
        Level(4, "The goal of the game is to guess if the next card in the deck is higher or lower than the previous card. If you get a number of guesses in a row right, you get bonus points. ", 2000, 0, 0, R.drawable.tomato, "High - Low"),
        Level(5, "On your turn, ask the other player if they have any cards of a specific rank. If they have matching cards in their hand, they have to give you all of them and you lay them out for scoring. If they don’t have any cards, then you draw a card. You get score based on how many pairs you and your opponent have at the end of the game.", 100, 0, 1, R.drawable.tree, "Go fish"),
        Level(6, "On your turn, ask the other player if they have any cards of a specific rank. If they have matching cards in their hand, they have to give you all of them and you lay them out for scoring. If they don’t have any cards, then you draw a card. You get score based on how many pairs you and your opponent have at the end of the game.", 200, 0, 1, R.drawable.carrot, "Go fish"),
        Level(7, "On your turn, ask the other player if they have any cards of a specific rank. If they have matching cards in their hand, they have to give you all of them and you lay them out for scoring. If they don’t have any cards, then you draw a card. You get score based on how many pairs you and your opponent have at the end of the game.", 400, 0, 1, R.drawable.bush, "Go fish"),
        Level(8, "The goal of Crazy Eights is to get rid of all the cards in your hand before the other player. You each start with 5 cards in your hand and a face-up card in the middle. If you have a card that matches either the suit or rank of the card in the middle, play it on your turn. Otherwise, you draw cards until you’re able to play one. The 8s are wild, so you can play them whenever you want and choose the suit for the next player. You get score based on what is left in your opponents hand when someones hand is empty", 100, 0, 2, R.drawable.beachball, "Crazy eight"),
        Level(9, "The goal of Crazy Eights is to get rid of all the cards in your hand before the other player. You each start with 5 cards in your hand and a face-up card in the middle. If you have a card that matches either the suit or rank of the card in the middle, play it on your turn. Otherwise, you draw cards until you’re able to play one. The 8s are wild, so you can play them whenever you want and choose the suit for the next player. You get score based on what is left in your opponents hand when someones hand is empty", 120, 0, 2, R.drawable.tomato, "Crazy eight"),
        Level(10, "The goal of Crazy Eights is to get rid of all the cards in your hand before the other player. You each start with 5 cards in your hand and a face-up card in the middle. If you have a card that matches either the suit or rank of the card in the middle, play it on your turn. Otherwise, you draw cards until you’re able to play one. The 8s are wild, so you can play them whenever you want and choose the suit for the next player. You get score based on what is left in your opponents hand when someones hand is empty", 140, 0, 2, R.drawable.fountain, "Crazy eight")
    )
    var currentLevel = 0





}