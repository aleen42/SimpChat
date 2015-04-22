package com.java.ui.component.treetable;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.Serializable;

public abstract class AbstractTreeTableModel extends DefaultTreeModel implements
        TableModel, Serializable {
    private static final long serialVersionUID = -1810683165492112232L;
    protected JTree tree;

    public AbstractTreeTableModel(TreeNode root) {
        super(root);
    }

    public AbstractTreeTableModel(TreeNode root, boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
    }

    public void addTableModelListener(TableModelListener l) {
        this.listenerList.add(TableModelListener.class, l);
    }

    public int findColumn(String columnName) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (columnName.equals(getColumnName(i))) {
                return i;
            }
        }

        return -1;
    }

    public void fireTableCellUpdated(int row, int column) {
        fireTableChanged(new TableModelEvent(this, row, row, column));
    }

    public void fireTableChanged(TableModelEvent e) {
        Object[] listeners = this.listenerList.getListenerList();

        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] != TableModelListener.class)
                continue;
            ((TableModelListener) listeners[(i + 1)]).tableChanged(e);
        }
    }

    public void fireTableDataChanged() {
        fireTableChanged(new TableModelEvent(this));
    }

    public void fireTableRowsDeleted(int firstRow, int lastRow) {
        fireTableChanged(new TableModelEvent(this, firstRow, lastRow, -1, -1));
    }

    public void fireTableRowsInserted(int firstRow, int lastRow) {
        fireTableChanged(new TableModelEvent(this, firstRow, lastRow, -1, 1));
    }

    public void fireTableRowsUpdated(int firstRow, int lastRow) {
        fireTableChanged(new TableModelEvent(this, firstRow, lastRow, -1, 0));
    }

    public void fireTableStructureChanged() {
        fireTableChanged(new TableModelEvent(this, -1));
    }

    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    public String getColumnName(int column) {
        String result = "";

        for (; column >= 0; column = column / 26 - 1) {
            result = (char) ((char) (column % 26) + 'A') + result;
        }

        return result;
    }

    public TableModelListener[] getTableModelListeners() {
        return (TableModelListener[]) this.listenerList
                .getListeners(TableModelListener.class);
    }

    public JTree getTree() {
        return this.tree;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    public void removeTableModelListener(TableModelListener l) {
        this.listenerList.remove(TableModelListener.class, l);
    }

    public void setTree(JTree tree) {
        this.tree = tree;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }
}