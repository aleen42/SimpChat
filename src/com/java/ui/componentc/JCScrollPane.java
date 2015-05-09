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
        private static final long serialVersionUID = -8174518362746135594L;

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

    private static final long serialVersionUID = -8833386850571879174L;
    private float alpha;
    private HeaderPane header;
    private Color headerDisabledForeground;
    private Font headerFont;
    private Color headerForeground;
    private JLabel headerLabel;
    private boolean headerVisible;
    private Image image;
    private boolean imageOnly;
    private Border insideBorder;
    private Border outsideBorder;
    private HeaderPane upperRightCorner;

    private Insets visibleInsets;

    public JCScrollPane() {
        this(null, 20, 30);
    }

    public JCScrollPane(Component view) {
        this(view, 20, 30);
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
        setHeaderFont(getFont());
        setHeaderVisible(true);
        setHeaderForeground(new Color(0, 28, 48));
        setHeaderDisabledForeground(new Color(128, 142, 152));
//        initHeader();
        setCorner("LOWER_RIGHT_CORNER", createLowerRightCorner());
        this.alpha = 1.0F;
        this.visibleInsets = new Insets(1, 1, 1, 1);

        this.viewport.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                int right = JCScrollPane.this.verticalScrollBar.isVisible() ? 1
                        : 0;
                int bottom = (JCScrollPane.this.horizontalScrollBar != null)
                        && (JCScrollPane.this.horizontalScrollBar.isVisible()) ? 1
                        : 0;
                JCScrollPane.this.setViewportBorder(new EmptyBorder(0, 0,
                        bottom, right));
            }
        });
    }

    public JCScrollPane(int vsbPolicy, int hsbPolicy) {
        this(null, vsbPolicy, hsbPolicy);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        if ((this.header != null) && (this.header.isVisible())) {
            this.header.revalidate();
        }
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

    public HeaderPane getHeader() {
        return this.header;
    }

    public Color getHeaderDisabledForeground() {
        return this.headerDisabledForeground;
    }

    public Font getHeaderFont() {
        return this.headerFont;
    }

    public Color getHeaderForeground() {
        return this.headerForeground;
    }

    public JLabel getHeaderLabel() {
        return this.headerLabel;
    }

    public String getHeaderText() {
        return this.headerLabel == null ? null : this.headerLabel.getText();
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

    protected void initHeader() {
        setColumnHeaderView(this.header = new HeaderPane(
                this.headerLabel = createHeaderLabel()));
        setCorner("UPPER_RIGHT_CORNER",
                this.upperRightCorner = new HeaderPane());
        this.headerLabel.setBorder(new EmptyBorder(0, 7, 0, 0));
        this.headerLabel.setOpaque(false);
        getHorizontalScrollBar().addAdjustmentListener(this);
        getVerticalScrollBar().addAdjustmentListener(this);
    }

    public boolean isHeaderVisible() {
        return (this.headerVisible) && (this.columnHeader != null);
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

    public void setColumnHeader(JViewport columnHeader) {
        super.setColumnHeader(columnHeader);

        if (columnHeader != null) {
            columnHeader.setVisible(this.headerVisible);
        }
    }

    public void setColumnHeaderView(Component view) {
        super.setColumnHeaderView(view);

        if (view != null) {
            if (this.headerFont != null) {
                updateHeaderProperty(view, "font", this.headerFont);
            }

            Color color = null;

            if ((isEnabled()) && (this.headerForeground != null)) {
                color = this.headerForeground;
            } else if ((!isEnabled())
                    && (this.headerDisabledForeground != null)) {
                color = this.headerDisabledForeground;
            }

            if (color != null) {
                updateHeaderProperty(view, "foreground", color);
            }
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (this.header != null) {
            this.header.setEnabled(enabled);
        }

        if (this.upperRightCorner != null) {
            this.upperRightCorner.setEnabled(enabled);
        }
        Component view;
        if ((this.viewport != null)
                && ((view = this.viewport.getView()) != null)) {
            view.setEnabled(enabled);
        }
        Component headerView;
        if ((this.columnHeader != null)
                && ((headerView = this.columnHeader.getView()) != null)) {
            updateHeaderProperty(headerView, "foreground",
                    enabled ? this.headerForeground
                            : this.headerDisabledForeground);
        }
    }

    public void setHeaderDisabledForeground(Color headerDisabledForeground) {
        this.headerDisabledForeground = headerDisabledForeground;
        Component view;
        if ((!isEnabled()) && (this.columnHeader != null)
                && ((view = this.columnHeader.getView()) != null)) {
            updateHeaderProperty(view, "foreground", headerDisabledForeground);
        }
    }

    public void setHeaderFont(Font headerFont) {
        this.headerFont = headerFont;
        Component view;
        if ((this.columnHeader != null)
                && ((view = this.columnHeader.getView()) != null)) {
            updateHeaderProperty(view, "font", headerFont);
        }
    }

    public void setHeaderForeground(Color headerForeground) {
        this.headerForeground = headerForeground;
        Component view;
        if ((isEnabled()) && (this.columnHeader != null)
                && ((view = this.columnHeader.getView()) != null)) {
            updateHeaderProperty(view, "foreground", headerForeground);
        }
    }

    public void setHeaderText(String text) {
        if (this.headerLabel != null) {
            this.headerLabel.setText(text);
        }
    }

    public void setHeaderVisible(boolean visible) {
        this.headerVisible = visible;

        if (this.columnHeader != null) {
            this.columnHeader.setVisible(this.headerVisible);
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

    private void updateHeaderProperty(Component c, String propertyName,
                                      Object value) {
        if (propertyName.equals("font")) {
            c.setFont((Font) value);
        } else if (propertyName.equals("foreground")) {
            c.setForeground((Color) value);
        }

        if ((c instanceof Container)) {
            for (Component child : ((Container) c).getComponents()) {
                updateHeaderProperty(child, propertyName, value);
            }
        }
    }

    @Deprecated
    public void updateUI() {
    }
}