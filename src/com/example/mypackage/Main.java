package com.example.mypackage;

import java.util.*;

public class Main {
    public static String[] robotNames = {"Yellow", "Blue", "Pink", "Red","Green"};
    public static Perudo perudo;
    public static GUI perudoGUI;

    public static void main(String[] args) {
        try{
            play();
        } catch (NoSuchElementException e){
            System.out.println(e.toString());
            System.out.println("Unable to perform operation, shutting down game");
        }

    }

    public static void play() {
        initializeGame();
        perudoGUI.startNewRound();
    }


    public static void initializeGame(){
        perudo = new Perudo();
        Player player = new Player("You");
        int playerNumber = 5;
        perudo.addPlayer(player);
        for (int i =0; i<playerNumber-1; i++){
            String playerName = robotNames[i];
            RobotPlayer robotPlayer = new RobotPlayer(playerName);
            perudo.addPlayer(robotPlayer);
        }
//        Set current player
        perudo.setCurrentPlayer(new Random().nextInt(playerNumber));
        perudoGUI = new GUI(perudo);
        perudoGUI.setPlayerFrame();
    }

}
