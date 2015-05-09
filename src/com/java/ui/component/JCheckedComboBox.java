package com.java.ui.component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class JCheckedComboBox extends JComboBox {
    private class CheckedListPopup extends JPopupMenu implements ComboPopup {
        protected class InvocationMouseHandler extends MouseAdapter {
            protected InvocationMouseHandler() {
            }

            public void mousePressed(MouseEvent e) {
                if ((SwingUtilities.isLeftMouseButton(e))
                        && (JCheckedComboBox.CheckedListPopup.this.checkedComboBox
                        .isEnabled())) {
                    if (JCheckedComboBox.CheckedListPopup.this.checkedComboBox
                            .isEditable()) {
                        Component editor = JCheckedComboBox.CheckedListPopup.this.checkedComboBox
                                .getEditor().getEditorComponent();

                        if ((!(editor instanceof JComponent))
                                || (((JComponent) editor)
                                .isRequestFocusEnabled())) {
                            editor.requestFocus();
                        }
                    } else if (JCheckedComboBox.CheckedListPopup.this.checkedComboBox
                            .isRequestFocusEnabled()) {
                        JCheckedComboBox.CheckedListPopup.this.checkedComboBox
                                .requestFocus();
                    }

                    JCheckedComboBox.CheckedListPopup.this.togglePopup();
                }
            }
        }

        private static final long serialVersionUID = 5017468454834870949L;
        protected JCheckedComboBox checkedComboBox;
        private KeyListener keySelectListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyChar() == ' ') || (e.getKeyChar() == '\n')) {
                    JCheckedComboBox.CheckedListPopup.this.checkedComboBox
                            .setSelectedIndexes(JCheckedComboBox.CheckedListPopup.this.checkedComboBox
                                    .getSelectedIndexes());
                }
            }
        };
        protected MouseListener mouseListener;

        private JScrollPane scrollPane;

        private MouseListener selectListener = new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    JCheckedComboBox.CheckedListPopup.this.checkedComboBox
                            .setSelectedIndexes(JCheckedComboBox.CheckedListPopup.this.checkedComboBox
                                    .getSelectedIndexes());
                }
            }
        };

        public CheckedListPopup(JComboBox checkedComboBox) {
            this.checkedComboBox = ((JCheckedComboBox) checkedComboBox);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setLayout(new BorderLayout());
            setFocusable(false);
            setLightWeightPopupEnabled(checkedComboBox
                    .isLightWeightPopupEnabled());
            JCheckBoxList checkedList = this.checkedComboBox.getCheckedList();

            if (checkedList != null) {
                this.scrollPane = new JScrollPane(checkedList);
                this.scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
                this.scrollPane.setFocusable(false);
                this.scrollPane.getVerticalScrollBar().setFocusable(false);
                add(this.scrollPane, "Center");
                checkedList.setFocusable(false);
                checkedList.setBorder(new EmptyBorder(0, 0, 0, 0));
                checkedList.addMouseListener(this.selectListener);
                checkedList.addKeyListener(this.keySelectListener);
            }
        }

        public KeyListener getKeyListener() {
            return null;
        }

        public JList getList() {
            return this.checkedComboBox.getCheckedList();
        }

        public MouseListener getMouseListener() {
            if (this.mouseListener == null) {
                this.mouseListener = new InvocationMouseHandler();
            }

            return this.mouseListener;
        }

        public MouseMotionListener getMouseMotionListener() {
            return null;
        }

        public void hide() {
            setVisible(false);
            this.checkedComboBox
                    .firePropertyChange("popupVisible", true, false);
        }

        public void show() {
            updatePopup();
            show(this.checkedComboBox, 0, this.checkedComboBox.getHeight());
        }

        protected void togglePopup() {
            if (isVisible()) {
                hide();
            } else {
                show();
            }
        }

        public void uninstallingUI() {
        }

        protected void updatePopup() {
            Insets insets = getInsets();
            Insets scrollerInsets = this.scrollPane.getInsets();
            Insets viewportInsets = this.scrollPane.getViewportBorder() == null ? null
                    : this.scrollPane.getViewportBorder().getBorderInsets(null);
            Insets listInsets = JCheckedComboBox.this.checkList.getInsets();
            insets.top += scrollerInsets.top + listInsets.top
                    + (viewportInsets == null ? 0 : viewportInsets.top);
            insets.bottom += scrollerInsets.bottom + listInsets.bottom
                    + (viewportInsets == null ? 0 : viewportInsets.bottom);
            int maxHeight = 200;
            JCheckBoxList list = this.checkedComboBox.getCheckedList();
            Dimension listSize = list.getPreferredSize();
            int preferredHeight = listSize.height
                    + insets.top
                    + insets.bottom
                    + (listSize.width > this.checkedComboBox.getWidth() ? this.scrollPane
                    .getHorizontalScrollBar().getPreferredSize().height
                    : 0);
            preferredHeight = preferredHeight > maxHeight ? maxHeight
                    : preferredHeight;
            setPreferredSize(new Dimension(
                    this.checkedComboBox.getSize().width, preferredHeight));
            List<Integer> selectedIndexes = this.checkedComboBox.getSelectedIndexes();
            list.clearSelection();
            list.setItemSelectedIndexes(selectedIndexes);
            list.setBackground(this.checkedComboBox.getBackground());
        }
    }

    private class PopupList<E> extends ArrayList<E> {
        private static final long serialVersionUID = 6562964910585649012L;

        private PopupList() {
        }

        public String toString() {
            if ((JCheckedComboBox.this.text4SelectedAll != null)
                    && (JCheckedComboBox.this.checkList.isSelectedAll())) {
                return JCheckedComboBox.this.text4SelectedAll;
            }

            StringBuilder sb = new StringBuilder();
            int size = size();

            for (int index = 0; index < size; index++) {
                sb.append(get(index));

                if (index == size - 1)
                    continue;
                sb.append(", ");
            }

            return sb.toString();
        }
    }

    private static final long serialVersionUID = 938062179790029115L;
    private JCheckBoxList checkList;

    private DefaultListModel listModel;

    private String text4SelectedAll;

    public JCheckedComboBox() {
        init(null);
    }

    public JCheckedComboBox(ComboBoxModel model) {
        super(model);
        init(null);
    }

    public JCheckedComboBox(Object[] listData) {
        super(listData);
        init(listData);
    }

    public JCheckedComboBox(Vector<?> listData) {
        super(listData);
        init(listData);
    }

    private void displaySelect() {
        PopupList<Object> list = new PopupList<Object>();

        for (Iterator<?> localIterator = this.checkList.getSelectedItems()
                .iterator(); localIterator.hasNext(); ) {
            Object o = localIterator.next();

            list.add(o);
        }

        getModel().setSelectedItem(list);
    }

    public JCheckBoxList getCheckedList() {
        return this.checkList;
    }

    public List<Integer> getSelectedIndexes() {
        return this.checkList.getItemSelectedIndexes();
    }

    public List<?> getSelectedItems() {
        return this.checkList.getSelectedItems();
    }

    public String getText4SelectedAll() {
        return this.text4SelectedAll;
    }

    private void init(Object listData) {
        this.checkList = new JCheckBoxList(
                this.listModel = new DefaultListModel());

        if (listData != null) {
            if ((listData instanceof Object[])) {
                for (Object data : (Object[]) listData) {
                    this.listModel.addElement(data);
                }
            } else if ((listData instanceof Vector)) {
                for (Iterator<?> localIterator = ((Vector<?>) listData).iterator(); localIterator
                        .hasNext(); ) {
                    Object data = localIterator.next();

                    this.listModel.addElement(data);
                }
            }
        }

        updateUI();
        this.checkList.setForeground(getForeground());
        displaySelect();
    }

    public void intervalAdded(ListDataEvent e) {
        super.intervalAdded(e);

        for (int index = e.getIndex0(); index <= e.getIndex1(); index++) {
            this.listModel.add(index, getItemAt(index));
        }

        displaySelect();
    }

    public void intervalRemoved(ListDataEvent e) {
        super.intervalRemoved(e);

        for (int index = e.getIndex1(); index >= e.getIndex0(); index--) {
            this.listModel.remove(index);
        }

        displaySelect();
    }

    public void selecteAll() {
        this.checkList.selectedAllItems();
        displaySelect();
    }

    public void selecteEmpty() {
        this.checkList.unselectedAllItems();
        displaySelect();
    }

    @Deprecated
    public void setEditable(boolean flag) {
        super.setEditable(false);
    }

    public void setForeground(Color fg) {
        super.setForeground(fg);

        if (this.checkList != null) {
            this.checkList.setForeground(fg);
        }
    }

    @Deprecated
    public void setSelectedIndex(int index) {
    }

    public void setSelectedIndexes(List<Integer> selectedIndexes) {
        this.checkList.setItemSelectedIndexes(selectedIndexes);
        displaySelect();
    }

    @Deprecated
    public void setSelectedItem(Object item) {
    }

    public void setSelectedItems(List<?> items) {
        this.checkList.setSelectedItems(items);
        displaySelect();
    }

    public void setText4SelectedAll(String text4SelectedAll) {
        this.text4SelectedAll = text4SelectedAll;
        repaint();
    }

    public void updateUI() {
        super.updateUI();
        try {
            BasicComboBoxUI ui = (BasicComboBoxUI) getUI();
            Field popupField = BasicComboBoxUI.class.getDeclaredField("popup");
            Field listBoxField = BasicComboBoxUI.class
                    .getDeclaredField("listBox");
            Method installListenersMethod = BasicComboBoxUI.class
                    .getDeclaredMethod("installListeners", new Class[0]);
            Method uninstallListenersMethod = BasicComboBoxUI.class
                    .getDeclaredMethod("uninstallListeners", new Class[0]);
            CheckedListPopup popup = new CheckedListPopup(this);
            popupField.setAccessible(true);
            listBoxField.setAccessible(true);
            installListenersMethod.setAccessible(true);
            uninstallListenersMethod.setAccessible(true);
            ui.unconfigureArrowButton();
            uninstallListenersMethod.invoke(ui, new Object[0]);
            popupField.set(ui, popup);
            listBoxField.set(ui, popup.getList());
            installListenersMethod.invoke(ui, new Object[0]);
            ui.configureArrowButton();
            SwingUtilities.updateComponentTreeUI(popup);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}