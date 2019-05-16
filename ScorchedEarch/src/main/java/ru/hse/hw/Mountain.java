package ru.hse.hw;

import java.util.Random;

public class Mountain {
    private double x1, y1;
    private double y2, x2;

    public Mountain(double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public double mountainFunction(double x) {
        return (y1 - y2) / (x1 - x2) * x  + (x1 * y2 - x2 * y1) / (x1 - x2);
    }

    public boolean isOnMountain(double x) {
        return x < x2 && x >= x1;
    }

    public boolean contains(double x, double y) {
        return isOnMountain(x) && mountainFunction(x) < y;
    }

    public double getRandomOverMountainX(Random random) {
        return x1 + random.nextInt((int)(x2 - x1 - 1));
    }

    public double getRandomOverMountainY(Random random, double x, int max) {
        int y = random.nextInt((int)(max - mountainFunction(x)));
        System.out.println("random y x=" + x + " max=" + max + " y=" + y +" f=" + mountainFunction(x));
        return Math.max(50, mountainFunction(x) - y);
    }

    public double getAngle() {
        return (y2 - y1) /(x2 - x1);
    }
}
