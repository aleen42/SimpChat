package com.java.ui.componentc;

import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;
import java.awt.*;
import java.lang.reflect.Field;

public class CTableUI extends BasicTableUI {
    public static ComponentUI createUI(JComponent table) {
        return new CTableUI();
    }

    protected void installDefaults() {
        try {
            Field isFileListField = BasicTableUI.class.getDeclaredField("isFileList");
            isFileListField.setAccessible(true);
            isFileListField.set(this, Boolean.valueOf(Boolean.TRUE.equals(this.table.getClientProperty("Table.isFileList"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void paintBackground(Graphics g, JComponent c) {
        if ((c instanceof JCTable)) {
            JCTable table = (JCTable) c;
            UIUtil.paintBackground(g, c, table.getBackground(), table.getBackground(), table.getImage(),
                    table.isImageOnly(), table.getAlpha(), table.getVisibleInsets());
        }
    }

    public void update(Graphics g, JComponent c) {
        paintBackground(g, c);
        super.update(g, c);
    }
}