package com.example.mypackage;

import java.util.Random;

public class RobotPlayer extends Player{
    public RobotPlayer(String name) {
        super(name);
    }

    public int[] makeABet(int[] previousBet){
        return new int[] {previousBet[0]+1,previousBet[1]};
    }
    public int[] makeABet(int numberOfDice){
        return new int[] {numberOfDice/3-1, new Random().nextInt(6)+1};
    }
    public boolean decideToBet(int[] currentBet, int numberOfDice){
        int currNumOfDice = currentBet[0];
        int currDieValue = currentBet[1];
        if (currDieValue > 1 && currNumOfDice > numberOfDice/3){
            return false;
        } else if (currDieValue == 1 && currNumOfDice > numberOfDice/6){
            return false;
        }
        return true;
    }
}
