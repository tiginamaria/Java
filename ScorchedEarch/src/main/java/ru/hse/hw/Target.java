package ru.hse.hw;

/**
 * Target for ScorchedEarch game which player need to kill. It has 3 lives.
 * When bullet reach target it decreases lives (small bullet take 1 live, middle - 2, big - 3).
 * When the number of lives is 0, target is totally done.
 */
public class Target {

    /**
     * All different sizes of bullets
     */
    private final static int SMALL = 3;
    private final static int MIDDLE = 6;
    private final static int BIG = 9;


    /**
     * Different radius for different bullet size
     */
    private final static double SMALL_RADIUS = 10;
    private final static double MIDDLE_RADIUS = 18;
    private final static double BIG_RADIUS = 25;

    /**
     * Flag which is true, when bullet decreased lives of target to 0
     */
    private boolean done = false;

    /**
     * Target position
     */
    private final double x, y;

    /**
     * Lives of target decrease when bullet reach it
     */
    private int lives;

    /**
     * Create target in given position
     * @param x position on OX axes
     * @param y position on OY axes
     */
    public Target(double x, double y) {
        this.x = x;
        this.y = y;
        lives = 3;
    }

    /**
     * Mark bullet done - bullets killed it
     */
    private void markDone() {
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /**
     * Get the matching radius for given bullet size
     * @param bulletSize bullet size
     * @return radius of bullet
     */
    public double getR(int bulletSize) {
        switch (bulletSize) {
            case SMALL:
                return SMALL_RADIUS;
            case MIDDLE:
                return MIDDLE_RADIUS;
            case BIG:
                return BIG_RADIUS;
        }
        return 0;
    }

    /**
     * Decrease target lives when bullet of given size reach it
     * @param bulletSize bullet size
     */
    public void decreaseLives(int bulletSize) {
        switch (bulletSize) {
            case SMALL:
                lives -= 1;
                break;
            case MIDDLE:
                lives -= 2;
                break;
            case BIG:
                lives -= 3;
                break;
        }
        if (lives <= 0) {
            markDone();
        }
    }
}
