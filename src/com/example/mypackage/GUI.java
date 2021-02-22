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
        // the clickable button

        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(newBetAction()){
                    perudo.setCurrentPlayerToNextPlayer();
                    goToNextPlayer();
                };
            }
        });

        newRoundButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startNewRound();
            }
        });
        revealDiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                revealPlayersDice();
                perudo.revealDice();
            }
        });

        // the panel with the button and text
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
//        frame.pack();
        frame.setVisible(true);


    }

    public void setPlayerFrame() {
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
    public void resetPlayerDice() {
        int playersNum = players.size();
        for (int i = 0; i < playersNum; i++) {
            Player player = players.get(i);
            playersPanel[i].setText(player.getName() + ": " + player.getNumberOfDice() + " dice\n");
        }

    }
    public void revealPlayersDice() {
        int playersNum = players.size();
        for (int i = 0; i < playersNum; i++) {
            Player player = players.get(i);
            playersPanel[i].setText(player.getName() + ": " + Arrays.toString(player.getDiceValues()) );
        }
    }

    public boolean newBetAction(){
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
        int i = players.indexOf(player);
        playersPanel[i].setText(player.getName() + ": "+ player.getNumberOfDice() + " dice. Bet:"
                + Arrays.toString(currentBet));

    }

    public void showPlayersDice(){
        int playersNum = players.size();
        for (int i = 0; i < playersNum; i++) {
            Player player = players.get(i);
            playersPanel[i].setText(player.getName() + " had: " + Arrays.toString(player.getDiceValues()));
        }
    }

    public void showDiceInHand(){
        textDiceInHand.setText(Arrays.toString(
                players.get(0).getDiceValues()));
    }
    public void startNewRound(){
        perudo.shuffleDice();
        showDiceInHand();
        resetPlayerDice();
        if (perudo.getCurrentPlayer() instanceof RobotPlayer) robotBet();

    }
    public void robotBet(){
        RobotPlayer robotPlayer = (RobotPlayer) perudo.getCurrentPlayer();
        int[] newBet = perudo.isfirstRound()
            ? robotPlayer.makeABet(perudo.getNumberOfDice())
            : robotPlayer.makeABet(perudo.getCurrentBet());

        perudo.makeABet(newBet);
//            Update UI with Bet Value
        showPlayersBet(robotPlayer, newBet);

    }
    public void goToNextPlayer() {
        while (perudo.getCurrentPlayer() instanceof RobotPlayer){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            RobotPlayer robotPlayer = (RobotPlayer) perudo.getCurrentPlayer();
            if(robotPlayer.decideToBet(perudo.getCurrentBet(),perudo.getNumberOfDice())){
                robotBet();
                perudo.setCurrentPlayerToNextPlayer();
            } else {
                showPlayersDice();
                perudo.revealDice();
            }
        }
    }
}
