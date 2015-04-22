package com.java.ui.componentc;

import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JCCheckBoxMenuItem extends JCheckBoxMenuItem {
    private static final long serialVersionUID = 4910658177623130650L;
    private Color disabledTextColor;
    private int preferredHeight;
    private Color selectedForeground;

    public JCCheckBoxMenuItem() {
        this(null, null, false);
    }

    public JCCheckBoxMenuItem(Action a) {
        this();
        setAction(a);
    }

    public JCCheckBoxMenuItem(Icon icon) {
        this(null, icon, false);
    }

    public JCCheckBoxMenuItem(String text) {
        this(text, null, false);
    }

    public JCCheckBoxMenuItem(String text, boolean selected) {
        this(text, null, selected);
    }

    public JCCheckBoxMenuItem(String text, Icon icon) {
        this(text, icon, false);
    }

    public JCCheckBoxMenuItem(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        setUI(new CCheckBoxMenuItemUI());
        setSelectedIcon(icon);
        setOpaque(false);
        setFont(UIUtil.getDefaultFont());
        setForeground(new Color(0, 20, 35));
        setBackground(Color.GRAY);
        setIconTextGap(0);
        setBorderPainted(false);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setFocusPainted(false);
        setRolloverEnabled(true);
        setMargin(new Insets(0, 0, 0, 0));
        this.selectedForeground = new Color(253, 253, 253);
        this.disabledTextColor = new Color(127, 137, 144);
        this.preferredHeight = 22;
    }

    public Color getDisabledTextColor() {
        return this.disabledTextColor;
    }

    public int getPreferredHeight() {
        return this.preferredHeight;
    }

    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();

        if (this.preferredHeight > 0) {
            size.height = this.preferredHeight;
        }

        return size;
    }

    public Color getSelectedForeground() {
        return this.selectedForeground;
    }

    public void setDisabledTextColor(Color disabledTextColor) {
        this.disabledTextColor = disabledTextColor;

        if (!isEnabled()) {
            repaint();
        }
    }

    public void setPreferredHeight(int preferredHeight) {
        this.preferredHeight = preferredHeight;
        revalidate();
    }

    public void setSelectedForeground(Color selectedForeground) {
        this.selectedForeground = selectedForeground;
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}