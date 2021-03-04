package com.example.mypackage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PerudoTest {

    private Perudo perudo;
    private ArrayList<Player> players;

    @BeforeEach
    void setUp() {
        perudo = new Perudo();
//        Add 4 Players
        Player player = new Player("Yellow");
        perudo.addPlayer(player);
        player = new Player("Blue");
        perudo.addPlayer(player);
        player = new Player("Green");
        perudo.addPlayer(player);
        player = new Player("Pink");
        perudo.addPlayer(player);


    }
    @Test
    void addPlayer() {
        ArrayList<Player> players = perudo.getPlayers();
        int numOfPlayersBefore = players.size();
        Player player = new Player("Purple");
        perudo.addPlayer(player);
        int numOfPlayersAfter = players.size();
//        Player numbers increased by 1
        assertEquals(numOfPlayersBefore + 1, numOfPlayersAfter);
//        Player is added to list of players
        assertTrue(players.contains(player));

    }

    @Test
    void removeAPlayer() {
        ArrayList<Player> players = perudo.getPlayers();
        int numOfPlayersBefore = players.size();
        Player player = players.get(0);
        perudo.removeAPlayer(player);
        int numOfPlayersAfter = players.size();
//        Player numbers reduced by 1
        assertEquals(numOfPlayersBefore - 1, numOfPlayersAfter);
//        Player is removed from list of players
        assertFalse(players.contains(player));
    }
    @Test
    void getNumberOfDice() {
//        After setup, each player should receive 5 dice.
        assertEquals(perudo.getNumberOfDice(),4*5);
    }

    @Test
    void getCurrentPlayer() {
        perudo.setCurrentPlayer(0);
        assertEquals(perudo.getCurrentPlayer().getName(), "Yellow");
    }

    @Test
    void makeABet() {
        perudo.setCurrentPlayer(0);
        int[] newBet = {3,4};
        perudo.makeABet(newBet);
        assertEquals(perudo.getCurrentBet()[0], 3);
        assertEquals(perudo.getCurrentBet()[1], 4);
    }

    @Test
    void getPreviousPlayer() {
//        Setting current player to Blue, check if next player returns Yellow.
        perudo.setCurrentPlayer(1);
        assertEquals(perudo.getCurrentPlayer().getName(), "Blue");
        Player player = perudo.getPreviousPlayer();
        assertEquals(player.getName(), "Yellow");

    }

    @Test
    void nextPlayer() {
//        Setting current player to Blue, check if next player returns Green.
        perudo.setCurrentPlayer(1);
        perudo.setCurrentPlayerToNextPlayer();
        assertEquals(perudo.getCurrentPlayer().getName(),"Green");
    }

    @Test
    void shuffleDice() {
//        Check if each players has in his hand dice with a value between 1 and 6
        perudo.shuffleDice();
        ArrayList<Player> players = perudo.getPlayers();
        for (Player player : players){
            for(int die : player.getDiceValues()){
                assertTrue(die >= 1 && die <= 6);
            }
        }
    }

    @Test
    void isCurrentBetCorrect_true() {
//        Check if player made a correct Bet
//        Initialize round with shuffling dice, setting currentPlayer, and setting a correct bet.
        perudo.shuffleDice();
        perudo.setCurrentPlayer(1);
        Player player = perudo.getCurrentPlayer();
        int[] diceInHand = player.getDiceValues();
        perudo.makeABet(new int[] {1,diceInHand[0]});
        perudo.calculateActualNumberOfDie();
        assertTrue(perudo.isCurrentBetCorrect());
    }
    @Test
    void isCurrentBetCorrect_false() {
//        Check if player made an incorrect Bet
//        Initialize round with shuffling dice, setting currentPlayer, and setting a incorrect bet.
        perudo.shuffleDice();
        perudo.setCurrentPlayer(1);
        perudo.makeABet(new int[] {26,6});
        perudo.calculateActualNumberOfDie();
        assertFalse(perudo.isCurrentBetCorrect());
    }

    @Test
    void isNewBetHigher_currentBetWithoutPacos(){
        perudo.shuffleDice();
        perudo.setCurrentPlayer(1);
        perudo.makeABet(new int[] {3,5});
        perudo.setCurrentPlayerToNextPlayer();
//        Keeping the same quantity and value of dice  should return false
        assertFalse(perudo.isNewBetHigher(3,5));
//        Increasing quantity of dice with any dice value should return true
        assertTrue(perudo.isNewBetHigher(4,3));
//        Increasing value of die with the same dice quantity should return true
        assertTrue(perudo.isNewBetHigher(3,6));
//        Decreasing value of dice without increasing number of dice should return false if not betting Pacos
        assertFalse(perudo.isNewBetHigher(3,4));
//        Dividing by 2 the quantity of die and betting Pacos (or 1) should return True
        assertTrue(perudo.isNewBetHigher(2,1));
//        Dividing by more than 2 the quantity of die and betting Pacos (or 1) should return False
        assertFalse(perudo.isNewBetHigher(1,1));
    }

    @Test
    void isNewBetHigher_currentBetWithPacos(){
        perudo.shuffleDice();
        perudo.setCurrentPlayer(1);
        perudo.makeABet(new int[] {2,1});
        perudo.setCurrentPlayerToNextPlayer();
//        Increasing number of pacos should return true
        assertTrue(perudo.isNewBetHigher(3,1));
//       Multiplying by 2 + 1 the number of dice should return true
        assertTrue(perudo.isNewBetHigher(5,2));
//       Having a number of dice less than multiplying by 2 + 1 should return false
        assertFalse(perudo.isNewBetHigher(4,2));
    }

    @Test
    void removeADie() {
//        Start of the game, all players have 5 dice. Looser has one die removed
//        If there is more than 2 players remaining and looser has only one die left, looser removed from game
//        If there is only 2 players, and looser has only one die left, end of the game
    }

    @Test
    void isBetValid() {
    }
}