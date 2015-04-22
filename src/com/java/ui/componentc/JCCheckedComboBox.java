package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class JCCheckedComboBox extends JCComboBox {
    private class CCheckedComboBoxUI extends CComboBoxUI {
        private CCheckedComboBoxUI() {
        }

        protected ComboPopup createPopup() {
            return new JCCheckedComboBox.CheckedListPopup(comboBox);
        }

        protected void selectNextPossibleValue() {
        }

        protected void selectPreviousPossibleValue() {
        }
    }

    private class CheckedListPopup extends JPopupMenu implements ComboPopup {
        protected class InvocationMouseHandler extends MouseAdapter {
            protected InvocationMouseHandler() {
            }

            public void mousePressed(MouseEvent e) {
                if ((SwingUtilities.isLeftMouseButton(e))
                        && (JCCheckedComboBox.CheckedListPopup.this.checkedComboBox
                        .isEnabled())
                        && (JCCheckedComboBox.CheckedListPopup.this.checkedComboBox
                        .isEditableAll())) {
                    if (JCCheckedComboBox.CheckedListPopup.this.checkedComboBox
                            .isEditable()) {
                        Component editor = JCCheckedComboBox.CheckedListPopup.this.checkedComboBox
                                .getEditor().getEditorComponent();

                        if ((!(editor instanceof JComponent))
                                || (((JComponent) editor)
                                .isRequestFocusEnabled())) {
                            editor.requestFocus();
                        }
                    } else if (JCCheckedComboBox.CheckedListPopup.this.checkedComboBox
                            .isRequestFocusEnabled()) {
                        JCCheckedComboBox.CheckedListPopup.this.checkedComboBox
                                .requestFocus();
                    }

                    JCCheckedComboBox.CheckedListPopup.this.togglePopup();
                }
            }
        }

        private static final long serialVersionUID = 6354944188855377523L;
        protected JCCheckedComboBox checkedComboBox;
        private KeyListener keySelectListener = new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if ((e.getKeyChar() == ' ') || (e.getKeyChar() == '\n')) {
                    JCCheckedComboBox.CheckedListPopup.this.checkedComboBox
                            .setSelectedIndexes(JCCheckedComboBox.CheckedListPopup.this.checkedComboBox
                                    .getSelectedIndexes());
                }
            }
        };
        protected MouseListener mouseListener;

        private JScrollPane scrollPane;

        private MouseListener selectListener = new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    JCCheckedComboBox.CheckedListPopup.this.checkedComboBox
                            .setSelectedIndexes(JCCheckedComboBox.CheckedListPopup.this.checkedComboBox
                                    .getSelectedIndexes());
                }
            }
        };

        public CheckedListPopup(JComboBox checkedComboBox) {
            this.checkedComboBox = ((JCCheckedComboBox) checkedComboBox);
            setUI(new BasicPopupMenuUI() {
                public void installDefaults() {
                }

                protected void uninstallDefaults() {
                }
            });
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setLayout(new BorderLayout());
            setOpaque(false);
            setFocusable(false);
            setLightWeightPopupEnabled(checkedComboBox
                    .isLightWeightPopupEnabled());
            configureList(JCCheckedComboBox.this.checkList);
            add(createScroller(JCCheckedComboBox.this.checkList), "Center");
        }

        private void configureList(JCCheckBoxList checkedList) {
            checkedList.setBorder(new EmptyBorder(0, 0, 0, 0));
            checkedList.setDisabledBorder(checkedList.getBorder());
            checkedList.setVisibleInsets(0, 0, 0, 0);
            checkedList.setAlpha(0.0F);
            checkedList.setRendererOpaque(false);
            checkedList.setFocusable(false);
            checkedList.addMouseListener(this.selectListener);
            checkedList.addKeyListener(this.keySelectListener);
        }

        private JScrollPane createScroller(JCCheckBoxList checkedList) {
            this.scrollPane = new JScrollPane(checkedList) {
                private static final long serialVersionUID = -3772916298038957310L;

                public JScrollBar createHorizontalScrollBar() {
                    JScrollPane.ScrollBar hBar = new JScrollPane.ScrollBar(0) {
                        private static final long serialVersionUID = -7726079574528933660L;

                        @Deprecated
                        public void updateUI() {
                        }
                    };
                    hBar.setUI(new CScrollBarUI());
                    hBar.setBorder(null);
                    return hBar;
                }

                public JScrollBar createVerticalScrollBar() {
                    JScrollPane.ScrollBar vBar = new JScrollPane.ScrollBar(1) {
                        private static final long serialVersionUID = -7301889224776927442L;

                        @Deprecated
                        public void updateUI() {
                        }
                    };
                    vBar.setUI(new CScrollBarUI());
                    vBar.setBorder(null);
                    return vBar;
                }

                @Deprecated
                public void updateUI() {
                }
            };
            this.scrollPane.setUI(new BasicScrollPaneUI() {
                protected void installDefaults(JScrollPane scrollpane) {
                }

                protected void uninstallDefaults(JScrollPane scrollpane) {
                }

                public void update(Graphics g, JComponent c) {
                    g.setColor(c.getBackground());
                    g.fillRect(0, 0, c.getWidth(), c.getHeight());
                    paint(g, c);
                }
            });
            this.scrollPane.getHorizontalScrollBar().setOpaque(false);
            this.scrollPane.getHorizontalScrollBar().setFocusable(false);
            this.scrollPane.getVerticalScrollBar().setOpaque(false);
            this.scrollPane.getVerticalScrollBar().setFocusable(false);
            this.scrollPane.getViewport().setOpaque(false);
            this.scrollPane.setViewportBorder(new EmptyBorder(0, 0, 0, 0));
            this.scrollPane.setOpaque(false);
            this.scrollPane.setBorder(UIResourceManager
                    .getBorder("ComboBoxPopupBorder"));
            this.scrollPane.setFocusable(false);
            return this.scrollPane;
        }

        public KeyListener getKeyListener() {
            return null;
        }

        public JCCheckBoxList getList() {
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

        public void setVisible(boolean visible) {
            super.setVisible(visible);

            if (visible) {
                float alpha = this.checkedComboBox.isImageOnly() ? 0.0F
                        : this.checkedComboBox.getAlpha();
                Color oldBg = this.checkedComboBox.getBackground();
                this.scrollPane.setBackground(new Color(oldBg.getRed(), oldBg
                        .getGreen(), oldBg.getBlue(), Math
                        .round(255.0F * alpha)));
                int right = this.scrollPane.getVerticalScrollBar().isVisible() ? 1
                        : 0;
                int bottom = this.scrollPane.getHorizontalScrollBar()
                        .isVisible() ? 1 : 0;
                this.scrollPane.setViewportBorder(new EmptyBorder(0, 0, bottom,
                        right));
            } else {
                this.checkedComboBox.resetBorder();
            }
        }

        public void show() {
            if (!this.checkedComboBox.isEditableAll()) {
                return;
            }

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
            Insets viewportInsets = this.scrollPane.getViewportBorder()
                    .getBorderInsets(null);
            Insets listInsets = JCCheckedComboBox.this.checkList.getInsets();
            insets.top += scrollerInsets.top + listInsets.top
                    + viewportInsets.top;
            insets.bottom += scrollerInsets.bottom + listInsets.bottom
                    + viewportInsets.bottom;
            int maxHeight = 230;
            Dimension listSize = JCCheckedComboBox.this.checkList
                    .getPreferredSize();
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
            List<Integer> selectedIndexes = this.checkedComboBox
                    .getSelectedIndexes();
            JCCheckedComboBox.this.checkList.clearSelection();
            JCCheckedComboBox.this.checkList
                    .setItemSelectedIndexes(selectedIndexes);
        }

        @Deprecated
        public void updateUI() {
            setUI(getUI());
        }
    }

    private class PopupList<E> extends ArrayList<E> {
        private static final long serialVersionUID = -641625025177013444L;

        private PopupList() {
        }

        public String toString() {
            if ((JCCheckedComboBox.this.text4SelectedAll != null)
                    && (JCCheckedComboBox.this.checkList.isSelectedAll())) {
                return JCCheckedComboBox.this.text4SelectedAll;
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

    private static final long serialVersionUID = 6723787291459475194L;

    private JCCheckBoxList checkList;

    private DefaultListModel listModel;

    private String text4SelectedAll;

    public JCCheckedComboBox() {
        init(null);
    }

    public JCCheckedComboBox(ComboBoxModel model) {
        super(model);
        init(null);
    }

    public JCCheckedComboBox(Object[] listData) {
        super(listData);
        init(listData);
    }

    public JCCheckedComboBox(Vector<?> listData) {
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

    public JCCheckBoxList getCheckedList() {
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
        this.checkList = new JCCheckBoxList(
                this.listModel = new DefaultListModel());

        if (listData != null) {
            if ((listData instanceof Object[])) {
                for (Object data : (Object[]) listData) {
                    this.listModel.addElement(data);
                }
            } else if ((listData instanceof Vector)) {
                for (Iterator<?> localIterator = ((Vector<?>) listData)
                        .iterator(); localIterator.hasNext(); ) {
                    Object data = localIterator.next();

                    this.listModel.addElement(data);
                }
            }
        }

        setUI(new CCheckedComboBoxUI());
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
}