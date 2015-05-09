package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HeaderPane extends JComponent {
    private static final Image BG_IMAGE = UIResourceManager.getImage("HeaderPaneBackgroundImage");
    private static final Image DISABLED_BG_IMAGE = UIUtil.toBufferedImage(BG_IMAGE, 0.5F, null);

    private static final long serialVersionUID = 5223934257160048018L;
    private int headerHeight;

    public HeaderPane() {
        this(null);
    }

    public HeaderPane(Component c) {
        setOpaque(false);
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));
        this.headerHeight = 21;

        if (c != null) {
            add(c, "Center");
        }
    }

    public int getHeaderHeight() {
        return this.headerHeight;
    }

    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = this.headerHeight;
        return size;
    }

    protected void paintComponent(Graphics g) {
        Image image = isEnabled() ? BG_IMAGE : DISABLED_BG_IMAGE;
        UIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), new Rectangle(0, 0, getWidth(), getHeight() - 1), this);
    }

    public void setHeaderHeight(int headerHeight) {
        this.headerHeight = headerHeight;
        revalidate();
    }
}