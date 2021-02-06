package com.example.mypackage;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        play();

    }
    public static void play() {
//        Initialize Game
        Perudo perudo = new Perudo();
        Scanner scanner = new Scanner(System.in);
//        Add players
        System.out.println("Enter number of players: ");
        int playerNumber = scanner.nextInt();
        for (int i =0; i<playerNumber; i++){
            System.out.println("Enter player #"+(i+1)+ " name:");
            String playerName = scanner.next();
            Player player = new Player(playerName);
            perudo.addPlayer(player);
            scanner.nextLine();
        }
//        Set current player
        perudo.setCurrentPlayer(new Random().nextInt(playerNumber));

        boolean quit = false;
        while (perudo.isRunning() && !quit){
            if (perudo.isfirstRound()){
                perudo.shuffleDice();
                System.out.println("It's "+perudo.getCurrentPlayer().getName() +" turn");
                System.out.println("Choose\n" +
                        "1 to make a bet\n" +
                        "0 to quit");
            }
            else {
                if (perudo.currentBetIsCorrect()){
                    perudo.nextPlayer();
                }
                System.out.println("\n################################\n");
                System.out.println("It's "+perudo.getCurrentPlayer().getName() +"'s turn,");
                System.out.println("Choose:\n" +
                        "\t1 to make a bet\n" +
                        "\t2 to reveal the dice\n" +
                        "\t0 to quit");
            }
            int choice = scanner.nextInt();
            scanner.nextLine();
            switch (choice) {
                case 0:
                    quit = true;
                    break;
                case 1:
                    System.out.print("Enter your bet for number of dice: ");
                    int numOfDice = scanner.nextInt();
                    System.out.print("Enter your bet for value of dice: ");
                    int dieValue = scanner.nextInt();
                    perudo.makeABet(new int[]{numOfDice, dieValue});
                    break;
                case 2:
                    perudo.revealDice();
                    break;
            }


        }
    }
}
