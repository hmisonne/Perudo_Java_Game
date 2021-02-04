package com.example.mypackage;

import java.util.ArrayList;

public class Perudo {
    private ArrayList<Player> players;
    private int[] currentBet;
    private Player previousPlayer;
    private Player currentPlayer;
    private boolean isfirstRound = true;

    public Perudo() {
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player){
         players.add(player);
    }

    public void shuffleDice(){
        for(Player player: players){
           player.shuffleDice();
        }
    }

    public void makeABet(int[] currentBet, Player player){

        int newBetNumOfDice = currentBet[0];
        int newBetDieValue = currentBet[1];
        System.out.println(player.getName()+ " is betting "+newBetNumOfDice+" "+newBetDieValue+"'s");
        if (this.isfirstRound || isBetValid(newBetNumOfDice, newBetDieValue)){
            this.currentBet = new int[]{newBetNumOfDice, newBetDieValue};
            this.currentPlayer = player;
            this.isfirstRound = false;
        }
        else{
            System.out.println("Please make another bet");
        }
    }

    private boolean isBetValid(int newBetNumOfDice, int newBetDieValue){
        int currentNumOfDice = this.currentBet[0];
        int currentDieValue = this.currentBet[1];
//        Not Betting Pacos
        if (newBetDieValue > 1){
            int minNumOfDiceToBet = currentNumOfDice;
            if(currentDieValue == 1){
                minNumOfDiceToBet *= 2;
                minNumOfDiceToBet ++;
            }
            if (newBetNumOfDice < minNumOfDiceToBet){
                System.out.println("If you're not betting Pacos, you need to be at least "+ minNumOfDiceToBet+ " dice");
                return false;
            }
            else if( newBetNumOfDice == minNumOfDiceToBet && newBetDieValue <= currentDieValue ){
                System.out.println("You need to increase the value of Die or increase the number of Dice");
                return false;
            }
            else {
                return true;
            }
        } else {
//            Betting Pacos
            int minNumOfDiceToBet = currentNumOfDice;
            if(currentDieValue != 1){
                minNumOfDiceToBet = currentNumOfDice / 2;
                if (currentNumOfDice % 2 != 0){
                    minNumOfDiceToBet ++;
                }
            } else {
                minNumOfDiceToBet ++;
            }

            if (newBetNumOfDice < minNumOfDiceToBet){
                System.out.println("You need to bet at least "+ minNumOfDiceToBet + " Pacos.");
                return false;
            } else {
                return true;
            }
        }
    }

    public void removeAPlayer(Player player){
        String name = player.getName();
        players.remove(player);
        System.out.println(name+ " removed from the game");

    }

    public void revealDice(Player currentPlayer) {
        this.previousPlayer = this.currentPlayer;
        this.currentPlayer = currentPlayer;
        int numOfDiceBet = currentBet[0];
        int dieValue = currentBet[1];
        int numOfDiceResult = 0;
        for(Player player: players){
            int[] diceValues = player.getDiceValues();
            for (int die : diceValues){
                if (die == dieValue){
                    numOfDiceResult ++;
                }
//                Adding Pacos, if the value bet is not a Paco
                if(dieValue != 1 && die ==1){
                    numOfDiceResult ++;
                }
            }

        }

        System.out.println("We have found: "+numOfDiceResult +" dice with a value of "+ dieValue + " or Pacos");
        System.out.println("The bet was for: "+ numOfDiceBet);
        boolean betResult = numOfDiceResult >= numOfDiceBet ;
        if(betResult){
            System.out.println(this.previousPlayer.getName() + " won his/her bet.");
            handleResult(this.currentPlayer);

        } else {
            System.out.println(this.previousPlayer.getName() + " was lying.");
            handleResult(this.previousPlayer);
        }
//        Reset Game
        this.isfirstRound = true;
    }

    public void handleResult(Player looser){
        int numOfDie = looser.getDiceValues().length;
        if (numOfDie > 1){
            looser.looseADie();
        }
        else {
            this.removeAPlayer(looser);
        }
    }
}
