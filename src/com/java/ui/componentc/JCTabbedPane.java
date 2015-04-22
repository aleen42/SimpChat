package com.java.ui.componentc;

import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class JCTabbedPane extends JTabbedPane {
    private static final long serialVersionUID = 7830093625100770074L;
    private Color disabledForeground;
    private Map<Component, Color> disabledForegroundMap;
    private int tabHeight;

    public JCTabbedPane() {
        this(1, 0);
    }

    public JCTabbedPane(int tabPlacement) {
        this(tabPlacement, 0);
    }

    public JCTabbedPane(int tabPlacement, int tabLayoutPolicy) {
        super(tabPlacement, tabLayoutPolicy);
        setUI(new CTabbedPaneUI());
        setFont(UIUtil.getDefaultFont());
        setForeground(new Color(0, 28, 48));
        setBackground(Color.GRAY);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setOpaque(false);
        setTabLayoutPolicy(1);
        this.tabHeight = 24;
        this.disabledForeground = new Color(123, 123, 122);
        this.disabledForegroundMap = new HashMap<Component, Color>();
    }

    public Color getDisabledForeground() {
        return this.disabledForeground;
    }

    public Color getDisabledForegroundAt(int tabIndex) {
        Component c = getComponentAt(tabIndex);
        Color color = c == null ? null : (Color) this.disabledForegroundMap
                .get(c);
        return color == null ? this.disabledForeground : color;
    }

    public int getTabHeight() {
        return this.tabHeight;
    }

    public void removeTabAt(int index) {
        Component c = getComponentAt(index);

        if (c != null) {
            this.disabledForegroundMap.remove(c);
        }

        super.removeTabAt(index);
    }

    public void setComponentAt(int index, Component component) {
        Component c = getComponentAt(index);
        Color color = null;

        if (c != null) {
            color = (Color) this.disabledForegroundMap.remove(c);
        }

        super.setComponentAt(index, component);

        if (color != null) {
            this.disabledForegroundMap.put(component, color);
        }
    }

    public void setDisabledForeground(Color disabledForeground) {
        this.disabledForeground = disabledForeground;
        repaint();
    }

    public void setDisabledForegroundAt(int tabIndex, Color disabledForeground) {
        Component c = getComponentAt(tabIndex);

        if (c != null) {
            this.disabledForegroundMap.put(c, disabledForeground);

            if ((!isEnabledAt(tabIndex)) || (!isEnabled())) {
                repaint();
            }
        }
    }

    public void setTabHeight(int tabHeight) {
        this.tabHeight = tabHeight;
        revalidate();
    }

    @Deprecated
    public void updateUI() {
    }
}