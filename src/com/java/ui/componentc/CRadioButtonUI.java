package com.java.ui.componentc;

import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.text.View;
import java.awt.*;

public class CRadioButtonUI extends BasicRadioButtonUI {
    private static final Object C_RADIO_BUTTON_UI_KEY = new Object();

    public static ComponentUI createUI(JComponent c) {
        AppContext appContext = AppContext.getAppContext();
        CRadioButtonUI cRadioButtonUI = (CRadioButtonUI) appContext
                .get(C_RADIO_BUTTON_UI_KEY);

        if (cRadioButtonUI == null) {
            cRadioButtonUI = new CRadioButtonUI();
            appContext.put(C_RADIO_BUTTON_UI_KEY, cRadioButtonUI);
        }

        return cRadioButtonUI;
    }

    private Rectangle iconRect = new Rectangle();

    private Dimension size = new Dimension();

    private Rectangle textRect = new Rectangle();

    private Rectangle viewRect = new Rectangle();

    public void installDefaults(AbstractButton b) {
    }

    public synchronized void paint(Graphics g, JComponent c) {
        AbstractButton button = (AbstractButton) c;
        ButtonModel model = button.getModel();
        Font font = c.getFont();
        g.setFont(font);
        FontMetrics fm = SwingUtilities2.getFontMetrics(c, g, font);
        Insets insets = c.getInsets();
        this.size = button.getSize(this.size);
        this.viewRect.x = insets.left;
        this.viewRect.y = insets.top;
        this.viewRect.width = (this.size.width - (insets.right + this.viewRect.x));
        this.viewRect.height = (this.size.height - (insets.bottom + this.viewRect.y));
        this.iconRect.x = (this.iconRect.y = this.iconRect.width = this.iconRect.height = 0);
        this.textRect.x = (this.textRect.y = this.textRect.width = this.textRect.height = 0);
        Icon altIcon = button.getIcon();
        String text = SwingUtilities.layoutCompoundLabel(c, fm, button
                .getText(), altIcon != null ? altIcon : getDefaultIcon(),
                button.getVerticalAlignment(), button.getHorizontalAlignment(),
                button.getVerticalTextPosition(), button
                .getHorizontalTextPosition(), this.viewRect,
                this.iconRect, this.textRect, button.getText() == null ? 0
                : button.getIconTextGap());

        if (altIcon != null) {
            if (!model.isEnabled()) {
                if (model.isSelected()) {
                    altIcon = button.getDisabledSelectedIcon();
                } else {
                    altIcon = button.getDisabledIcon();
                }
            } else if ((model.isPressed()) && (model.isArmed())) {
                altIcon = button.getPressedIcon();

                if (((button instanceof JCRadioButton)) && (model.isSelected())) {
                    altIcon = ((JCRadioButton) button).getPressedSelectedIcon();
                }

                if (altIcon == null) {
                    altIcon = button.getSelectedIcon();
                }
            } else if (model.isSelected()) {
                if ((button.isRolloverEnabled()) && (model.isRollover())) {
                    altIcon = button.getRolloverSelectedIcon();

                    if (altIcon == null) {
                        altIcon = button.getSelectedIcon();
                    }
                } else {
                    altIcon = button.getSelectedIcon();
                }
            } else if ((button.isRolloverEnabled()) && (model.isRollover())) {
                altIcon = button.getRolloverIcon();
            }

            if (altIcon == null) {
                altIcon = button.getIcon();
            }

            altIcon.paintIcon(c, g, this.iconRect.x, this.iconRect.y);
        } else {
            getDefaultIcon().paintIcon(c, g, this.iconRect.x, this.iconRect.y);
        }

        if (text != null) {
            View view = (View) c.getClientProperty("html");

            if (view != null) {
                view.paint(g, this.textRect);
            } else {
                paintText(g, button, this.textRect, text);
            }

            if ((button.hasFocus()) && (button.isFocusPainted())
                    && (this.textRect.width > 0) && (this.textRect.height > 0)) {
                paintFocus(g, this.textRect, this.size);
            }
        }
    }

    protected void paintText(Graphics g, JComponent c, Rectangle textRect,
                             String text) {
        if ((c instanceof JCRadioButton)) {
            JCRadioButton radioButton = (JCRadioButton) c;
            ButtonModel model = radioButton.getModel();
            FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
            int mnemIndex = radioButton.getDisplayedMnemonicIndex();

            if (model.isEnabled()) {
                g.setColor(radioButton.getForeground());
            } else {
                g.setColor(radioButton.getDisabledTextColor());
            }

            SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemIndex,
                    textRect.x, textRect.y + fm.getAscent());
        } else {
            super.paintText(g, c, textRect, text);
        }
    }

    protected void uninstallDefaults(AbstractButton b) {
    }
}