package com.java.ui.componentc;

import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.View;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class CLabelUI extends BasicLabelUI {
    private static final Object EXTEND_LABEL_UI_KEY = new Object();

    private static CLabelUI extendLabelUI = new CLabelUI();

    public static ComponentUI createUI(JComponent c) {
        if (System.getSecurityManager() != null) {
            AppContext appContext = AppContext.getAppContext();
            CLabelUI safeExtendLabelUI = (CLabelUI) appContext.get(EXTEND_LABEL_UI_KEY);

            if (safeExtendLabelUI == null) {
                safeExtendLabelUI = new CLabelUI();
                appContext.put(EXTEND_LABEL_UI_KEY, safeExtendLabelUI);
            }

            return safeExtendLabelUI;
        }

        return extendLabelUI;
    }

    private Rectangle contentBounds = new Rectangle();

    private Rectangle iconR = new Rectangle();

    private Rectangle textR = new Rectangle();

    private String computeContentBounds(JComponent c, Graphics g,
                                        FontMetrics metrics) {
        JLabel label = (JLabel) c;
        String text = label.getText();
        Icon icon = label.isEnabled() ? label.getIcon() : label
                .getDisabledIcon();
        Insets insets = label.getInsets(null);
        String clippedText = null;

        if ((icon == null) && (text == null)) {
            this.contentBounds.setBounds(0, 0, 0, 0);
        } else {
            Rectangle viewR = new Rectangle();
            this.iconR.x = (this.iconR.y = this.iconR.width = this.iconR.height = 0);
            this.textR.x = (this.textR.y = this.textR.width = this.textR.height = 0);
            viewR.x = insets.left;
            viewR.y = insets.top;
            viewR.width = (c.getWidth() - (insets.left + insets.right));
            viewR.height = (c.getHeight() - (insets.top + insets.bottom));
            clippedText = layoutCL(label, metrics, text, icon, viewR, this.iconR, this.textR);
            int x1 = Math.min(this.iconR.x, this.textR.x);
            int x2 = Math.max(this.iconR.x + this.iconR.width, this.textR.x + this.textR.width);
            int y1 = Math.min(this.iconR.y, this.textR.y);
            int y2 = Math.max(this.iconR.y + this.iconR.height, this.textR.y + this.textR.height);
            this.contentBounds.setBounds(x1, y1, x2 - x1, y2 - y1);
        }

        return clippedText;
    }

    private Dimension computeMove(JLabel label, int contentWidth,
                                  int contentHeight, double theta) {
        int ha = label.getHorizontalAlignment();
        int va = label.getVerticalAlignment();
        boolean leftToRight = label.getComponentOrientation().isLeftToRight();
        double moveX = 0.0D;
        double moveY = 0.0D;

        if ((ha == 2) || ((leftToRight) && (ha == 10)) || ((!leftToRight) && (ha == 11))) {
            moveX = (contentWidth - (Math.abs(Math.cos(theta) * contentWidth) + Math.abs(Math.sin(theta) * contentHeight))) / 2.0D;
        } else if ((ha == 4) || ((!leftToRight) && (ha == 10))
                || ((leftToRight) && (ha == 11))) {
            moveX = -(contentWidth - (Math.abs(Math.cos(theta) * contentWidth) + Math.abs(Math.sin(theta) * contentHeight))) / 2.0D;
        }

        if (va == 1) {
            moveY = (contentHeight - (Math.abs(Math.sin(theta) * contentWidth) + Math.abs(Math.cos(theta) * contentHeight))) / 2.0D;
        } else if (va == 3) {
            moveY = -(contentHeight - (Math.abs(Math.sin(theta) * contentWidth) + Math.abs(Math.cos(theta) * contentHeight))) / 2.0D;
        }

        return new Dimension((int) Math.round(moveX), (int) Math.round(moveY));
    }

    protected void installDefaults(JLabel c) {
    }

    public void paint(Graphics g, JComponent c) {
        if ((c instanceof JCLabel)) {
            JCLabel cLabel = (JCLabel) c;
            String text = cLabel.getText();
            Icon icon = cLabel.isEnabled() ? cLabel.getIcon() : cLabel.getDisabledIcon();

            if ((icon == null) && (text == null)) {
                return;
            }

            Graphics2D g2d = (Graphics2D) g;
            FontMetrics metrics = SwingUtilities2.getFontMetrics(cLabel, g2d);
            Composite oldComposite = g2d.getComposite();
            String clippedText = computeContentBounds(c, g2d, metrics);
            int contentWidth = this.contentBounds.width;
            int contentHeight = this.contentBounds.height;
            double theta = Math.toRadians(cLabel.getAngle() % 360.0D);
            double rotateX = this.contentBounds.x + contentWidth / 2.0D;
            double rotateY = this.contentBounds.y + contentHeight / 2.0D;
            Dimension move = computeMove(cLabel, contentWidth, contentHeight,
                    theta);
            AffineTransform trans = g2d.getTransform();
            trans.translate(-move.width, -move.height);
            trans.rotate(theta, rotateX, rotateY);
            g2d.setTransform(trans);

            if ((icon != null) && (cLabel.getIconAlpha() > 0.0F)) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(cLabel.getIconAlpha()));
                icon.paintIcon(c, g2d, this.iconR.x, this.iconR.y);
            }

            if (text != null) {
                View view = (View) c.getClientProperty("html");
                g2d.translate(cLabel.getDeltaX(), cLabel.getDeltaY());

                if (view != null) {
                    view.paint(g2d, this.textR);
                } else if (cLabel.getTextAlpha() > 0.0F) {
                    int textX = this.textR.x;
                    int textY = this.textR.y + metrics.getAscent();
                    g2d.setComposite(AlphaComposite.SrcOver.derive(cLabel
                            .getTextAlpha()));

                    if (cLabel.isEnabled()) {
                        paintEnabledText(cLabel, g2d, clippedText, textX, textY);
                    } else {
                        paintDisabledText(cLabel, g2d, clippedText, textX,
                                textY);
                    }
                }

                g2d.translate(-cLabel.getDeltaX(), -cLabel.getDeltaY());
            }

            trans.rotate(-theta, rotateX, rotateY);
            trans.translate(move.width, move.height);
            g2d.setTransform(trans);
            g2d.setComposite(oldComposite);
        } else {
            super.paint(g, c);
        }
    }

    protected void paintDisabledText(JLabel label, Graphics g, String text,
                                     int textX, int textY) {
        if ((label instanceof JCLabel)) {
            JCLabel cLabel = (JCLabel) label;
            int mnemIndex = cLabel.getDisplayedMnemonicIndex();
            g.setColor(cLabel.getDisabledForeground());
            SwingUtilities2.drawStringUnderlineCharAt(cLabel, g, text, mnemIndex, textX, textY);
        } else {
            super.paintDisabledText(label, g, text, textX, textY);
        }
    }

    protected void paintEnabledText(JLabel label, Graphics g, String text,
                                    int textX, int textY) {
        int mnemIndex = label.getDisplayedMnemonicIndex();
        g.setColor(label.getForeground());
        SwingUtilities2.drawStringUnderlineCharAt(label, g, text, mnemIndex,
                textX, textY);
    }

    public void update(Graphics g, JComponent c) {
        if ((c instanceof JCLabel)) {
            JCLabel cLabel = (JCLabel) c;

            if (cLabel.getBackgroundAlpha() > 0.0F) {
                Graphics2D g2d = (Graphics2D) g;
                Composite oldComposite = g2d.getComposite();
                g2d.setComposite(AlphaComposite.SrcOver.derive(cLabel
                        .getBackgroundAlpha()));
                g2d.setColor(c.getBackground());
                g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
                g2d.setComposite(oldComposite);
            }

            paint(g, c);
        } else {
            super.update(g, c);
        }
    }
}