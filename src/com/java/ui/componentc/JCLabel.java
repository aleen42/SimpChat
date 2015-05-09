package com.java.ui.componentc;

import com.java.ui.util.UIUtil;

import javax.swing.*;
import java.awt.*;

public class JCLabel extends JLabel {
    private static final long serialVersionUID = -736116392575679784L;
    private double angle;
    private float backgroundAlpha;
    private int deltaX;
    private int deltaY;
    private Color disabledForeground;
    private float iconAlpha;
    private float textAlpha;

    public JCLabel() {
        this("", null, 10);
    }

    public JCLabel(Icon image) {
        this(null, image, 0);
    }

    public JCLabel(Icon image, int horizontalAlignment) {
        this(null, image, horizontalAlignment);
    }

    public JCLabel(String text) {
        this(text, null, 10);
    }

    public JCLabel(String text, Icon icon, int horizontalAlignment) {
        super(text, icon, horizontalAlignment);
        this.angle = 0.0D;
        this.textAlpha = (this.iconAlpha = 1.0F);
        this.backgroundAlpha = 0.0F;
        this.disabledForeground = new Color(103, 117, 127);
        setUI(new CLabelUI());
        setBackground(Color.GRAY);
        setForeground(new Color(0, 28, 48));
        setFont(UIUtil.getDefaultFont());
        super.setOpaque(false);
    }

    public JCLabel(String text, int horizontalAlignment) {
        this(text, null, horizontalAlignment);
    }

    public double getAngle() {
        return this.angle;
    }

    public float getBackgroundAlpha() {
        return this.backgroundAlpha;
    }

    public int getDeltaX() {
        return this.deltaX;
    }

    public int getDeltaY() {
        return this.deltaY;
    }

    public Color getDisabledForeground() {
        return this.disabledForeground;
    }

    public float getIconAlpha() {
        return this.iconAlpha;
    }

    public float getTextAlpha() {
        return this.textAlpha;
    }

    public void setAngle(double angle) {
        this.angle = angle;
        repaint();
    }

    public void setBackgroundAlpha(float backgroundAlpha) {
        if ((backgroundAlpha >= 0.0F) && (backgroundAlpha <= 1.0F)) {
            this.backgroundAlpha = backgroundAlpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid backgroundAlpha:" + backgroundAlpha);
        }
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
        repaint();
    }

    public void setDeltaY(int deltaY) {
        this.deltaY = deltaY;
        repaint();
    }

    public void setDisabledForeground(Color disabledForeground) {
        this.disabledForeground = disabledForeground;

        if (!isEnabled()) {
            repaint();
        }
    }

    public void setIconAlpha(float iconAlpha) {
        if ((iconAlpha >= 0.0F) && (iconAlpha <= 1.0F)) {
            this.iconAlpha = iconAlpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid iconAlpha:" + iconAlpha);
        }
    }

    @Deprecated
    public void setOpaque(boolean isOpaque) {
    }

    public void setTextAlpha(float textAlpha) {
        if ((textAlpha >= 0.0F) && (textAlpha <= 1.0F)) {
            this.textAlpha = textAlpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid textAlpha:" + textAlpha);
        }
    }

    @Deprecated
    public void updateUI() {
    }
}