package com.java.ui.component.treetable;

import com.java.ui.componentc.GridBorder;
import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.lang.reflect.Field;

public class TreeTableCellRenderer extends JTree implements TableCellRenderer {
    private class TreeCellRenderer extends DefaultTreeCellRenderer {
        private static final long serialVersionUID = -7459869452121826190L;
        private int row;

        private TreeCellRenderer() {
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            this.row = row;
            super.getTreeCellRendererComponent(tree, value, sel, expanded,
                    leaf, row, hasFocus);
            setForeground(tree.getForeground());
            return this;
        }

        public void paint(Graphics g) {
            Icon icon = getIcon();
            TreeTableCellRenderer.this.treeTable.setTreeRendererTextStartAt(
                    this.row,
                    getInsets().left
                            + (icon == null ? 0 : icon.getIconWidth()
                            + getIconTextGap()));
            super.paint(g);
        }
    }

    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1,
            1, 1);

    private static final GridBorder GRID_BORDER = new GridBorder(Color.WHITE,
            0, 0, 0, 0);

    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1,
            1);

    private static final long serialVersionUID = 913551938920374007L;
    private boolean hasFocus;
    private boolean isSelected;
    protected Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;
    private Border treeBorder;
    private JTreeTable treeTable;

    private int visibleRow;

    public TreeTableCellRenderer(JTreeTable treeTable,
                                 AbstractTreeTableModel model) {
        super(model);
        this.treeTable = treeTable;
        model.setTree(this);
        setEditable(false);
        setShowsRootHandles(true);
        setCellRenderer(new TreeCellRenderer());
        ((JComponent) getCellRenderer()).setOpaque(true);
        setPaintLines(false);
    }

    private Border getNoFocusBorder() {
        Border border = DefaultLookup.getBorder(this, this.ui,
                "Table.cellNoFocusBorder");

        if (System.getSecurityManager() != null) {
            if (border != null) {
                return border;
            }

            return SAFE_NO_FOCUS_BORDER;
        }
        if ((border != null)
                && ((this.noFocusBorder == null) || (this.noFocusBorder == DEFAULT_NO_FOCUS_BORDER))) {
            return border;
        }

        return this.noFocusBorder;
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(isSelected ? table.getSelectionBackground() : table
                .getBackground());
        setForeground(isSelected ? table.getSelectionForeground() : table
                .getForeground());
        setOpaque(table.isOpaque());
        setFont(table.getFont());
        this.visibleRow = row;
        this.isSelected = isSelected;
        this.hasFocus = hasFocus;

        if (hasFocus) {
            this.treeBorder = null;

            if (isSelected) {
                this.treeBorder = DefaultLookup.getBorder(this, this.ui,
                        "Table.focusSelectedCellHighlightBorder");
            }

            if (this.treeBorder == null) {
                this.treeBorder = DefaultLookup.getBorder(this, this.ui,
                        "Table.focusCellHighlightBorder");
            }
        } else {
            this.treeBorder = getNoFocusBorder();
        }

        return this;
    }

    public void paint(Graphics g) {
        int rowMargin = this.treeTable.getRowMargin();
        int columnMargin = this.treeTable.getColumnModel().getColumnMargin();
        int deltaY = -(this.visibleRow * getRowHeight() + rowMargin / 2
                - (rowMargin + 1) % 2 + (this.treeBorder == null ? 0
                : this.treeBorder.getBorderInsets(this).top));
        Rectangle paintRect = new Rectangle(0, 0, getWidth(), getRowHeight()
                - rowMargin);
        g.translate(0, deltaY);
        super.paint(g);
        g.translate(0, -deltaY);

        if (this.treeBorder != null) {
            this.treeBorder.paintBorder(this, g, paintRect.x, paintRect.y,
                    paintRect.width, paintRect.height);
        }

        if ((!this.isSelected) && (!this.hasFocus)
                && ((columnMargin <= 0) || (rowMargin <= 0))) {
            paintRect.width -= (columnMargin > 0 ? 0 : Math
                    .round(-columnMargin / 2.0F));
            GRID_BORDER.setColor(this.treeTable.getGridColor());
            GRID_BORDER.setInsets(0, 0, rowMargin > 0 ? 0 : 1,
                    columnMargin > 0 ? 0 : 1);
            GRID_BORDER.paintBorder(this, g, paintRect.x, paintRect.y,
                    paintRect.width, paintRect.height);
        }
    }

    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, 0, width, this.treeTable.getHeight());
    }

    public void setPaintLines(boolean paintLines) {
        try {
            Field paintLinesField = BasicTreeUI.class
                    .getDeclaredField("paintLines");
            paintLinesField.setAccessible(true);
            paintLinesField.set((BasicTreeUI) getUI(),
                    Boolean.valueOf(paintLines));
            this.treeTable.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}