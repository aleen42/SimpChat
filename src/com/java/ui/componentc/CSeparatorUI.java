package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;
import java.awt.*;

public class CSeparatorUI extends BasicSeparatorUI {
    private static final Image IMAGE_H = UIResourceManager
            .getImage("SeparatorImageH");

    private static final Image IMAGE_V = UIResourceManager
            .getImage("SeparatorImageV");

    public static ComponentUI createUI(JComponent c) {
        return new CSeparatorUI();
    }

    public Dimension getPreferredSize(JComponent c) {
        Dimension size = super.getPreferredSize(c);
        Insets insets = c.getInsets();

        if (((JSeparator) c).getOrientation() == 1) {
            return new Dimension(IMAGE_V.getWidth(null) + insets.left
                    + insets.right, size.height);
        }

        return new Dimension(size.width, IMAGE_H.getHeight(null) + insets.top
                + insets.bottom);
    }

    protected void installDefaults(JSeparator s) {
    }

    public void paint(Graphics g, JComponent c) {
        Insets insets = c.getInsets();
        int width = c.getWidth() - insets.left - insets.right;
        int height = c.getHeight() - insets.top - insets.bottom;
        Rectangle rect;
        Image image;

        if (((JSeparator) c).getOrientation() == 1) {
            image = IMAGE_V;
            int thickness = image.getWidth(null);
            rect = new Rectangle(insets.left, insets.top, thickness, height);
        } else {
            image = IMAGE_H;
            int thickness = image.getHeight(null);
            rect = new Rectangle(insets.left, insets.top, width, thickness);
        }

        UIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), rect, c);
    }
}