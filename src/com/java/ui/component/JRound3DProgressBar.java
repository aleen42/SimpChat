package com.java.ui.component;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JRound3DProgressBar extends JProgressBar {
    private static final long serialVersionUID = 1632391629083296112L;
    private Color fontColor;
    private Color fontCoverClor;

    public JRound3DProgressBar() {
        this(0);
    }

    public JRound3DProgressBar(BoundedRangeModel newModel) {
        super(newModel);
        init();
    }

    public JRound3DProgressBar(int orient) {
        this(orient, 0, 100);
    }

    public JRound3DProgressBar(int min, int max) {
        this(0, min, max);
    }

    public JRound3DProgressBar(int orient, int min, int max) {
        super(orient, min, max);
        init();
    }

    public Color getFontColor() {
        return this.fontColor;
    }

    public Color getFontCoverClor() {
        return this.fontCoverClor;
    }

    private void init() {
        this.fontColor = Color.ORANGE;
        this.fontCoverClor = UIResourceManager.getWhiteColor();
        setUI(new Round3DProgressBarUI());
        setFont(UIUtil.getDefaultFont());
        setForeground(Color.BLUE);
        setBackground(Color.GRAY);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setBorderPainted(false);
        setOpaque(false);
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
        repaint();
    }

    public void setFontCoverClor(Color fontCoverClor) {
        this.fontCoverClor = fontCoverClor;
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}