package com.java.ui.componentc;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class JCCheckBoxList extends JCList implements ListDataListener {
    private class KeyListener extends KeyAdapter {
        private KeyListener() {
        }

        public void keyPressed(KeyEvent e) {
            char keyChar = e.getKeyChar();

            if ((keyChar == ' ') || (keyChar == '\n')) {
                JCCheckBoxList.this.reverseSelected();
            }
        }
    }

    private class MouseListener extends MouseAdapter {
        private MouseListener() {
        }

        public void mouseReleased(MouseEvent e) {
            JCCheckBox box = (JCCheckBox) JCCheckBoxList.this.getCellRenderer();
            boolean selected = JCCheckBoxList.this
                    .isItemSelectedAt(JCCheckBoxList.this.getSelectedIndex());
            box.setSelected(selected);
            Icon icon = selected ? box.getSelectedIcon() : box.getIcon();
            Insets insets = JCCheckBoxList.this.getInsets();
            int startX = insets.left
                    + (JCCheckBoxList.this.actionAllRow ? 0
                    : box.getInsets().left);
            int endX = JCCheckBoxList.this.actionAllRow ? JCCheckBoxList.this
                    .getWidth() - insets.right : startX
                    + (icon == null ? 0 : icon.getIconWidth());

            if ((e.getX() >= startX) && (e.getX() <= endX)
                    && (SwingUtilities.isLeftMouseButton(e))) {
                JCCheckBoxList.this.reverseSelected();
            }
        }
    }

    private static final long serialVersionUID = 964269643694520103L;

    private boolean actionAllRow;

    private List<Object> selectedList;

    public JCCheckBoxList() {
        init();
    }

    public JCCheckBoxList(ListModel dataModel) {
        super(dataModel);
        init();
    }

    public JCCheckBoxList(Object[] listData) {
        super(listData);
        init();
    }

    public JCCheckBoxList(Vector<?> listData) {
        super(listData);
        init();
    }

    private void changeAll(boolean selected) {
        ListModel model = getModel();

        for (int index = 0; index < model.getSize(); index++) {
            this.selectedList.set(index, selected ? model.getElementAt(index)
                    : null);
        }

        updateCellRenderer();
    }

    public void contentsChanged(ListDataEvent e) {
        ListModel model = (ListModel) e.getSource();

        for (int index = e.getIndex0(); index <= e.getIndex1(); index++) {
            if (this.selectedList.get(index) == null)
                continue;
            this.selectedList.set(index, model.getElementAt(index));
        }
    }

    public List<Object> getAllItems() {
        ArrayList<Object> items = new ArrayList<Object>();
        ListModel model = getModel();

        for (int index = 0; index < model.getSize(); index++) {
            items.add(model.getElementAt(index));
        }

        return items;
    }

    public List<Integer> getItemSelectedIndexes() {
        List<Integer> list = new ArrayList<Integer>();

        for (int index = 0; index < this.selectedList.size(); index++) {
            if (this.selectedList.get(index) == null)
                continue;
            list.add(Integer.valueOf(index));
        }

        return list;
    }

    public List<?> getSelectedItems() {
        ArrayList<Object> list = new ArrayList<Object>();

        for (Iterator<Object> localIterator = this.selectedList.iterator(); localIterator
                .hasNext(); ) {
            Object item = localIterator.next();

            if (item == null)
                continue;
            list.add(item);
        }

        return list;
    }

    private void init() {
        this.selectedList = new ArrayList<Object>();

        for (int index = 0; index < getModel().getSize(); index++) {
            this.selectedList.add(null);
        }

        setCellRenderer(new CCheckBoxListCellRenderer.UIResource(
                this.selectedList));
        addMouseListener(new MouseListener());
        addKeyListener(new KeyListener());
        getModel().addListDataListener(this);
    }

    public void intervalAdded(ListDataEvent e) {
        for (int index = e.getIndex0(); index <= e.getIndex1(); index++) {
            this.selectedList.add(index, null);
        }
    }

    public void intervalRemoved(ListDataEvent e) {
        for (int index = e.getIndex1(); index >= e.getIndex0(); index--) {
            this.selectedList.remove(index);
        }
    }

    public boolean isActionAllRow() {
        return this.actionAllRow;
    }

    public boolean isItemSelectedAt(int index) {
        if ((index >= 0) && (index < getModel().getSize())) {
            return this.selectedList.get(index) != null;
        }

        return false;
    }

    public boolean isSelectedAll() {
        int count = getModel().getSize();
        return (count > 0) && (getItemSelectedIndexes().size() == count);
    }

    public boolean isSelectedEmpty() {
        return getItemSelectedIndexes().size() == 0;
    }

    public boolean isSelectedItem(Object item) {
        ListModel model = getModel();
        int itemIndex = -1;

        for (int index = 0; index < model.getSize(); index++) {
            if (item != model.getElementAt(index))
                continue;
            itemIndex = index;
            break;
        }

        return isItemSelectedAt(itemIndex);
    }

    private void reverseSelected() {
        reverseSelected(getSelectedIndex());
    }

    public void reverseSelected(int index) {
        if ((index >= 0) && (index < getModel().getSize())) {
            this.selectedList.set(index,
                    this.selectedList.get(index) == null ? getModel()
                            .getElementAt(index) : null);
            updateCellRenderer();
        }
    }

    public void selectedAllItems() {
        changeAll(true);
    }

    public void setActionAllRow(boolean actionAllRow) {
        this.actionAllRow = actionAllRow;
    }

    public void setItemSelectedAt(int index, boolean selected) {
        ListModel model = getModel();

        if ((index >= 0) && (index < model.getSize())) {
            this.selectedList.set(index, selected ? model.getElementAt(index)
                    : null);
            updateCellRenderer();
        }
    }

    public void setItemSelectedIndexes(List<Integer> indexes) {
        ListModel model = getModel();

        for (int index = 0; index < model.getSize(); index++) {
            this.selectedList.set(index, indexes.contains(Integer
                    .valueOf(index)) ? model.getElementAt(index) : null);
        }

        updateCellRenderer();
    }

    public void setModel(ListModel model) {
        if (model == null) {
            throw new IllegalArgumentException("model must be non null");
        }

        ListModel oldModel = getModel();
        oldModel.removeListDataListener(this);
        super.setModel(model);
        this.selectedList.clear();

        for (int index = 0; index < model.getSize(); index++) {
            this.selectedList.add(null);
        }

        model.addListDataListener(this);
    }

    public void setSelectedItem(Object item, boolean selected) {
        ListModel model = getModel();
        int itemIndex = -1;

        for (int index = 0; index < model.getSize(); index++) {
            if (item != model.getElementAt(index))
                continue;
            itemIndex = index;
            break;
        }

        setItemSelectedAt(itemIndex, selected);
        updateCellRenderer();
    }

    public void setSelectedItems(List<?> items) {
        ListModel model = getModel();

        for (int index = 0; index < model.getSize(); index++) {
            Object item = model.getElementAt(index);
            this.selectedList.set(index, items.contains(item) ? item : null);
        }

        updateCellRenderer();
    }

    public void unselectedAllItems() {
        changeAll(false);
    }

    private void updateCellRenderer() {
        repaint();
    }
}