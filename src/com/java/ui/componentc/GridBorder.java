package com.java.ui.componentc;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class GridBorder extends AbstractBorder {
    private static final long serialVersionUID = -3127922461735948603L;
    private int bottom;
    private Color color;
    private int left;
    private int right;
    private int top;

    public GridBorder(Color color) {
        this(color, 1);
    }

    public GridBorder(Color color, int thickness) {
        this(color, thickness, thickness, thickness, thickness);
    }

    public GridBorder(Color color, int top, int left, int bottom, int right) {
        this.color = color;
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public Insets getBorderInsets() {
        return new Insets(this.top, this.left, this.bottom, this.right);
    }

    public Insets getBorderInsets(Component c) {
        return getBorderInsets();
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = this.left;
        insets.top = this.top;
        insets.right = this.right;
        insets.bottom = this.bottom;
        return insets;
    }

    public Color getColor() {
        return this.color;
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.translate(x, y);
        g.setColor(this.color);

        if (this.top > 0) {
            g.fillRect(0, 0, width, this.top);
        }

        if (this.left > 0) {
            g.fillRect(0, 0, this.left, height);
        }

        if (this.bottom > 0) {
            g.fillRect(0, height - this.bottom, width, this.bottom);
        }

        if (this.right > 0) {
            g.fillRect(width - this.right, 0, this.right, height);
        }

        g.translate(-x, -y);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setInsets(int top, int left, int bottom, int right) {
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }
}