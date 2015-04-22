package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import sun.awt.AppContext;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.io.Serializable;

public class CListCellRenderer extends JLabel implements ListCellRenderer,
        Serializable {
    private static class RendererUI extends BasicLabelUI {
        private static final Image BG_IMAGE = UIResourceManager
                .getImage("SelectedItemBackgroundImage");

        private static final Image BG_IMAGE_DISABLED = UIResourceManager
                .getImage("SelectedItemDisabledBackgroundImage");

        private static final Object RENDERER_UI_KEY = new Object();

        protected static RendererUI rendererUI = new RendererUI();

        public static ComponentUI createUI(JComponent c) {
            if (System.getSecurityManager() != null) {
                AppContext appContext = AppContext.getAppContext();
                RendererUI safeRendererUI = (RendererUI) appContext
                        .get(RENDERER_UI_KEY);

                if (safeRendererUI == null) {
                    safeRendererUI = new RendererUI();
                    appContext.put(RENDERER_UI_KEY, safeRendererUI);
                }

                return safeRendererUI;
            }

            return rendererUI;
        }

        protected void installDefaults(JLabel c) {
        }

        private void paintBackground(Graphics g, JComponent c) {
            if (((CListCellRenderer) c).selected) {
                Rectangle paintRect = new Rectangle(0, 0, c.getWidth(),
                        c.getHeight());

                JList list = ((CListCellRenderer) c).list;
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

    private static final long serialVersionUID = 3622492300888739118L;
    private JList list;

    private boolean selected;

    public CListCellRenderer() {
        setUI(new RendererUI());
        setBorder(UIResourceManager.getBorder("ListRendererBorder"));
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        if ((value instanceof Icon)) {
            setIcon((Icon) value);
        } else {
            setText(value == null ? "" : value.toString());
        }

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

        setFont(list.getFont());
        setForeground(isSelected ? list.getSelectionForeground() : fg);
        return this;
    }

    @Deprecated
    public void updateUI() {
    }
}