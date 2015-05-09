package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuItemUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.event.KeyEvent;

public class CMenuItemUI extends BasicMenuItemUI {
    private static Rectangle acceleratorRect = new Rectangle();
    protected static final int DEFAULT_ACC_GAP = 5;
    protected static final int DEFAULT_LEFT_GAP = 35;
    protected static final int DEFAULT_RIGHT_GAP = 20;
    protected static final int ICON_AREA_WIDTH = 25;

    private static Rectangle iconRect = new Rectangle();

    private static Rectangle rect = new Rectangle();

    private static Rectangle textRect = new Rectangle();

    private static Rectangle viewRect = new Rectangle(32767, 32767);

    private static Rectangle zeroRect = new Rectangle(0, 0, 0, 0);

    public static ComponentUI createUI(JComponent c) {
        return new CMenuItemUI();
    }

    private String acceleratorDelimiter;

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

    private JComponent getMenuItemParent(JMenuItem item) {
        Container parent = item.getParent();

        if ((parent instanceof JComponent)) {
            return (JComponent) parent;
        }

        return null;
    }

    public Dimension getPreferredSize(JComponent c) {
        JMenuItem item = (JMenuItem) c;
        String text = item.getText();
        KeyStroke accelerator = item.getAccelerator();
        String acceleratorText = "";
        Font font = item.getFont();
        FontMetrics fm = item.getFontMetrics(font);
        FontMetrics fmAccel = item.getFontMetrics(this.acceleratorFont);
        JComponent parent = getMenuItemParent(this.menuItem);
        Icon icon = item.getIcon();
        int iconHeight = icon == null ? 0 : icon.getIconHeight();

        if (accelerator != null) {
            int modifiers = accelerator.getModifiers();
            int keyCode = accelerator.getKeyCode();

            if (modifiers > 0) {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers)
                        + this.acceleratorDelimiter;
            }

            if (keyCode != 0) {
                acceleratorText = acceleratorText
                        + KeyEvent.getKeyText(keyCode);
            } else {
                acceleratorText = acceleratorText + accelerator.getKeyChar();
            }
        }

        resetRects();
        layoutMenuItem(fm, text, fmAccel, acceleratorText,
                item.getVerticalAlignment(), item.getHorizontalAlignment(),
                item.getVerticalTextPosition(),
                item.getHorizontalTextPosition(), viewRect, iconRect, textRect,
                acceleratorRect);
        addMaxWidth(parent, "maxTextWidth", textRect.width);
        addMaxWidth(parent, "maxAccWidth", acceleratorRect.width
                + (acceleratorRect.width > 0 ? 5 : 0));
        rect.width += 55;
        rect.height = max(new int[]{textRect.height, acceleratorRect.height,
                iconHeight});
        return rect.getSize();
    }

    protected Color getTextColor() {
        ButtonModel model = this.menuItem.getModel();
        Color color = null;

        if ((this.menuItem instanceof JCMenuItem)) {
            JCMenuItem cItem = (JCMenuItem) this.menuItem;

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

    protected void installDefaults() {
        this.acceleratorFont = UIUtil.getDefaultFont();
        this.acceleratorDelimiter = "+";
    }

    protected void layoutIcon(Rectangle iconRect) {
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
    }

    private String layoutMenuItem(FontMetrics fm, String text,
                                  FontMetrics fmAccel, String acceleratorText, int verticalAlignment,
                                  int horizontalAlignment, int verticalTextPosition,
                                  int horizontalTextPosition, Rectangle viewRect, Rectangle iconRect,
                                  Rectangle textRect, Rectangle acceleratorRect) {
        SwingUtilities.layoutCompoundLabel(this.menuItem, fm, text, null,
                verticalAlignment, horizontalAlignment, verticalTextPosition,
                horizontalTextPosition, viewRect, iconRect, textRect, 0);

        if ((acceleratorText == null) || (acceleratorText.equals(""))) {
            acceleratorRect.width = (acceleratorRect.height = 0);
            acceleratorText = "";
        } else {
            acceleratorRect.width = SwingUtilities2.stringWidth(this.menuItem,
                    fmAccel, acceleratorText);
            acceleratorRect.height = fmAccel.getHeight();
        }

        acceleratorRect.x = (viewRect.x + viewRect.width
                - acceleratorRect.width - 20);
        acceleratorRect.y = (textRect.y + textRect.height / 2 - acceleratorRect.height / 2);
        textRect.x += 35;
        layoutIcon(iconRect);
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
        JMenuItem item = (JMenuItem) c;
        Font font = c.getFont();
        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, font);
        FontMetrics fmAccel = SwingUtilities2.getFontMetrics(c, g,
                this.acceleratorFont);
        KeyStroke accelerator = item.getAccelerator();
        String acceleratorText = "";

        resetRects();
        viewRect.setBounds(0, 0, item.getWidth(), item.getHeight());

        if (accelerator != null) {
            int modifiers = accelerator.getModifiers();
            int keyCode = accelerator.getKeyCode();

            if (modifiers > 0) {
                acceleratorText = KeyEvent.getKeyModifiersText(modifiers)
                        + this.acceleratorDelimiter;
            }

            if (keyCode != 0) {
                acceleratorText = acceleratorText
                        + KeyEvent.getKeyText(keyCode);
            } else {
                acceleratorText = acceleratorText + accelerator.getKeyChar();
            }
        }

        String text = layoutMenuItem(fm, item.getText(), fmAccel,
                acceleratorText, item.getVerticalAlignment(),
                item.getHorizontalAlignment(), item.getVerticalTextPosition(),
                item.getHorizontalTextPosition(), viewRect, iconRect, textRect,
                acceleratorRect);

        paintBackground(g, item);
        paintIcon(g, item, iconRect);

        if (text != null) {
            View view = (View) c.getClientProperty("html");
            g.setFont(font);

            if (view != null) {
                view.paint(g, textRect);
            } else {
                paintText(g, item, textRect, text);
            }
        }

        if ((acceleratorText != null) && (!acceleratorText.equals(""))) {
            paintAcceleratorText(g, item, acceleratorRect, acceleratorText);
        }
    }

    protected void paintAcceleratorText(Graphics g, JMenuItem menuItem,
                                        Rectangle acceleratorRect, String acceleratorText) {
        ButtonModel model = menuItem.getModel();
        FontMetrics fmAccel = SwingUtilities2.getFontMetrics(menuItem, g,
                this.acceleratorFont);
        boolean isCItem = menuItem instanceof JCMenuItem;
        JCMenuItem cItem = isCItem ? (JCMenuItem) menuItem : null;
        JComponent parent = getMenuItemParent(menuItem);
        Color color = null;
        int accOffset = 0;

        if (parent != null) {
            Integer maxValueInt = (Integer) parent
                    .getClientProperty("maxAccWidth");
            int maxValue = maxValueInt != null ? maxValueInt.intValue()
                    : acceleratorRect.width;
            accOffset = maxValue - acceleratorRect.width;
        }

        if (!model.isEnabled()) {
            color = this.disabledForeground == null ? Color.GRAY
                    : isCItem ? cItem.getDisabledTextColor()
                    : this.disabledForeground;
        } else if (model.isArmed()) {
            color = isCItem ? cItem.getSelectedForeground()
                    : this.acceleratorSelectionForeground;
        } else {
            color = isCItem ? cItem.getForeground()
                    : this.acceleratorForeground;
        }

        g.setFont(this.acceleratorFont);
        g.setColor(color);
        SwingUtilities2.drawString(menuItem, g, acceleratorText,
                acceleratorRect.x - accOffset + 5,
                acceleratorRect.y + fmAccel.getAscent());
    }

    protected void paintBackground(Graphics g, JMenuItem menuItem) {
        ButtonModel model = menuItem.getModel();

        if ((menuItem.isEnabled()) && (model.isArmed())) {
            UIUtil.paintImage(
                    g,
                    UIResourceManager.getImage("MenuItemBackgroundImage"),
                    new Insets(1, 1, 1, 1),
                    new Rectangle(0, 0, menuItem.getWidth(), menuItem
                            .getHeight()), menuItem);
        }
    }

    protected void paintIcon(Graphics g, JMenuItem menuItem, Rectangle iconRect) {
        ButtonModel model = menuItem.getModel();
        Icon icon = menuItem.getIcon();
        boolean existDisabledIcon = true;

        if (icon != null) {
            if (!model.isEnabled()) {
                icon = menuItem.getDisabledIcon();

                if (icon == null) {
                    icon = menuItem.getIcon();
                    existDisabledIcon = false;
                }
            } else if ((model.isPressed()) && (model.isArmed())) {
                icon = menuItem.getPressedIcon();

                if (icon == null) {
                    icon = menuItem.getIcon();
                }
            }

            if (icon != null) {
                Graphics2D g2d = (Graphics2D) g;
                Composite oldComposite = g2d.getComposite();

                if (!existDisabledIcon) {
                    g2d.setComposite(AlphaComposite.SrcOver.derive(0.5F));
                }

                icon.paintIcon(menuItem, g2d, iconRect.x, iconRect.y);
                g2d.setComposite(oldComposite);
            }
        }
    }

    protected void paintText(Graphics g, JMenuItem menuItem,
                             Rectangle textRect, String text) {
        FontMetrics fm = SwingUtilities2.getFontMetrics(menuItem, g);
        int mnemIndex = menuItem.getDisplayedMnemonicIndex();
        Color color = getTextColor();

        if (color != null) {
            g.setColor(color);
            SwingUtilities2.drawStringUnderlineCharAt(menuItem, g, text,
                    mnemIndex, textRect.x, textRect.y + fm.getAscent());
        } else {
            super.paintText(g, menuItem, textRect, text);
        }
    }

    private void resetRects() {
        iconRect.setBounds(zeroRect);
        textRect.setBounds(zeroRect);
        acceleratorRect.setBounds(zeroRect);
        viewRect.setBounds(0, 0, 32767, 32767);
        rect.setBounds(zeroRect);
    }

    protected void uninstallDefaults() {
    }
}