package com.example.mypackage;

public class Main {

    public static void main(String[] args) {
        Player player1 = new Player("Helene");
        Player player2 = new Player("Raf");
        Player player3 = new Player("Vincent");
        Player player4 = new Player("Tiphaine");

        Perudo perudo = new Perudo();
        perudo.addPlayer(player1);
        perudo.addPlayer(player2);
        perudo.addPlayer(player3);
        perudo.addPlayer(player4);

        perudo.shuffleDice();
        int[] currentBet = new int[] {3,3};
        perudo.makeABet(currentBet, player1);
//        perudo.revealDice(player2);

        perudo.shuffleDice();
        currentBet = new int[] {2,5};
        perudo.makeABet(currentBet, player2);
        currentBet = new int[] {5,3};
        perudo.makeABet(currentBet, player2);
        perudo.revealDice(player3);

        perudo.shuffleDice();
        currentBet = new int[] {2,1};
        perudo.makeABet(currentBet, player4);
        currentBet = new int[] {3,1};
        perudo.makeABet(currentBet, player4);
        perudo.revealDice(player1);

        currentBet = new int[] {6,2};
        perudo.makeABet(currentBet, player4);
        perudo.revealDice(player1);
    }
}
