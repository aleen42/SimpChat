package com.java.ui.chart;

import java.awt.*;
import java.awt.geom.Area;

public class Pie3D extends Chart3DBean {
    private static final long serialVersionUID = -7777404623107653308L;
    private double extentAngle;
    private double percent;
    private Number rawData;
    private double startAngle;

    public Pie3D(String name, double startAngle, double extentAngle) {
        this.name = name;
        this.startAngle = startAngle;
        this.extentAngle = extentAngle;
        this.textColor = Color.BLACK;
        this.legendBounds = new Rectangle();
        this.textPosition = new Point();
        setArea(new Area());
    }

    public double getExtentAngle() {
        return this.extentAngle;
    }

    public double getPercent() {
        return this.percent;
    }

    public Color getPieColor() {
        return super.getColor();
    }

    public Number getRawData() {
        return this.rawData;
    }

    public double getStartAngle() {
        return this.startAngle;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public void setPieColor(Color pieColor) {
        super.setColor(pieColor);
    }

    public void setRawData(Number rawData) {
        this.rawData = rawData;
    }
}