package com.example.mypackage;

import java.util.Random;

public class RobotPlayer extends Player{
    public RobotPlayer(String name) {
        super(name);
    }

    public int[] makeABet(int[] currentBet){
//        Choose a random number for dieValue and calculate numberOfDice accordingly
        int dieValue = new Random().nextInt(6)+1;
        int numberOfDice = currentBet[0];
//        First option: new Bet dieValue == currentBet die Value
//          Includes also new bet with a lower die value than current bet -> Increase quantity of die by 1.
        if(dieValue == currentBet[1] || (dieValue > 1 && dieValue < currentBet[1])){
            numberOfDice ++ ;
        }
//        Other options: new Bet dieValue != currentBet die Value
//          If current bet is with Pacos, double the quantity of dice
        else if(currentBet[1]==1){
            numberOfDice *= 2;
            numberOfDice ++;
        }
//          If new Bet is with Pacos, divide by 2 the quantity of dice
        else if(dieValue == 1){
            numberOfDice /= 2;
            if (currentBet[0] % 2 != 0){
                numberOfDice ++;
            }
        }
//          Else, new die value is higher than current die value, keep die quantity

        return new int[] {numberOfDice,dieValue};

    }
    public int[] makeABet(int numberOfDice){
//            The die's value will be between 2 and 6 (random number between 0 and 4, +2)
        int dieValue = new Random().nextInt(5)+2;
        if (numberOfDice >= 6){
            return new int[] {(numberOfDice/3)-1, dieValue};
        } else {
            return new int[] {1, dieValue};
        }
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
