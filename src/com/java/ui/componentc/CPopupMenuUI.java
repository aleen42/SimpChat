package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.DefaultMenuLayout;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class CPopupMenuUI extends BasicPopupMenuUI {
    private class PropertyChangeHandler implements PropertyChangeListener {
        private PropertyChangeHandler() {
        }

        public void propertyChange(PropertyChangeEvent e) {
            if ("lookAndFeel".equalsIgnoreCase(e.getPropertyName())) {
                SwingUtilities
                        .updateComponentTreeUI(CPopupMenuUI.this.popupMenu);
            }
        }
    }

    public static ComponentUI createUI(JComponent c) {
        return new CPopupMenuUI();
    }

    private PropertyChangeHandler propertyChangeListener;

    public void installDefaults() {
        if ((this.popupMenu.getLayout() == null)
                || ((this.popupMenu.getLayout() instanceof UIResource))) {
            this.popupMenu.setLayout(new DefaultMenuLayout(this.popupMenu, 1));
        }
    }

    protected void installListeners() {
        super.installListeners();

        if (this.propertyChangeListener == null) {
            this.propertyChangeListener = new PropertyChangeHandler();
        }

        UIManager.addPropertyChangeListener(this.propertyChangeListener);
    }

    protected void uninstallDefaults() {
    }

    protected void uninstallListeners() {
        super.uninstallListeners();

        if (this.propertyChangeListener != null) {
            UIManager.removePropertyChangeListener(this.propertyChangeListener);
        }
    }

    public void update(Graphics g, JComponent c) {
        UIUtil.paintImage(
                g,
                UIResourceManager.getImage("MenuBackgroundImage"),
                new Insets(9, 32, 9, 8),
                new Rectangle(0, 0, this.popupMenu.getWidth(), this.popupMenu
                        .getHeight()), this.popupMenu);
        paint(g, c);
    }
}