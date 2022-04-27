package org.cis120.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
public class GameCourt extends JPanel {

    // the state of the game logic
    private Snake snake;
    private EdibleBlock foodBlock;
    private EdibleBlock speedBlock;

    private boolean playing = false; // whether the game is running
    private boolean died = false; // whether the user has died
    private boolean movedOnceAfterChange = false; // var to handle keyboard spam glitches
    private final JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int COURT_WIDTH = 500;
    public static final int COURT_HEIGHT = 500;
    public static final int SQUARE_VELOCITY = 4;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 50;

    public GameCourt(JLabel status) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // The timer is an object which triggers an action periodically with the
        // given INTERVAL. We register an ActionListener with this timer, whose
        // actionPerformed() method is called each time the timer triggers. We
        // define a helper method called tick() that actually does everything
        // that should be done in a single time step.
        Timer timer = new Timer(INTERVAL, e -> tick());
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the square to move as long as an arrow key
        // is pressed, by changing the square's velocity accordingly. (The tick
        // method below actually moves the square.)

        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT  && playing) {
                    if(isVertical() && movedOnceAfterChange){
                        snake.setDirection(Direction.LEFT);
                        movedOnceAfterChange = false;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT  && playing) {
                    if(isVertical() && movedOnceAfterChange){
                        snake.setDirection(Direction.RIGHT);
                        movedOnceAfterChange = false;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN && playing) {
                    if(!isVertical() && movedOnceAfterChange){
                        snake.setDirection(Direction.DOWN);
                        movedOnceAfterChange = false;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_UP && playing) {
                    if(!isVertical() && movedOnceAfterChange){
                        snake.setDirection(Direction.UP);
                        movedOnceAfterChange = false;
                    }
                } else if(e.getKeyCode() == KeyEvent.VK_SPACE){
                    if(playing){
                        saveCloseGame();
                    }
                    else if (!died){
                        playing = true;
                        tick();
                    }
                }
            }
        });

        this.status = status;
    }

    private boolean isVertical() {
        return (snake.getDirection() != Direction.RIGHT
                && snake.getDirection() != Direction.LEFT);
    }

    private void saveCloseGame() {
        try {
            FileWriter fileWriter = new FileWriter("snake-save.bin");
            fileWriter.write(foodBlock.fileSaveInfo() + "\n");
            fileWriter.write(speedBlock.fileSaveInfo() + "\n");
            fileWriter.write(snake.fileSaveInfo());
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Unable to Save Game!");
        }
        playing = false;
        status.setText("Game Saved, Press Space to Resume or Exit the Game!");
    }

    public void reset(boolean buttonPress) {
        if(buttonPress){
            noSavedGame();
            return;
        }
        try {
            FileInputStream fis = new FileInputStream("snake-save.bin");
            Scanner scanner = new Scanner(fis);
            //FoodBlock
            String[] scannedLine = scanner.nextLine().split(",");
            int xPos = Integer.parseInt(scannedLine[0]);
            int yPos = Integer.parseInt(scannedLine[1]);
            foodBlock = new FoodBlock(COURT_WIDTH, COURT_HEIGHT, xPos, yPos);

            //SpeedBlock
            scannedLine = scanner.nextLine().split(",");
            xPos = Integer.parseInt(scannedLine[0]);
            yPos = Integer.parseInt(scannedLine[1]);
            speedBlock = new SpeedBlock(COURT_WIDTH, COURT_HEIGHT, xPos, yPos);

            //Snake - velocity, direction, size, all snake body coords
            int velocity = scanner.nextInt();
            scanner.nextLine();
            int direction = scanner.nextInt();
            scanner.nextLine();
            int snakeSize = scanner.nextInt();
            scanner.nextLine();
            int initX = 0;
            int initY = 0;
            ArrayList<Pair> snakeBody = new ArrayList<>();
            for (int i = 0; i < snakeSize; i++) {
                scannedLine = scanner.nextLine().split(",");
                xPos = Integer.parseInt(scannedLine[0]);
                yPos = Integer.parseInt(scannedLine[1]);
                if(i == 0){
                    initX = xPos;
                    initY = yPos;
                }
                if(xPos < 0 || xPos > COURT_WIDTH || yPos < 0 || yPos > COURT_HEIGHT){
                    noSavedGame();
                    return;
                }
                snakeBody.add(new Pair(xPos, yPos));
            }
            snake = new Snake(COURT_WIDTH, COURT_HEIGHT, Color.black, velocity, initX,
                    initY, snakeBody, direction);
            loadSavedGame();
        } catch (Exception e) {
            status.setText("Unable to load saved game, loading default...");
            noSavedGame();
        }
    }
    private void loadSavedGame() {
        playing = true;
        died = false;
        status.setText("Running...");
        requestFocusInWindow();
    }

    /**
     * (Re-)set the game to its initial state.
     */
    private void noSavedGame() {
        snake = new Snake(COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
        foodBlock = new FoodBlock(COURT_WIDTH, COURT_HEIGHT);
        speedBlock = new SpeedBlock(COURT_WIDTH, COURT_HEIGHT);

        playing = true;
        died = false;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor
     * triggers.
     */
    void tick() {
        if (playing) {
            // advance the square and snitch in their current direction.
            status.setText("Length: " + snake.getBodySize() + "   Speed: " + snake.getVelocity());
            snake.move();
            if(!movedOnceAfterChange){
                movedOnceAfterChange = true;
            }
            if(snake.intersects(speedBlock)){
                speedBlock.eatInteraction(snake);
                status.setText("Length: " + snake.getBodySize() + "   Speed: " + snake.getVelocity());
            } else if(snake.intersects(foodBlock)){
                foodBlock.eatInteraction(snake);
                status.setText("Length: " + snake.getBodySize() + "   Speed: " + snake.getVelocity());
            } else if(snake.intersectSelf()){
                playing = false;
                died = true;
                status.setText("You ate yourself! You lose.");
            } else if(snake.hitWall() != null){
                playing = false;
                died = true;
                status.setText("You lose!");
            }
            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        snake.draw(g);
        foodBlock.draw(g);
        speedBlock.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}