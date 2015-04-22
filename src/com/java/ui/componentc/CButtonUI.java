package com.java.ui.componentc;

import com.java.ui.util.UIUtil;
import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class CButtonUI extends BasicButtonUI {
    private static final Object C_BUTTON_UI_KEY = new Object();

    public static ComponentUI createUI(JComponent c) {
        AppContext appContext = AppContext.getAppContext();
        CButtonUI cButtonUI = (CButtonUI) appContext.get(C_BUTTON_UI_KEY);

        if (cButtonUI == null) {
            cButtonUI = new CButtonUI();
            appContext.put(C_BUTTON_UI_KEY, cButtonUI);
        }

        return cButtonUI;
    }

    private int pressMoveX;

    private int pressMoveY;

    protected void installDefaults(AbstractButton b) {
    }

    public void paint(Graphics g, JComponent c) {
        this.pressMoveX = (this.pressMoveY = 0);
        super.paint(g, c);
    }

    protected void paintBackgroundImage(Graphics g, JComponent c) {
        if ((c instanceof JCButton)) {
            JCButton button = (JCButton) c;
            ButtonModel model = button.getModel();
            Image image = button.getImage();
            Image tempImage = null;
            Insets insets = button.getImageInsets();

            if (image == null) {
                return;
            }

            if (!model.isEnabled()) {
                tempImage = button.getDisabledImage();
            } else if ((model.isPressed()) && (model.isArmed())) {
                tempImage = button.getPressedImage();
            } else if ((button.isRolloverEnabled()) && (model.isRollover())) {
                tempImage = button.getRolloverImage();
            } else if ((button.isFocusPainted()) && (button.isFocusable()) && (button.hasFocus())) {
                tempImage = button.getFocusImage();
                insets = button.getFocusImageInsets();
            }

            if (tempImage != null) {
                image = tempImage;
            }

            Rectangle paintRect = new Rectangle(0, 0, button.getWidth(), button.getHeight());
            UIUtil.paintImage(g, image, insets, paintRect, button);
        }
    }

    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if (((b instanceof JCButton)) && (((JCButton) b).isPaintPressDown())) {
            this.pressMoveX = (this.pressMoveY = 1);
        }

        super.paintButtonPressed(g, b);
    }

    protected void paintFocus(Graphics g, AbstractButton button, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
        if (!(button instanceof JCButton)) {
            super.paintFocus(g, button, viewRect, textRect, iconRect);
        }
    }

    protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();

        if ((c instanceof JCButton)) {
            g2d.setComposite(AlphaComposite.SrcOver.derive(((JCButton) c).getAlpha()));
        }

        super.paintIcon(g, c, new Rectangle(iconRect.x + this.pressMoveX, iconRect.y + this.pressMoveY, iconRect.width, iconRect.height));
        g2d.setComposite(oldComposite);
    }

    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        if ((c instanceof JCButton)) {
            JCButton button = (JCButton) c;
            ButtonModel model = button.getModel();
            FontMetrics fm = SwingUtilities2.getFontMetrics(c, g);
            int mnemIndex = button.getDisplayedMnemonicIndex();

            if (model.isEnabled()) {
                g.setColor(button.getForeground());
            } else {
                g.setColor(button.getDisabledTextColor());
            }

            SwingUtilities2.drawStringUnderlineCharAt(c, g, text, mnemIndex, textRect.x + this.pressMoveX, textRect.y + this.pressMoveY + fm.getAscent());
        } else {
            super.paintText(g, c, textRect, text);
        }
    }

    protected void uninstallDefaults(AbstractButton b) {
    }

    public void update(Graphics g, JComponent c) {
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();
        boolean opaque = c.isOpaque();

        if ((c instanceof JCButton)) {
            g2d.setComposite(AlphaComposite.SrcOver.derive(((JCButton) c).getAlpha()));
            opaque = (opaque) || (!((JCButton) c).isImageOnly());
        }

        if (opaque) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }

        paintBackgroundImage(g, c);
        g2d.setComposite(oldComposite);
        paint(g, c);
    }
}