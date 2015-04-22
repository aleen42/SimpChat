package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;

public class CSliderUI extends BasicSliderUI {
    private static final Image BG_IMAGE_H = UIResourceManager
            .getImage("SliderBackgroundImageH");

    private static final Image BG_IMAGE_V = UIResourceManager
            .getImage("SliderBackgroundImageV");

    private static final Composite DISABLED_COMPOSITE = AlphaComposite.SrcOver
            .derive(0.5F);

    private static final Icon THUMB_ICON_H = UIResourceManager
            .getIcon("SliderThumbIconH");

    private static final Icon THUMB_ICON_V = UIResourceManager
            .getIcon("SliderThumbIconV");

    private static final Icon THUMB_MINI_ICON_H = UIResourceManager
            .getIcon("SliderMiniThumbIconH");

    private static final Icon THUMB_MINI_ICON_V = UIResourceManager
            .getIcon("SliderMiniThumbIconV");

    private static final Color VALUE_COLOR_INNER_1 = UIResourceManager
            .getColor("SliderInnerColor1");

    private static final Color VALUE_COLOR_INNER_2 = UIResourceManager
            .getColor("SliderInnerColor2");

    private static final Color VALUE_COLOR_OUTER_1 = UIResourceManager
            .getColor("SliderOuterColor1");

    private static final Color VALUE_COLOR_OUTER_2 = UIResourceManager
            .getColor("SliderOuterColor2");

    private static final Color VALUE_MINI_BACKGROUND = UIResourceManager
            .getColor("SliderMiniBackground");

    private static final Color VALUE_MINI_COLOR_INNER_1 = UIResourceManager
            .getColor("SliderMiniInnerColor1");

    private static final Color VALUE_MINI_COLOR_INNER_2 = UIResourceManager
            .getColor("SliderMiniInnerColor2");

    private static final Color VALUE_MINI_COLOR_OUTER = UIResourceManager
            .getColor("SliderMiniOuterColor");

    public static ComponentUI createUI(JComponent c) {
        return new CSliderUI();
    }

    public CSliderUI() {
        this(null);
    }

    public CSliderUI(JSlider slider) {
        super(slider);
    }

    public void calculateGeometry() {
        super.calculateGeometry();
    }

    protected Dimension getThumbSize() {
        if ((this.slider instanceof JCSlider)) {
            boolean miniMode = ((JCSlider) this.slider).isMiniMode();
            Icon icon = null;

            if (this.slider.getOrientation() == 0) {
                icon = miniMode ? THUMB_MINI_ICON_H : THUMB_ICON_H;
            } else {
                icon = miniMode ? THUMB_MINI_ICON_V : THUMB_ICON_V;
            }

            return icon == null ? null : new Dimension(icon.getIconWidth(),
                    icon.getIconHeight());
        }

        return super.getThumbSize();
    }

    protected void installDefaults(JSlider slider) {
        this.focusInsets = new InsetsUIResource(0, 0, 0, 0);
    }

    public void paintFocus(Graphics g) {
    }

    protected void paintMajorTickForHorizSlider(Graphics g,
                                                Rectangle tickBounds, int x) {
        Color oldColor = g.getColor();
        setTickColor(g, true);
        super.paintMajorTickForHorizSlider(g, tickBounds, x);
        g.setColor(oldColor);
    }

    protected void paintMajorTickForVertSlider(Graphics g,
                                               Rectangle tickBounds, int y) {
        Color oldColor = g.getColor();
        setTickColor(g, true);
        super.paintMajorTickForVertSlider(g, tickBounds, y);
        g.setColor(oldColor);
    }

    protected void paintMinorTickForHorizSlider(Graphics g,
                                                Rectangle tickBounds, int x) {
        Color oldColor = g.getColor();
        setTickColor(g, false);
        super.paintMinorTickForHorizSlider(g, tickBounds, x);
        g.setColor(oldColor);
    }

    protected void paintMinorTickForVertSlider(Graphics g,
                                               Rectangle tickBounds, int y) {
        Color oldColor = g.getColor();
        setTickColor(g, false);
        super.paintMinorTickForVertSlider(g, tickBounds, y);
        g.setColor(oldColor);
    }

    public void paintThumb(Graphics g) {
        if ((this.slider instanceof JCSlider)) {
            boolean miniMode = ((JCSlider) this.slider).isMiniMode();
            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();
            Icon icon = null;

            if (!this.slider.isEnabled()) {
                g2d.setComposite(DISABLED_COMPOSITE);
            }

            g2d.translate(this.thumbRect.x, this.thumbRect.y);

            if (this.slider.getOrientation() == 0) {
                icon = miniMode ? THUMB_MINI_ICON_H : THUMB_ICON_H;
            } else {
                icon = miniMode ? THUMB_MINI_ICON_V : THUMB_ICON_V;
            }

            if (icon != null) {
                icon.paintIcon(this.slider, g2d, 0, 0);
            }

            g2d.translate(-this.thumbRect.x, -this.thumbRect.y);
            g2d.setComposite(oldComposite);
        } else {
            super.paintThumb(g);
        }
    }

    public void paintTrack(Graphics g) {
        if ((this.slider instanceof JCSlider)) {
            boolean miniMode = ((JCSlider) this.slider).isMiniMode();
            boolean horizontal = this.slider.getOrientation() == 0;
            int trackThickness = miniMode ? 4 : 9;
            Rectangle rect = new Rectangle(this.trackRect);
            int delta = (horizontal ? rect.height : rect.width)
                    - trackThickness;
            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();

            if (!this.slider.isEnabled()) {
                g2d.setComposite(DISABLED_COMPOSITE);
            }

            if (miniMode) {
                if (horizontal) {
                    rect.y += delta / 2;
                    rect.height -= delta;
                } else {
                    rect.x += delta / 2;
                    rect.width -= delta;
                }

                g2d.setColor(VALUE_MINI_COLOR_OUTER);
                g2d.drawRoundRect(rect.x, rect.y, rect.width - 1,
                        rect.height - 1, 2, 2);
                g2d.setColor(VALUE_MINI_BACKGROUND);
                g2d.fillRect(rect.x + 1, rect.y + 1, rect.width - 2,
                        rect.height - 2);
            } else {
                Insets imageInsets = new Insets(2, 2, 2, 2);
                Image image;
                if (horizontal) {
                    image = BG_IMAGE_H;
                    rect.y += delta / 2;
                    rect.height -= delta;
                } else {
                    image = BG_IMAGE_V;
                    rect.x += delta / 2;
                    rect.width -= delta;
                }

                UIUtil.paintImage(g2d, image, imageInsets, rect, this.slider);
            }

            paintValue(g2d, rect, miniMode);
            g2d.setComposite(oldComposite);
        } else {
            super.paintTrack(g);
        }
    }

    private void paintValue(Graphics2D g2d, Rectangle rect, boolean miniMode) {
        int value = this.slider.getValue();
        int max = this.slider.getMaximum();
        int min = this.slider.getMinimum();
        float percent = (value - min) / (max - min);
        boolean leftToRight = this.slider.getComponentOrientation()
                .isLeftToRight();
        boolean inverted = this.slider.getInverted();
        boolean horizontal = this.slider.getOrientation() == 0;
        Rectangle paintRect = new Rectangle();
        Paint oldPaint = g2d.getPaint();
        Paint paint1 = null;
        Paint paint2 = null;

        if (horizontal) {
            if (((!leftToRight) && (inverted))
                    || ((leftToRight) && (!inverted))) {
                paintRect.setBounds(1, 1, Math.round(rect.width * percent) - 2,
                        rect.height - 2);
                paint1 = new GradientPaint(paintRect.x, paintRect.y,
                        miniMode ? VALUE_MINI_COLOR_INNER_1
                                : VALUE_COLOR_OUTER_1, paintRect.x
                        + paintRect.width - 1, paintRect.y,
                        miniMode ? VALUE_MINI_COLOR_INNER_2
                                : VALUE_COLOR_OUTER_2);
                paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1,
                        paintRect.y + 1, VALUE_COLOR_INNER_1, paintRect.x
                        + paintRect.width - 2, paintRect.y + 1,
                        VALUE_COLOR_INNER_2);
            } else if (((!leftToRight) && (!inverted))
                    || ((leftToRight) && (inverted))) {
                int x = Math.round(1.0F + (1.0F - percent) * rect.width);
                paintRect.setBounds(x, 1, Math.round(rect.width * percent) - 2,
                        rect.height - 2);
                paint1 = new GradientPaint(paintRect.x, paintRect.y,
                        miniMode ? VALUE_MINI_COLOR_INNER_2
                                : VALUE_COLOR_OUTER_2, paintRect.x
                        + paintRect.width - 1, paintRect.y,
                        miniMode ? VALUE_MINI_COLOR_INNER_1
                                : VALUE_COLOR_OUTER_1);
                paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1,
                        paintRect.y + 1, VALUE_COLOR_INNER_2, paintRect.x
                        + paintRect.width - 2, paintRect.y + 1,
                        VALUE_COLOR_INNER_1);
            }

        } else if (inverted) {
            paintRect.setBounds(1, 1, rect.width - 2,
                    Math.round(rect.height * percent) - 2);
            paint1 = new GradientPaint(paintRect.x, paintRect.y,
                    miniMode ? VALUE_MINI_COLOR_INNER_1 : VALUE_COLOR_OUTER_1,
                    paintRect.x, paintRect.y + paintRect.height - 1,
                    miniMode ? VALUE_MINI_COLOR_INNER_2 : VALUE_COLOR_OUTER_2);
            paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1,
                    paintRect.y + 1, VALUE_COLOR_INNER_1, paintRect.x + 1,
                    paintRect.y + paintRect.height - 2, VALUE_COLOR_INNER_2);
        } else {
            int y = Math.round(1.0F + (1.0F - percent) * rect.height);
            paintRect.setBounds(1, y, rect.width - 2,
                    Math.round(rect.height * percent) - 2);
            paint1 = new GradientPaint(paintRect.x, paintRect.y,
                    miniMode ? VALUE_MINI_COLOR_INNER_2 : VALUE_COLOR_OUTER_2,
                    paintRect.x, paintRect.y + paintRect.height - 1,
                    miniMode ? VALUE_MINI_COLOR_INNER_1 : VALUE_COLOR_OUTER_1);
            paint2 = miniMode ? null : new GradientPaint(paintRect.x + 1,
                    paintRect.y + 1, VALUE_COLOR_INNER_2, paintRect.x + 1,
                    paintRect.y + paintRect.height - 2, VALUE_COLOR_INNER_1);
        }

        g2d.translate(rect.x, rect.y);
        g2d.setPaint(paint1);
        g2d.fillRect(paintRect.x, paintRect.y, paintRect.width,
                paintRect.height);

        if (!miniMode) {
            g2d.setPaint(paint2);
            g2d.fillRect(paintRect.x + 1, paintRect.y + 1, paintRect.width - 2,
                    paintRect.height - 2);
        }

        g2d.setPaint(oldPaint);
        g2d.translate(-rect.x, -rect.y);
    }

    private void setTickColor(Graphics g, boolean major) {
        if ((this.slider instanceof JCSlider)) {
            if (this.slider.isEnabled()) {
                g.setColor(major ? ((JCSlider) this.slider).getMajorTickColor()
                        : ((JCSlider) this.slider).getMinorTickColor());
            } else {
                g.setColor(UIResourceManager
                        .getColor("SliderDisabledTickColor"));
            }
        }
    }
}