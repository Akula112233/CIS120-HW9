package org.cis120.snake;

/**
 * A basic game object starting in the upper left corner of the game court. It
 * is displayed as a square of a specified color.
 */

public class Pair {
    private int xPos;
    private int yPos;

    public Pair(int x, int y) {
        xPos = x;
        yPos = y;
    }

    public void moveTo(Pair newPair) {
        xPos = newPair.getxPos();
        yPos = newPair.getyPos();
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }
}
