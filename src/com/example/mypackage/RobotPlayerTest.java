package com.example.mypackage;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class RobotPlayerTest {
    private RobotPlayer robotPlayer;

    @BeforeEach
    public void setUp(){
        robotPlayer = new RobotPlayer("Yellow");
    }

    @Test
    void makeABet_firstBet() {
        int[] newBet = robotPlayer.makeABet(12);
        int numOfDice = newBet[0];
        int dieValue = newBet[1];
        int min = 1; int max =6;
        assertTrue(min <= dieValue && dieValue <= max);
        assertTrue(numOfDice > 0);

    }

    @Test
    void makeABet_withPreviousBetInfo() {
        int[] newBet = robotPlayer.makeABet(new int[] {3,3});
        int numOfDice = newBet[0];
        int dieValue = newBet[1];
        int min = 1; int max =6;
        assertTrue(min <= dieValue && dieValue <= max);
        assertTrue(numOfDice > 0);
    }

    @Test
    void decideToBet() {
        int[] currentBet = {3,3};
        int numberOfDice = 12;
//        Decide to bet returns True if num of dice in current bet is less than number of dice / 3
        assertTrue(robotPlayer.decideToBet(currentBet,numberOfDice));
        currentBet = new int[] {3,3};
//        Else returns false.
        numberOfDice = 2;
        assertFalse(robotPlayer.decideToBet(currentBet,numberOfDice));
    }
}