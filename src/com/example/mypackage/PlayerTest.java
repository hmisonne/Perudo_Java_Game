package com.example.mypackage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
        assertEquals(player.getDiceValues().size(),5);
    }

    @Test
    void getNumberOfDice() {
//        Each player get 5 dice at the beginning of the game
        assertEquals(player.getNumberOfDice(),5);
    }

    @Test
    void shuffleDice() {
//        After shuffling dice, a player should still have the same number of dice.
        ArrayList<Dice> diceInHandBefore = player.getDiceValues();
        player.shuffleDice();
        ArrayList<Dice> diceInHand = player.getDiceValues();
        assertEquals(diceInHandBefore.size(), diceInHand.size());
//        After shuffling dice, each die should have a value between 1 and 6
        for(Dice die : diceInHand){
            assertTrue(die.getValue() >= 1 && die.getValue() <= 6);
        }
    }

    @Test
    void looseADie() {
        int numOfDiceBefore = player.getDiceValues().size();
        player.looseADie();
        int numOfDiceAfter = player.getDiceValues().size();
        assertEquals(numOfDiceBefore, numOfDiceAfter + 1);

    }
}