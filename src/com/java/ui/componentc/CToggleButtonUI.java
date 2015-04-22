package com.java.ui.componentc;

import com.java.ui.util.UIUtil;
import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import java.awt.*;

public class CToggleButtonUI extends BasicToggleButtonUI {
    private static final Object C_TOGGLE_BUTTON_UI_KEY = new Object();

    public static ComponentUI createUI(JComponent c) {
        AppContext appContext = AppContext.getAppContext();
        CToggleButtonUI cToggleButtonUI = (CToggleButtonUI) appContext.get(C_TOGGLE_BUTTON_UI_KEY);

        if (cToggleButtonUI == null) {
            cToggleButtonUI = new CToggleButtonUI();
            appContext.put(C_TOGGLE_BUTTON_UI_KEY, cToggleButtonUI);
        }

        return cToggleButtonUI;
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
        if ((c instanceof JCToggleButton)) {
            JCToggleButton button = (JCToggleButton) c;
            ButtonModel model = button.getModel();
            Image image = button.getImage();
            Image tempImage = null;

            if (image == null) {
                return;
            }

            if (!model.isEnabled()) {
                tempImage = model.isSelected() ? button.getDisabledSelectedImage() : button.getDisabledImage();
            } else if ((model.isPressed()) && (model.isArmed())) {
                tempImage = button.getPressedImage();

                if (tempImage == null) {
                    tempImage = button.getSelectedImage();
                }
            } else if (model.isSelected()) {
                if ((button.isRolloverEnabled()) && (model.isRollover())) {
                    tempImage = button.getRolloverSelectedImage();

                    if (tempImage == null) {
                        tempImage = button.getSelectedImage();
                    }
                } else {
                    tempImage = button.getSelectedImage();
                }
            } else if ((button.isRolloverEnabled()) && (model.isRollover())) {
                tempImage = button.getRolloverImage();
            }

            if (tempImage != null) {
                image = tempImage;
            }

            Rectangle paintRect = new Rectangle(0, 0, button.getWidth(), button.getHeight());
            UIUtil.paintImage(g, image, button.getImageInsets(), paintRect, button);
        }
    }

    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        if (((b instanceof JCToggleButton)) && (((JCToggleButton) b).isPaintPressDown())) {
            this.pressMoveX = (this.pressMoveY = 1);
        }

        super.paintButtonPressed(g, b);
    }

    protected void paintFocus(Graphics g, AbstractButton button, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
        if (!(button instanceof JCToggleButton)) {
            super.paintFocus(g, button, viewRect, textRect, iconRect);
        }
    }

    protected void paintIcon(Graphics g, JComponent c, Rectangle iconRect) {
        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();

        if ((c instanceof JCToggleButton)) {
            g2d.setComposite(AlphaComposite.SrcOver.derive(((JCToggleButton) c).getAlpha()));
        }

        super.paintIcon(g, c, new Rectangle(iconRect.x + this.pressMoveX, iconRect.y + this.pressMoveY, iconRect.width, iconRect.height));
        g2d.setComposite(oldComposite);
    }

    protected void paintText(Graphics g, JComponent c, Rectangle textRect, String text) {
        if ((c instanceof JCToggleButton)) {
            JCToggleButton button = (JCToggleButton) c;
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

        if ((c instanceof JCToggleButton)) {
            g2d.setComposite(AlphaComposite.SrcOver.derive(((JCToggleButton) c).getAlpha()));
            opaque = (opaque) || (!((JCToggleButton) c).isImageOnly());
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