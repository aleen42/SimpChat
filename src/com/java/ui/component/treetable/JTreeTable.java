package com.java.ui.component.treetable;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class JTreeTable extends JTable {
    private class MouseHandler extends MouseAdapter {
        final JTable table = JTreeTable.this;

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
            TreePath path = JTreeTable.this.tree.getPathForRow(row);

            if (path == null) {
                return;
            }

            Object node = path.getLastPathComponent();

            if ((col != 0) || (path == null)
                    || (JTreeTable.this.model.isLeaf(node))) {
                return;
            }

            if ((clickCount % 2 == 0)
                    && (!JTreeTable.this.tree.getShowsRootHandles())
                    && (node == JTreeTable.this.model.getRoot())) {
                toggleExpandState(JTreeTable.this.tree, path, selRow);
            } else if (clickCount % 2 == 1) {
                Rectangle rect = this.table.getCellRect(row, colV, false);

                if (rect.contains(point)) {
                    BasicTreeUI ui = (BasicTreeUI) JTreeTable.this.tree.getUI();
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
                            toggleExpandState(JTreeTable.this.tree, path,
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

            JTreeTable.this.model.fireTableDataChanged();
            this.table.getSelectionModel().setSelectionInterval(selRow, selRow);
        }
    }

    private static final long serialVersionUID = -8142407744125469821L;
    private CommonTreeTableModel model;
    private boolean paintTreeLines;
    private TreeTableCellRenderer tree;

    private Map<Integer, Integer> treeRendererTextStartMap;

    public JTreeTable(CommonTreeTableModel model) {
        this.model = model;
        init();
    }

    public JTreeTable(DefaultMutableTreeNode root, String[] columnsName,
                      Class<?>[] columnsClass, String[] getMethodsName,
                      String[] setMethodsName) {
        this(root, columnsName, columnsClass, getMethodsName, setMethodsName,
                false);
    }

    public JTreeTable(DefaultMutableTreeNode root, String[] columnsName,
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
        this.tree = new TreeTableCellRenderer(this, this.model);
        setRowHeight(20);
        setModel(this.model);
        getColumn(this.model.getColumnName(0)).setCellRenderer(this.tree);
        getColumn(this.model.getColumnName(0)).setCellEditor(
                new TreeColumnCellEditor());
        addMouseListener(new MouseHandler());
    }

    public boolean isPaintTreeLines() {
        return this.paintTreeLines;
    }

    public void setPaintTreeLines(boolean paintTreeLines) {
        if (this.tree != null) {
            this.paintTreeLines = paintTreeLines;
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

    public void superUpdateUI() {
        super.updateUI();
    }

    public void updateUI() {
        if (this.tree != null) {
            DefaultTreeCellRenderer treeRenderer = (DefaultTreeCellRenderer) this.tree
                    .getCellRenderer();
            treeRenderer.setOpenIcon(UIManager.getIcon("Tree.openIcon"));
            treeRenderer.setClosedIcon(UIManager.getIcon("Tree.closedIcon"));
            treeRenderer.setLeafIcon(UIManager.getIcon("Tree.leafIcon"));
        }

        editingCanceled(null);
        super.updateUI();
        setPaintTreeLines(isPaintTreeLines());
    }
}