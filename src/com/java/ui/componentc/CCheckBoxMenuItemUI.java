package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseEvent;

public class CCheckBoxMenuItemUI extends CMenuItemUI {
    private static final Icon DEFAULT_ICON = UIResourceManager.getIcon("CheckBoxMenuItemSelectedIcon");

    public static ComponentUI createUI(JComponent c) {
        return new CCheckBoxMenuItemUI();
    }

    protected String getPropertyPrefix() {
        return "CheckBoxMenuItem";
    }

    protected Color getTextColor() {
        ButtonModel model = this.menuItem.getModel();
        Color color = null;

        if ((this.menuItem instanceof JCCheckBoxMenuItem)) {
            JCCheckBoxMenuItem cItem = (JCCheckBoxMenuItem) this.menuItem;

            if (!model.isEnabled()) {
                color = cItem.getDisabledTextColor();
            } else if (model.isArmed()) {
                color = cItem.getSelectedForeground();
            } else {
                color = cItem.getForeground();
            }

        }

        return color;
    }

    protected void layoutIcon(Rectangle iconRect) {
        Icon icon = this.menuItem.getSelectedIcon();
        icon = icon == null ? DEFAULT_ICON : icon;
        int iconWidth = icon.getIconWidth();
        int iconHeight = icon.getIconHeight();
        iconRect.x = (int) Math.round((25 - iconWidth) / 2.0D);
        iconRect.y = (int) Math.round((this.menuItem.getHeight() - iconHeight) / 2.0D);
        iconRect.width = iconWidth;
        iconRect.height = iconHeight;
    }

    protected void paintIcon(Graphics g, JMenuItem menuItem, Rectangle iconRect) {
        if (menuItem.isSelected()) {
            Icon icon = menuItem.getSelectedIcon();
            icon = icon == null ? DEFAULT_ICON : icon;
            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();

            if (!menuItem.isEnabled()) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(0.5F));
            }

            icon.paintIcon(menuItem, g2d, iconRect.x, iconRect.y);
            g2d.setComposite(oldComposite);
        }
    }

    public void processMouseEvent(JMenuItem item, MouseEvent e, MenuElement[] path, MenuSelectionManager manager) {
        Point p = e.getPoint();

        if ((p.x >= 0) && (p.x < item.getWidth()) && (p.y >= 0) && (p.y < item.getHeight())) {
            if (e.getID() == 502) {
                manager.clearSelectedPath();
                item.doClick(0);
            } else {
                manager.setSelectedPath(path);
            }
        } else if (item.getModel().isArmed()) {
            MenuElement[] newPath = new MenuElement[path.length - 1];

            int i = 0;
            for (int c = path.length - 1; i < c; i++) {
                newPath[i] = path[i];
            }

            manager.setSelectedPath(newPath);
        }
    }
}