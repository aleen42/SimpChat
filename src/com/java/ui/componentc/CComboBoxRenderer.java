package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import sun.awt.AppContext;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import java.awt.*;
import java.io.Serializable;

public class CComboBoxRenderer extends JLabel implements ListCellRenderer,
        Serializable {
    private static class RendererUI extends BasicLabelUI {
        private static final Image BG_IMAGE = UIResourceManager.getImage("SelectedItemBackgroundImage");

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
            Rectangle paintRect = new Rectangle(0, 0, c.getWidth(),
                    c.getHeight());
            UIUtil.paintImage(g, BG_IMAGE, new Insets(1, 1, 1, 1), paintRect, c);
        }

        protected void uninstallDefaults(JLabel c) {
        }

        public void update(Graphics g, JComponent c) {
            if (c.isOpaque()) {
                paintBackground(g, c);
            }

            paint(g, c);
        }
    }

    private static final Border NORMAL_BORDER = UIResourceManager
            .getBorder("ComboBoxRendererBorder");

    public static final Border SELECTED_BORDER = UIResourceManager
            .getBorder("ComboBoxRendererSelectedBorder");
    private static final long serialVersionUID = 3622492300888739118L;

    private JComboBox combo;

    public CComboBoxRenderer(JComboBox combo) {
        setUI(new RendererUI());
        this.combo = combo;
    }

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        if ((value instanceof Icon)) {
            setIcon((Icon) value);
        } else {
            setText(value == null ? "" : value.toString());
        }

        setBorder(NORMAL_BORDER);
        setOpaque(isSelected);
        setFont(this.combo.getFont());
        setForeground(isSelected ? UIResourceManager
                .getColor("TextSelectionForeground") : this.combo
                .getForeground());
        return this;
    }

    public Dimension getPreferredSize() {
        Dimension size;
        if ((getText() == null) || (getText().isEmpty())) {
            setText(" ");
            size = super.getPreferredSize();
            setText("");
        } else {
            size = super.getPreferredSize();
        }

        if (size.height < 20) {
            size.height = 20;
        }

        return size;
    }

    @Deprecated
    public void updateUI() {
    }
}