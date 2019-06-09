package ru.hse.hw;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Peace of environment of the game. Every Mountain is section of line along which tank can move.
 */
public class Mountain {

    /**
     * Start x position of line section.
     */
    private final double x1;

    /**
     * Start y position of line section.
     */
    private final double y1;

    /**
     * End x position of line section.
     */
    private final double x2;

    /**
     * End x position of line section.
     */
    private final double y2;

    /**
     * Coefficient between x in equation ax+by+c=0.
     */
    private final double a;

    /**
     * Coefficient between y in equation ax+by+c=0.
     */
    private final double b;

    /**
     * Free coefficient in equation ax+by+c=0.
     */
    private final double c;

    /**
     * Create mountains from given start and end position.
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
        a = (y1 - y2);
        b = (x1 - x2);
        c = (x1 * y2 - x2 * y1);
    }

    /**
     * From given x position on mountain line gets it's y position.
     * @param x position on OX axes
     * @return position on OY axes
     */
    public double mountainFunction(double x) {
        return a / b * x  + c / b;
    }

    /**
     * Check if mountain line contains given x.
     * @param x position on OX axes
     */
    public boolean isOnMountain(double x) {
        return x < x2 && x >= x1;
    }

    /**
     * Check if bullet is under the mountain line.
     * @param x bullet position on OX axes
     * @param y bullet position on OY axes
     * @param r bullet radius
     * @return true if bullet is under the mountain line, false otherwise
     */
    public boolean contains(double x, double y, double r) {
        return isOnMountain(x) && (mountainFunction(x) < y || intersects(x, y, r));
    }

    /**
     * Check if bullet touch mountain.
     * @param x bullet position on OX axes
     * @param y bullet position on OY axes
     * @param r bullet radius
     * @return true if bullet touch mountain, false otherwise
     */
    private boolean intersects(double x, double y, double r) {
        double d = (Math.abs(a * x + b * y + c)) / Math.sqrt(a * a + b * b);
        return (r >= d);
    }

    /**
     * Calculate random x coordinate on mountain line.
     * @param random randomiser
     * @return required random x coordinate
     */
    public double getRandomOverMountainX(@NotNull Random random) {
        return x1 + random.nextInt((int)(x2 - x1 - 1));
    }

    /**
     * Calculate random y coordinate of point under the given x on mountain line.
     * @param random randomiser
     * @param x given point on mountain line
     * @param max max returned answer
     * @return required random y coordinate
     */
    public double getRandomOverMountainY(@NotNull Random random, double x, int max) {
        int y = random.nextInt((int)(max - mountainFunction(x)));
        return Math.max(50, mountainFunction(x) - y);
    }

    /**
     * Get the angle of mountain line in radians.
     * @return mountain angle
     */
    public double getAngle() {
        return (y2 - y1) /(x2 - x1);
    }
}
