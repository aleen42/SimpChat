package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import sun.awt.AppContext;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CTableCellRenderer extends DefaultTableCellRenderer {
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
            CTableCellRenderer renderer = (CTableCellRenderer) c;

            if (renderer.selected) {
                Rectangle paintRect = new Rectangle(0, 0, c.getWidth(),
                        c.getHeight());
                int columnCount = renderer.table.getColumnCount();
                Image image;

                if ((renderer.table != null) && (!renderer.table.isEnabled())) {
                    image = BG_IMAGE_DISABLED;
                } else {
                    image = BG_IMAGE;
                }

                if (columnCount > 1) {
                    int width = image.getWidth(null);
                    int height = image.getHeight(null);

                    if (renderer.column == 0) {
                        image = UIUtil.cutImage(image, new Rectangle(0, 0,
                                width - 1, height), c);
                    } else if (renderer.column == columnCount - 1) {
                        image = UIUtil.cutImage(image, new Rectangle(1, 0,
                                width - 1, height), c);
                    } else {
                        image = UIUtil.cutImage(image, new Rectangle(1, 0,
                                width - 2, height), c);
                    }
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

    public static class UIResource extends CTableCellRenderer

    {
        private static final long serialVersionUID = 416233352847216000L;
    }

    private static final GridBorder GRID_BORDER = new GridBorder(Color.GRAY, 0,
            0, 0, 0);
    private static final long serialVersionUID = 6677889202548351952L;
    private Border cellBorder;
    private Border cellInsideBorder;
    private int column;

    private boolean selected;

    private JTable table;

    public CTableCellRenderer() {
        setUI(new RendererUI());
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                row, column);
        this.selected = isSelected;
        this.column = column;
        this.table = table;
        Color fg = table.getForeground();

        if (((table instanceof JCTable)) && (!table.isEnabled())) {
            fg = ((JCTable) table).getDisabledForeground();
        }

        if ((table instanceof JCTable)) {
            JCTable cTable = (JCTable) table;
            Color color1 = cTable.getRendererBackground1();
            Color color2 = cTable.getRendererBackground2();
            setOpaque((isSelected)
                    || ((cTable.isRendererOpaque()) && (color1 != null) && (color2 != null)));
            setBackground(row % 2 == 0 ? color1 : color2);
        } else {
            setOpaque(isSelected);
        }

        setFont(table.getFont());
        setForeground(isSelected ? table.getSelectionForeground() : fg);
        setText(value == null ? "" : value.toString());
        showGridLine();
        return this;
    }

    private void showGridLine() {
        if ((this.table instanceof JCTable)) {
            JCTable cTable = (JCTable) this.table;
            boolean showColumnLines = cTable.isShowColumnLines();
            boolean showRowLines = cTable.isShowRowLines();
            int right = (showColumnLines)
                    && (this.column < this.table.getColumnCount() - 1) ? 1 : 0;
            int bottom = showRowLines ? 1 : 0;
            GRID_BORDER.setColor(this.table.getGridColor());
            GRID_BORDER.setInsets(0, 0, bottom, right);

            if ((this.cellBorder == null)
                    || (this.cellInsideBorder != cTable.getCellInsideBorder())) {
                setBorder(this.cellBorder = new CompoundBorder(GRID_BORDER,
                        this.cellInsideBorder = cTable.getCellInsideBorder()));
            } else {
                setBorder(this.cellBorder);
            }
        }
    }

    @Deprecated
    public void updateUI() {
    }
}