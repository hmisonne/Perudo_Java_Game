package com.example.mypackage;


import java.util.Random;

public class Player{
    private final String name;
    private int[] diceValues;

    public Player(String name) {
        this.diceValues = new int[5];
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int[] getDiceValues() {
        return diceValues;
    }


    public void shuffleDice(){
        Random rand = new Random();
        int numOfDice = diceValues.length ;
        for(int i = 0 ; i < numOfDice; i++){
            diceValues[i] = rand.nextInt(6)+1;
        }
//        System.out.println(name+ " now has: "+ Arrays.toString(diceValues));
    }

    public void looseADie(){
        if(diceValues.length < 1){
            System.out.println("not valid");
        }
        else {
            System.out.println(this.name + " is loosing a die");
            this.diceValues = new int[diceValues.length -1];
        }
    }

}
