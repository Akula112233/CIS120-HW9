package org.cis120.snake;

import java.awt.*;

/**
 * An object in the game.
 *
 * Game objects exist in the game court. They have a position, velocity, size
 * and bounds. Their velocity controls how they move; their position should
 * always be within their bounds.
 */
public abstract class GameObj {
    /*
     * Current position of the object (in terms of graphics coordinates)
     *
     * Coordinates are given by the upper-left hand corner of the object. This
     * position should always be within bounds:
     * 0 <= px <= maxX 0 <= py <= maxY
     */
    private int px;
    private int py;

    /* Size of object, in pixels. */
    private int width;
    private int height;

    /* Velocity: number of pixels to move every time move() is called. */
    private int velocity;

    private Direction direction = Direction.UP;

    /*
     * Upper bounds of the area in which the object can be positioned. Maximum
     * permissible x, y positions for the upper-left hand corner of the object.
     */
    private final int maxX;
    private final int maxY;

    /**
     * Constructor
     */
    public GameObj(
            int velocity, int px, int py, int width, int height, int courtWidth,
            int courtHeight
    ) {
        this.velocity = velocity;
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;

        // take the width and height into account when setting the bounds for
        // the upper left corner of the object.
        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;
    }

    // **********************************************************************************
    // * GETTERS
    // **********************************************************************************
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }

    public int getVelocity() {
        return this.velocity;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Direction getDirection() {
        return this.direction;
    }

    // **************************************************************************
    // * SETTERS
    // **************************************************************************
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    public void setPy(int py) {
        this.py = py;
        clip();
    }

    public void setDirection(Direction d, boolean playing) {
        if (!playing) {
            return;
        }
        this.direction = d;
    }

    public void setVelocity(int newVelocity) {
        this.velocity = newVelocity;
    }

    public void changeSize(int newSize) {
        this.width = newSize;
        this.height = newSize;
    }

    // **************************************************************************
    // * UPDATES AND OTHER METHODS
    // **************************************************************************

    /**
     * Prevents the object from going outside the bounds of the area
     * designated for the object (i.e. Object cannot go outside the active
     * area the user defines for it).
     */
    private void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }

    /**
     * Moves the object by its velocity. Ensures that the object does not go
     * outside its bounds by clipping.
     */
    public void move(boolean playing) {
        if (!playing) {
            return;
        }
        System.out.println(this.direction);
        switch (this.direction) {
            case UP -> this.py -= this.velocity;
            case DOWN -> this.py += this.velocity;
            case LEFT -> this.px -= this.velocity;
            case RIGHT -> this.px += this.velocity;
            default -> {
            }
        }
    }

    /**
     * Determine whether this game object is currently intersecting another
     * object.
     *
     * Intersection is determined by comparing bounding boxes. If the bounding
     * boxes overlap, then an intersection is considered to occur.
     *
     * @param that The other object
     * @return Whether this object intersects the other object.
     */
    public boolean intersects(GameObj that) {
        return (this.px + this.width >= that.px
                && this.py + this.height >= that.py
                && that.px + that.width >= this.px
                && that.py + that.height >= this.py);
    }

    /**
     * Determine whether this game object will intersect another in the next
     * time step, assuming that both objects continue with their current
     * velocity.
     *
     * Intersection is determined by comparing bounding boxes. If the bounding
     * boxes (for the next time step) overlap, then an intersection is
     * considered to occur.
     *
     * @param that The other object
     * @return Whether an intersection will occur.
     */
    public boolean willIntersect(GameObj that) {
        int thisNextX = this.px;
        int thisNextY = this.py;
        int thatNextX = that.px;
        int thatNextY = that.py;

        switch (this.direction) {
            case UP -> thisNextY -= this.velocity;
            case DOWN -> thisNextY += this.velocity;
            case LEFT -> thisNextX -= this.velocity;
            case RIGHT -> thisNextX += this.velocity;
        }

        switch (this.direction) {
            case UP -> thatNextY -= this.velocity;
            case DOWN -> thatNextY += this.velocity;
            case LEFT -> thatNextX -= this.velocity;
            case RIGHT -> thatNextX += this.velocity;
        }

        return (thisNextX + this.width >= thatNextX
                && thisNextY + this.height >= thatNextY
                && thatNextX + that.width >= thisNextX
                && thatNextY + that.height >= thisNextY);
    }

    /**
     * Update the velocity of the object in response to hitting an obstacle in
     * the given direction. If the direction is null, this method has no effect
     * on the object.
     *
     * @param d The direction in which this object hit an obstacle
     */
    public void bounce(Direction d) {
        direction = d;
    }

    /**
     * Determine whether the game object will hit a wall in the next time step.
     * If so, return the direction of the wall in relation to this game object.
     *
     * @return Direction of impending wall, null if all clear.
     */
    public Direction hitWall() {
        if (this.direction == Direction.LEFT && this.px - this.velocity < 0) {
            return Direction.LEFT;
        } else if (this.direction == Direction.RIGHT && this.px + this.velocity > this.maxX) {
            return Direction.RIGHT;
        }

        if (this.direction == Direction.UP && this.py - this.velocity < 0) {
            return Direction.UP;
        } else if (this.direction == Direction.DOWN && this.py + this.velocity > this.maxY) {
            return Direction.DOWN;
        }

        return null;
    }

    /**
     * Determine whether the game object will hit another object in the next
     * time step. If so, return the direction of the other object in relation to
     * this game object.
     *
     * @param that The other object
     * @return Direction of impending object, null if all clear.
     */
    public Direction hitObj(GameObj that) {
        if (this.willIntersect(that)) {
            double halfThisWidth = (double) this.width / 2;
            double halfThatWidth = (double) that.width / 2;
            double halfThisHeight = (double) this.height / 2;
            double halfThatHeight = (double) that.height / 2;
            double dx = that.px + halfThatWidth - (this.px + halfThisWidth);
            double dy = that.py + halfThatHeight - (this.py + halfThisHeight);

            double theta = Math.acos(dx / (Math.sqrt(dx * dx + dy * dy)));
            double diagTheta = Math.atan2(halfThisWidth, halfThisWidth);

            if (theta <= diagTheta) {
                return Direction.RIGHT;
            } else if (theta <= Math.PI - diagTheta) {
                // Coordinate system for GUIs is switched
                if (dy > 0) {
                    return Direction.DOWN;
                } else {
                    return Direction.UP;
                }
            } else {
                return Direction.LEFT;
            }
        } else {
            return null;
        }
    }

    /**
     * Default draw method that provides how the object should be drawn in the
     * GUI. This method does not draw anything. Subclass should override this
     * method based on how their object should appear.
     *
     * @param g The <code>Graphics</code> context used for drawing the object.
     *          Remember graphics contexts that we used in OCaml, it gives the
     *          context in which the object should be drawn (a canvas, a frame,
     *          etc.)
     */
    public abstract void draw(Graphics g);

    public abstract String fileSaveInfo();
}