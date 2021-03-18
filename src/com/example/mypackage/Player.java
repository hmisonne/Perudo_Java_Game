package com.example.mypackage;


import java.util.ArrayList;
import java.util.Arrays;

public class Player{
    private final String name;
    private ArrayList<Dice> cupOfDice;

    public Player(String name) {
        this.cupOfDice = new ArrayList<>(5);
        this.name = name;

        for(int i= 0; i< 5; i ++){
            cupOfDice.add(new Dice());
        }
    }

    public String getName() {
        return name;
    }

    public ArrayList<Dice> getDiceValues() {
        return cupOfDice;
    }

    public int getNumberOfDice() {
        return cupOfDice.size();
    }


    public void shuffleDice(){
        for(Dice die: cupOfDice){
            die.shuffle();
        }
    }

    public void looseADie(){
        if(cupOfDice.size() < 1){
            System.out.println("not valid");
        }
        else {
            System.out.println(this.name + " is loosing a die");
            this.cupOfDice.remove(0);
        }
    }

}
