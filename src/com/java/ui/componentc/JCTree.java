package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Vector;

public class JCTree extends JTree {
    private static final Icon COLLAPSED_ICON = UIResourceManager
            .getIcon("TreeCollapsedIcon");
    public static final Border DEFAULT_DISABLED_OUTSIDE_BORDER = new LineBorder(
            new Color(84, 165, 213, 128));

    public static final Border DEFAULT_INSIDE_BORDER = new EmptyBorder(1, 8, 1,
            1);

    public static final Border DEFAULT_OUTSIDE_BORDER = new LineBorder(
            new Color(84, 165, 213));

    private static final Color DISABLED_BG = UIResourceManager
            .getColor("TextDisabledBackground");

    private static final Icon EXPANDED_ICON = UIResourceManager
            .getIcon("TreeExpandedIcon");

    private static final long serialVersionUID = -645647197684495937L;
    private float alpha;
    private Color background;
    private Border border;
    private Border disabledBorder;
    private Color disabledForeground;
    private Image image;
    private boolean imageOnly;
    private boolean paintLines;
    private Color selectionForeground;
    private Insets visibleInsets;

    public JCTree() {
        this(getDefaultTreeModel());
    }

    public JCTree(Hashtable<?, ?> value) {
        super(value);
        init();
    }

    public JCTree(Object[] value) {
        super(value);
        init();
    }

    public JCTree(TreeModel newModel) {
        super(newModel);
        init();
    }

    public JCTree(TreeNode root) {
        this(root, false);
    }

    public JCTree(TreeNode root, boolean asksAllowsChildren) {
        this(new DefaultTreeModel(root, asksAllowsChildren));
    }

    public JCTree(Vector<?> value) {
        super(value);
        init();
    }

    public float getAlpha() {
        return this.alpha;
    }

    public Icon getCollapsedIcon() {
        return ((BasicTreeUI) getUI()).getCollapsedIcon();
    }

    public Border getDisabledBorder() {
        return this.disabledBorder;
    }

    public Color getDisabledForeground() {
        return this.disabledForeground;
    }

    public Icon getExpandedIcon() {
        return ((BasicTreeUI) getUI()).getExpandedIcon();
    }

    public Image getImage() {
        return this.image;
    }

    public TreePath getPathForY(int y) {
        TreePath closestPath = getClosestPathForLocation(0, y);

        if (closestPath != null) {
            Rectangle pathBounds = getPathBounds(closestPath);

            if ((pathBounds != null) && (y >= pathBounds.y)
                    && (y < pathBounds.y + pathBounds.height)) {
                return closestPath;
            }
        }

        return null;
    }

    public Color getSelectionForeground() {
        return this.selectionForeground;
    }

    public Insets getVisibleInsets() {
        return this.visibleInsets;
    }

    private void init() {
        setUI(new CTreeUI());
        setExpandedIcon(EXPANDED_ICON);
        setCollapsedIcon(COLLAPSED_ICON);
        setBorder(new CompoundBorder(DEFAULT_OUTSIDE_BORDER,
                DEFAULT_INSIDE_BORDER));
        setDisabledBorder(new CompoundBorder(DEFAULT_DISABLED_OUTSIDE_BORDER,
                DEFAULT_INSIDE_BORDER));
        setFont(UIUtil.getDefaultFont());
        setBackground(UIResourceManager.getWhiteColor());
        setForeground(Color.BLACK);
        setSelectionForeground(UIResourceManager.getWhiteColor());
        setDisabledForeground(new Color(123, 123, 122));
        super.setOpaque(false);
        setRowHeight(20);
        setShowsRootHandles(true);
        setLargeModel(false);
        setScrollsOnExpand(true);
        this.alpha = 1.0F;
        this.visibleInsets = new Insets(1, 1, 1, 1);
        this.paintLines = true;
    }

    public boolean isImageOnly() {
        return this.imageOnly;
    }

    public boolean isPaintLines() {
        return this.paintLines;
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

    public void setCollapsedIcon(Icon icon) {
        ((BasicTreeUI) getUI()).setCollapsedIcon(icon);
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

    public void setExpandedIcon(Icon icon) {
        ((BasicTreeUI) getUI()).setExpandedIcon(icon);
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

    public void setPaintLines(boolean paintLines) {
        if (this.paintLines != paintLines) {
            try {
                this.paintLines = paintLines;
                Field paintLinesField = BasicTreeUI.class
                        .getDeclaredField("paintLines");
                paintLinesField.setAccessible(true);
                paintLinesField.set(getUI(), Boolean.valueOf(paintLines));
                repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setSelectionForeground(Color selectionForeground) {
        this.selectionForeground = selectionForeground;
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