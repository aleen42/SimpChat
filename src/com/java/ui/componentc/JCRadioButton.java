package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JCRadioButton extends JRadioButton {
    private static final long serialVersionUID = 4883029994947635375L;
    private Color disabledTextColor;
    private Icon pressedSelectedIcon;

    public JCRadioButton() {
        this(null, null, false);
    }

    public JCRadioButton(Action a) {
        this();
        setAction(a);
    }

    public JCRadioButton(Icon icon) {
        this(null, icon, false);
    }

    public JCRadioButton(Icon icon, boolean selected) {
        this(null, icon, selected);
    }

    public JCRadioButton(String text) {
        this(text, null, false);
    }

    public JCRadioButton(String text, boolean selected) {
        this(text, null, selected);
    }

    public JCRadioButton(String text, Icon icon) {
        this(text, icon, false);
    }

    public JCRadioButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        setUI(new CRadioButtonUI());
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
        setIcon(UIResourceManager.getIconByName("radiobutton_normal.png"));
        setDisabledIcon(UIResourceManager.getIconByName("radiobutton_disabled.png"));
        setSelectedIcon(UIResourceManager.getIconByName("radiobutton_selected.png"));
        setDisabledSelectedIcon(UIResourceManager.getIconByName("radiobutton_disabled_selected.png"));
        setRolloverIcon(UIResourceManager.getIconByName("radiobutton_rollover.png"));
        setRolloverSelectedIcon(UIResourceManager.getIconByName("radiobutton_rollover_selected.png"));
        setPressedIcon(UIResourceManager.getIconByName("radiobutton_rollover.png"));
        setPressedSelectedIcon(UIResourceManager.getIconByName("radiobutton_rollover_selected.png"));
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