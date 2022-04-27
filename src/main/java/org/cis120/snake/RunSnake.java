package org.cis120.snake;

// imports necessary libraries for Java swing

import javax.swing.*;
import java.awt.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class RunSnake implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for
        // local variables.

        // Top-level frame in which game components live.
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("TOP LEVEL FRAME");
        frame.setLocation(400, 50);

        Object[] options = {"OK"};
        String instructionsText = "This game is a play on the original game of snake.\n " +
                "Use the arrow keys to travel around the board but don't hit walls! \n" +
                "You can eat the mushroom block and the star block.\n" +
                "The mushroom block grows your snake's body and the star makes you faster!\n" +
                "You can press space to pause and save your game at any time.\n" +
                "Simply press space again to resume play, or exit to take a break!\n" +
                "When you reopen the game, it will bring up your previous save.\n" +
                "ENJOY!";
        JOptionPane.showOptionDialog(null,
            instructionsText,
            "Game Instructions",
            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
            null, options,options[0]);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Running...");
        status_panel.add(status);

        // Main playing area
        final GameCourt court = new GameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset(true));
        control_panel.add(reset);

/*        //Information Button
        final JButton informationButton = new JButton("Instructions");
        reset.addActionListener(e -> JOptionPane.showOptionDialog(null,
                instructionsText,
                "Game Instructions",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options,options[0]));
        control_panel.add(informationButton);*/

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game
        court.reset(false);
    }
}