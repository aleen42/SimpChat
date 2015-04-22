package com.java.ui.chart;

import java.awt.*;
import java.awt.geom.Area;
import java.io.Serializable;

public abstract class Chart3DBean implements Serializable {
    private static final long serialVersionUID = 4363749894056800443L;
    protected Area area;
    protected Color color;
    protected Color darkerColor;
    protected Rectangle legendBounds;
    protected String name;
    protected boolean selected;
    protected String text;
    protected Color textColor;
    protected Point textPosition;
    protected String toolTipText;

    public Area getArea() {
        return this.area;
    }

    public Color getColor() {
        return this.color;
    }

    public Color getDarkerColor() {
        return this.darkerColor;
    }

    public Rectangle getLegendBounds() {
        return this.legendBounds;
    }

    public String getName() {
        return this.name;
    }

    public String getText() {
        return this.text;
    }

    public Color getTextColor() {
        return this.textColor;
    }

    public Point getTextPosition() {
        return this.textPosition;
    }

    public String getToolTipText() {
        return this.toolTipText;
    }

    public boolean isSelected() {
        return this.selected;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setDarkerColor(Color darkerColor) {
        this.darkerColor = darkerColor;
    }

    public void setLegendBounds(int x, int y, int width, int height) {
        this.legendBounds.setBounds(x, y, width, height);
    }

    public void setLegendLocation(int x, int y) {
        this.legendBounds.setLocation(x, y);
    }

    public void setLegendSize(int width, int height) {
        this.legendBounds.setSize(width, height);
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setTextPosition(int x, int y) {
        this.textPosition.setLocation(x, y);
    }

    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }
}