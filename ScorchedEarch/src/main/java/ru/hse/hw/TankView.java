package ru.hse.hw;

import javafx.geometry.Side;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Rotate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static javafx.geometry.Side.BOTTOM;

public class TankView extends Pane {

    private Tank tank;

    private Image tankImageLeft = new Image(new FileInputStream("src/main/resources/images/tankL.png"));
    private Image tankImageRight = new Image(new FileInputStream("src/main/resources/images/tankR.png"));
    private Image tankImageStay = new Image(new FileInputStream("src/main/resources/images/tankS.png"));
    private Image tankImageBarrel = new Image(new FileInputStream("src/main/resources/images/tankF.png"));
    private ImageView tankViewLeft = new ImageView(tankImageLeft);
    private ImageView tankViewRight = new ImageView(tankImageRight);
    private ImageView tankViewStay = new ImageView(tankImageStay);
    private ImageView tankViewBarrel = new ImageView(tankImageBarrel);
    private ImageView currentView = tankViewLeft;

    private final static int offsetX = 25;
    private final static int offsetY = 25;
    private final static int offsetBarrelX = -15;
    private final static int offsetBarrelY = 17;


    public TankView(double x, double y) throws FileNotFoundException {
        tank = new Tank(x, y);
        setOrientation(BOTTOM);
    }

    public void setOrientation(Side side) {
        if (currentView == tankViewStay) {
            getChildren().remove(tankViewBarrel);
        }
        getChildren().remove(currentView);

        switch (side) {
            case RIGHT:
                currentView = tankViewRight;
                getChildren().add(tankViewRight);
                break;
            case LEFT:
                currentView = tankViewLeft;
                getChildren().add(tankViewLeft);
                break;
            case BOTTOM:
                currentView = tankViewStay;
                getChildren().add(tankViewStay);
                getChildren().add(tankViewBarrel);
                setBarrelOrientation();
                break;
        }
        setX(tank.getX());
        setY(tank.getY());
        rotate(tank.getTankAngle());
    }

    private void setBarrelOrientation() {
        tankViewBarrel.setTranslateX(tankViewStay.getTranslateX() - offsetBarrelX);
        tankViewBarrel.setTranslateY(tankViewStay.getTranslateY() - offsetBarrelY);
        tankViewBarrel.setFitHeight(30);
        tankViewBarrel.setFitWidth(18);
    }

    public void setX(double x) {
        tank.setX(x);
        setTranslateX(x - offsetX);
    }

    public void setY(double y) {
        tank.setY(y);
        setTranslateY(y - offsetY);
    }

    public double getX() {
        return tank.getX();
    }

    public double getY() {
        return tank.getY();
    }

    public void makeTankMove(List<Mountain> mountains, Side side) {
        tank.move(mountains, side);
        setX(tank.getX());
        setY(tank.getY());
        setRotate(tank.getTankAngle());
    }

    public void makeBarrelMove(Side side) {
        if (currentView != tankViewStay) {
            setOrientation(BOTTOM);
        }
        getChildren().remove(tankViewBarrel);
        var angle = tank.moveBarrel(side);
        tankViewBarrel.getTransforms().add(new Rotate(angle,
                tankViewBarrel.getX() + tankViewBarrel.getFitWidth() / 2,
                tankViewBarrel.getY() + tankViewBarrel.getFitHeight()));
        getChildren().add(tankViewBarrel);
    }

    public void rotate(double angle) {
        if (currentView == tankViewStay) {
            currentView.setRotate(0);
            return;
        }
        currentView.setRotate(Math.toDegrees(Math.atan(angle)));
    }
}

