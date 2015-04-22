package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import java.awt.*;

public class CTabbedPaneUI extends BasicTabbedPaneUI {
    private class ScrollableTabButton extends JCButton implements UIResource,
            SwingConstants {
        private static final long serialVersionUID = 8901211504611624538L;

        public ScrollableTabButton(int direction) {
            setRequestFocusEnabled(false);

            if (direction == 7) {
                setImage(UIResourceManager.getImage("TabbedPanePreviousImage"));
                setDisabledImage(UIResourceManager
                        .getImage("TabbedPanePreviousDisabledImage"));
                setRolloverImage(UIResourceManager
                        .getImage("TabbedPanePreviousRolloverImage"));
                setPressedImage(UIResourceManager
                        .getImage("TabbedPanePreviousPressedImage"));
            } else if (direction == 3) {
                setImage(UIResourceManager.getImage("TabbedPaneNextImage"));
                setDisabledImage(UIResourceManager
                        .getImage("TabbedPaneNextDisabledImage"));
                setRolloverImage(UIResourceManager
                        .getImage("TabbedPaneNextRolloverImage"));
                setPressedImage(UIResourceManager
                        .getImage("TabbedPaneNextPressedImage"));
            }
        }

        public Dimension getMaximumSize() {
            return new Dimension(2147483647, 2147483647);
        }

        public Dimension getMinimumSize() {
            return new Dimension(5, 5);
        }

        public Dimension getPreferredSize() {
            Image image = getImage();
            return image != null ? new Dimension(image.getWidth(null),
                    image.getHeight(null)) : super.getPreferredSize();
        }

        public boolean isFocusTraversable() {
            return false;
        }
    }

    public class TabbedPaneLayout extends BasicTabbedPaneUI.TabbedPaneLayout {
        public TabbedPaneLayout() {
            super();
        }

        protected void normalizeTabRuns(int tabPlacement, int tabCount,
                                        int start, int max) {
            if ((tabPlacement == 1) || (tabPlacement == 3)) {
                super.normalizeTabRuns(tabPlacement, tabCount, start, max);
            }
        }

        protected void padSelectedTab(int tabPlacement, int selectedIndex) {
        }

        protected void rotateTabRuns(int tabPlacement, int selectedRun) {
        }
    }

    public static ComponentUI createUI(JComponent c) {
        return new CTabbedPaneUI();
    }

    protected int calculateTabHeight(int tabPlacement, int tabIndex,
                                     int fontHeight) {
        if ((this.tabPane instanceof JCTabbedPane)) {
            int tabHeight = ((JCTabbedPane) this.tabPane).getTabHeight();

            if (tabHeight >= 0) {
                return tabHeight;
            }
        }

        return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
    }

    protected LayoutManager createLayoutManager() {
        if (this.tabPane.getTabLayoutPolicy() == 1) {
            return super.createLayoutManager();
        }

        return new TabbedPaneLayout();
    }

    protected JButton createScrollButton(int direction) {
        if ((direction != 5) && (direction != 1) && (direction != 3)
                && (direction != 7)) {
            throw new IllegalArgumentException(
                    "Direction must be one of: SOUTH, NORTH, EAST or WEST");
        }

        return new ScrollableTabButton(direction);
    }

    protected Icon getIconForTab(int tabIndex) {
        return this.tabPane.getIconAt(tabIndex);
    }

    protected int getTabLabelShiftX(int tabPlacement, int tabIndex,
                                    boolean isSelected) {
        return 0;
    }

    protected int getTabLabelShiftY(int tabPlacement, int tabIndex,
                                    boolean isSelected) {
        return 0;
    }

    protected void installDefaults() {
        this.tabRunOverlay = 2;
        this.textIconGap = 5;
        this.tabInsets = new Insets(1, 9, 1, 9);
        this.selectedTabPadInsets = new Insets(2, 2, 2, 1);
        this.contentBorderInsets = new Insets(0, 0, 0, 0);
        this.tabAreaInsets = new Insets(0, 0, 0, 0);
    }

    public void paint(Graphics g, JComponent c) {
        paintTabBarBackground(g);
        super.paint(g, c);
    }

    protected void paintContentBorder(Graphics g, int tabPlacement,
                                      int selectedIndex) {
    }

    protected void paintFocusIndicator(Graphics g, int tabPlacement,
                                       Rectangle[] rects, int tabIndex, Rectangle iconRect,
                                       Rectangle textRect, boolean isSelected) {
    }

    protected void paintIcon(Graphics g, int tabPlacement, int tabIndex,
                             Icon icon, Rectangle iconRect, boolean isSelected) {
        if (icon != null) {
            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();

            if ((!this.tabPane.isEnabled())
                    || (!this.tabPane.isEnabledAt(tabIndex))) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(0.5F));
            }

            icon.paintIcon(this.tabPane, g, iconRect.x, iconRect.y);
            g2d.setComposite(oldComposite);
        }
    }

    protected void paintTabBackground(Graphics g, int tabPlacement,
                                      int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Image image = null;
        boolean tabEnabled = (this.tabPane.isEnabledAt(tabIndex))
                && (this.tabPane.isEnabled());

        if (isSelected) {
            image = UIResourceManager.getImage("TabbedPanePressedImage");
        } else if ((getRolloverTab() == tabIndex) && (tabEnabled)) {
            image = UIResourceManager.getImage("TabbedPaneRolloverImage");
        }

        if (image != null) {
            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();

            if (!tabEnabled) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(0.5F));
            }

            UIUtil.paintImage(g, image, new Insets(3, 1, 1, 1), new Rectangle(
                    x, y, w, h), this.tabPane);
            g2d.setComposite(oldComposite);
        }

        if ((!this.tabPane.isEnabledAt(tabIndex)) && (this.tabPane.isEnabled())
                && (!isSelected)) {
            g.setColor(new Color(255, 255, 255, 127));
            g.fillRect(x, y, w, h);
        }
    }

    protected void paintTabBarBackground(Graphics g) {
        Rectangle rect = null;
        Insets insets = this.tabPane.getInsets();
        int tabPlacement = this.tabPane.getTabPlacement();
        int width = this.tabPane.getWidth();
        int height = this.tabPane.getHeight();
        int tabBarWidth = calculateTabAreaWidth(tabPlacement, this.runCount,
                this.maxTabWidth);
        int tabBarHeight = calculateTabAreaHeight(tabPlacement, this.runCount,
                this.maxTabHeight);

        switch (tabPlacement) {
            case 3:
                rect = new Rectangle(insets.left, height - insets.bottom
                        - tabBarHeight, width - insets.left - insets.right,
                        tabBarHeight);
                break;
            case 2:
                rect = new Rectangle(insets.left, insets.top, tabBarWidth, height
                        - insets.top - insets.bottom);
                break;
            case 4:
                rect = new Rectangle(width - insets.right - tabBarWidth,
                        insets.top, tabBarWidth, height - insets.top
                        - insets.bottom);
                break;
            case 1:
            default:
                rect = new Rectangle(insets.left, insets.top, width - insets.left
                        - insets.right, tabBarHeight);
        }

        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();

        if (!this.tabPane.isEnabled()) {
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5F));
        }

        UIUtil.paintImage(g,
                UIResourceManager.getImage("TabbedPaneBackgroundImage"),
                new Insets(1, 1, 1, 1), rect, this.tabPane);
        g2d.setComposite(oldComposite);
    }

    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
                                  int x, int y, int w, int h, boolean isSelected) {
        int rolloverTab = getRolloverTab();
        boolean tabEnabled = (this.tabPane.isEnabledAt(tabIndex))
                && (this.tabPane.isEnabled());

        if (((rolloverTab == tabIndex) && (tabEnabled)) || (isSelected)) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();

        if (!tabEnabled) {
            g2d.setComposite(AlphaComposite.SrcOver.derive(0.5F));
        }

        if ((tabPlacement == 2) || (tabPlacement == 4)) {
            Image image = UIResourceManager.getImage("TabbedPaneSplitHImage");
            UIUtil.paintImage(g, image, new Insets(2, 1, 2, 1), new Rectangle(
                    x, y + (h - 1), w, 2), this.tabPane);
        } else {
            Image image = UIResourceManager.getImage("TabbedPaneSplitVImage");
            UIUtil.paintImage(g, image, new Insets(2, 1, 2, 1), new Rectangle(x
                    + (w - 2), y, 2, h), this.tabPane);
        }

        g2d.setComposite(oldComposite);
    }

    protected void paintText(Graphics g, int tabPlacement, Font font,
                             FontMetrics metrics, int tabIndex, String title,
                             Rectangle textRect, boolean isSelected) {
        g.setFont(font);
        View view = getTextViewForTab(tabIndex);

        if (view != null) {
            view.paint(g, textRect);
        } else {
            int mnemIndex = this.tabPane.getDisplayedMnemonicIndexAt(tabIndex);

            if ((this.tabPane.isEnabled())
                    && (this.tabPane.isEnabledAt(tabIndex))) {
                g.setColor(this.tabPane.getForegroundAt(tabIndex));
                SwingUtilities2.drawStringUnderlineCharAt(this.tabPane, g,
                        title, mnemIndex, textRect.x,
                        textRect.y + metrics.getAscent());
            } else if ((this.tabPane instanceof JCTabbedPane)) {
                g.setColor(((JCTabbedPane) this.tabPane)
                        .getDisabledForegroundAt(tabIndex));
                SwingUtilities2.drawStringUnderlineCharAt(this.tabPane, g,
                        title, mnemIndex, textRect.x,
                        textRect.y + metrics.getAscent());
            } else {
                super.paintText(g, tabPlacement, font, metrics, tabIndex,
                        title, textRect, isSelected);
            }
        }
    }

    protected void setRolloverTab(int index) {
        int oldRolloverTab = getRolloverTab();
        Rectangle rect1 = null;
        Rectangle rect2 = null;
        super.setRolloverTab(index);

        if ((oldRolloverTab >= 0)
                && (oldRolloverTab < this.tabPane.getTabCount())) {
            rect1 = getTabBounds(this.tabPane, oldRolloverTab);
        }

        if (index >= 0) {
            rect2 = getTabBounds(this.tabPane, index);
        }

        if (rect1 != null) {
            if (rect2 != null) {
                this.tabPane.repaint(rect1.union(rect2));
            } else {
                this.tabPane.repaint(rect1);
            }
        } else if (rect2 != null) {
            this.tabPane.repaint(rect2);
        }
    }

    protected void uninstallDefaults() {
    }
}