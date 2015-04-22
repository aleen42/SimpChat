package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import java.awt.*;

public class JCScrollTree extends JCScrollPane {
    private static final Color DISABLED_BG = UIResourceManager
            .getColor("TextDisabledBackground");
    private static final long serialVersionUID = -9002172235348323323L;
    private Color background;
    private Border border;
    private Border disabledBorder;
    private JCTree tree;

    public JCScrollTree() {
        this(new JCTree());
    }

    public JCScrollTree(JCTree tree) {
        setViewportView(this.tree = tree);
        init();
    }

    public JCScrollTree(TreeModel newModel) {
        this(new JCTree(newModel));
    }

    public JCScrollTree(TreeNode root) {
        this(new JCTree(root));
    }

    public Border getDisabledBorder() {
        return this.disabledBorder;
    }

    public Color getDisabledForeground() {
        return this.tree.getDisabledForeground();
    }

    public JCTree getTree() {
        return this.tree;
    }

    private void init() {
        setBorder(new LineBorder(new Color(84, 165, 213)));
        setDisabledBorder(new LineBorder(new Color(84, 165, 213, 128)));
        setBackground(UIResourceManager.getWhiteColor());
        setHeaderVisible(false);
        this.tree.setBorder(new EmptyBorder(0, 7, 0, 0));
        this.tree.setDisabledBorder(this.tree.getBorder());
        this.tree.setVisibleInsets(0, 0, 0, 0);
        this.tree.setAlpha(0.0F);
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
        this.tree.setDisabledForeground(disabledForeground);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        super.setBorder(enabled ? this.border : this.disabledBorder);
        super.setBackground(enabled ? this.background : DISABLED_BG);
    }
}