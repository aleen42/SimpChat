package com.java.ui.chart;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

public class Bar3D extends Chart3DBean {
    private static final long serialVersionUID = 6928396989040217938L;
    private Number data;
    private int offsetX;
    private int offsetY;
    private Rectangle2D rect;
    private float topAlpha;

    public Bar3D(String name) {
        this.name = name;
        this.textColor = Color.BLACK;
        this.rect = new Rectangle2D.Double();
        this.legendBounds = new Rectangle();
        this.textPosition = new Point();
        setArea(new Area());
    }

    public Color getBarColor() {
        return super.getColor();
    }

    public Number getData() {
        return this.data;
    }

    public int getOffsetX() {
        return this.offsetX;
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    public Rectangle2D getRect() {
        return this.rect;
    }

    public float getTopAlpha() {
        return this.topAlpha;
    }

    public void setBarColor(Color barColor) {
        super.setColor(barColor);
    }

    public void setData(Number data) {
        this.data = data;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public void setRect(double x, double y, double width, double height) {
        this.rect.setRect(x, y, width, height);
    }

    public void setTopAlpha(float topAlpha) {
        this.topAlpha = topAlpha;
    }
}