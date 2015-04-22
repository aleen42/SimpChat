package com.java.ui.component;

import com.java.ui.util.UIResourceManager;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class Round3DProgressBarUI extends BasicProgressBarUI {
    private static final float[] FRACTIONS = {0.0F, 0.55F, 1.0F};

    public static ComponentUI createUI(JComponent c) {
        return new Round3DProgressBarUI();
    }

    private Color[] colors = new Color[3];
    private Paint paint;
    private int startX;

    private int startY;

    private void createColors(Color baseColor) {
        int red = (int) Math.round(baseColor.getRed() * 0.25D + 191.25D);
        int green = (int) Math.round(baseColor.getGreen() * 0.25D + 191.25D);
        int blue = (int) Math.round(baseColor.getBlue() * 0.25D + 191.25D);
        Color tmp94_91 = new Color(red, green, blue);
        this.colors[2] = tmp94_91;
        this.colors[0] = tmp94_91;
        this.colors[1] = baseColor;
    }

    protected Rectangle getBox(Rectangle r) {
        Rectangle rect = super.getBox(r);

        if (rect != null) {
            if (this.progressBar.getOrientation() == 0) {
                rect.x = this.startX;
            } else {
                rect.y = this.startY;
            }
        }

        return rect;
    }

    protected int getBoxLength(int availableLength, int otherDimension) {
        Insets insets = this.progressBar.getInsets();
        int barRectWidth = this.progressBar.getWidth()
                - (insets.right + insets.left);
        int barRectHeight = this.progressBar.getHeight()
                - (insets.top + insets.bottom);
        int step = getStep();
        int frameCount = getFrameCount() / 2;
        int currentFrame = getAnimationIndex() % frameCount;
        int baseLength = (int) Math.round(availableLength / 6.0D);
        int boxLength = baseLength;

        if (this.progressBar.getOrientation() == 0) {
            int endX = currentFrame * step;

            if (endX < baseLength) {
                this.startX = 0;
            } else {
                this.startX = (endX - baseLength);

                if (endX > barRectWidth) {
                    this.startX = (this.startX > barRectWidth ? barRectWidth
                            : this.startX);
                    endX = barRectWidth;
                }
            }

            boxLength = endX - this.startX;
        } else {
            this.startY = (barRectHeight - currentFrame * step);
            int endY;

            if (this.startY > barRectHeight - baseLength) {
                endY = barRectHeight;
            } else {
                endY = this.startY + baseLength;

                if (this.startY < 0) {
                    endY = endY < 0 ? 0 : endY;
                    this.startY = 0;
                }
            }

            boxLength = endY - this.startY;
        }

        return boxLength;
    }

    private int getStep() {
        Insets insets = this.progressBar.getInsets();
        double frameCount = getFrameCount() / 2.0D;
        int barRectWidth = this.progressBar.getWidth()
                - (insets.right + insets.left);
        int barRectHeight = this.progressBar.getHeight()
                - (insets.top + insets.bottom);
        int step = this.progressBar.getOrientation() == 0 ? barRectWidth
                : barRectHeight;
        step = (int) Math.round((step + step / 6.0D) / frameCount);
        return step;
    }

    private void paintBackground(Graphics2D g2d, Insets insets,
                                 int barRectWidth, int barRectHeight) {
        if (this.progressBar.getOrientation() == 0) {
            createColors(this.progressBar.getBackground());
            this.paint = new LinearGradientPaint(0.0F, insets.top, 0.0F,
                    barRectHeight + insets.top, FRACTIONS, this.colors);
            g2d.setPaint(this.paint);
            g2d.fillRoundRect(insets.left, insets.top, barRectWidth,
                    barRectHeight, barRectHeight, barRectHeight);
        } else {
            createColors(this.progressBar.getBackground());
            this.paint = new LinearGradientPaint(barRectWidth + insets.left,
                    0.0F, insets.left, 0.0F, FRACTIONS, this.colors);
            g2d.setPaint(this.paint);
            g2d.fillRoundRect(insets.left, insets.top, barRectWidth,
                    barRectHeight, barRectWidth, barRectWidth);
        }
    }

    protected void paintDeterminate(Graphics g, JComponent c) {
        if (!(g instanceof Graphics2D)) {
            return;
        }

        Insets insets = this.progressBar.getInsets();
        int barRectWidth = this.progressBar.getWidth()
                - (insets.right + insets.left);
        int barRectHeight = this.progressBar.getHeight()
                - (insets.top + insets.bottom);

        if ((barRectWidth <= 0) || (barRectHeight <= 0)) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        Paint oldPaint = g2d.getPaint();
        RenderingHints oldHints = g2d.getRenderingHints();
        int amountFull = getAmountFull(insets, barRectWidth, barRectHeight);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        paintBackground(g2d, insets, barRectWidth, barRectHeight);

        if (this.progressBar.getOrientation() == 0) {
            createColors(this.progressBar.getForeground());
            this.paint = new LinearGradientPaint(0.0F, insets.top, 0.0F,
                    barRectHeight + insets.top, FRACTIONS, this.colors);
            g2d.setPaint(this.paint);
            g2d.fillRoundRect(insets.left, insets.top, amountFull,
                    barRectHeight, barRectHeight, barRectHeight);
        } else {
            createColors(this.progressBar.getForeground());
            this.paint = new LinearGradientPaint(barRectWidth + insets.left,
                    0.0F, insets.left, 0.0F, FRACTIONS, this.colors);
            g2d.setPaint(this.paint);
            g2d.fillRoundRect(insets.left, insets.top
                    + (barRectHeight - amountFull), barRectWidth, amountFull,
                    barRectWidth, barRectWidth);
        }

        g2d.setPaint(oldPaint);
        g2d.setRenderingHints(oldHints);

        if (this.progressBar.isStringPainted()) {
            paintString(g2d, insets.left, insets.top, barRectWidth,
                    barRectHeight, amountFull, insets);
        }
    }

    protected void paintIndeterminate(Graphics g, JComponent c) {
        if (!(g instanceof Graphics2D)) {
            return;
        }

        Insets insets = this.progressBar.getInsets();
        int barRectWidth = this.progressBar.getWidth()
                - (insets.right + insets.left);
        int barRectHeight = this.progressBar.getHeight()
                - (insets.top + insets.bottom);

        if ((barRectWidth <= 0) || (barRectHeight <= 0)) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        Paint oldPaint = g2d.getPaint();
        RenderingHints oldHints = g2d.getRenderingHints();
        this.boxRect = getBox(this.boxRect);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        paintBackground(g2d, insets, barRectWidth, barRectHeight);

        if (this.boxRect != null) {
            if (this.progressBar.getOrientation() == 0) {
                createColors(this.progressBar.getForeground());
                this.paint = new LinearGradientPaint(0.0F, insets.top, 0.0F,
                        barRectHeight + insets.top, FRACTIONS, this.colors);
                g2d.setPaint(this.paint);
                g2d.fillRoundRect(this.boxRect.x, this.boxRect.y,
                        this.boxRect.width, this.boxRect.height, barRectHeight,
                        barRectHeight);
            } else {
                createColors(this.progressBar.getForeground());
                this.paint = new LinearGradientPaint(
                        barRectWidth + insets.left, 0.0F, insets.left, 0.0F,
                        FRACTIONS, this.colors);
                g2d.setPaint(this.paint);
                g2d.fillRoundRect(this.boxRect.x, this.boxRect.y,
                        this.boxRect.width, this.boxRect.height, barRectWidth,
                        barRectWidth);
            }
        }

        g2d.setPaint(oldPaint);
        g2d.setRenderingHints(oldHints);

        if (this.progressBar.isStringPainted()) {
            if (this.progressBar.getOrientation() == 0) {
                paintString(g2d, insets.left, insets.top, barRectWidth,
                        barRectHeight, this.boxRect.x, this.boxRect.width,
                        insets);
            } else {
                paintString(g2d, insets.left, insets.top, barRectWidth,
                        barRectHeight, this.boxRect.y, this.boxRect.height,
                        insets);
            }
        }
    }

    protected void paintString(Graphics g, int x, int y, int width, int height,
                               int amountFull, Insets b) {
        if (this.progressBar.getOrientation() == 0) {
            if (this.progressBar.isIndeterminate()) {
                this.boxRect = getBox(this.boxRect);
                paintString(g, x, y, width, height, this.boxRect.x,
                        this.boxRect.width, b);
            } else {
                paintString(g, x, y, width, height, x, amountFull, b);
            }

        } else if (this.progressBar.isIndeterminate()) {
            this.boxRect = getBox(this.boxRect);
            paintString(g, x, y, width, height, this.boxRect.y,
                    this.boxRect.height, b);
        } else {
            paintString(g, x, y, width, height, y + height - amountFull,
                    amountFull, b);
        }
    }

    private void paintString(Graphics g, int x, int y, int width, int height,
                             int fillStart, int amountFull, Insets b) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(this.progressBar.getFont());
        String str = this.progressBar.getString();
        Point renderLocation = getStringPlacement(g2d, str, x, y, width, height);
        Rectangle oldClip = g2d.getClipBounds();
        boolean is3D = this.progressBar instanceof JRound3DProgressBar;
        Color fontColor = is3D ? ((JRound3DProgressBar) this.progressBar)
                .getFontColor() : Color.ORANGE;
        Color fontCoverClor = is3D ? ((JRound3DProgressBar) this.progressBar)
                .getFontCoverClor() : UIResourceManager.getWhiteColor();

        if (this.progressBar.getOrientation() == 0) {
            g2d.setColor(fontColor);
            SwingUtilities2.drawString(this.progressBar, g2d, str,
                    renderLocation.x, renderLocation.y);
            g2d.setColor(fontCoverClor);
            g2d.clipRect(fillStart, y, amountFull, height);
            SwingUtilities2.drawString(this.progressBar, g2d, str,
                    renderLocation.x, renderLocation.y);
        } else {
            g2d.setColor(fontColor);
            AffineTransform rotate = AffineTransform
                    .getRotateInstance(1.570796326794897D);
            g2d.setFont(this.progressBar.getFont().deriveFont(rotate));
            renderLocation = getStringPlacement(g2d, str, x, y, width, height);
            SwingUtilities2.drawString(this.progressBar, g2d, str,
                    renderLocation.x, renderLocation.y);
            g2d.setColor(fontCoverClor);
            g2d.clipRect(x, fillStart, width, amountFull);
            SwingUtilities2.drawString(this.progressBar, g2d, str,
                    renderLocation.x, renderLocation.y);
        }

        g2d.setClip(oldClip);
    }
}