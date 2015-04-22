package com.java.ui.componentc;

import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JCMenuItem extends JMenuItem {
    private static final long serialVersionUID = -1476962131254999081L;
    private Icon disabledIcon;
    private Color disabledTextColor;
    private int preferredHeight;
    private Color selectedForeground;

    public JCMenuItem() {
        this(null, null);
    }

    public JCMenuItem(Action a) {
        this();
        setAction(a);
    }

    public JCMenuItem(Icon icon) {
        this(null, icon);
    }

    public JCMenuItem(String text) {
        this(text, null);
    }

    public JCMenuItem(String text, Icon icon) {
        super(text, icon);
        init();
    }

    public JCMenuItem(String text, int mnemonic) {
        super(text, mnemonic);
        init();
    }

    public Icon getDisabledIcon() {
        return this.disabledIcon;
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

    private void init() {
        setUI(new CMenuItemUI());
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

    public void setDisabledIcon(Icon disabledIcon) {
        super.setDisabledIcon(disabledIcon);
        this.disabledIcon = disabledIcon;
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