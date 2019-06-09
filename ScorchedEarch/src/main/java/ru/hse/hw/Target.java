package ru.hse.hw;

/**
 * Target for ScorchedEarch game which player need to kill. It has 3 lives.
 * When bullet reach target it decreases lives (small bullet take 1 live, middle - 2, big - 3).
 * When the number of lives is 0, target is totally done.
 */
public class Target {

    /**
     * Different radius for different bullet size.
     */
    public final static double SMALL_RADIUS = 10;
    public final static double MIDDLE_RADIUS = 18;
    public final static double BIG_RADIUS = 25;

    /**
     * Flag which is true, when bullet decreased lives of target to 0.
     */
    private boolean done = false;

    /**
     * Target x position.
     */
    private final double x;

    /**
     * Target y position.
     */
    private final double y;

    /**
     * Lives of target decrease when bullet reach it.
     */
    private int lives;

    /**
     * Create target in given position.
     * @param x position on OX axes
     * @param y position on OY axes
     */
    public Target(double x, double y) {
        this.x = x;
        this.y = y;
        lives = 3;
    }

    /**
     * Mark bullet done - bullets killed it.
     */
    private void markDone() {
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    /**
     * Get the matching radius for given bullet size.
     * @param bulletSize bullet size
     * @return radius of bullet
     */
    public double getR(int bulletSize) {
        switch (bulletSize) {
            case 0:
                return SMALL_RADIUS;
            case 1:
                return MIDDLE_RADIUS;
            case 2:
                return BIG_RADIUS;
            default:
                return -1;
        }
    }

    /**
     * Decrease target lives when bullet of given size reach it.
     * @param bulletId bullet size
     */
    public void decreaseLives(int bulletId) {
        switch (bulletId) {
            case 0:
                lives -= 1;
                break;
            case 1:
                lives -= 2;
                break;
            case 2:
                lives -= 3;
                break;
        }
        if (lives <= 0) {
            markDone();
        }
    }

    /**
     * Check if target contains bullet with given parameters.
     * @param x bullet position on OX axes
     * @param y bullet position on OY axes
     * @param id bullet id
     */
    public boolean contains(double x, double y, int id) {
        return Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) <= Math.pow(getR(id) + Bullet.getSizeById(id), 2);
    }
}
