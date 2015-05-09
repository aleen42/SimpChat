package com.java.ui.component.treetable;

import com.java.ui.componentc.ImageBorder;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

public class TreeColumnCellEditor extends DefaultCellEditor {
    private static final Border ERROR_BORDER = new LineBorder(Color.RED);
    private static final Border GENERIC_BORDER = new LineBorder(Color.BLACK);

    private static final long serialVersionUID = 2475808674866003675L;
    private Class<?>[] argTypes;
    private Constructor<?> constructor;
    private JTextField field;
    private Border insideBorder;
    private Object value;

    public TreeColumnCellEditor() {
        super(new JTextField());
        getComponent().setName("TreeColumn.editor");
        this.argTypes = new Class[]{String.class};
        this.field = ((JTextField) getComponent());
    }

    public Object getCellEditorValue() {
        return this.value;
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.value = null;
        table.clearSelection();

        if ((table instanceof JTreeTable)) {
            Insets insets = GENERIC_BORDER.getBorderInsets(this.field);
            Rectangle rect = table.getCellRect(row, column, false);
            rect.x += insets.left;
            rect.y += insets.top;
            rect.width -= insets.left + insets.right;
            rect.height -= insets.top + insets.bottom;
            BufferedImage image = UIUtil.getGraphicsConfiguration(
                    this.editorComponent).createCompatibleImage(rect.width,
                    rect.height, 3);
            Graphics2D g2d = image.createGraphics();
            g2d.translate(-rect.x, -rect.y);
            table.paint(g2d);
            g2d.dispose();
            JTreeTable treeTable = (JTreeTable) table;
            JTree tree = treeTable.getTree();
            JLabel renderer = (JLabel) tree.getCellRenderer();
            int textStart = treeTable.getTreeRendererTextStartAt(row);

            if (textStart < 0) {
                Icon icon = renderer.getIcon();
                textStart = renderer.getInsets().left
                        + (icon == null ? 0 : icon.getIconWidth()
                        + renderer.getIconTextGap());
            }

            this.insideBorder = new ImageBorder(image, 0,
                    tree.getRowBounds(row).x + textStart - insets.left, 0, 0);
        }

        this.field.setBorder(new CompoundBorder(GENERIC_BORDER,
                this.insideBorder));
        try {
            Class<?> type = table.getColumnClass(column);

            if (type == Object.class) {
                type = String.class;
            }

            this.constructor = type.getConstructor(this.argTypes);
        } catch (Exception e) {
            return null;
        }

        return super.getTableCellEditorComponent(table, value, isSelected, row,
                column);
    }

    public boolean stopCellEditing() {
        String value = (String) super.getCellEditorValue();

        if ("".equals(value)) {
            if (this.constructor.getDeclaringClass() == String.class) {
                this.value = value;
            }

            super.stopCellEditing();
        }

        try {
            this.value = this.constructor.newInstance(new Object[]{value});
        } catch (Exception e) {
            this.field.setBorder(new CompoundBorder(ERROR_BORDER,
                    this.insideBorder));
            return false;
        }

        return super.stopCellEditing();
    }
}