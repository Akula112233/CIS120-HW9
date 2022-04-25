package org.cis120.snake;

import java.awt.*;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a circle of a specified color.
 */
public class BodyBlock extends GameObj {
    public static final int SIZE = 20;
    public static int INIT_POS_X;
    public static int INIT_POS_Y;
    public static int INIT_VEL;

    final private Color color;

    public BodyBlock(int courtWidth, int courtHeight, Color color, int xPos, int yPos, Snake snake) {
        super(INIT_VEL, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);

        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}