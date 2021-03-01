package com.example.mypackage;

import java.util.*;

public class MainCommandLine {
    public static String[] robotNames = {"Yellow", "Blue", "Pink", "Red","Green"};
    public static boolean quit = false;
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
    private static int getInt(int lowerBound, int upperBound) {
        Scanner scanner = new Scanner(System.in);
        while (true){
            try {
                int input;
                while (true) {
                    input = scanner.nextInt();
                    if(input <= upperBound && input >= lowerBound)
                        break;
                    System.out.println("Please enter a number between "+lowerBound+" and "+upperBound);
                }
                return input;
            } catch (InputMismatchException e){
                scanner.nextLine();
                System.out.println("Please enter a number between "+lowerBound+" and "+upperBound);
            }
        }
    }
    private static int getInt() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            try {
                int input;
                while (true) {
                    input = scanner.nextInt();
                    if(input > 0){
                        break;
                    }
                    System.out.println("Please enter a positive number");
                }
                return input;
            } catch (InputMismatchException e){
                scanner.nextLine();
                System.out.println("Please enter a number");
            }
        }
    }

    public static void play() {
//        Initialize Game
        Perudo perudo = initializeGame();
        while (perudo.isRunning() && !quit){
            if (perudo.isfirstRound()){
                perudo.shuffleDice();
                System.out.println("It's "+perudo.getCurrentPlayer().getName() +" turn");
                if (perudo.getCurrentPlayer() instanceof RobotPlayer){
                    RobotPlayer robotPlayer = (RobotPlayer) perudo.getCurrentPlayer();
                    int[] newBet = robotPlayer.makeABet(perudo.getNumberOfDice());
                    perudo.makeABet(newBet);
                }
                else{
                    System.out.println("You currently have: "+ Arrays.toString(perudo.getCurrentPlayer().getDiceValues()));
                    System.out.println("Choose\n" +
                            "1 to make a bet\n" +
                            "0 to quit");
                    int choice = getInt(0,1);
                    processUserSelection(choice);
                }

            }
            else {
                if (!perudo.higherBetIsRequired()){
                    perudo.setCurrentPlayerToNextPlayer();
                }
                System.out.println("\n################################\n");
                System.out.println("It's "+perudo.getCurrentPlayer().getName() +"'s turn,");
                if (perudo.getCurrentPlayer() instanceof RobotPlayer){
                    RobotPlayer robotPlayer = (RobotPlayer) perudo.getCurrentPlayer();
                    if(robotPlayer.decideToBet(perudo.getCurrentBet(),perudo.getNumberOfDice())){
                        int[] newBet = robotPlayer.makeABet(perudo.getCurrentBet());
                        perudo.makeABet(newBet);
                    } else {
                        perudo.revealDice();
                    }

                }
                else {
                    System.out.println("You currently have: "+ Arrays.toString(perudo.getCurrentPlayer().getDiceValues()));
                    System.out.println("Choose:\n" +
                            "\t1 to make a bet\n" +
                            "\t2 to reveal the dice\n" +
                            "\t0 to quit");
                    int choice = getInt(0,2);
                    processUserSelection( choice);
                }
            }
        }
    }


    public static Perudo initializeGame(){
        perudo = new Perudo();
        Scanner scanner = new Scanner(System.in);
//        Add players
        System.out.println("Enter number of players: ");
        int playerNumber = getInt(2,6);

        System.out.println("Enter player your name: ");
        String playerName = scanner.next();
        Player player = new Player(playerName);
        perudo.addPlayer(player);
        for (int i =0; i<playerNumber-1; i++){
            playerName = robotNames[i];
            RobotPlayer robotPlayer = new RobotPlayer(playerName);
            perudo.addPlayer(robotPlayer);
        }
//        Set current player
        perudo.setCurrentPlayer(new Random().nextInt(playerNumber));
        return perudo;
    }

    public static void processUserSelection( int choice){
        switch (choice) {
            case 0:
                quit = true;
                break;
            case 1:
                System.out.print("Enter your bet for number of dice: ");
                int numOfDice = getInt();
                System.out.print("Enter your bet for value of dice: ");
                int dieValue = getInt(1,6);
                int[] newBet = new int[]{numOfDice, dieValue};
                perudo.makeABet(newBet);
                break;
            case 2:
                perudo.revealDice();
                break;
        }
    }
}
