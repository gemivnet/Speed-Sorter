package net.gemiv.speedsorter;

import java.util.ArrayList;

public class Combo {

    ArrayList<Shape> items;

    public Combo(ArrayList<Shape> items) {
        this.items = items;
    }

    public int getColor(int i) {
        return items.get(i).getColor();
    }

    public int getShape(int i) {return items.get(i).getShape(); }

    public boolean isDone(int i) {
        return items.get(i).isDone();
    }

    public void setDone(int i) {
        items.get(i).setDone();
    }

    public int getPoints(int i) { return items.get(i).getPoints(); }

    public void reset() {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).unsetDone();
        }
    }

}

class Shape {

    int color, points, shape, powerup = 0;

    boolean done = false;

    public Shape(int color, int shape, int points) {
        this.color = color;
        this.shape = shape;
        this.points = points;
    }

    public Shape(int color, int shape) {
        this.color = color;
        this.shape = shape;
    }

    public void setPowerUp(int powerup) { this.powerup = powerup; }

    public int getPowerUp() { return powerup; }

    public int getPoints() { return points;}

    public int getColor() {
        return color;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone() {
        done = true;
    }

    public void unsetDone() {
        done = false;
    }

    public int getShape() { return shape; }

}