package com.java.ui.componentc.treetable;

import com.java.ui.component.treetable.AbstractTreeTableModel;
import com.java.ui.componentc.CTreeUI;
import com.java.ui.componentc.GridBorder;
import com.java.ui.componentc.JCTree;
import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class CTreeTableCellRenderer extends JCTree implements TableCellRenderer {
    private class TreeCellRenderer extends CTreeUI.CTreeCellRenderer {
        private static final long serialVersionUID = -7459869452121826190L;
        private int row;

        public TreeCellRenderer() {
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus) {
            this.row = row;
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
            return this;
        }

        public void paint(Graphics g) {
            Icon icon = getIcon();
            CTreeTableCellRenderer.this.treeTable.setTreeRendererTextStartAt(
                    this.row,
                    getInsets().left
                            + (icon == null ? 0 : icon.getIconWidth()
                            + getIconTextGap()));
            super.paint(g);
        }
    }

    private static final Image BG_IMAGE = UIResourceManager
            .getImage("SelectedItemBackgroundImage");

    private static final Image BG_IMAGE_DISABLED = UIResourceManager
            .getImage("SelectedItemDisabledBackgroundImage");

    private static final GridBorder GRID_BORDER = new GridBorder(Color.GRAY, 0,
            0, 0, 0);
    private static final long serialVersionUID = 913551938920374007L;
    private int column;
    private boolean paintBackground;
    private boolean selected;
    private JCTreeTable treeTable;

    private int visibleRow;

    public CTreeTableCellRenderer(JCTreeTable treeTable,
                                  AbstractTreeTableModel model) {
        super(model);
        this.treeTable = treeTable;
        model.setTree(this);
        setEditable(false);
        setAlpha(0.0F);
        setCellRenderer(new TreeCellRenderer());
        ((JComponent) getCellRenderer()).setOpaque(false);
        setPaintLines(false);
        setBorder(new EmptyBorder(0, 5, 0, 0));
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        this.selected = isSelected;
        this.column = column;
        this.visibleRow = row;
        Color fg = this.treeTable.isEnabled() ? this.treeTable.getForeground()
                : this.treeTable.getDisabledForeground();
        Color color1 = this.treeTable.getRendererBackground1();
        Color color2 = this.treeTable.getRendererBackground2();
        this.paintBackground = ((isSelected) || ((this.treeTable
                .isRendererOpaque()) && (color1 != null) && (color2 != null)));
        setBackground(row % 2 == 0 ? color1 : color2);
        setFont(table.getFont());
        setForeground(isSelected ? table.getSelectionForeground() : fg);
        showGridLine();
        return this;
    }

    public void paint(Graphics g) {
        int deltaY = -(this.visibleRow * getRowHeight() + 1);
        Rectangle paintRect = new Rectangle(0, 0, getWidth(), getRowHeight());

        if (this.paintBackground) {
            if (this.selected) {
                int columnCount = this.treeTable.getColumnCount();
                Image image = this.treeTable.isEnabled() ? BG_IMAGE
                        : BG_IMAGE_DISABLED;

                if (columnCount > 1) {
                    int width = image.getWidth(null);
                    int height = image.getHeight(null);

                    if (this.column == 0) {
                        image = UIUtil.cutImage(image, new Rectangle(0, 0,
                                width - 1, height), this);
                    } else if (this.column == columnCount - 1) {
                        image = UIUtil.cutImage(image, new Rectangle(1, 0,
                                width - 1, height), this);
                    } else {
                        image = UIUtil.cutImage(image, new Rectangle(1, 0,
                                width - 2, height), this);
                    }
                }

                UIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), paintRect,
                        this);
            } else {
                g.setColor(getBackground());
                g.fillRect(paintRect.x, paintRect.y, paintRect.width,
                        paintRect.height);
            }
        }

        g.translate(0, deltaY);
        super.paint(g);
        g.translate(0, -deltaY);
        GRID_BORDER.paintBorder(this, g, paintRect.x, paintRect.y,
                paintRect.width, paintRect.height);
    }

    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, 0, width, this.treeTable.getHeight());
    }

    public void setPaintLines(boolean paintLines) {
        super.setPaintLines(paintLines);
        this.treeTable.repaint();
    }

    private void showGridLine() {
        boolean showColumnLines = this.treeTable.isShowColumnLines();
        boolean showRowLines = this.treeTable.isShowRowLines();
        int right = (showColumnLines)
                && (this.column < this.treeTable.getColumnCount() - 1) ? 1 : 0;
        int bottom = showRowLines ? 1 : 0;
        GRID_BORDER.setColor(this.treeTable.getGridColor());
        GRID_BORDER.setInsets(0, 0, bottom, right);
    }
}