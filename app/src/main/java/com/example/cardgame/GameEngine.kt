package com.example.cardgame

object GameEngine {

    var gameLevels = listOf(
        Level(1, "Rules for level 1: \n High - Low \nThe goal of the game is to guess if the next card in the deck is higher or lower than the previous card. If you get a number of guesses in a row right, you get bonus points. ", 500, 0, 0, R.drawable.beetroot),
        Level(2, "Rules for level 2: \n High - Low \nThe goal of the game is to guess if the next card in the deck is higher or lower than the previous card. If you get a number of guesses in a row right, you get bonus points. ", 1000, 0, 0, R.drawable.beachball),
        Level(3, "Rules for level 3: \n High - Low \nThe goal of the game is to guess if the next card in the deck is higher or lower than the previous card. If you get a number of guesses in a row right, you get bonus points. ", 1500, 0, 0, R.drawable.carrot),
        Level(4, "Rules for level 4: \n High - Lowe \nThe goal of the game is to guess if the next card in the deck is higher or lower than the previous card. If you get a number of guesses in a row right, you get bonus points. ", 2000, 0, 0, R.drawable.tomato),
        Level(5, "Rules for level 5: \n Go fish! \nOn your turn, ask the other player if they have any cards of a specific rank. If they have matching cards in their hand, they have to give you all of them and you lay them out for scoring. If they don’t have any cards, then you draw a card. You get score based on how many pairs you and your opponent have at the end of the game.", 100, 0, 1, R.drawable.tree),
        Level(6, "Rules for level 6: \n Go fish! \nOn your turn, ask the other player if they have any cards of a specific rank. If they have matching cards in their hand, they have to give you all of them and you lay them out for scoring. If they don’t have any cards, then you draw a card. You get score based on how many pairs you and your opponent have at the end of the game.", 300, 0, 1, R.drawable.carrot),
        Level(7, "Rules for level 7: \n Go fish! \nOn your turn, ask the other player if they have any cards of a specific rank. If they have matching cards in their hand, they have to give you all of them and you lay them out for scoring. If they don’t have any cards, then you draw a card. You get score based on how many pairs you and your opponent have at the end of the game.", 500, 0, 1, R.drawable.bush),
        Level(8, "Rules for level 8: \n Crazy eight \nThe goal of Crazy Eights is to get rid of all the cards in your hand before the other player. You each start with 5 cards in your hand and a face-up card in the middle. If you have a card that matches either the suit or rank of the card in the middle, play it on your turn. Otherwise, you draw cards until you’re able to play one. The 8s are wild, so you can play them whenever you want and choose the suit for the next player. You get score based on what is left in your opponents hand when someones hand is empty", 100, 0, 2, R.drawable.beachball),
        Level(9, "Rules for level 9: \n Crazy eight \nThe goal of Crazy Eights is to get rid of all the cards in your hand before the other player. You each start with 5 cards in your hand and a face-up card in the middle. If you have a card that matches either the suit or rank of the card in the middle, play it on your turn. Otherwise, you draw cards until you’re able to play one. The 8s are wild, so you can play them whenever you want and choose the suit for the next player. You get score based on what is left in your opponents hand when someones hand is empty", 10, 120, 2, R.drawable.tomato),
        Level(10, "Rules for level 10: \n Crazy eight \nThe goal of Crazy Eights is to get rid of all the cards in your hand before the other player. You each start with 5 cards in your hand and a face-up card in the middle. If you have a card that matches either the suit or rank of the card in the middle, play it on your turn. Otherwise, you draw cards until you’re able to play one. The 8s are wild, so you can play them whenever you want and choose the suit for the next player. You get score based on what is left in your opponents hand when someones hand is empty", 10, 140, 2, R.drawable.fountain)
    )
    var currentLevel = 0;





}