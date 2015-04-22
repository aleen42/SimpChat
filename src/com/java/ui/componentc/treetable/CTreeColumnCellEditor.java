package com.java.ui.componentc.treetable;

import com.java.ui.componentc.ImageBorder;
import com.java.ui.componentc.JCTable;
import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;

public class CTreeColumnCellEditor extends DefaultCellEditor {
    private static final Border ERROR_BORDER = new CompoundBorder(
            new LineBorder(Color.RED, 2), new EmptyBorder(0, 0, 0, 1));
    private static final Border GENERIC_BORDER = UIResourceManager
            .getBorder("TreeColumnEditorBorder");

    private static final Color SELECTION_COLOR = UIResourceManager
            .getColor("TextSelectionColor");

    private static final long serialVersionUID = 2475808674866003675L;
    private Class<?>[] argTypes;
    private Constructor<?> constructor;
    private JTextField field;
    private Border insideBorder;
    private Object value;

    public CTreeColumnCellEditor() {
        super(new JTextField() {
            private static final long serialVersionUID = -5666529738511463777L;

            @Deprecated
            public void updateUI() {
            }
        });
        getComponent().setName("TreeColumn.editor");
        this.argTypes = new Class[]{String.class};
        this.field = ((JTextField) getComponent());
        this.field.setUI(new BasicTextFieldUI());
        this.field.setMargin(new Insets(0, 0, 0, 0));
        this.field.setCaretColor(Color.BLACK);
        this.field.setSelectionColor(SELECTION_COLOR);
        this.field.setSelectedTextColor(UIResourceManager
                .getColor("TextSelectionForeground"));
    }

    public Object getCellEditorValue() {
        return this.value;
    }

    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.value = null;
        table.clearSelection();

        if ((table instanceof JCTreeTable)) {
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
            JCTreeTable treeTable = (JCTreeTable) table;
            JTree tree = treeTable.getTree();
            JLabel treeRenderer = (JLabel) tree.getCellRenderer();
            int textStart = treeTable.getTreeRendererTextStartAt(row);

            if (textStart < 0) {
                Icon icon = treeRenderer.getIcon();
                textStart = treeRenderer.getInsets().left
                        + (icon == null ? 0 : icon.getIconWidth()
                        + treeRenderer.getIconTextGap());
            }

            this.insideBorder = new ImageBorder(image, 0,
                    tree.getRowBounds(row).x + textStart - insets.left, 0, 0);
        }

        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Component c = renderer.getTableCellRendererComponent(table, value,
                isSelected, true, row, column);
        this.field.setFont(c.getFont());
        this.field.setBackground(JCTable.createEditorBackground(table, c));
        this.field.setForeground(table.getForeground());
        this.field.setBorder(new CompoundBorder(GENERIC_BORDER,
                this.insideBorder));
        this.field.setOpaque(this.field.getBackground() != null);
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