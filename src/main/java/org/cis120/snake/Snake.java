package org.cis120.snake;

import java.awt.*;
import java.util.ArrayList;

public class Snake extends GameObj {
    public static final int SIZE = 10;
    public static final int INIT_POS_X = GameCourt.COURT_WIDTH/2-(SIZE / 2);
    public static final int INIT_POS_Y = GameCourt.COURT_HEIGHT/2-(SIZE / 2);
    public static final int INIT_VEL = 10;

    private final ArrayList<Pair> snakeBody;
    private int tailXPos;
    private int tailYPos;

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

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        for (Pair snakePiece: snakeBody) {
            System.out.println(snakePiece.getxPos() + ",  " + snakePiece.getyPos());
            g.fillRect(snakePiece.getxPos(), snakePiece.getyPos(), this.getWidth(), this.getHeight());
        }
    }

    @Override
    public void move () {
        for (int i = snakeBody.size()-1; i > 0; i--) {
            snakeBody.get(i).moveTo(snakeBody.get(i-1));
        }
        setTail(snakeBody.get(snakeBody.size()-1).getxPos(),snakeBody.get(snakeBody.size()-1).getyPos());
        Pair head = snakeBody.get(0);
        Pair newHead;
        switch (this.getDirection()) {
            case UP -> newHead = new Pair(head.getxPos(), head.getyPos() - getVelocity());
            case DOWN -> newHead = new Pair(head.getxPos(), head.getyPos() + getVelocity());
            case LEFT -> newHead = new Pair(head.getxPos() - getVelocity(), head.getyPos());
            case RIGHT -> newHead = new Pair(head.getxPos() + getVelocity(), head.getyPos());
            default -> {
                newHead = head;
            }
        }
        head.moveTo(newHead);
        this.setPx(head.getxPos());
        this.setPy(head.getyPos());
    }

    public void incrementBody() {
//        int xPos;
//        int yPos;
//        int secondToLastX = snakeBody.get(snakeBody.size()-2).getxPos();
//        int secondToLastY = snakeBody.get(snakeBody.size()-2).getyPos();
//        if(secondToLastX - tailXPos < 0){
//            xPos =
//        }
        snakeBody.add(new Pair(tailXPos, tailYPos));
        System.out.print("EHRLEJRLEJT");
    }

    public void setTail(int xPos, int yPos){
        tailXPos = xPos;
        tailYPos = yPos;
    }

    public void increaseSpeed() {
        if(getVelocity() > 0){
            setVelocity(getVelocity()+1);
        }else if(getVelocity() < 0){
            setVelocity(getVelocity()+1);
        }
    }
}