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

/**
 * View for tank.
 */
public class TankView extends Pane {

    private final ImageView tankViewLeft = new ImageView(new Image(new FileInputStream("src/main/resources/images/tankL.png")));
    private final ImageView tankViewRight = new ImageView(new Image(new FileInputStream("src/main/resources/images/tankR.png")));
    private final ImageView tankViewStay = new ImageView(new Image(new FileInputStream("src/main/resources/images/tankS.png")));
    private final ImageView barrelView = new ImageView(new Image(new FileInputStream("src/main/resources/images/tankF.png")));
    private ImageView currentTankView = tankViewStay;

    /**
     * The tank logic
     */
    private Tank tank;

    /**
     * Sizes to barrel fit tank
     */
    private final static int offsetBarrelX = 7;
    private final static int offsetBarrelY = 40;

    /**
     * Sizes to tank fit mountains
     */
    private static double offsetX;
    private static double offsetY;

    /**
     * Create tank in given position
     * @param x position on OX axes
     * @param y position on OY axes
     * @throws FileNotFoundException when no image of target is found
     */
    public TankView(double x, double y) throws FileNotFoundException {
        tank = new Tank(x, y);
        tankViewLeft.setFitWidth(60);
        tankViewLeft.setFitHeight(30);
        tankViewRight.setFitWidth(60);
        tankViewRight.setFitHeight(30);
        tankViewStay.setFitWidth(40);
        tankViewStay.setFitHeight(40);
        setOrientation(BOTTOM);
    }

    /**
     * Set TankView on one of three states:
     * RIGHT - tank goes right
     * LEFT - tank goes left
     * STAY - tank stays and ready to fire
     * @param side state of the tank
     */
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
        updateTankView();
        rotateTank(tank.getTankAngle());
    }

    /**
     * Set position and angle of barrel image
     */
    private void setBarrelOrientation() {
        barrelView.setX(tank.getX() - offsetBarrelX);
        barrelView.setY(tank.getY() - offsetBarrelY);
        barrelView.setFitWidth(15);
        barrelView.setFitHeight(28);
    }

    /**
     * Calculates sizes for tank image can fit the environment
     */
    private void calculateOffset() {
        double alpha = Math.toDegrees(Math.atan(currentTankView.getFitHeight() / currentTankView.getFitWidth()));
        double betta = currentTankView == tankViewStay ? 0 : tank.getTankAngle();
        double gamma = alpha - betta;
        double diagonal = Math.sqrt(Math.pow(currentTankView.getFitHeight()/ 2, 2) + Math.pow(currentTankView.getFitWidth() / 2, 2));
        offsetX = Math.cos(Math.toRadians(gamma)) * diagonal;
        offsetY = Math.sin(Math.toRadians(gamma)) * diagonal;
    }

    /**
     * Move tank image
     */
    private void updateTankView() {
        calculateOffset();
        currentTankView.setX(tank.getX() - offsetX);
        currentTankView.setY(tank.getY() - offsetY);
    }

    /**
     * Get the end of the barrel image to set fire from it
     * @return end point of the barrel
     */
    public Point2D getBarrelPosition() {
        calculateOffset();
        return new Point2D(
                barrelView.getX() + barrelView.getFitWidth() / 2 + barrelView.getFitHeight() * Math.sin(Math.toRadians(tank.getBarrelAngle())),
                barrelView.getY() + (barrelView.getFitHeight() - barrelView.getFitHeight() * Math.cos(Math.toRadians(tank.getBarrelAngle()))));
    }

    public double getBarrelAngle() {
        return tank.getBarrelAngle();
    }

    /**
     * Move tank image along the mountains
     * @param mountains piece of the environment
     * @param side side where tank is moving
     */
    public void makeTankMove(List<Mountain> mountains, Side side) {
        tank.move(mountains, side);
        updateTankView();
        rotateTank(tank.getTankAngle());
    }

    /**
     * Move barrel image
     * @param side side to move the barrel
     */
    public void makeBarrelMove(Side side) {
        if (currentTankView != tankViewStay) {
            setOrientation(BOTTOM);
        }
        barrelView.getTransforms().clear();
        setBarrelOrientation();
        tank.moveBarrel(side);
        barrelView.getTransforms().add(new Rotate(tank.getBarrelAngle(), barrelView.getX() + barrelView.getFitWidth() / 2, barrelView.getY() + barrelView.getFitHeight()));
    }

    /**
     * Rotate tank image
     * @param angle angle to rotate
     */
    private void rotateTank(double angle) {
        if (currentTankView != tankViewStay) {
            currentTankView.getTransforms().clear();
            currentTankView.getTransforms().add(new Rotate(Math.toDegrees(Math.atan(angle)), tank.getX(), tank.getY()));
        }
    }
}

