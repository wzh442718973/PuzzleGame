package com.phosphorescent.fluorescent;

/**
 * Created by zyw on 2017/10/19.
 */
public class Point {
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    private int x=0;
    private int y=0;
}
