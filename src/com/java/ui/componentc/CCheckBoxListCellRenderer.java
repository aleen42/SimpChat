package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import sun.awt.AppContext;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.io.Serializable;
import java.util.List;

public class CCheckBoxListCellRenderer extends JCCheckBox implements
        ListCellRenderer, Serializable {
    private static class RendererUI extends CCheckBoxUI {
        private static final Image BG_IMAGE = UIResourceManager
                .getImage("SelectedItemBackgroundImage");

        private static final Image BG_IMAGE_DISABLED = UIResourceManager
                .getImage("SelectedItemDisabledBackgroundImage");

        private static final Object RENDERER_UI_KEY = new Object();

        public static ComponentUI createUI(JComponent b) {
            AppContext appContext = AppContext.getAppContext();
            CCheckBoxUI checkboxUI = (CCheckBoxUI) appContext
                    .get(RENDERER_UI_KEY);

            if (checkboxUI == null) {
                checkboxUI = new CCheckBoxUI();
                appContext.put(RENDERER_UI_KEY, checkboxUI);
            }

            return checkboxUI;
        }

        private void paintBackground(Graphics g, JComponent c) {
            if (((CCheckBoxListCellRenderer) c).selected) {
                Rectangle paintRect = new Rectangle(0, 0, c.getWidth(),
                        c.getHeight());

                JList list = ((CCheckBoxListCellRenderer) c).list;
                Image image;

                if ((list != null) && (!list.isEnabled())) {
                    image = BG_IMAGE_DISABLED;
                } else {
                    image = BG_IMAGE;
                }

                UIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), paintRect,
                        c);
            } else {
                g.setColor(c.getBackground());
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        }

        public void update(Graphics g, JComponent c) {
            if (c.isOpaque()) {
                paintBackground(g, c);
            }

            paint(g, c);
        }
    }

    public static class UIResource extends CCheckBoxListCellRenderer {
        private static final long serialVersionUID = 3395332241183382595L;

        public UIResource(List<Object> selectedList) {
            super(selectedList);
        }
    }

    private static final long serialVersionUID = -5977306258474788249L;
    private JList list;

    private boolean selected;

    private List<?> selectedList;

    public CCheckBoxListCellRenderer(List<?> selectedList) {
        setUI(new RendererUI());
        setBorder(new EmptyBorder(0, 1, 0, 0));
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
        this.list = list;
        this.selected = isSelected;
        Color fg = list.getForeground();

        if (((list instanceof JCList)) && (!list.isEnabled())) {
            fg = ((JCList) list).getDisabledForeground();
        }

        if ((list instanceof JCList)) {
            JCList cList = (JCList) list;
            Color color1 = cList.getRendererBackground1();
            Color color2 = cList.getRendererBackground2();
            setOpaque((isSelected)
                    || ((cList.isRendererOpaque()) && (color1 != null) && (color2 != null)));
            setBackground(index % 2 == 0 ? color1 : color2);
        } else {
            setOpaque(isSelected);
        }

        setText(value == null ? "" : value.toString());
        setFont(list.getFont());
        setForeground(isSelected ? list.getSelectionForeground() : fg);
        setSelected(index < this.selectedList.size());
        return this;
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