package com.java.ui.componentc;

import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JCSlider extends JSlider {
    private static final long serialVersionUID = -1988588632695957051L;
    private Color majorTickColor;
    private boolean miniMode;
    private Color minorTickColor;
    private CSliderUI ui;

    public JCSlider() {
        this(0, 0, 100, 50);
    }

    public JCSlider(BoundedRangeModel brm) {
        super(brm);
        init();
    }

    public JCSlider(int orientation) {
        this(orientation, 0, 100, 50);
    }

    public JCSlider(int min, int max) {
        this(0, min, max, (min + max) / 2);
    }

    public JCSlider(int min, int max, int value) {
        this(0, min, max, value);
    }

    public JCSlider(int orientation, int min, int max, int value) {
        super(orientation, min, max, value);
        init();
    }

    public Color getMajorTickColor() {
        return this.majorTickColor;
    }

    public Color getMinorTickColor() {
        return this.minorTickColor;
    }

    private void init() {
        this.majorTickColor = new Color(76, 181, 237);
        this.minorTickColor = new Color(78, 160, 209);
        setUI(this.ui = new CSliderUI());
        setFont(UIUtil.getDefaultFont());
        setBackground(Color.GRAY);
        setForeground(new Color(0, 28, 48));
        setOpaque(false);
        setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    public boolean isMiniMode() {
        return this.miniMode;
    }

    public void setMajorTickColor(Color majorTickColor) {
        this.majorTickColor = majorTickColor;
        repaint();
    }

    public void setMiniMode(boolean miniMode) {
        this.miniMode = miniMode;
        this.ui.calculateGeometry();
        repaint();
    }

    public void setMinorTickColor(Color minorTickColor) {
        this.minorTickColor = minorTickColor;
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}