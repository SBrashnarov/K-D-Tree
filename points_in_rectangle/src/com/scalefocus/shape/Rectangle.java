package com.scalefocus.shape;

public class Rectangle {

    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public Rectangle(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public boolean intersect(Rectangle other) {
        return this.x1 <= other.x2
                && this.x2 >= other.x1
                && this.y1 <= other.y2
                && this.y2 >= other.y1;
    }

    public boolean containsPoint(Point point) {
        return point.getX() >= this.x1
                && point.getX() <= this.x2
                && point.getY() >= this.y1
                && point.getY() <= this.y2;
    }

    public int getX1() {
        return x1;
    }

    public int getY1() {
        return y1;
    }

    public int getX2() {
        return x2;
    }

    public int getY2() {
        return y2;
    }
}
