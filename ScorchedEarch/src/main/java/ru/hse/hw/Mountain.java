package ru.hse.hw;

import java.util.Random;

/**
 * Peace of environment of the game. Every Mountain is section of line along which tank can move.
 */
public class Mountain {

    /**
     * Start position of line section
     */
    private double x1, y1;
    /**
     * End position of line section
     */
    private double y2, x2;

    /**
     * Create mountains from given start and end position
     * @param x1 start position on OX axes
     * @param y1 start position on OY axes
     * @param x2 end position on OX axes
     * @param y2 end position on OY axes
     */
    public Mountain(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * From given x position on mountain line gets it's y position
     * @param x position on OX axes
     * @return position on OY axes
     */
    public double mountainFunction(double x) {
        return (y1 - y2) / (x1 - x2) * x  + (x1 * y2 - x2 * y1) / (x1 - x2);
    }

    /**
     * Check if mountain line contains given x
     * @param x position on OX axes
     * @return
     */
    public boolean isOnMountain(double x) {
        return x < x2 && x >= x1;
    }

    /**
     * Check if mountain line contains point (x, y)
     * @param x position on OX axes
     * @param y position on OY axes
     * @return true if mountain line contains given point, false otherwise
     */
    public boolean contains(double x, double y) {
        return isOnMountain(x) && mountainFunction(x) < y;
    }

    /**
     * Calculate random x coordinate on mountain line
     * @param random randomiser
     * @return required random x coordinate
     */
    public double getRandomOverMountainX(Random random) {
        return x1 + random.nextInt((int)(x2 - x1 - 1));
    }

    /**
     * Calculate random y coordinate of point under the given x on mountain line
     * @param random randomiser
     * @param x given point on mountain line
     * @param max max returned answer
     * @return required random y coordinate
     */
    public double getRandomOverMountainY(Random random, double x, int max) {
        int y = random.nextInt((int)(max - mountainFunction(x)));
        return Math.max(50, mountainFunction(x) - y);
    }

    /**
     * Get the angle of mountain line in radians
     * @return mountain angle
     */
    public double getAngle() {
        return (y2 - y1) /(x2 - x1);
    }
}
