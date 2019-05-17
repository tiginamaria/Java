package ru.hse.hw;

import javafx.geometry.Side;
import java.util.List;

import static javafx.geometry.Side.*;

/**
 * Main object of the game. It can move to left and right along the mountains and move it's barrel
 */
public class Tank {

    /**
     * Step in pixels to ones move tank
     */
    private final static double STEP = 0.8;

    /**
     * Angle in degree to ones move barrel
     */
    private final static int BARREL_ROTATE_ANGLE = 1;

    /**
     * Tank position
     */
    private double x, y;

    /**
     * Barrel angle in degree [-90, 90]
     */
    private double barrelAngle = 0;

    /**
     * Tank angle in radians
     */
    private double tankAngle;

    /**
     * Create tank on given position
     * @param x position on OX axes
     * @param y position on OY axes
     */
    public Tank(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates new angle for barrel after move on given angle( if new angle is out of [-90, 90] leave old angle)
     * @param side side to move barrel
     */
    public void moveBarrel(Side side) {
        if ((side == BOTTOM && barrelAngle > -90.0) || (side == TOP && barrelAngle < 90.0)) {
            var angle = (side == TOP) ? BARREL_ROTATE_ANGLE : -BARREL_ROTATE_ANGLE;
            barrelAngle += angle;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getBarrelAngle() {
        return barrelAngle;
    }

    public double getTankAngle() {
        return tankAngle;
    }

    /**
     * Calculates new tank position after move along the mountains(given list of "lines")
     * @param mountains list of lines to move along
     * @param side side to move tank
     */
    public void move(List<Mountain> mountains, Side side) {
        double newX = x + (side == RIGHT ? STEP : -STEP);
        for (var mountain : mountains) {
            if (mountain.isOnMountain(newX)) {
                x = newX;
                y = mountain.mountainFunction(newX);
                tankAngle = mountain.getAngle();
            }
        }
    }
}
