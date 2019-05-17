package ru.hse.hw;
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
     * Bullet position
     */
    private double x, y;

    /**
     * Size of bullet
     */
    private final int size;

    /**
     * speed of bullet
     */
    private final double speed;

    /**
     * angle of bullet
     */
    private final double angle;

    /**
     * Current time on fly
     */
    private double time;

    /**
     * Create bullet with given start position, angle and size
     * @param x position on OX axes
     * @param y position on OY axes
     * @param size size of bullet
     * @param angle angle of fire
     */
    public Bullet(double x, double y, int size,  double angle) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.angle = Math.toRadians(90 - angle);
        this.speed = 60.0 / size;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Calculates new bullet position after move according to lows of physics(parabola trajectory)
     */
    public void move() {
        time += 0.2;
        x += time * speed * Math.cos(angle);
        y += -time * speed * Math.sin(angle) + G * time * time / 2;
    }

    /**
     * Check if bullet hit the mountains(cross the mountain line)
     * @param mountains mountain lines
     * @return true if hit, false otherwise
     */
    public boolean hitMountains(List<Mountain> mountains) {
        for (var mountain : mountains) {
            if (mountain.contains(x, y)) {
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
    public boolean hitTarget(Target target) {
        if ((target.getX() - x) * (target.getX() - x) + (target.getY() - y) * (target.getY() - y) <= target.getR(size) * target.getR(size)) {
            target.decreaseLives(size);
            return true;
        }
        return false;
    }

}
