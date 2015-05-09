package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Vector;

public class JCScrollList extends JCScrollPane {
    private static final Color DISABLED_BG = UIResourceManager
            .getColor("TextDisabledBackground");
    private static final long serialVersionUID = -1708752774205915517L;
    private Color background;
    private Border border;
    private Border disabledBorder;
    protected JCList list;

    public JCScrollList() {
        this(new JCList());
    }

    public JCScrollList(JCList list) {
        setViewportView(this.list = list);
        init();
    }

    public JCScrollList(ListModel dataModel) {
        this(new JCList(dataModel));
    }

    public JCScrollList(Vector<?> listData) {
        this(new JCList(listData));
    }

    public Border getDisabledBorder() {
        return this.disabledBorder;
    }

    public Color getDisabledForeground() {
        return this.list.getDisabledForeground();
    }

    public JCList getList() {
        return this.list;
    }

    private void init() {
        setBorder(new LineBorder(new Color(84, 165, 213)));
        setDisabledBorder(new LineBorder(new Color(84, 165, 213, 128)));
        setBackground(UIResourceManager.getWhiteColor());
        setHeaderVisible(false);
        this.list.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.list.setDisabledBorder(this.list.getBorder());
        this.list.setVisibleInsets(0, 0, 0, 0);
        this.list.setAlpha(0.0F);
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
        this.list.setDisabledForeground(disabledForeground);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        super.setBorder(enabled ? this.border : this.disabledBorder);
        super.setBackground(enabled ? this.background : DISABLED_BG);
    }
}