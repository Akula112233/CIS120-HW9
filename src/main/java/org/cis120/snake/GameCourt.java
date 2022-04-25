package org.cis120.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * GameCourt
 *
 * This class holds the primary game logic for how different objects interact
 * with one another. Take time to understand how the timer interacts with the
 * different methods and how it repaints the GUI on every tick().
 */
public class GameCourt extends JPanel {

    // the state of the game logic
    private Snake snake; // the Black Square, keyboard control
    private Circle snitch; // the Golden Snitch, bounces
    private FoodBlock foodBlock; // the Poison Mushroom, doesn't move
    private SpeedBlock speedBlock;

    private boolean playing = false; // whether the game is running
    private final JLabel status; // Current status text, i.e. "Running..."

    // Game constants
    public static final int COURT_WIDTH = 800;
    public static final int COURT_HEIGHT = 800;
    public static final int SQUARE_VELOCITY = 4;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;

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
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    if(snake.getDirection() != Direction.RIGHT){
                        snake.setDirection(Direction.LEFT);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if(snake.getDirection() != Direction.LEFT){
                        snake.setDirection(Direction.RIGHT);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if(snake.getDirection() != Direction.UP){
                        snake.setDirection(Direction.DOWN);
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if(snake.getDirection() != Direction.DOWN){
                        snake.setDirection(Direction.UP);
                    }
                }
            }
        });

        this.status = status;
    }

    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
        snake = new Snake(COURT_WIDTH, COURT_HEIGHT, Color.BLACK);
        foodBlock = new FoodBlock(COURT_WIDTH, COURT_HEIGHT);
        speedBlock = new SpeedBlock(COURT_WIDTH, COURT_HEIGHT);
        snitch = new Circle(COURT_WIDTH, COURT_HEIGHT, Color.YELLOW);

        playing = true;
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
            snake.move();
//            snitch.move();

            // make the snitch bounce off walls...
            snitch.bounce(snitch.hitWall());
            // ...and the mushroom
//            snitch.bounce(snitch.hitObj(poison));

            // check for the game end conditions
//            if (square.intersects(poison)) {
//                playing = false;
//                status.setText("You lose!");
//            } else
            if(snake.intersects(foodBlock)){
                foodBlock.eatInteraction(snake);
            }

            if(snake.intersects(speedBlock)){
                speedBlock.eatInteraction(snake);
            }

            if(snake.hitWall() != null){
                snake.setVelocity(snake.getVelocity() / 2);
                snake.move();
                playing = false;
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
//        poison.draw(g);
//        snitch.draw(g);
        foodBlock.draw(g);
        speedBlock.draw(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }
}