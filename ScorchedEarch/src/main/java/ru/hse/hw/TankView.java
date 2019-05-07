package ru.hse.hw;

import javafx.geometry.Point2D;
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
    private ImageView barrelView = new ImageView(tankImageBarrel);
    private ImageView currentTankView = tankViewLeft;

    private final static int offsetX = 25;
    private final static int offsetY = 25;
    private final static int offsetBarrelX = 10;
    private final static int offsetBarrelY = 45;


    public TankView(double x, double y) throws FileNotFoundException {
        tank = new Tank(x, y);
        setOrientation(BOTTOM);
    }

    public void setOrientation(Side side) {
        if (currentTankView == tankViewStay) {
            getChildren().remove(barrelView);
        }
        getChildren().remove(currentTankView);

        switch (side) {
            case RIGHT:
                currentTankView = tankViewRight;
                getChildren().add(tankViewRight);
                break;
            case LEFT:
                currentTankView = tankViewLeft;
                getChildren().add(tankViewLeft);
                break;
            case BOTTOM:
                currentTankView = tankViewStay;
                getChildren().add(tankViewStay);
                getChildren().add(barrelView);
                setBarrelOrientation();
                break;
        }
        setTankPosition(tank.getPosition());
        rotateTank(tank.getTankAngle());
    }

    private void setBarrelOrientation() {
        barrelView.setTranslateX(tank.getX() - offsetBarrelX);
        barrelView.setTranslateY(tank.getY() - offsetBarrelY);
        barrelView.setFitWidth(18);
        barrelView.setFitHeight(30);
    }

    public void setTankPosition(Point2D position) {
        tank.setPosition(position);
        currentTankView.setTranslateX(position.getX() - offsetX);
        currentTankView.setTranslateY(position.getY() - offsetY);
    }

    public double getBarrelAngle() {
        return tank.getBarrelAngle();
    }

    public Point2D getBarrelPosition() {
        return new Point2D(barrelView.getTranslateX() + barrelView.getFitWidth() / 2, barrelView.getTranslateY());
    }

    public void makeTankMove(List<Mountain> mountains, Side side) {
        tank.move(mountains, side);
        setTankPosition(tank.getPosition());
        rotateTank(tank.getTankAngle());
    }

    public void makeBarrelMove(Side side) {
        if (currentTankView != tankViewStay) {
            setOrientation(BOTTOM);
        }
        setBarrelOrientation();
        var angle = tank.moveBarrel(side);
        barrelView.getTransforms().add(new Rotate(angle, barrelView.getFitWidth() / 2, barrelView.getFitHeight()));
    }

    public void rotateTank(double angle) {
        if (currentTankView == tankViewStay) {
            currentTankView.setRotate(0);
            return;
        }
        currentTankView.setRotate(Math.toDegrees(Math.atan(angle)));
    }
}

