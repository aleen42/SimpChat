package com.java.ui.componentc;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class CTableColumnModel extends DefaultTableColumnModel implements
        ActionListener {
    private class CheckBoxMenuItem extends JCCheckBoxMenuItem {
        private static final long serialVersionUID = 2333247143034833883L;
        private TableColumn column;

        public CheckBoxMenuItem(String text, TableColumn column) {
            super();
            this.column = column;
        }

        public TableColumn getColumn() {
            return this.column;
        }
    }

    private static final long serialVersionUID = -8111507962954048460L;
    private List<TableColumn> currentColumns;
    private Map<TableColumn, CheckBoxMenuItem> initialMap;

    private JPopupMenu menu;

    public CTableColumnModel(JPopupMenu menu) {
        this.currentColumns = new ArrayList<TableColumn>();
        this.initialMap = new LinkedHashMap<TableColumn, CheckBoxMenuItem>();
        this.menu = menu;
    }

    public void actionPerformed(ActionEvent e) {
        CheckBoxMenuItem item = (CheckBoxMenuItem) e.getSource();
        setColumnVisible(item.getColumn(), item.isSelected());
    }

    public void addColumn(TableColumn column) {
        CheckBoxMenuItem item = createMenuItem(column);
        this.currentColumns.add(column);
        this.initialMap.put(column, item);
        this.menu.add(item);
        super.addColumn(column);
    }

    private CheckBoxMenuItem createMenuItem(TableColumn column) {
        CheckBoxMenuItem item = new CheckBoxMenuItem(column.getIdentifier()
                .toString(), column);
        item.setSelected(true);
        item.addActionListener(this);
        return item;
    }

    public int getColumnCount(boolean includeHidden) {
        if (includeHidden) {
            return this.initialMap.size();
        }

        return getColumnCount();
    }

    private TableColumn getColumnIncludeHidden(Object identifier) {
        for (TableColumn column : this.initialMap.keySet()) {
            if (column.getIdentifier().equals(identifier)) {
                return column;
            }
        }

        return null;
    }

    public List<TableColumn> getColumns(boolean includeHidden) {
        if (includeHidden) {
            return new ArrayList<TableColumn>(this.initialMap.keySet());
        }

        return Collections.list(getColumns());
    }

    public boolean isColumnVisible(Object identifier) {
        TableColumn column = getColumnIncludeHidden(identifier);
        return column != null ? isColumnVisible(column) : false;
    }

    public boolean isColumnVisible(TableColumn column) {
        CheckBoxMenuItem item = (CheckBoxMenuItem) this.initialMap.get(column);
        return (item != null) && (item.isSelected());
    }

    public void moveColumn(int columnIndex, int newIndex) {
        if (columnIndex != newIndex) {
            updateCurrentColumns(columnIndex, newIndex);
        }

        super.moveColumn(columnIndex, newIndex);
    }

    public void removeColumn(TableColumn column) {
        this.currentColumns.remove(column);
        this.menu.remove((Component) this.initialMap.remove(column));
        super.removeColumn(column);
    }

    public void setColumnVisible(Object identifier, boolean visible) {
        TableColumn column = getColumnIncludeHidden(identifier);

        if (column != null) {
            setColumnVisible(column, visible);
        }
    }

    public void setColumnVisible(TableColumn column, boolean visible) {
        if (visible) {
            super.addColumn(column);
            Integer addIndex = Integer.valueOf(this.currentColumns
                    .indexOf(column));

            for (int i = 0; i < getColumnCount() - 1; i++) {
                TableColumn tableCol = getColumn(i);
                int actualPosition = this.currentColumns.indexOf(tableCol);

                if (actualPosition <= addIndex.intValue())
                    continue;
                super.moveColumn(getColumnCount() - 1, i);
                break;
            }

        } else {
            super.removeColumn(column);
        }

        CheckBoxMenuItem menuItem = (CheckBoxMenuItem) this.initialMap
                .get(column);

        if (menuItem.isSelected() != visible) {
            menuItem.removeActionListener(this);
            menuItem.setSelected(visible);
            menuItem.addActionListener(this);
        }
    }

    private void updateCurrentColumns(int oldIndex, int newIndex) {
        TableColumn movedColumn = (TableColumn) this.tableColumns.elementAt(oldIndex);
        int oldPosition = this.currentColumns.indexOf(movedColumn);
        TableColumn targetColumn = (TableColumn) this.tableColumns.elementAt(newIndex);
        int newPosition = this.currentColumns.indexOf(targetColumn);
        this.currentColumns.remove(oldPosition);
        this.currentColumns.add(newPosition, movedColumn);
    }
}