package com.example.mypackage;

import java.util.Random;

public class Dice {
    private int value;

    public Dice() {
        Random random = new Random();
        this.value = random.nextInt(6)+1;
    }

    public int getValue() {
        return value;
    }

    public void shuffle() {
        Random random = new Random();
        this.value = random.nextInt(6)+1;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
