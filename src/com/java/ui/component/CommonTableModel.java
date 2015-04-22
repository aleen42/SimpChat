package com.java.ui.component;

import com.java.ui.util.Util;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonTableModel<E> extends AbstractTableModel {
    private static final long serialVersionUID = -3141048810337880013L;
    private Class<?>[] columnsClass;
    private String[] columnsName;
    private List<E> dataList;
    private TableCellEditableController editableController;
    private String[] getMethodsName;
    private String[] setMethodsName;
    private JTable table;
    private Class<?>[] unPrimitiveClass;

    public CommonTableModel(JTable table, String[] columnsName,
                            Class<?>[] columnsClass, String[] getMethodsName,
                            String[] setMethodsName, List<E> dataList) {
        this.table = table;
        this.columnsName = columnsName;
        this.columnsClass = columnsClass;
        this.unPrimitiveClass = Util.createUnprimitiveClasses(columnsClass);
        this.getMethodsName = getMethodsName;
        this.setMethodsName = setMethodsName;
        this.dataList = dataList;
    }

    public int[] convertRowIndexesToModel(int[] rowIndexInView) {
        int[] mIndexes = new int[rowIndexInView.length];
        int rowCount = getRowCount();
        int arrayIndex = 0;

        for (int vIndex : rowIndexInView) {
            if ((vIndex >= 0) && (vIndex < rowCount)) {
                mIndexes[(arrayIndex++)] = this.table
                        .convertRowIndexToModel(vIndex);
            } else {
                mIndexes[(arrayIndex++)] = vIndex;
            }
        }

        return mIndexes;
    }

    public int[] convertRowIndexesToView(int[] rowIndexesInModel) {
        int[] vIndexes = new int[rowIndexesInModel.length];
        int rowCount = getRowCount();
        int arrayIndex = 0;

        for (int mIndex : rowIndexesInModel) {
            if ((mIndex >= 0) && (mIndex < rowCount)) {
                vIndexes[(arrayIndex++)] = this.table
                        .convertRowIndexToView(mIndex);
            } else {
                vIndexes[(arrayIndex++)] = mIndex;
            }
        }

        return vIndexes;
    }

    public void delAllRow() {
        int rowCount = getRowCount();

        if (rowCount > 0) {
            this.dataList.clear();
            fireTableRowsDeleted(0, rowCount - 1);
        }
    }

    public void delRowAt(int row) {
        if ((row >= 0) && (row < getRowCount())) {
            this.dataList.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    public void delRowData(Object obj) {
        int pos;
        if ((obj != null) && ((pos = this.dataList.indexOf(obj)) >= 0)) {
            delRowAt(pos);
        }
    }

    public void delRowOnViewAt(int row) {
        if ((row >= 0) && (row < getRowCount())) {
            delRowAt(this.table.convertRowIndexToModel(row));
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
            this.dataList.remove(row);
            fireTableRowsDeleted(row, row);
        }
    }

    public void delRowsArea(int firstRow, int lastRow) {
        int[] rows = new int[lastRow - firstRow + 1];

        for (int i = 0; i < rows.length; i++) {
            rows[i] = (firstRow + i);
        }

        delRows(rows);
    }

    public void delRowsAreaOnView(int firstRow, int lastRow) {
        int[] rows = new int[lastRow - firstRow + 1];

        for (int i = 0; i < rows.length; i++) {
            rows[i] = (firstRow + i);
        }

        delRowsOnView(rows);
    }

    public void delRowsOnView(int[] rows) {
        delRows(convertRowIndexesToModel(rows));
    }

    public void delSelectedRow() {
        delRowOnViewAt(this.table.getSelectedRow());
    }

    public void delSelectedRows() {
        delRowsOnView(this.table.getSelectedRows());
    }

    public void delSubRowDatas(List<E> subObjs) {
        if ((subObjs != null) && (!subObjs.isEmpty())) {
            for (Object obj : subObjs) {
                delRowData(obj);
            }
        }
    }

    public List<E> getAllRowDatas() {
        return getAreaRowDatas(0, getRowCount() - 1);
    }

    public List<E> getAllRowDatasOnView() {
        return getAreaRowDatasOnView(0, getRowCount() - 1);
    }

    public List<E> getAreaRowDatas(int firstRow, int lastRow) {
        int[] rows = new int[lastRow - firstRow + 1];

        for (int i = 0; i < rows.length; i++) {
            rows[i] = (firstRow + i);
        }

        return getSubRowDatas(rows);
    }

    public List<E> getAreaRowDatasOnView(int firstRow, int lastRow) {
        int[] rows = new int[lastRow - firstRow + 1];

        for (int i = 0; i < rows.length; i++) {
            rows[i] = (firstRow + i);
        }

        return getSubRowDatasOnView(rows);
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
        return this.dataList.size();
    }

    public E getRowDataAt(int row) {
        if ((row >= 0) && (row < getRowCount())) {
            return this.dataList.get(row);
        }

        return null;
    }

    public E getRowDataOnViewAt(int row) {
        return getRowDataAt(this.table.convertRowIndexToModel(row));
    }

    public E getSelectedRowData() {
        return getRowDataOnViewAt(this.table.getSelectedRow());
    }

    public List<E> getSelectedRowDatas() {
        return getSubRowDatasOnView(this.table.getSelectedRows());
    }

    @SuppressWarnings("unchecked")
    public List<E> getSubRowDatas(int[] rows) {
        @SuppressWarnings("rawtypes")
        List subList = new ArrayList<Object>();
        int rowCount = getRowCount();

        for (int row : rows) {
            if ((row >= rowCount) || (row < 0))
                continue;
            subList.add(getRowDataAt(row));
        }

        return subList.isEmpty() ? null : subList;
    }

    public List<E> getSubRowDatasOnView(int[] rows) {
        return getSubRowDatas(convertRowIndexesToModel(rows));
    }

    public JTable getTable() {
        return this.table;
    }

    public Object getValueAt(int row, int column) {
        Object value = null;

        if ((!this.dataList.isEmpty()) && (row < getRowCount())
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
        row = this.table.convertRowIndexToModel(row);
        column = this.table.convertColumnIndexToModel(column);
        return getValueAt(row, column);
    }

    public void insertRowData(E rowData) {
        insertRowDataAt(getRowCount(), rowData);
    }

    public void insertRowDataAt(int row, E rowData) {
        if ((row >= 0) && (row <= getRowCount()) && (rowData != null)) {
            this.dataList.add(row, rowData);
            fireTableRowsInserted(row, row);
            scrollToRowAt(row);
        }
    }

    public void insertRowDataOnView(E rowData) {
        insertRowDataOnViewAt(getRowCount(), rowData);
    }

    public void insertRowDataOnViewAt(int row, E rowData) {
        int rowCount = getRowCount();

        if ((row >= 0) && (row <= rowCount) && (rowData != null)) {
            if (row == rowCount) {
                insertRowDataAt(this.table.convertRowIndexToModel(row - 1) + 1,
                        rowData);
            } else {
                insertRowDataAt(this.table.convertRowIndexToModel(row), rowData);
            }
        }
    }

    public void insertRowDatas(int startRow, List<E> rowDatas) {
        if ((startRow >= 0) && (startRow <= getRowCount())
                && (rowDatas != null) && (!rowDatas.isEmpty())) {
            int endRow = startRow + rowDatas.size() - 1;
            this.dataList.addAll(startRow, rowDatas);
            fireTableRowsInserted(startRow, endRow);
            scrollToRowAt(endRow);
        }
    }

    public void insertRowDatas(List<E> rowDatas) {
        insertRowDatas(getRowCount(), rowDatas);
    }

    public void insertRowDatasOnView(int startRow, List<E> rowDatas) {
        int rowCount = getRowCount();

        if ((startRow >= 0) && (startRow <= rowCount) && (rowDatas != null)
                && (!rowDatas.isEmpty())) {
            if (startRow == rowCount) {
                insertRowDatas(
                        this.table.convertRowIndexToModel(rowCount - 1) + 1,
                        rowDatas);
            } else {
                insertRowDatas(this.table.convertRowIndexToModel(startRow),
                        rowDatas);
            }
        }
    }

    public boolean isCellEditable(int row, int column) {
        if (this.editableController != null) {
            return this.editableController.isCellEditable(row, column);
        }

        return false;
    }

    public boolean isCellEditableOnView(int row, int column) {
        row = this.table.convertRowIndexToModel(row);
        column = this.table.convertColumnIndexToModel(column);
        return isCellEditable(row, column);
    }

    public void refreshRow(int row) {
        if ((row >= 0) && (row < getRowCount())) {
            fireTableRowsUpdated(row, row);
        }
    }

    public void refreshRowOnView(int row) {
        if ((row >= 0) && (row < getRowCount())) {
            refreshRow(this.table.convertRowIndexToModel(row));
        }
    }

    public void refreshUI() {
        fireTableDataChanged();
    }

    public void scrollLastRow() {
        scrollToRowOnViewAt(this.table.getRowCount() - 1);
    }

    public void scrollToRowAt(int row) {
        scrollToRowOnViewAt(this.table.convertRowIndexToView(row));
    }

    public void scrollToRowOnViewAt(int row) {
        Rectangle rect = this.table.getCellRect(row, 0, true);
        this.table.scrollRectToVisible(rect);
    }

    public void setDataList(List<E> dataList) {
        if (dataList != null) {
            this.dataList = dataList;
            refreshUI();
        }
    }

    public void setEditableController(
            TableCellEditableController editableController) {
        this.editableController = editableController;
    }

    public void setRowDataAt(int row, E obj) {
        if ((row >= 0) && (row < getRowCount()) && (obj != null)) {
            this.dataList.set(row, obj);
            fireTableRowsUpdated(row, row);
        }
    }

    public void setRowDataOnViewAt(int row, E obj) {
        if ((row >= 0) && (row < getRowCount()) && (obj != null)) {
            setRowDataAt(this.table.convertRowIndexToModel(row), obj);
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
                fireTableCellUpdated(row, column);
            }
        }
    }

    public void setValueOnViewAt(Object value, int row, int column) {
        row = this.table.convertRowIndexToModel(row);
        column = this.table.convertColumnIndexToModel(column);
        setValueAt(value, row, column);
    }
}