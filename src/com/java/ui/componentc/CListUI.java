package com.java.ui.componentc;

import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicListUI;
import java.awt.*;

public class CListUI extends BasicListUI {
    public static ComponentUI createUI(JComponent list) {
        return new CListUI();
    }

    protected void installDefaults() {
        this.list.setLayout(null);
    }

    private void paintBackground(Graphics g, JComponent c) {
        if ((c instanceof JCList)) {
            JCList list = (JCList) c;
            UIUtil.paintBackground(g, c, list.getBackground(),
                    list.getBackground(), list.getImage(), list.isImageOnly(),
                    list.getAlpha(), list.getVisibleInsets());
        }
    }

    protected void uninstallDefaults() {
        if ((this.list.getTransferHandler() instanceof UIResource)) {
            this.list.setTransferHandler(null);
        }
    }

    public void update(Graphics g, JComponent c) {
        paintBackground(g, c);
        super.update(g, c);
    }
}