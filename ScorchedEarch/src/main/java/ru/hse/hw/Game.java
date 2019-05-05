package ru.hse.hw;

import javafx.geometry.Side;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static javafx.geometry.Side.*;

public class Game {

    private List<Mountain> mountains;

    private TankView tankView;

    private List<Bullet> bullets = new ArrayList<>();

    public Game(GameUI gameUI) throws FileNotFoundException {
        tankView = new TankView(0, 400);
        gameUI.addToPane(tankView);
    }

    private Bullet createBullet(double size) {
        var bullet = new Bullet(tankView.getX(), tankView.getY(), size);
        bullets.add(bullet);
        return bullet;
    }

    /*
    public void makeFire() {
        tank.fire(createBullet(5));
    }
    */

    public void moveTankToSide(Side side) {
        switch (side) {
            case LEFT:
                tankView.setOrientation(LEFT);
                tankView.makeTankMove(mountains, LEFT);
                break;
            case RIGHT:
                tankView.setOrientation(RIGHT);
                tankView.makeTankMove(mountains,RIGHT);
                break;
        }
    }

    public void moveBarrelToSide(Side side) {
        switch (side) {
            case TOP:
                tankView.makeBarrelMove(TOP);
                break;
            case BOTTOM:
                tankView.makeBarrelMove(BOTTOM);
                break;
        }
    }

    public void addMountains(List<Mountain> mountains) {
        this.mountains = mountains;
    }
}
