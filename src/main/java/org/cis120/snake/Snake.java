package org.cis120.snake;

import java.awt.*;
import java.util.ArrayList;

public class Snake extends GameObj {
    public static final int SIZE = 10;
    public static final int INIT_POS_X = GameCourt.COURT_WIDTH / 2 - (SIZE / 2);
    public static final int INIT_POS_Y = GameCourt.COURT_HEIGHT / 2 - (SIZE / 2);
    public static final int INIT_VEL = 10;

    private final ArrayList<Pair> snakeBody;
    private int tailXPos;
    private int tailYPos;

    private boolean incrementedSpeed = false;

    private final Color color;

    /**
     * Note that, because we don't need to do anything special when constructing
     * a Square, we simply use the superclass constructor called with the
     * correct parameters.
     */
    public Snake(int courtWidth, int courtHeight, Color color) {
        super(INIT_VEL, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight);
        this.color = color;
        this.tailXPos = INIT_POS_X;
        this.tailYPos = INIT_POS_Y;
        snakeBody = new ArrayList<>();
        incrementBody();
    }

    public Snake(
            int courtWidth, int courtHeight, Color color, int velocity, int initX,
            int initY, ArrayList<Pair> snakeBody, int direction
    ) {
        super(velocity, initX, initY, SIZE, SIZE, courtWidth, courtHeight);
        this.color = color;
        this.tailXPos = snakeBody.get(snakeBody.size() - 1).getxPos();
        this.tailYPos = snakeBody.get(snakeBody.size() - 1).getyPos();
        this.snakeBody = snakeBody;
        switch (direction) {
            case 1: {
                setDirection(Direction.LEFT, true);
                break;
            }
            case 2: {
                setDirection(Direction.UP, true);
                break;
            }
            case 3: {
                setDirection(Direction.RIGHT, true);
                break;
            }
            default: {
                setDirection(Direction.DOWN, true);
                break;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        for (Pair snakePiece : snakeBody) {
            System.out.println(snakePiece.getxPos() + ",  " + snakePiece.getyPos());
            g.fillRect(
                    snakePiece.getxPos(), snakePiece.getyPos(), this.getWidth(), this.getHeight()
            );
        }
    }

    @Override
    public void move(boolean playing) {
        /*
         * Pair tempOldBlockPos1 = new Pair(snakeBody.get(0).getxPos(),
         * snakeBody.get(0).getyPos());;
         * Pair tempOldBlockPos2 = null;
         * if(snakeBody.size() > 1){
         * tempOldBlockPos2 = new Pair(snakeBody.get(1).getxPos(),
         * snakeBody.get(1).getyPos());
         * }
         * for (int i = 1; i < snakeBody.size(); i++) {
         * Pair newPos = tempOldBlockPos1;
         * if(incrementedSpeed){
         * switch (this.getDirection()) {
         * case UP -> newPos = new Pair(newPos.getxPos(), newPos.getyPos() -
         * getVelocity());
         * case DOWN -> newPos = new Pair(newPos.getxPos(), newPos.getyPos() +
         * getVelocity());
         * case LEFT -> newPos = new Pair(newPos.getxPos() - getVelocity(),
         * newPos.getyPos());
         * case RIGHT -> newPos = new Pair(newPos.getxPos() + getVelocity(),
         * newPos.getyPos());
         * default -> {}
         * }
         * }
         * snakeBody.get(i).moveTo(newPos);
         * tempOldBlockPos1 = new Pair(tempOldBlockPos2.getxPos(),
         * tempOldBlockPos2.getyPos());
         * if(i < snakeBody.size() - 1){
         * tempOldBlockPos2 = new Pair(snakeBody.get(i+1).getxPos(),
         * snakeBody.get(i+1).getyPos());
         * }
         * }
         * if(incrementedSpeed){
         * incrementedSpeed = false;
         * }
         */
        if (hitWall() != null || intersectSelf() || !playing) {
            return;
        }
        for (int i = snakeBody.size() - 1; i > 0; i--) {
            snakeBody.get(i).moveTo(snakeBody.get(i - 1));
        }
        setTail(
                snakeBody.get(snakeBody.size() - 1).getxPos(),
                snakeBody.get(snakeBody.size() - 1).getyPos()
        );
        Pair head = snakeBody.get(0);
        Pair newHead;
        switch (this.getDirection()) {
            case UP: {
                newHead = new Pair(head.getxPos(), head.getyPos() - getVelocity());
                break;
            }
            case DOWN: {
                newHead = new Pair(head.getxPos(), head.getyPos() + getVelocity());
                break;
            }
            case LEFT: {
                newHead = new Pair(head.getxPos() - getVelocity(), head.getyPos());
                break;
            }
            case RIGHT: {
                newHead = new Pair(head.getxPos() + getVelocity(), head.getyPos());
                break;
            }
            default: {
                newHead = head;
            }
        }
        head.moveTo(newHead);
        this.setPx(head.getxPos());
        this.setPy(head.getyPos());
    }

    public void incrementBody() {
        int xPos;
        int yPos;
        if (snakeBody.size() <= 1) {
            switch (this.getDirection()) {
                case DOWN: {
                    xPos = tailXPos;
                    yPos = tailYPos - SIZE;
                    break;
                }
                case RIGHT: {
                    xPos = tailXPos - SIZE;
                    yPos = tailYPos;
                    break;
                }
                default: {
                    xPos = tailXPos;
                    yPos = tailYPos;
                }
            }
        } else {
            int secondToLastX = snakeBody.get(snakeBody.size() - 2).getxPos();
            int secondToLastY = snakeBody.get(snakeBody.size() - 2).getyPos();
            if (secondToLastX - tailXPos < 0) { // going left
                xPos = tailXPos;
                yPos = tailYPos;
            } else if (secondToLastX - tailXPos > 0) { // going right
                xPos = tailXPos - SIZE;
                yPos = tailYPos;
            } else if (secondToLastY - tailYPos < 0) { // going up
                xPos = tailXPos;
                yPos = tailYPos;
            } else { // Going down - (secondToLastY - tailYPos > 0)
                xPos = tailXPos;
                yPos = tailYPos - SIZE;
            }
        }

        snakeBody.add(new Pair(xPos, yPos));
    }

    public void setTail(int xPos, int yPos) {
        tailXPos = xPos;
        tailYPos = yPos;
    }

    /*
     * @Override
     * public boolean intersects(EdibleBlock block) {
     * return (getPx() + getWidth() >= block.getPx()
     * && getPy() + getHeight() >= block.getPy()
     * && block.getPx() + block.getWidth() >= getPx()
     * && block.getPy() + block.getHeight() >= getPy());
     * }
     */

    public boolean intersectSelf() {
        for (int i = 3; i < snakeBody.size(); i++) {
            Pair bodyPiece = snakeBody.get(i);
            if (getPx() + getWidth() > bodyPiece.getxPos()
                    && getPy() + getHeight() > bodyPiece.getyPos()
                    && bodyPiece.getxPos() + SIZE > getPx()
                    && bodyPiece.getyPos() + SIZE > getPy()) {
                return true;
            }
        }
        return false;
    }

    public int getBodySize() {
        return snakeBody.size();
    }

    public void increaseSpeed() {
        setVelocity(getVelocity() + 1);
        incrementedSpeed = true;
    }

    @Override
    public String fileSaveInfo() {
        int direction = 0;
        switch (this.getDirection()) {
            case LEFT: {
                direction = 1;
                break;
            }
            case UP: {
                direction = 2;
                break;
            }
            case RIGHT: {
                direction = 3;
                break;
            }
            default: {
                direction = 4;
                break;
            }
        }
        String toReturn = getVelocity() + "\n" + direction + "\n" + snakeBody.size();
        for (Pair p : snakeBody) {
            toReturn += "\n" + p.getxPos() + "," + p.getyPos();
        }
        return toReturn;
    }
}