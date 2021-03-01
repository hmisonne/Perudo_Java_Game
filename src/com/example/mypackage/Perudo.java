package com.example.mypackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Perudo {
    private ArrayList<Player> players;
    private int[] currentBet;
    private Player currentPlayer;
    private boolean isfirstRound;
    private boolean isRunning;
    private boolean higherBetIsRequired;


    public Perudo() {
        this.players = new ArrayList<>();
        this.isfirstRound = true;
        this.isRunning = true;
    }


    public int[] getCurrentBet() {
        return currentBet;
    }

    public int getNumberOfDice() {
        int numOfDie = 0;
        for(Player player : this.players){
            numOfDie += player.getNumberOfDice();
        }
        return numOfDie;
    }

    public boolean higherBetIsRequired() {
        return higherBetIsRequired;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int playerIndex) {
        this.currentPlayer = players.get(playerIndex);
    }

    public boolean isfirstRound() {
        return isfirstRound;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public void shuffleDice(){
        for(Player player: players){
           player.shuffleDice();
        }
    }
    public Player getPreviousPlayer(){
        int playerIndex = players.indexOf(currentPlayer);
        int previousPlayerIndex;
        if (playerIndex == 0){
            previousPlayerIndex = players.size() -1 ;
        } else {
            previousPlayerIndex = playerIndex - 1;
        }
        return players.get(previousPlayerIndex);
    }

    public void setCurrentPlayerToNextPlayer(){
        int playerIndex = players.indexOf(currentPlayer);
        int nextPlayerIndex;
//        If end of the list, go back to first player
        if (players.size() == (playerIndex + 1)){
            nextPlayerIndex = 0;
        } else {
            nextPlayerIndex = playerIndex + 1;
        }
        this.currentPlayer = players.get(nextPlayerIndex);
    }

    public void makeABet(int[] currentBet){

        int newBetNumOfDice = currentBet[0];
        int newBetDieValue = currentBet[1];
        System.out.println(currentPlayer.getName()+ " is betting "+newBetNumOfDice+" "+newBetDieValue+"'s");

        if (this.isfirstRound || isNewBetHigher(newBetNumOfDice, newBetDieValue)){
            this.currentBet = new int[]{newBetNumOfDice, newBetDieValue};
            this.higherBetIsRequired = false;
            this.isfirstRound = false;
        }
        else{
            this.higherBetIsRequired = true;
            System.out.println("Please make another bet");
        }
    }

    public boolean isNewBetHigher(int newBetNumOfDice, int newBetDieValue){
        int currentNumOfDice = this.currentBet[0];
        int currentDieValue = this.currentBet[1];

        if(currentDieValue < 1 || currentDieValue >6){
            System.out.println("Die value can only be between 1 and 6");
            return false;
        }
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

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void removeAPlayer(Player player){
        String name = player.getName();
        players.remove(player);
        System.out.println(name+ " removed from the game");

    }

    public void revealDice() {
        System.out.println(currentPlayer.getName()+ " wants to see the dice");
        boolean betIsCorrect = isCurrentBetCorrect();
        int looserIndex;
        Player looser;
        if(betIsCorrect){
            looser = this.currentPlayer;
            System.out.println(getPreviousPlayer().getName() + " made a correct bet.");
            looserIndex = this.players.indexOf(looser);
        } else {
            looser = getPreviousPlayer();
            System.out.println(getPreviousPlayer().getName() + " was lying.");
            looserIndex = this.players.indexOf(looser);
        }
        this.removeADie(looser);
        if (this.isRunning){
            if (looserIndex == this.players.size()){
                looserIndex --;
            }
            this.setCurrentPlayer(looserIndex);
//        Reset Game
            this.isfirstRound = true;
        }
    }

    public boolean isCurrentBetCorrect(){
//        Get Bet information
        int numOfDiceBet = currentBet[0];
        int dieValue = currentBet[1];
        System.out.println("The current bet was for: "+ numOfDiceBet +" "+ dieValue);
//        Compare Bet with actual results
        int numOfDiceResult = 0;
        for(Player player: players){
            int[] diceValues = player.getDiceValues();
            System.out.println(player.getName()+ " had: "+ Arrays.toString(diceValues));
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

        System.out.println("There was: "+numOfDiceResult +" dice with a value of "+ dieValue + " (including Pacos)");
        return numOfDiceResult >= numOfDiceBet;
    }

    public void removeADie(Player looser){
        int numOfDie = looser.getNumberOfDice();
        if (numOfDie > 1){
            looser.looseADie();
        }
        else {
            this.removeAPlayer(looser);
//            Check if only one player remaining
            if (this.players.size() == 1){
                System.out.println(this.players.get(0).getName() + " won!");
                this.isRunning = false;
            }
        }
    }

    public boolean decideToBet(RobotPlayer robotPlayer){
        if(this.isfirstRound()){
            return true;
        } else {
            return robotPlayer.decideToBet(this.getCurrentBet(),this.getNumberOfDice());
        }
    }

    public int[] robotBet(RobotPlayer robotPlayer){
        int[] newBet = this.isfirstRound()
                ? robotPlayer.makeABet(this.getNumberOfDice())
                : robotPlayer.makeABet(this.getCurrentBet());

        this.makeABet(newBet);
        return newBet;
    }
}
