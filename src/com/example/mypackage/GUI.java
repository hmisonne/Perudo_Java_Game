package com.example.mypackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class GUI {
    private JLabel labelNum = new JLabel("Number of dice: ");
    private JTextField textFieldNum = new JTextField();
    private JLabel labelVal = new JLabel("Die Value: ");
    private JTextField textFieldVal = new JTextField();
    private JLabel labelDiceInHand = new JLabel("Dice in hand: ");
    private JLabel textDiceInHand = new JLabel();
    private JLabel labelWarning = new JLabel();
    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel();
    private JLabel[] playersPanel;
    private Perudo perudo;
    private ArrayList<Player> players;
    private JButton betButton = new JButton("Enter");
    private JButton revealDiceButton = new JButton("Reveal Dice");
    private JButton newRoundButton = new JButton("New Round");



    public GUI(Perudo perudo) {
        this.perudo = perudo;
        this.players = perudo.getPlayers();
//      Add 3 buttons to make a new bet, start a new round and reveal dice
        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(newBetAction()){
                    perudo.setCurrentPlayerToNextPlayer();
                    Thread queryThread = new Thread(() -> robotPlay());
                    queryThread.start();
                };
            }
        });

        newRoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread queryThread = new Thread(() -> startNewRound());
                queryThread.start();
            }
        });
        revealDiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                revealPlayersDice();
                perudo.revealDice();
            }
        });

        // Configure main panel
        JPanel betPanel = new JPanel();
        betPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        betPanel.setLayout(new GridLayout(0, 5));
        betPanel.add(labelNum);
        betPanel.add(textFieldNum);
        betPanel.add(labelVal);
        betPanel.add(textFieldVal);
        betPanel.add(betButton);
        betPanel.add(labelDiceInHand);
        betPanel.add(textDiceInHand);
        betPanel.add(labelWarning);
        betPanel.add(revealDiceButton);
        betPanel.add(newRoundButton);
        frame.setSize(800,800);
        mainPanel.setLayout(new GridLayout(3, 3));
        mainPanel.setBackground(new Color(150,150,150));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

        // set up the frame and display it
        frame.add(betPanel, BorderLayout.NORTH);
        frame.add(mainPanel);
        frame.setSize(800,800); // TODO
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Perudo");
        frame.setVisible(true);
        setPlayerFrame();
    }

    public void setPlayerFrame() {
//        Update the UI main panel with player's information
        int playersNum = players.size();
        playersPanel = new JLabel[playersNum];
        for (int i = 0; i < playersNum; i++) {
            playersPanel[i] = new JLabel();
            Player player = players.get(i);
            playersPanel[i].setText(player.getName() + ": " + player.getNumberOfDice() + " dice\n");
            mainPanel.add(playersPanel[i]);
            playersPanel[i].setFont(new Font("MV Boli", Font.BOLD, 15));
        }

    }

    public void startNewRound(){
//        At the beginning of each new round:
//          All players shuffle their dice,
//          The main player will see his combination of dice and how many dice has each player
//          A new round is started, iterating through each robot player.
        System.out.println("New Round Started");
        perudo.shuffleDice();
        showDiceInHand();
        resetPlayerDice();
        robotPlay();
    }


    public void robotPlay() {
//        Iterate through robot players until player is no longer a robot or robot decide to reveal the dice
        while (perudo.getCurrentPlayer() instanceof RobotPlayer){
//            Add wait time between robot actions
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }

            RobotPlayer robotPlayer = (RobotPlayer) perudo.getCurrentPlayer();
            if (perudo.decideToBet(robotPlayer)){
                perudo.robotBet(robotPlayer);
                showPlayersBet(robotPlayer, perudo.getCurrentBet());
                perudo.setCurrentPlayerToNextPlayer();
            }
            else {
                showPlayersDice();
                perudo.revealDice();
                return;
            }
        }
    }


    public void resetPlayerDice() {
//        Update the UI with the number of dice per player
        int playersNum = players.size();
        for (int i = 0; i < playersNum; i++) {
            Player player = players.get(i);
            playersPanel[i].setText(player.getName() + ": " + player.getNumberOfDice() + " dice\n");
        }

    }
    public void revealPlayersDice() {
//        Update the UI to display the dice per player
        int playersNum = players.size();
        for (int i = 0; i < playersNum; i++) {
            Player player = players.get(i);
            playersPanel[i].setText(player.getName() + ": " + Arrays.toString(player.getDiceValues()) );
        }
    }

    public boolean newBetAction(){
//        Check if user input to make a new bet is correct and update UI accordingly
        try {
            int num = Integer.parseInt(textFieldNum.getText()) ;
            int val = Integer.parseInt(textFieldVal.getText());
            if(perudo.isfirstRound() || perudo.isNewBetHigher(num, val)){
                perudo.makeABet(new int[] {num, val});
                showPlayersBet(players.get(0), new int[] {num, val});
                labelWarning.setText("You made a new bet: "+num+ " "+ val+"'s");
                return true;
            } else {
                labelWarning.setText("Invalid Bet.");
                return false;
            }
        } catch (NumberFormatException e){
            labelWarning.setText("Please enter a number.");
            return false;
        }
    }


    public void showPlayersBet(Player player, int[] currentBet){
//        Update the UI with player's bet
        int i = players.indexOf(player);
        playersPanel[i].setText(player.getName() + ": "+ player.getNumberOfDice() + " dice. Bet:"
                + Arrays.toString(currentBet));

    }

    public void showPlayersDice(){
//        Update the UI with what each player had on the current round.
        int playersNum = players.size();
        for (int i = 0; i < playersNum; i++) {
            Player player = players.get(i);
            playersPanel[i].setText(player.getName() + " had: " + Arrays.toString(player.getDiceValues()));
        }
    }

    public void showDiceInHand(){
//        Update the UI with the set of dice the player has.
        textDiceInHand.setText(Arrays.toString(
                players.get(0).getDiceValues()));
    }



}
