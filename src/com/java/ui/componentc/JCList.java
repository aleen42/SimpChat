package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Vector;

public class JCList extends JList {
    private static final Color DISABLED_BG = UIResourceManager
            .getColor("TextDisabledBackground");
    private static final long serialVersionUID = 4140226075621300872L;
    private float alpha;
    private Color background;
    private Border border;
    private Border disabledBorder;
    private Color disabledForeground;
    private Image image;
    private boolean imageOnly;
    private Color rendererBackground1;
    private Color rendererBackground2;
    private boolean rendererOpaque;
    private Insets visibleInsets;

    public JCList() {
        this(new AbstractListModel() {
            private static final long serialVersionUID = 7026637611528615834L;

            public Object getElementAt(int i) {
                return "No Data Model";
            }

            public int getSize() {
                return 0;
            }
        });
    }

    public JCList(ListModel dataModel) {
        super(dataModel);
        setUI(new CListUI());
        setBorder(new CompoundBorder(new LineBorder(new Color(84, 165, 213)),
                new EmptyBorder(1, 1, 1, 1)));
        setDisabledBorder(new CompoundBorder(new LineBorder(new Color(84, 165,
                213, 128)), new EmptyBorder(1, 1, 1, 1)));
        setFixedCellHeight(20);
        setCellRenderer(new CListCellRenderer());
        setFont(UIUtil.getDefaultFont());
        setBackground(UIResourceManager.getWhiteColor());
        setForeground(Color.WHITE);
        setSelectionForeground(UIResourceManager.getWhiteColor());
        setDisabledForeground(new Color(123, 123, 122));
        setSelectionBackground(Color.BLUE);
        super.setOpaque(false);
        this.alpha = 1.0F;
        this.visibleInsets = new Insets(1, 1, 1, 1);
        this.rendererOpaque = true;
        this.rendererBackground1 = new Color(251, 251, 255);
        this.rendererBackground2 = new Color(243, 248, 251);
    }

    public JCList(final Object[] listData) {
        this(new AbstractListModel() {
            private static final long serialVersionUID = 4555057559640490290L;

            public Object getElementAt(int i) {
                return listData[i];
            }

            public int getSize() {
                return listData.length;
            }
        });
    }

    public JCList(final Vector<?> listData) {
        this(new AbstractListModel() {
            private static final long serialVersionUID = 4410354317412379521L;

            public Object getElementAt(int i) {
                return listData.elementAt(i);
            }

            public int getSize() {
                return listData.size();
            }
        });
    }

    public float getAlpha() {
        return this.alpha;
    }

    public Border getDisabledBorder() {
        return this.disabledBorder;
    }

    public Color getDisabledForeground() {
        return this.disabledForeground;
    }

    public Image getImage() {
        return this.image;
    }

    public Color getRendererBackground1() {
        return this.rendererBackground1;
    }

    public Color getRendererBackground2() {
        return this.rendererBackground2;
    }

    public Insets getVisibleInsets() {
        return this.visibleInsets;
    }

    public boolean isImageOnly() {
        return this.imageOnly;
    }

    public boolean isRendererOpaque() {
        return this.rendererOpaque;
    }

    public void setAlpha(float alpha) {
        if ((alpha >= 0.0F) && (alpha <= 1.0F)) {
            this.alpha = alpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid alpha:" + alpha);
        }
    }

    public void setBackground(Color background) {
        this.background = background;
        super.setBackground(background);
    }

    public void setBorder(Border border) {
        this.border = border;
        super.setBorder(border);
    }

    public void setDisabledBorder(Border disabledBorder) {
        this.disabledBorder = disabledBorder;

        if (!isEnabled()) {
            super.setBorder(disabledBorder);
        }
    }

    public void setDisabledForeground(Color disabledForeground) {
        this.disabledForeground = disabledForeground;

        if (!isEnabled()) {
            repaint();
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        super.setBorder(enabled ? this.border : this.disabledBorder);
        super.setBackground(enabled ? this.background : DISABLED_BG);
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setImageOnly(boolean imageOnly) {
        this.imageOnly = imageOnly;
        repaint();
    }

    @Deprecated
    public void setOpaque(boolean isOpaque) {
    }

    public void setRendererBackground1(Color rendererBackground1) {
        this.rendererBackground1 = rendererBackground1;
        repaint();
    }

    public void setRendererBackground2(Color rendererBackground2) {
        this.rendererBackground2 = rendererBackground2;
        repaint();
    }

    public void setRendererOpaque(boolean rendererOpaque) {
        this.rendererOpaque = rendererOpaque;
        repaint();
    }

    public void setVisibleInsets(int top, int left, int bottom, int right) {
        this.visibleInsets.set(top, left, bottom, right);
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}