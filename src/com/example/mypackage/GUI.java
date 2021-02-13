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


    public GUI(Perudo perudo) {
        this.perudo = perudo;
        this.players = perudo.getPlayers();
        // the clickable button

        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newBetAction();
                goToNextPlayer();
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
        revealDiceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                revealPlayersDice();
//                perudo.revealDice();
            }
        });
    }

    public void revealPlayersDice() {
        int playersNum = players.size();
        for (int i = 0; i < playersNum; i++) {
            Player player = players.get(i);
            playersPanel[i].setText(player.getName() + ": " + Arrays.toString(player.getDiceValues()) );
        }
    }

    public void newBetAction(){
        try {
            int num = Integer.parseInt(textFieldNum.getText()) ;
            int val = Integer.parseInt(textFieldVal.getText());
            if(perudo.isNewBetHigher(num, val)){
                labelWarning.setText("You made a new bet: "+num+ " "+ val+"'s");
                perudo.makeABet(new int[] {num, val});
                showPlayersBet(players.get(0), new int[] {num, val});
            } else {
                labelWarning.setText("Invalid Bet.");
            };
        } catch (NumberFormatException e){
            labelWarning.setText("Please enter a number.");
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
            playersPanel[i] = new JLabel();
            Player player = players.get(i);
            playersPanel[i].setText(player.getName() + " had: " + Arrays.toString(player.getDiceValues()));
        }
    }

    public void showDiceInHand(){
        textDiceInHand.setText(Arrays.toString(
                players.get(0).getDiceValues()));
    }

    public void goToNextPlayer() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        perudo.nextPlayer();
        if (perudo.getCurrentPlayer() instanceof RobotPlayer){
            RobotPlayer robotPlayer = (RobotPlayer) perudo.getCurrentPlayer();
            if(robotPlayer.decideToBet(perudo.getCurrentBet(),perudo.getNumberOfDice())){
                int[] newBet = robotPlayer.makeABet(perudo.getCurrentBet());
                perudo.makeABet(newBet);
                showPlayersBet(robotPlayer, newBet);
            } else {
                perudo.revealDice();
                showPlayersDice();
            }

        }
    }
}
