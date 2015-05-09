package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.MouseEvent;

public class CMenuUI extends BasicMenuUI {
    protected class CMouseInputHandler extends BasicMenuUI.MouseInputHandler {
        protected CMouseInputHandler() {
            super();
        }

        public void mouseEntered(MouseEvent evt) {
            JMenu menu = (JMenu) evt.getSource();

            if ((!(menu instanceof JCMenu))
                    || (((JCMenu) menu).isShowWhenRollover())) {
                super.mouseEntered(evt);
            }

            if ((menu.isTopLevelMenu()) && (menu.isRolloverEnabled())) {
                menu.getModel().setRollover(true);
                CMenuUI.this.menuItem.repaint();
            }
        }

        public void mouseExited(MouseEvent evt) {
            JMenu menu = (JMenu) evt.getSource();

            if ((!(menu instanceof JCMenu))
                    || (((JCMenu) menu).isShowWhenRollover())) {
                super.mouseExited(evt);
            }

            if (menu.isRolloverEnabled()) {
                menu.getModel().setRollover(false);
                CMenuUI.this.menuItem.repaint();
            }
        }
    }

    private static Rectangle arrowIconRect = new Rectangle();
    protected static final Icon DEFAULT_ARROW_ICON = UIResourceManager
            .getIcon("MenuArrowIcon");
    protected static final int DEFAULT_ARROW_LEFT_GAP = 28;
    protected static final int DEFAULT_ARROW_RIGHT_GAP = 8;
    protected static final int DEFAULT_GAP = 5;
    protected static final int DEFAULT_LEFT_GAP = 35;

    protected static final int ICON_AREA_WIDTH = 25;

    private static Rectangle iconRect = new Rectangle();

    private static Rectangle rect = new Rectangle();

    private static Rectangle textRect = new Rectangle();

    private static Rectangle viewRect = new Rectangle(32767, 32767);

    private static Rectangle zeroRect = new Rectangle(0, 0, 0, 0);

    public static ComponentUI createUI(JComponent c) {
        return new CMenuUI();
    }

    private void addMaxWidth(JComponent parent, String propertyName,
                             int curWidth) {
        Integer maxWidth = null;

        if (parent != null) {
            maxWidth = (Integer) parent.getClientProperty(propertyName);
        }

        if (maxWidth == null) {
            maxWidth = Integer.valueOf(0);
        }

        if (curWidth > maxWidth.intValue()) {
            maxWidth = Integer.valueOf(curWidth);

            if (parent != null) {
                parent.putClientProperty(propertyName, maxWidth);
            }
        }

        if (maxWidth.intValue() > 0) {
            rect.width += maxWidth.intValue();
        }
    }

    protected MouseInputListener createMouseInputListener(JComponent c) {
        return new CMouseInputHandler();
    }

    private JComponent getMenuParent(JMenu menu) {
        Container parent = menu.getParent();

        if (((parent instanceof JComponent)) && (!menu.isTopLevelMenu())) {
            return (JComponent) parent;
        }

        return null;
    }

    public Dimension getPreferredSize(JComponent c) {
        JMenu menu = (JMenu) c;
        String text = menu.getText();
        Font font = menu.getFont();
        FontMetrics fm = menu.getFontMetrics(font);
        JComponent parent = getMenuParent(menu);
        Icon icon = menu.getIcon();
        int iconHeight = icon == null ? 0 : icon.getIconHeight();

        resetRects();
        layoutMenu(fm, text, menu.getVerticalAlignment(),
                menu.getHorizontalAlignment(), menu.getVerticalTextPosition(),
                menu.getHorizontalTextPosition(), viewRect, iconRect,
                arrowIconRect, textRect);
        addMaxWidth(parent, "maxTextWidth", textRect.width);
        rect.height = max(new int[]{textRect.height, iconHeight,
                DEFAULT_ARROW_ICON.getIconHeight()});

        if (menu.isTopLevelMenu()) {
            rect.width += 10;
        } else {
            rect.width += 71 + DEFAULT_ARROW_ICON.getIconWidth();
        }

        return rect.getSize();
    }

    protected void installDefaults() {
    }

    protected void layoutIcon(Rectangle iconRect, Rectangle arrowIconRect) {
        JMenu menu = (JMenu) this.menuItem;

        if (!menu.isTopLevelMenu()) {
            Icon icon = this.menuItem.getIcon();

            if (icon != null) {
                int iconWidth = icon.getIconWidth();
                int iconHeight = icon.getIconHeight();
                iconRect.x = (int) Math.round((25 - iconWidth) / 2.0D);
                iconRect.y = (int) Math
                        .round((this.menuItem.getHeight() - iconHeight) / 2.0D);
                iconRect.width = iconWidth;
                iconRect.height = iconHeight;
            }

            icon = DEFAULT_ARROW_ICON;
            int iconWidth = icon.getIconWidth();
            int iconHeight = icon.getIconHeight();
            arrowIconRect.x = (this.menuItem.getWidth() - iconWidth - 8);
            arrowIconRect.y = (int) Math
                    .round((this.menuItem.getHeight() - iconHeight) / 2.0D);
            arrowIconRect.width = iconWidth;
            arrowIconRect.height = iconHeight;
        }
    }

    private String layoutMenu(FontMetrics fm, String text,
                              int verticalAlignment, int horizontalAlignment,
                              int verticalTextPosition, int horizontalTextPosition,
                              Rectangle viewRect, Rectangle iconRect, Rectangle arrowIconRect,
                              Rectangle textRect) {
        SwingUtilities.layoutCompoundLabel(this.menuItem, fm, text, null,
                verticalAlignment, horizontalAlignment, verticalTextPosition,
                horizontalTextPosition, viewRect, iconRect, textRect, 0);
        textRect.x += (((JMenu) this.menuItem).isTopLevelMenu() ? 5 : 35);
        layoutIcon(iconRect, arrowIconRect);
        return text;
    }

    private int max(int[] values) {
        int maxValue = -2147483648;

        for (int value : values) {
            if (value <= maxValue)
                continue;
            maxValue = value;
        }

        return maxValue;
    }

    public void paint(Graphics g, JComponent c) {
        JMenu menu = (JMenu) c;
        Font font = c.getFont();
        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, font);
        resetRects();
        viewRect.setBounds(0, 0, menu.getWidth(), menu.getHeight());
        String text = layoutMenu(fm, menu.getText(),
                menu.getVerticalAlignment(), menu.getHorizontalAlignment(),
                menu.getVerticalTextPosition(),
                menu.getHorizontalTextPosition(), viewRect, iconRect,
                arrowIconRect, textRect);
        paintBackground(g, menu);

        if (!menu.isTopLevelMenu()) {
            paintIcon(g, menu, iconRect, arrowIconRect);
        }

        if (text != null) {
            View view = (View) c.getClientProperty("html");
            g.setFont(font);

            if (view != null) {
                view.paint(g, textRect);
            } else {
                paintText(g, menu, textRect, text);
            }
        }
    }

    protected void paintBackground(Graphics g, JMenu menu) {
        ButtonModel model = menu.getModel();

        if ((menu.isEnabled())
                && ((model.isSelected()) || ((menu.isTopLevelMenu()) && (model
                .isRollover())))) {
            UIUtil.paintImage(g, UIResourceManager
                    .getImage("MenuItemBackgroundImage"),
                    new Insets(1, 1, 1, 1), new Rectangle(0, 0,
                    menu.getWidth(), menu.getHeight()), menu);
        }
    }

    protected void paintIcon(Graphics g, JMenu menu, Rectangle iconRect,
                             Rectangle arrowIconRect) {
        ButtonModel model = menu.getModel();
        Icon icon = menu.getIcon();
        boolean existDisabledIcon = true;
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();
        Composite composite = AlphaComposite.SrcOver.derive(0.5F);

        if (icon != null) {
            if (!model.isEnabled()) {
                icon = menu.getDisabledIcon();

                if (icon == null) {
                    icon = menu.getIcon();
                    existDisabledIcon = false;
                }
            } else if ((model.isPressed()) && (model.isArmed())) {
                icon = menu.getPressedIcon();

                if (icon == null) {
                    icon = menu.getIcon();
                }
            }

            if (icon != null) {
                if (!existDisabledIcon) {
                    g2d.setComposite(composite);
                }

                icon.paintIcon(menu, g2d, iconRect.x, iconRect.y);
                g2d.setComposite(oldComposite);
            }
        }

        if (!menu.isEnabled()) {
            g2d.setComposite(composite);
        }

        DEFAULT_ARROW_ICON.paintIcon(menu, g2d, arrowIconRect.x,
                arrowIconRect.y);
        g2d.setComposite(oldComposite);
    }

    protected void paintText(Graphics g, JMenu menu, Rectangle textRect,
                             String text) {
        ButtonModel model = menu.getModel();
        FontMetrics fm = SwingUtilities2.getFontMetrics(menu, g);
        int mnemIndex = menu.getDisplayedMnemonicIndex();
        Color color = null;

        if ((menu instanceof JCMenu)) {
            JCMenu cMenu = (JCMenu) menu;

            if (!model.isEnabled()) {
                color = cMenu.getDisabledTextColor();
            } else if ((model.isSelected())
                    || ((menu.isTopLevelMenu()) && (model.isRollover()))) {
                color = cMenu.getSelectedForeground();
            } else {
                color = cMenu.getForeground();
            }

        }

        if (color != null) {
            g.setColor(color);
            SwingUtilities2.drawStringUnderlineCharAt(menu, g, text, mnemIndex,
                    textRect.x, textRect.y + fm.getAscent());
        } else {
            super.paintText(g, menu, textRect, text);
        }
    }

    private void resetRects() {
        iconRect.setBounds(zeroRect);
        arrowIconRect.setBounds(zeroRect);
        textRect.setBounds(zeroRect);
        viewRect.setBounds(0, 0, 32767, 32767);
        rect.setBounds(zeroRect);
    }

    protected void uninstallDefaults() {
        this.menuItem.setArmed(false);
        this.menuItem.setSelected(false);
        this.menuItem.resetKeyboardActions();
    }
}