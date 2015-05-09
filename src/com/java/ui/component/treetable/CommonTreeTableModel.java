package com.java.ui.component.treetable;

import com.java.ui.component.TableCellEditableController;
import com.java.ui.util.Util;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class CommonTreeTableModel extends AbstractTreeTableModel {
    private static final long serialVersionUID = 766211503325659293L;
    private Class<?>[] columnsClass;
    private String[] columnsName;
    private TableCellEditableController editableController;
    private String[] getMethodsName;
    private String[] setMethodsName;
    private JTable table;
    private Class<?>[] unPrimitiveClass;

    private CommonTreeTableModel(DefaultMutableTreeNode root,
                                 boolean asksAllowsChildren) {
        super(root, asksAllowsChildren);
    }

    public CommonTreeTableModel(JTable table, DefaultMutableTreeNode root,
                                String[] columnsName, Class<?>[] columnsClass,
                                String[] getMethodsName, String[] setMethodsName,
                                boolean asksAllowsChildren) {
        this(root, asksAllowsChildren);
        this.table = table;
        this.columnsName = columnsName;
        this.columnsClass = columnsClass;
        this.unPrimitiveClass = Util.createUnprimitiveClasses(columnsClass);
        this.getMethodsName = getMethodsName;
        this.setMethodsName = setMethodsName;
    }

    public void delAllRow() {
        int rowCount = getRowCount();

        if (rowCount > 0) {
            setRoot(null);
            fireTableRowsDeleted(0, rowCount - 1);
        }
    }

    public void delRowAt(int row) {
        if ((row >= 0) && (row < getRowCount())) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.tree
                    .getPathForRow(row).getLastPathComponent();

            if (node == getRoot()) {
                delAllRow();
            } else {
                removeNodeFromParent(node);
                fireTableRowsDeleted(row, row);
            }
        }
    }

    public void delRowData(Object obj) {
        int pos = -1;

        for (int i = 0; (i < getRowCount()) && (obj != null); i++) {
            if (obj != getRowDataAt(i))
                continue;
            pos = i;
            break;
        }

        if (pos >= 0) {
            delRowAt(pos);
        }
    }

    public void delRows(int[] rows) {
        int rowCount = getRowCount();
        int subRowCount = rows.length;

        Arrays.sort(rows);

        for (int i = subRowCount - 1; i >= 0; i--) {
            int row = rows[i];

            if ((row < 0) || (row >= rowCount))
                continue;
            delRowAt(row);
        }
    }

    public void delRowsArea(int firstRow, int lastRow) {
        int[] rows = new int[lastRow - firstRow + 1];

        for (int i = 0; i < rows.length; i++) {
            rows[i] = (firstRow + i);
        }

        delRows(rows);
    }

    public void delSelectedRow() {
        delRowAt(this.table.getSelectedRow());
    }

    public void delSelectedRows() {
        delRows(this.table.getSelectedRows());
    }

    public void delSubRowDatas(List<Object> subObjs) {
        if ((subObjs != null) && (!subObjs.isEmpty())) {
            for (Iterator<Object> localIterator = subObjs.iterator(); localIterator
                    .hasNext(); ) {
                Object obj = localIterator.next();

                delRowData(obj);
            }
        }
    }

    public List<Object> getAllRowDatas() {
        return getAreaRowDatas(0, getRowCount() - 1);
    }

    public List<Object> getAreaRowDatas(int firstRow, int lastRow) {
        int[] rows = new int[lastRow - firstRow + 1];

        for (int i = 0; i < rows.length; i++) {
            rows[i] = (firstRow + i);
        }

        return getSubRowDatas(rows);
    }

    public Class<?> getColumnClass(int column) {
        return this.unPrimitiveClass[column];
    }

    public Class<?> getColumnClassOnView(int column) {
        return getColumnClass(this.table.convertColumnIndexToModel(column));
    }

    public int getColumnCount() {
        return this.columnsName.length;
    }

    public String getColumnName(int column) {
        return this.columnsName[column];
    }

    public String getColumnNameOnView(int column) {
        return getColumnName(this.table.convertColumnIndexToModel(column));
    }

    public int getRowCount() {
        return this.tree.getRowCount();
    }

    public Object getRowDataAt(int row) {
        if ((row >= 0) && (row < getRowCount())) {
            return ((DefaultMutableTreeNode) this.tree.getPathForRow(row)
                    .getLastPathComponent()).getUserObject();
        }

        return null;
    }

    public Object getSelectedRowData() {
        return getRowDataAt(this.table.getSelectedRow());
    }

    public List<Object> getSelectedRowDatas() {
        return getSubRowDatas(this.table.getSelectedRows());
    }

    public List<Object> getSubRowDatas(int[] rows) {
        ArrayList<Object> subList = new ArrayList<Object>();
        int rowCount = getRowCount();

        for (int row : rows) {
            if ((row >= rowCount) || (row < 0))
                continue;
            subList.add(getRowDataAt(row));
        }

        return subList.isEmpty() ? null : subList;
    }

    public JTable getTreeTable() {
        return this.table;
    }

    public Object getValueAt(int row, int column) {
        Object value = null;

        if ((this.tree.getRowCount() > 0) && (row < getRowCount())
                && (column < getColumnCount())) {
            Object rowData = getRowDataAt(row);
            Class<? extends Object> rowClass = rowData.getClass();
            try {
                Method method = rowClass.getMethod(this.getMethodsName[column],
                        new Class[0]);
                value = method.invoke(rowData, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return value;
    }

    public Object getValueOnViewAt(int row, int column) {
        column = this.table.convertColumnIndexToModel(column);
        return getValueAt(row, column);
    }

    public void insertNodeAtFirst(MutableTreeNode newChild,
                                  MutableTreeNode parent) {
        insertNodeInto(newChild, parent, 0);
    }

    public void insertNodeAtLast(MutableTreeNode newChild,
                                 MutableTreeNode parent) {
        insertNodeInto(newChild, parent, parent.getChildCount());
    }

    public void insertNodeInto(MutableTreeNode newChild,
                               MutableTreeNode parent, int index) {
        super.insertNodeInto(newChild, parent, index);
        fireTableDataChanged();
        TreePath path = new TreePath(getPathToRoot(parent));

        if (this.tree.isCollapsed(path)) {
            this.tree.expandPath(path);
        }

        scrollToRowAt(this.tree.getRowForPath(path) + index + 1);
    }

    public boolean isCellEditable(int row, int column) {
        if (this.editableController != null) {
            return this.editableController.isCellEditable(row, column);
        }

        return false;
    }

    public boolean isCellEditableOnView(int row, int column) {
        column = this.table.convertColumnIndexToModel(column);
        return isCellEditable(row, column);
    }

    public void refreshRow(int row) {
        if ((row >= 0) && (row < getRowCount())) {
            nodeChanged((DefaultMutableTreeNode) this.tree.getPathForRow(row)
                    .getLastPathComponent());
            fireTableRowsUpdated(row, row);
        }
    }

    public void refreshUI() {
        for (int i = 0; i < getRowCount(); i++) {
            nodeChanged((DefaultMutableTreeNode) this.tree.getPathForRow(i)
                    .getLastPathComponent());
        }

        fireTableDataChanged();
    }

    public void scrollLastRow() {
        scrollToRowAt(this.table.getRowCount() - 1);
    }

    public void scrollToRowAt(int row) {
        Rectangle rect = this.table.getCellRect(row, 0, true);
        this.table.scrollRectToVisible(rect);
    }

    public void setEditableController(
            TableCellEditableController editableController) {
        this.editableController = editableController;
    }

    public void setRowDataAt(int row, Object obj) {
        if ((row >= 0) && (row < getRowCount()) && (obj != null)) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) this.tree
                    .getPathForRow(row).getLastPathComponent();
            node.setUserObject(obj);
            nodeChanged(node);
            fireTableRowsUpdated(row, row);
        }
    }

    public void setValueAt(Object value, int row, int column) {
        if ((row >= 0) && (row < getRowCount()) && (column >= 0)
                && (column < getColumnCount())) {
            Object rowData = getRowDataAt(row);
            Class<? extends Object> rowClass = rowData.getClass();
            try {
                Method method = rowClass.getMethod(this.setMethodsName[column],
                        new Class[]{this.columnsClass[column]});
                method.invoke(rowData, new Object[]{value});
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                nodeChanged((DefaultMutableTreeNode) this.tree.getPathForRow(
                        row).getLastPathComponent());
                fireTableCellUpdated(row, column);
            }
        }
    }

    public void setValueOnViewAt(Object value, int row, int column) {
        column = this.table.convertColumnIndexToModel(column);
        setValueAt(value, row, column);
    }
}