package com.example.mypackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GUI implements ActionListener {
    private JLabel labelNum = new JLabel("Number of dice: ");
    private JTextField textFieldNum = new JTextField();
    private JLabel labelVal = new JLabel("Die Value: ");
    private JTextField textFieldVal = new JTextField();
    private JFrame frame = new JFrame();
    private JPanel mainPanel = new JPanel();
    private JLabel[] playersPanel;
    private Perudo perudo;

    public GUI(Perudo perudo) {
        this.perudo = perudo;
        // the clickable button
        JButton button = new JButton("Enter");
        button.addActionListener(this);

        // the panel with the button and text
        JPanel betPanel = new JPanel();
        betPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        betPanel.setLayout(new GridLayout(0, 5));
        betPanel.add(labelNum);
        betPanel.add(textFieldNum);
        betPanel.add(labelVal);
        betPanel.add(textFieldVal);
        betPanel.add(button);
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

    public void setPlayerFrame(ArrayList<Player> players) {
        int playersNum = players.size();

        playersPanel = new JLabel[playersNum];
        for (int i = 0; i < playersNum; i++) {
            playersPanel[i] = new JLabel();
            Player player = players.get(i);
            if (player instanceof RobotPlayer) {
                playersPanel[i].setText(player.getName() + ": " + player.getNumberOfDice() + " dice\n");
            } else {
                playersPanel[i].setText(player.getName() + ": "
                        + Arrays.toString(player.getDiceValues())
                );
            }


            mainPanel.add(playersPanel[i]);
            playersPanel[i].setFont(new Font("MV Boli", Font.BOLD, 15));
        }
    }

    // process the button clicks
    public void actionPerformed(ActionEvent e) {
        int num = Integer.parseInt(textFieldNum.getText()) ;
        int val = Integer.parseInt(textFieldVal.getText());
        perudo.makeABet(new int[] {num, val});
        System.out.println("num: "+ num + " val: "+ val);
//        labelNum.setText("Number of clicks:  " + clicks);
    }


    public void updateUI(Player player, int[] currentBet, ArrayList<Player> players){
        int i = players.indexOf(player);
        if (player instanceof RobotPlayer){
            playersPanel[i].setText(player.getName() + ": "+ player.getNumberOfDice() + " dice. Bet:"
                    + Arrays.toString(currentBet));
        } else {
            playersPanel[i].setText(player.getName() + ": "
                    + Arrays.toString(player.getDiceValues())
                    + " Bet: "
                    + Arrays.toString(currentBet));
        }
    }
}


