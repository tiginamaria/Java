package ru.hse.hw;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Tank fires and bullet flies according to lows of physics. When it reaches mountain or target it stops.
 */
public class Bullet {

    /**
     * Gravity constant
     */
    private static final double G = 9.8;

    /**
     * Max bullet speed
     */
    private static final double SPEED = 300.0;

    /**
     * Bullet start x position
     */
    private double x0;

    /**
     * Bullet start y position
     */
    private double y0;

    /**
     * Bullet current x position
     */
    private double x;

    /**
     * Bullet current y position
     */
    private double y;

    /**
     * Bullet size
     */
    private final double size;

    /**
     * Bullet id
     */
    private final int id;

    /**
     * Bullet speed
     */
    private final double speed;

    /**
     * Bullet angle
     */
    private final double angle;

    /**
     * Current time on fly
     */
    private double time;

    /**
     * Flag if bullet exploded or not
     */
    private boolean isExploded = false;

    /**
     * Different radius for different bullet size
     */
    public final static double SMALL = 4;
    public final static double MIDDLE = 6;
    public final static double BIG = 8;

    /**
     * Create bullet with given start position, angle and size
     * @param x0 position on OX axes
     * @param y0 position on OY axes
     * @param id size of bullet
     * @param angle angle of fire
     */
    public Bullet(double x0, double y0, int id,  double angle) {
        this.x0 = x0;
        this.y0 = y0;
        this.id = id;
        this.size = getSizeById(id);
        this.angle = Math.toRadians(90 - angle);
        this.speed = SPEED / size;
    }

    /**
     * Get size of target for each bullet type
     * @param id bullet id
     * @return size of target
     */
    public static double getSizeById(int id) {
        switch (id) {
            case 0:
                return SMALL;
            case 1:
                return MIDDLE;
            default:
                return BIG;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isExploded() {
        return isExploded;
    }

    /**
     * Calculates new bullet position after move according to laws of physics(parabola trajectory)
     */
    public void move() {
        time += 0.2;
        x = x0 + time * speed * Math.cos(angle);
        y = y0 - time * speed * Math.sin(angle) + G * time * time / 2;
    }

    /**
     * Check if bullet hit the mountains(cross the mountain line)
     * @param mountains mountain lines
     * @return true if hit, false otherwise
     */
    public boolean hitMountains(@NotNull List<Mountain> mountains) {
        for (var mountain : mountains) {
            if (mountain.contains(x, y, size)) {
                isExploded = true;
                return true;
            }
        }
        return false;
    }

    /**
     * Check if bullet hit the target and is yes, make lives of target decrease
     * @param target target to check
     * @return true if hit, false otherwise
     */
    public boolean hitTarget(@NotNull Target target) {
        if (target.contains(x, y, id)) {
            target.decreaseLives(id);
            isExploded = true;
            return true;
        }
        return false;
    }
}
