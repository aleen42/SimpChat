package com.java.ui.componentc.treetable;

import com.java.ui.component.treetable.CommonTreeTableModel;
import com.java.ui.componentc.JCTable;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JCTreeTable extends JCTable {
    private class MouseHandler extends MouseAdapter {
        final JTable table = JCTreeTable.this;

        private MouseHandler() {
        }

        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }

            Point point = e.getPoint();
            int clickCount = e.getClickCount();
            int selRow = this.table.getSelectedRow();
            int row = this.table.rowAtPoint(point);
            int colV = this.table.columnAtPoint(point);
            int col = this.table.convertColumnIndexToModel(colV);
            TreePath path = JCTreeTable.this.tree.getPathForRow(row);

            if (path == null) {
                return;
            }

            Object node = path.getLastPathComponent();

            if ((col != 0) || (path == null)
                    || (JCTreeTable.this.model.isLeaf(node))) {
                return;
            }

            if ((clickCount % 2 == 0)
                    && (!JCTreeTable.this.tree.getShowsRootHandles())
                    && (node == JCTreeTable.this.model.getRoot())) {
                toggleExpandState(JCTreeTable.this.tree, path, selRow);
            } else if (clickCount % 2 == 1) {
                Rectangle rect = this.table.getCellRect(row, colV, false);

                if (rect.contains(point)) {
                    BasicTreeUI ui = (BasicTreeUI) JCTreeTable.this.tree
                            .getUI();
                    try {
                        Method method = BasicTreeUI.class.getDeclaredMethod(
                                "isLocationInExpandControl", new Class[]{
                                TreePath.class, Integer.TYPE,
                                Integer.TYPE});
                        method.setAccessible(true);
                        boolean toggle = ((Boolean) method.invoke(
                                ui,
                                new Object[]{path,
                                        Integer.valueOf(point.x - rect.x),
                                        Integer.valueOf(point.y - rect.y)}))
                                .booleanValue();

                        if (toggle) {
                            toggleExpandState(JCTreeTable.this.tree, path,
                                    selRow);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        private void toggleExpandState(JTree tree, TreePath path, int selRow) {
            if (!tree.isExpanded(path)) {
                tree.expandPath(path);
            } else {
                tree.collapsePath(path);
            }

            JCTreeTable.this.model.fireTableDataChanged();
            this.table.getSelectionModel().setSelectionInterval(selRow, selRow);
        }
    }

    private static final long serialVersionUID = -8142407744125469821L;
    private CommonTreeTableModel model;
    private CTreeTableCellRenderer tree;

    private Map<Integer, Integer> treeRendererTextStartMap;

    public JCTreeTable(CommonTreeTableModel model) {
        this.model = model;
        init();
    }

    public JCTreeTable(DefaultMutableTreeNode root, String[] columnsName,
                       Class<?>[] columnsClass, String[] getMethodsName,
                       String[] setMethodsName) {
        this(root, columnsName, columnsClass, getMethodsName, setMethodsName,
                false);
    }

    public JCTreeTable(DefaultMutableTreeNode root, String[] columnsName,
                       Class<?>[] columnsClass, String[] getMethodsName,
                       String[] setMethodsName, boolean asksAllowsChildren) {
        this.model = new CommonTreeTableModel(this, root, columnsName,
                columnsClass, getMethodsName, setMethodsName,
                asksAllowsChildren);
        init();
    }

    public JTree getTree() {
        return this.tree;
    }

    public int getTreeRendererTextStartAt(int row) {
        Integer start = (Integer) this.treeRendererTextStartMap.get(Integer
                .valueOf(row));
        return start == null ? -1 : start.intValue();
    }

    private void init() {
        this.treeRendererTextStartMap = new HashMap<Integer, Integer>();
        this.tree = new CTreeTableCellRenderer(this, this.model);
        setModel(this.model);
        getColumn(this.model.getColumnName(0)).setCellRenderer(this.tree);
        getColumn(this.model.getColumnName(0)).setCellEditor(
                new CTreeColumnCellEditor());
        addMouseListener(new MouseHandler());
    }

    public boolean isPaintTreeLines() {
        return this.tree == null ? false : this.tree.isPaintLines();
    }

    public void setPaintTreeLines(boolean paintTreeLines) {
        if (this.tree != null) {
            this.tree.setPaintLines(paintTreeLines);
        }
    }

    public void setRowHeight(int rowHeight) {
        super.setRowHeight(rowHeight);

        if (this.tree != null) {
            this.tree.setRowHeight(rowHeight);
        }
    }

    @Deprecated
    public void setRowHeight(int row, int rowHeight) {
        setRowHeight(rowHeight);
    }

    public void setRowMargin(int rowMargin) {
        super.setRowMargin(Math.max(0, rowMargin));
    }

    @Deprecated
    public void setRowSorter(RowSorter<? extends TableModel> sorter) {
        super.setRowSorter(null);
    }

    public void setTreeRendererTextStartAt(int row, int textStart) {
        this.treeRendererTextStartMap.put(Integer.valueOf(row),
                Integer.valueOf(textStart));
    }
}