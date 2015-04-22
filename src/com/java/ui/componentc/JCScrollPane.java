package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicViewportUI;
import java.awt.*;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class JCScrollPane extends JScrollPane implements AdjustmentListener {
    protected class CScrollBar extends JScrollPane.ScrollBar {
//        private static final long serialVersionUID = -8174518362746135594L;

        public CScrollBar(int orientation) {
            super(orientation);
            setUI(new CScrollBarUI());
            setOpaque(false);
            setBorder(null);
        }

        @Deprecated
        public void updateUI() {
        }
    }

//    private static final long serialVersionUID = -8833386850571879174L;
    private float alpha;
    private Image image;
    private boolean imageOnly;
    private Border insideBorder;
    private Border outsideBorder;
    private HeaderPane upperRightCorner;

    private Insets visibleInsets;

    public JCScrollPane() {
        this(null, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public JCScrollPane(Component view) {
        this(view, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public JCScrollPane(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        setUI(new CScrollPaneUI());
        super.setBorder(new CompoundBorder(this.outsideBorder = super
                .getBorder(), this.insideBorder = new EmptyBorder(1, 1, 1, 1)));
        super.setOpaque(false);
        setBackground(Color.GRAY);
        setForeground(Color.BLACK);
        setFont(UIUtil.getDefaultFont());
        setCorner("LOWER_RIGHT_CORNER", createLowerRightCorner());
        this.alpha = 1.0F;
        this.visibleInsets = new Insets(1, 1, 1, 1);

        this.viewport.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int right = JCScrollPane.this.verticalScrollBar.isVisible() ? 1 : 0;
                int bottom = (JCScrollPane.this.horizontalScrollBar != null) && (JCScrollPane.this.horizontalScrollBar.isVisible()) ? 1 : 0;
                JCScrollPane.this.setViewportBorder(new EmptyBorder(0, 0, bottom, right));
            }
        });
    }

    public JCScrollPane(int vsbPolicy, int hsbPolicy) {
        this(null, vsbPolicy, hsbPolicy);
    }

    protected JLabel createHeaderLabel() {
        JLabel label = new JLabel() {
            private static final long serialVersionUID = 5966526868775976808L;

            @Deprecated
            public void updateUI() {
            }
        };
        label.setUI(new BasicLabelUI() {
            protected void installDefaults(JLabel c) {
            }
        });
        return label;
    }

    public JScrollBar createHorizontalScrollBar() {
        return new CScrollBar(0);
    }

    private JComponent createLowerRightCorner() {
        JLabel label = new JLabel(
                UIResourceManager.getIcon("ScrollPaneLowerRightCornerIcon")) {
            private static final long serialVersionUID = 5657359502965563304L;

            @Deprecated
            public void updateUI() {
            }
        };
        label.setUI(new BasicLabelUI() {
            protected void installDefaults(JLabel c) {
            }
        });
        label.setOpaque(false);
        return label;
    }

    public JScrollBar createVerticalScrollBar() {
        return new CScrollBar(1);
    }

    protected JViewport createViewport() {
        JViewport viewport = new JViewport() {
            private static final long serialVersionUID = -4480846449651574857L;

            @Deprecated
            public void updateUI() {
            }
        };
        viewport.setUI(new BasicViewportUI() {
            protected void installDefaults(JComponent c) {
            }
        });
        viewport.setFont(UIUtil.getDefaultFont());
        viewport.setForeground(Color.BLACK);
        viewport.setBackground(UIResourceManager.getEmptyColor());
        viewport.setOpaque(false);
        return viewport;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public Image getImage() {
        return this.image;
    }

    public Border getInsideBorder() {
        return this.insideBorder;
    }

    public Insets getVisibleInsets() {
        return this.visibleInsets;
    }

    public boolean isImageOnly() {
        return this.imageOnly;
    }

    public void setAlpha(float alpha) {
        if ((alpha >= 0.0F) && (alpha <= 1.0F)) {
            this.alpha = alpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid alpha:" + alpha);
        }
    }

    public void setBorder(Border border) {
        if ((border == null) && (this.visibleInsets != null)) {
            this.visibleInsets.set(0, 0, 0, 0);
        }

        this.outsideBorder = border;
        super.setBorder(new CompoundBorder(this.outsideBorder,
                this.insideBorder));
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (this.upperRightCorner != null) {
            this.upperRightCorner.setEnabled(enabled);
        }
        Component view;
        if ((this.viewport != null)
                && ((view = this.viewport.getView()) != null)) {
            view.setEnabled(enabled);
        }
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setImageOnly(boolean imageOnly) {
        this.imageOnly = imageOnly;
        repaint();
    }

    public void setInsideBorder(Border insideBorder) {
        this.insideBorder = insideBorder;
        super.setBorder(new CompoundBorder(this.outsideBorder, insideBorder));
    }

    @Deprecated
    public void setOpaque(boolean isOpaque) {
    }

    public void setVisibleInsets(int top, int left, int bottom, int right) {
        this.visibleInsets.set(top, left, bottom, right);
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}