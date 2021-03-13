package com.example.mypackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame {
//    Reference to perudo game instance and list of players
    private Perudo perudo;
    private ArrayList<Player> players;
    private boolean playerRemoved = false;
//    Information on top
    private JLabel labelNum = new JLabel("Number of dice: ");
    private JTextField textFieldNum = new JTextField();
    private JLabel labelVal = new JLabel("Die Value: ");
    private JTextField textFieldVal = new JTextField();
    private JLabel labelDiceInHand = new JLabel("Dice in hand: ");
    private JLabel labelCurrentBet = new JLabel("Current Bet: ");
    private JLabel textCurrentBet = new JLabel();
    private JLabel labelWarning = new JLabel();
    private JLabel gameInfo = new JLabel();
    private JLabel gameResult = new JLabel();
    private JButton betButton = new JButton("Enter");
    private JButton revealDiceButton = new JButton("Reveal Dice");
    private JButton newRoundButton = new JButton("New Round");
    private JLabel textDiceInHand = new JLabel();

//    Main frame
    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel();
    private JPanel[] playersPanel;
    private JLabel[] playersPanelBetInfo;


    public GUI(Perudo perudo) {
        this.perudo = perudo;
        this.players = perudo.getPlayers();
//      Add 3 buttons to make a new bet, start a new round and reveal dice
        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(newBetAction()){
                    textCurrentBet.setText(Arrays.toString(perudo.getCurrentBet()));
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
                Player looser = perudo.revealDice();
                displayRoundResult(looser);
            }
        });

        // Configure top panel
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
        betPanel.add(labelCurrentBet);
        betPanel.add(textCurrentBet);

        frame.setSize(800,800);

        //        Configure central panel which displays players
        mainPanel.setLayout(new GridLayout(3, 3));
        mainPanel.setBackground(new Color(150,150,150));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));

//        Configure bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        bottomPanel.setLayout(new GridLayout(0, 1));
        bottomPanel.add(gameInfo);
        bottomPanel.add(gameResult);
        // set up the frame and display it
        frame.add(betPanel, BorderLayout.NORTH);
        frame.add(bottomPanel, BorderLayout.SOUTH);
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
        playersPanel = new JPanel[playersNum];
        playersPanelBetInfo = new JLabel[playersNum];
        for (int i = 0; i < playersNum; i++) {
            playersPanel[i] = new JPanel(new BorderLayout());
            playersPanelBetInfo[i] = new JLabel();
            playersPanel[i].add(playersPanelBetInfo[i], BorderLayout.NORTH);
            playersPanel[i].setBackground(new Color(150,150,150));
            mainPanel.add(playersPanel[i]);
        }

    }

    public void startNewRound(){
//        At the beginning of each new round:
//          All players shuffle their dice,
//          The main player will see his combination of dice and how many dice has each player
//          A new round is started, iterating through each robot player.
        gameInfo.setText("New Round Started");
        gameResult.setText("");
        textCurrentBet.setText("");
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
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }

            RobotPlayer robotPlayer = (RobotPlayer) perudo.getCurrentPlayer();
            if (perudo.decideToBet(robotPlayer)){
                perudo.robotBet(robotPlayer);
                int[] currentBet = perudo.getCurrentBet();
                showPlayersBet(robotPlayer, currentBet);
                textCurrentBet.setText(Arrays.toString(currentBet));
                gameInfo.setText(robotPlayer.getName() + " is betting "+ Arrays.toString(currentBet));
                perudo.setCurrentPlayerToNextPlayer();
            }
            else {
                revealPlayersDice();
                int index = perudo.getPlayers().indexOf(robotPlayer);
                playersPanelBetInfo[index].setText(robotPlayer.getName() + ": " + robotPlayer.getNumberOfDice() + " dice - Wants to see the dice");
                gameInfo.setText(robotPlayer.getName() + " wants to see the dice");
                Player looser = perudo.revealDice();
                displayRoundResult(looser);

                return;
            }
        }
        gameInfo.setText("It's your turn");
    }

    public void clearPlayerPanel(int i){
        playersPanel[i].removeAll();
        playersPanel[i].revalidate();
        playersPanel[i].repaint();
    }

    public void resetPlayerDice() {
//        Update the UI with the number of dice per player
        int playersNum = players.size();
        int[] playerDiceValues = players.get(0).getDiceValues();
        if (playerRemoved){
            clearPlayerPanel(players.size());
            playerRemoved = false;
        }
        for (int i = 0; i < playersNum; i++) {
            Player player = players.get(i);
            clearPlayerPanel(i);
            playersPanelBetInfo[i].setText(player.getName() + ": " + player.getNumberOfDice() + " dice");
            playersPanel[i].add(playersPanelBetInfo[i], BorderLayout.NORTH);

            JPanel myPanel = new JPanel();
            myPanel.setBackground(new Color(150,150,150));
            playersPanel[i].add(myPanel, BorderLayout.CENTER);
            for (int j = 0; j < player.getNumberOfDice(); j++) {
                String diceValue;
                if(player instanceof RobotPlayer){
                    diceValue = "pic/diceBlank.png";
                } else {
                    diceValue = "pic/dice"+playerDiceValues[j]+".png";
                }
                myPanel.add(new JLabel(new ImageIcon(diceValue)));
            }
        }

    }

    public void displayRoundResult(Player looser){
//        Update the UI to display the result of the round.
        gameResult.setText("There is a total of: "
                + perudo.getNumOfDiceResult()
                + " "
                + perudo.getCurrentBet()[1] + "'s");

        if (looser instanceof RobotPlayer){
            gameInfo.setText(looser.getName() + " is loosing a die.");
        } else {
            gameInfo.setText("You are loosing a die.");
        }
        //        Check if looser is still in the game
        if (perudo.getPlayers().indexOf(looser) == -1){
            playerRemoved = true;
        }
    }
    public void revealPlayersDice() {
//        Update the UI to display the dice per player
        int playersNum = players.size();
        for (int i = 0; i < playersNum; i++) {
            Player player = players.get(i);
            int[] diceValues = player.getDiceValues();
//
            JPanel myPanel = new JPanel();
            myPanel.setBackground(new Color(150,150,150));
            playersPanel[i].add(myPanel, BorderLayout.CENTER);
            for (int j = 0; j < diceValues.length; j++) {
                String diceValue = "pic/dice"+diceValues[j]+".png";
                myPanel.add(new JLabel(new ImageIcon(diceValue)));
            }

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
        playersPanelBetInfo[i].setText(player.getName() + ": " + player.getNumberOfDice() + " dice"+" - Bet: " + Arrays.toString(currentBet));
    }

    public void showDiceInHand(){
//        Update the UI with the set of dice the player has.
        textDiceInHand.setText(Arrays.toString(
                players.get(0).getDiceValues()));
    }

}
