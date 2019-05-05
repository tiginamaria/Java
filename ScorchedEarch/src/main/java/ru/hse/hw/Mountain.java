package ru.hse.hw;

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

    public boolean contains(double x) {
        return x < x2 && x >= x1;
    }

    public boolean hitted(double x, double y) {
        return contains(x) && mountainFunction(x) <= y;
    }

    public double getAngle() {
        return (y2 - y1) /(x2 - x1);
    }
}
