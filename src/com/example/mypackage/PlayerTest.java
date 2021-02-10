package com.example.mypackage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Blue");
    }

    @Test
    void getName() {
        assertEquals(player.getName(),"Blue");
    }

    @Test
    void getDiceValues() {
//        Each player get 5 dice at the beginning of the game
        assertEquals(player.getDiceValues().length,5);
    }

    @Test
    void getNumberOfDice() {
//        Each player get 5 dice at the beginning of the game
        assertEquals(player.getNumberOfDice(),5);
    }

    @Test
    void shuffleDice() {
//        After shuffling dice, a player should still have the same number of dice.
        int[] diceInHandBefore = player.getDiceValues();
        player.shuffleDice();
        int[] diceInHand = player.getDiceValues();
        assertEquals(diceInHandBefore.length, diceInHand.length);
//        After shuffling dice, each die should have a value between 1 and 6
        for(int die : diceInHand){
            assertTrue(die >= 1 && die <= 6);
        }
    }

    @Test
    void looseADie() {
        int[] diceInHandBefore = player.getDiceValues();
        player.looseADie();
        int[] diceInHand = player.getDiceValues();
        assertEquals(diceInHandBefore.length, diceInHand.length + 1);

    }
}