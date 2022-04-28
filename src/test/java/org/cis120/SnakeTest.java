package org.cis120;

import org.cis120.snake.*;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class SnakeTest {
    int courtWidth = 100;
    int courtHeight = 100;
    boolean playing = true;
    boolean died = false;

    private void testMoveHelper(Snake snake, EdibleBlock block1, EdibleBlock block2) {
        if (playing) {
            // advance the square and snitch in their current direction.
            snake.move(playing);
            if (block1 != null && snake.intersects(block1)) {
                block1.eatInteraction(snake, true);
            }
            if (block2 != null && snake.intersects(block2)) {
                block2.eatInteraction(snake, true);
            }
            if (snake.intersectSelf()) {
                playing = false;
                died = true;
            }
            if (snake.hitWall() != null) {
                playing = false;
                died = true;
            }
        }
    }

    // test eating, speed***

    @Test
    public void testEatFood() {
        ArrayList<Pair> snakeBody = new ArrayList<>();
        snakeBody.add(new Pair(2, 0));
        Snake snake = new Snake(
                courtWidth, courtHeight, Color.BLACK, 1, 0, 0,
                snakeBody, 4
        );
        EdibleBlock foodBlock = new FoodBlock(courtWidth, courtHeight, 0, 11);
        testMoveHelper(snake, null, foodBlock);
        assertEquals(2, snake.getBodySize());
    }

    @Test
    public void testSpeedIncreaseAndMove() {
        ArrayList<Pair> snakeBody = new ArrayList<>();
        snakeBody.add(new Pair(2, 0));
        Snake snake = new Snake(
                courtWidth, courtHeight, Color.BLACK, 1, 0, 0,
                snakeBody, 4
        );
        EdibleBlock block = new SpeedBlock(courtWidth, courtHeight, 0, 12);
        testMoveHelper(snake, block, null);
        assertEquals(1, snake.getVelocity());
        testMoveHelper(snake, block, null);
        assertEquals(2, snake.getVelocity());
        testMoveHelper(snake, block, null);
        assertEquals(4, snake.getPy());
    }

    @Test
    public void testEatTwice() {
        ArrayList<Pair> snakeBody = new ArrayList<>();
        snakeBody.add(new Pair(2, 0));
        Snake snake = new Snake(
                courtWidth, courtHeight, Color.BLACK, 1, 0, 0,
                snakeBody, 4
        );
        EdibleBlock foodBlock = new FoodBlock(courtWidth, courtHeight, 0, 11);
        EdibleBlock foodBlock2 = new FoodBlock(courtWidth, courtHeight, 0, 12);
        testMoveHelper(snake, foodBlock, foodBlock2);
        assertEquals(2, snake.getBodySize());
        testMoveHelper(snake, foodBlock, foodBlock2);
        assertEquals(3, snake.getBodySize());
    }

    @Test
    public void testSpeedTwice() {
        ArrayList<Pair> snakeBody = new ArrayList<>();
        snakeBody.add(new Pair(2, 0));
        Snake snake = new Snake(
                courtWidth, courtHeight, Color.BLACK, 1, 0, 0,
                snakeBody, 4
        );
        EdibleBlock block = new SpeedBlock(courtWidth, courtHeight, 0, 12);
        EdibleBlock block2 = new SpeedBlock(courtWidth, courtHeight, 0, 15);
        testMoveHelper(snake, block, block2);
        assertEquals(1, snake.getVelocity());
        testMoveHelper(snake, block, block2);
        assertEquals(2, snake.getVelocity());
        testMoveHelper(snake, block, block2);
        assertEquals(2, snake.getVelocity());
        assertEquals(4, snake.getPy());
        testMoveHelper(snake, block, block2);
        assertEquals(6, snake.getPy());
        assertEquals(3, snake.getVelocity());
    }

    @Test
    public void testEatThenSpeed() {
        ArrayList<Pair> snakeBody = new ArrayList<>();
        snakeBody.add(new Pair(0, 0));
        Snake snake = new Snake(
                courtWidth, courtHeight, Color.BLACK, 1, 0, 0,
                snakeBody, 4
        );
        EdibleBlock block = new FoodBlock(courtWidth, courtHeight, 0, 12);
        EdibleBlock block2 = new SpeedBlock(courtWidth, courtHeight, 0, 13);
        testMoveHelper(snake, block, block2);
        assertEquals(1, snake.getVelocity());
        assertEquals(1, snake.getBodySize());
        assertEquals(1, snake.getPy());
        testMoveHelper(snake, block, block2);
        assertEquals(1, snake.getVelocity());
        assertEquals(2, snake.getBodySize());
        assertEquals(2, snake.getPy());
        testMoveHelper(snake, block, block2);
        assertEquals(2, snake.getVelocity());
        assertEquals(2, snake.getBodySize());
        assertEquals(3, snake.getPy());
        testMoveHelper(snake, block, block2);
        assertEquals(2, snake.getVelocity());
        assertEquals(2, snake.getBodySize());
        assertEquals(5, snake.getPy());
    }

    // test wall bumping***
    @Test
    public void testWallBump() {
        ArrayList<Pair> snakeBody = new ArrayList<>();
        snakeBody.add(new Pair(89, 0));
        Snake snake = new Snake(
                courtWidth, courtHeight, Color.BLACK, 1, 89, 0,
                snakeBody, 3
        );
        assertNull(snake.hitWall());
        testMoveHelper(snake, null, null);
        assertNotNull(snake.hitWall());
    }

    // test self bumping
    @Test
    public void testSelfBump() {
        ArrayList<Pair> snakeBody = new ArrayList<>();
        snakeBody.add(new Pair(80, 50));
        snakeBody.add(new Pair(80, 40));
        snakeBody.add(new Pair(80, 30));
        snakeBody.add(new Pair(70, 30));
        snakeBody.add(new Pair(60, 30));
        snakeBody.add(new Pair(50, 30));
        snakeBody.add(new Pair(40, 30));
        snakeBody.add(new Pair(30, 30));
        snakeBody.add(new Pair(20, 30));
        snakeBody.add(new Pair(10, 30));

        Snake snake = new Snake(
                200, 200, Color.BLACK, 10, 60, 50,
                snakeBody, 1
        );
        testMoveHelper(snake, null, null);
        assertFalse(snake.intersectSelf());
        testMoveHelper(snake, null, null);
        assertFalse(snake.intersectSelf());
        snake.setDirection(Direction.UP, playing);
        testMoveHelper(snake, null, null);
        assertFalse(snake.intersectSelf());
        testMoveHelper(snake, null, null);
        assertTrue(snake.intersectSelf());
    }

    // Some edge cases***
    // If bumping into tail of snake
    @Test
    public void testSelfBumpTail() {
        ArrayList<Pair> snakeBody = new ArrayList<>();
        snakeBody.add(new Pair(80, 50));
        snakeBody.add(new Pair(80, 40));
        snakeBody.add(new Pair(80, 30));
        snakeBody.add(new Pair(70, 30));
        snakeBody.add(new Pair(60, 30));
        snakeBody.add(new Pair(50, 30));
        snakeBody.add(new Pair(40, 30));
        snakeBody.add(new Pair(30, 30));
        snakeBody.add(new Pair(20, 30));

        Snake snake = new Snake(
                200, 200, Color.BLACK, 10, 60, 50,
                snakeBody, 1
        );
        testMoveHelper(snake, null, null);
        assertFalse(snake.intersectSelf());
        testMoveHelper(snake, null, null);
        assertFalse(snake.intersectSelf());
        snake.setDirection(Direction.UP, playing);
        testMoveHelper(snake, null, null);
        assertFalse(snake.intersectSelf());
        testMoveHelper(snake, null, null);
        assertTrue(snake.intersectSelf());
    }

    // If moving after hitting wall
    @Test
    public void testWallBumpThenMove() {
        ArrayList<Pair> snakeBody = new ArrayList<>();
        snakeBody.add(new Pair(89, 0));
        Snake snake = new Snake(
                courtWidth, courtHeight, Color.BLACK, 1, 89, 0,
                snakeBody, 3
        );
        assertNull(snake.hitWall());
        testMoveHelper(snake, null, null);
        assertEquals(90, snake.getPx());
        assertNotNull(snake.hitWall());
        testMoveHelper(snake, null, null);
        assertEquals(90, snake.getPx());
    }
    // If changing direction and moving after hitting wall
    @Test
    public void testWallBumpChangeDirectionThenMove() {
        ArrayList<Pair> snakeBody = new ArrayList<>();
        snakeBody.add(new Pair(89, 0));
        Snake snake = new Snake(
                courtWidth, courtHeight, Color.BLACK, 1, 89, 0,
                snakeBody, 3
        );
        assertNull(snake.hitWall());
        testMoveHelper(snake, null, null);
        assertEquals(90, snake.getPx());
        assertNotNull(snake.hitWall());
        snake.setDirection(Direction.DOWN, snake.hitWall() == null);
        assertEquals(Direction.RIGHT, snake.getDirection());
        testMoveHelper(snake, null, null);
        assertEquals(90, snake.getPx());
    }

    // If eating overlapped food and speed block
    @Test
    public void testEatSpeedOverlap() {
        ArrayList<Pair> snakeBody = new ArrayList<>();
        snakeBody.add(new Pair(0, 0));
        Snake snake = new Snake(
                courtWidth, courtHeight, Color.BLACK, 1, 0, 0,
                snakeBody, 4
        );
        EdibleBlock block = new FoodBlock(courtWidth, courtHeight, 0, 12);
        EdibleBlock block2 = new SpeedBlock(courtWidth, courtHeight, 0, 12);
        testMoveHelper(snake, block, block2);
        assertEquals(1, snake.getVelocity());
        assertEquals(1, snake.getBodySize());
        assertEquals(1, snake.getPy());
        testMoveHelper(snake, block, block2);
        assertEquals(2, snake.getVelocity());
        assertEquals(2, snake.getBodySize());
        assertEquals(2, snake.getPy());
        testMoveHelper(snake, block, block2);
        assertEquals(2, snake.getVelocity());
        assertEquals(2, snake.getBodySize());
        assertEquals(4, snake.getPy());
    }
}
