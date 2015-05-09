package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import java.awt.*;
import java.awt.event.MouseEvent;

public class CTableHeaderUI extends BasicTableHeaderUI {
    public class CMouseInputHandler extends BasicTableHeaderUI.MouseInputHandler {
        public CMouseInputHandler() {
            super();
        }

        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                super.mousePressed(e);
            }
        }
    }

    private static final Image BG_IMAGE = UIResourceManager.getImage("TableHeaderDefaultImage");

    private static final Image DISABLED_BG_IMAGE = UIUtil.toBufferedImage(BG_IMAGE, 0.5F, null);

    public static ComponentUI createUI(JComponent header) {
        return new CTableHeaderUI();
    }

    protected MouseInputListener createMouseInputListener() {
        return new CMouseInputHandler();
    }

    public int getRolloverColumn() {
        return super.getRolloverColumn();
    }

    protected void installDefaults() {
    }

    protected void rolloverColumnUpdated(int oldColumn, int newColumn) {
        this.header.repaint(this.header.getHeaderRect(oldColumn));
        this.header.repaint(this.header.getHeaderRect(newColumn));
    }

    public void update(Graphics g, JComponent c) {
        Image image = c.isEnabled() ? BG_IMAGE : DISABLED_BG_IMAGE;
        UIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), new Rectangle(0, 0, c.getWidth(), c.getHeight() - 1), c);
        paint(g, c);
    }
}