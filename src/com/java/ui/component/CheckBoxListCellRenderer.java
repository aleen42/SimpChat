package com.java.ui.component;

import sun.swing.DefaultLookup;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class CheckBoxListCellRenderer extends JCheckBox implements
        ListCellRenderer, Serializable {
    public static class UIResource extends CheckBoxListCellRenderer {
        private static final long serialVersionUID = -4437418539365040975L;

        public UIResource(List<Object> selectedList) {
            super(selectedList);
        }
    }

    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1,
            1, 1);

    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1,
            1);
    private static final long serialVersionUID = 4141370835229193265L;

    private List<?> selectedList;

    public CheckBoxListCellRenderer(List<?> selectedList) {
        setOpaque(true);
        setBorder(getNoFocusBorder());
        setName("List.cellRenderer");
        this.selectedList = selectedList;
    }

    public void firePropertyChange(String propertyName, boolean oldValue,
                                   boolean newValue) {
    }

    public void firePropertyChange(String propertyName, byte oldValue,
                                   byte newValue) {
    }

    public void firePropertyChange(String propertyName, char oldValue,
                                   char newValue) {
    }

    public void firePropertyChange(String propertyName, double oldValue,
                                   double newValue) {
    }

    public void firePropertyChange(String propertyName, float oldValue,
                                   float newValue) {
    }

    public void firePropertyChange(String propertyName, int oldValue,
                                   int newValue) {
    }

    public void firePropertyChange(String propertyName, long oldValue,
                                   long newValue) {
    }

    protected void firePropertyChange(String propertyName, Object oldValue,
                                      Object newValue) {
        if ((propertyName.equals("text"))
                || (((propertyName.equals("font")) || (propertyName
                .equals("foreground"))) && (oldValue != newValue) && (getClientProperty("html") != null))) {
            super.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    public void firePropertyChange(String propertyName, short oldValue,
                                   short newValue) {
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        setComponentOrientation(list.getComponentOrientation());
        Color bg = null;
        Color fg = null;
        JList.DropLocation dropLocation = list.getDropLocation();

        if ((dropLocation != null) && (!dropLocation.isInsert())
                && (dropLocation.getIndex() == index)) {
            bg = DefaultLookup.getColor(this, this.ui,
                    "List.dropCellBackground");
            fg = DefaultLookup.getColor(this, this.ui,
                    "List.dropCellForeground");
            isSelected = true;
        }

        if (isSelected) {
            setBackground(bg == null ? list.getSelectionBackground() : bg);
            setForeground(fg == null ? list.getSelectionForeground() : fg);
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        setText(value == null ? "" : value.toString());
        setEnabled(list.isEnabled());
        setFont(list.getFont());
        setSelected(index < this.selectedList.size());

        Border border = null;

        if (cellHasFocus) {
            if (isSelected) {
                border = DefaultLookup.getBorder(this, this.ui,
                        "List.focusSelectedCellHighlightBorder");
            }

            if (border == null) {
                border = DefaultLookup.getBorder(this, this.ui,
                        "List.focusCellHighlightBorder");
            }
        } else {
            border = getNoFocusBorder();
        }

        setBorder(border);
        return this;
    }

    private Border getNoFocusBorder() {
        Border border = DefaultLookup.getBorder(this, this.ui,
                "List.cellNoFocusBorder");

        if (System.getSecurityManager() != null) {
            if (border != null) {
                return border;
            }

            return SAFE_NO_FOCUS_BORDER;
        }

        if ((border != null)
                && ((noFocusBorder == null) || (noFocusBorder == DEFAULT_NO_FOCUS_BORDER))) {
            return border;
        }

        return noFocusBorder;
    }

    public void invalidate() {
    }

    public boolean isOpaque() {
        Color back = getBackground();
        Component p = getParent();

        if (p != null) {
            p = p.getParent();
        }

        boolean colorMatch = (back != null) && (p != null)
                && (back.equals(p.getBackground())) && (p.isOpaque());
        return (!colorMatch) && (super.isOpaque());
    }

    public void repaint() {
    }

    public void repaint(long tm, int x, int y, int width, int height) {
    }

    public void repaint(Rectangle r) {
    }

    public void revalidate() {
    }

    public void validate() {
    }
}