package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JCCheckBox extends JCheckBox {
    private static final long serialVersionUID = 3502336131066875949L;
    private Color disabledTextColor;
    private Icon pressedSelectedIcon;

    public JCCheckBox() {
        this(null, null, false);
    }

    public JCCheckBox(Action a) {
        this();
        setAction(a);
    }

    public JCCheckBox(Icon icon) {
        this(null, icon, false);
    }

    public JCCheckBox(Icon icon, boolean selected) {
        this(null, icon, selected);
    }

    public JCCheckBox(String text) {
        this(text, null, false);
    }

    public JCCheckBox(String text, boolean selected) {
        this(text, null, selected);
    }

    public JCCheckBox(String text, Icon icon) {
        this(text, icon, false);
    }

    public JCCheckBox(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        setUI(new CCheckBoxUI());
        setForeground(new Color(0, 28, 48));
        setBackground(Color.GRAY);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentAreaFilled(false);
        setFont(UIUtil.getDefaultFont());
        setOpaque(false);
        setBorderPainted(false);
        setHorizontalAlignment(10);
        setRolloverEnabled(true);
        setIconTextGap(5);
        setMargin(new Insets(0, 0, 0, 0));
        this.disabledTextColor = new Color(103, 117, 127);
        setIcon(UIResourceManager.getIconByName("checkbox_normal.png"));
        setDisabledIcon(UIResourceManager.getIconByName("checkbox_disabled.png"));
        setSelectedIcon(UIResourceManager.getIconByName("checkbox_selected.png"));
        setDisabledSelectedIcon(UIResourceManager.getIconByName("checkbox_disabled_selected.png"));
        setRolloverIcon(UIResourceManager.getIconByName("checkbox_rollover.png"));
        setRolloverSelectedIcon(UIResourceManager.getIconByName("checkbox_rollover_selected.png"));
        setPressedIcon(UIResourceManager.getIconByName("checkbox_rollover.png"));
        setPressedSelectedIcon(UIResourceManager.getIconByName("checkbox_rollover_selected.png"));
    }

    public Color getDisabledTextColor() {
        return this.disabledTextColor;
    }

    public Icon getPressedSelectedIcon() {
        return this.pressedSelectedIcon;
    }

    public void setDisabledTextColor(Color disabledTextColor) {
        this.disabledTextColor = disabledTextColor;

        if (!isEnabled()) {
            repaint();
        }
    }

    public void setPressedSelectedIcon(Icon pressedSelectedIcon) {
        this.pressedSelectedIcon = pressedSelectedIcon;
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}