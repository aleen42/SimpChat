package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import sun.swing.SwingUtilities2;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;

public class JCTableHeader extends JTableHeader {
    private class CTableCellHeaderRenderer extends
            DefaultTableCellHeaderRenderer {
        private static final long serialVersionUID = 6719110852077652189L;
        private final Image BORDER_IMAGE = UIResourceManager
                .getImage("TableHeaderSplitImage");

        private final Icon DOWN_ICON = UIResourceManager
                .getIcon("TableHeaderDownIcon");

        private final Border EMPTY_BORDER = new EmptyBorder(0, 0, 1, 0);

        private final Image PRESS_IMAGE = UIResourceManager
                .getImage("TableHeaderPressedImage");

        private final Image ROLLOVER_IMAGE = UIResourceManager
                .getImage("TableHeaderRolloverImage");

        private Icon sortIcon;

        private final Border SPLIT_BORDER = new CompoundBorder(
                this.EMPTY_BORDER, new ImageBorder(this.BORDER_IMAGE, 0, 0, 0,
                2));

        private int state = 0;
        private final Icon UP_ICON = UIResourceManager
                .getIcon("TableHeaderUpIcon");

        public CTableCellHeaderRenderer() {
            setUI(new JCTableHeader.RendererUI());
            setHorizontalAlignment(2);
        }

        private Image getImage() {
            Image image = null;

            if (JCTableHeader.this.isPressable()) {
                switch (this.state) {
                    case 2:
                        image = this.ROLLOVER_IMAGE;
                        break;
                    case 1:
                        image = this.PRESS_IMAGE;
                }

                image = (image == null) && (this.sortIcon != null) ? this.ROLLOVER_IMAGE
                        : image;

                if ((image != null) && (!JCTableHeader.this.isEnabled())) {
                    image = UIUtil.toBufferedImage(image, 0.5F, this);
                }
            }

            return image;
        }

        private Icon getSortIcon() {
            return this.sortIcon;
        }

        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus, int row,
                                                       int column) {
            JLabel renderer = (JLabel) super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            TableColumn draggedColumn = JCTableHeader.this.getDraggedColumn();
            SortOrder sortOrder = getColumnSortOrder(table, column);
            this.sortIcon = null;

            if ((draggedColumn != null)
            /**
             * && (column ==
             * SwingUtilities2.convertColumnIndexToView(JCTableHeader
             * .this.getColumnModel(),draggedColumn.getModelIndex()))
             */
                    ) {
                this.state = 1;
            } else if ((isSelected) || (hasFocus)
                    || (column == JCTableHeader.this.getRolloverColumn())) {
                this.state = 2;
            } else {
                this.state = 0;
            }

            if (sortOrder != null) {
                switch (sortOrder) {
                    case ASCENDING:
                        this.sortIcon = this.UP_ICON;
                        break;
                    case DESCENDING:
                        this.sortIcon = this.DOWN_ICON;
                }

            }

            renderer.setBackground(JCTableHeader.this.getBackground());
            renderer.setForeground(JCTableHeader.this.getForeground());
            renderer.setFont(JCTableHeader.this.getFont());
            renderer.setBorder(column == table.getColumnCount() - 1 ? this.EMPTY_BORDER
                    : this.SPLIT_BORDER);
            renderer.setIcon(null);
            renderer.setOpaque(JCTableHeader.this.isOpaque());
            return renderer;
        }

        private boolean isPress() {
            if (!JCTableHeader.this.isPressable()) {
                return false;
            }

            return this.state == 1;
        }

        @Deprecated
        public void updateUI() {
        }
    }

    private class HeaderListener extends MouseAdapter {
        private TableColumn cachedResizingColumn;

        private HeaderListener() {
        }

        private void cacheResizingColumn(MouseEvent e) {
            TableColumn column = JCTableHeader.this.getResizingColumn();

            if (column != null) {
                this.cachedResizingColumn = column;
            }
        }

        private boolean isColumnVisible(TableColumn column) {
            boolean visible = false;

            if (column != null) {
                TableColumnModel model = JCTableHeader.this.getColumnModel();

                for (int colIndex = 0; colIndex < model.getColumnCount(); colIndex++) {
                    if (model.getColumn(colIndex) != column)
                        continue;
                    visible = true;
                    break;
                }

            }

            return visible;
        }

        public void mouseClicked(MouseEvent e) {
            if ((!shouldIgnore(e)) && (e.getClickCount() == 2)
                    && (isColumnVisible(this.cachedResizingColumn))) {
                JTable table = JCTableHeader.this.getTable();

                if ((table instanceof JCTable)) {
                    ((JCTable) table).packColumn(this.cachedResizingColumn, -1);
                }
            }

            uncacheResizingColumn();
        }

        public void mouseExited(MouseEvent e) {
            uncacheResizingColumn();
        }

        public void mousePressed(MouseEvent e) {
            if (!shouldIgnore(e)) {
                cacheResizingColumn(e);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if ((JCTableHeader.this.showPopupMenu)
                    && (SwingUtilities.isRightMouseButton(e))
                    && (JCTableHeader.this.table.isEnabled())) {
                JTable table = JCTableHeader.this.getTable();

                if ((table instanceof JCTable)) {
                    JPopupMenu menu = ((JCTable) table).getColumnControlMenu();

                    if ((menu != null) && (menu.getComponentCount() > 0)) {
                        menu.show(JCTableHeader.this, e.getX(), e.getY());
                    }
                }
            }
        }

        private boolean shouldIgnore(MouseEvent e) {
            return (!SwingUtilities.isLeftMouseButton(e))
                    || (!JCTableHeader.this.table.isEnabled())
                    || (!JCTableHeader.this.packEnabled);
        }

        private void uncacheResizingColumn() {
            this.cachedResizingColumn = null;
        }
    }

    private class RendererUI extends BasicLabelUI {
        private final Insets IMAGE_INSETS = new Insets(1, 1, 1, 1);
        private int pressMoveX;
        private int pressMoveY;

        private RendererUI() {
        }

        protected void installDefaults(JLabel c) {
        }

        protected void paintDisabledText(JLabel label, Graphics g, String text,
                                         int textX, int textY) {
            paintEnabledText(label, g, text, textX, textY);
        }

        protected void paintEnabledText(JLabel label, Graphics g, String text,
                                        int textX, int textY) {
            int mnemIndex = label.getDisplayedMnemonicIndex();
            g.setColor(label.getForeground());
            SwingUtilities2.drawStringUnderlineCharAt(label, g, text,
                    mnemIndex, textX + 5 + this.pressMoveX, textY
                    + this.pressMoveY);
        }

        private void paintIcon(Graphics g, JComponent c) {
            if ((c instanceof JCTableHeader.CTableCellHeaderRenderer)) {
                JCTableHeader.CTableCellHeaderRenderer renderer = (JCTableHeader.CTableCellHeaderRenderer) c;
                Icon icon = renderer.getSortIcon();

                if (icon != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    Composite oldComposite = g2d.getComposite();
                    Insets insets = c.getInsets();
                    int x = c.getWidth() - insets.right - 5
                            - icon.getIconWidth();
                    int y = (c.getHeight() - insets.top - insets.bottom - icon
                            .getIconHeight())
                            / 2
                            + insets.top
                            + this.pressMoveY;

                    if (!JCTableHeader.this.isEnabled()) {
                        g2d.setComposite(AlphaComposite.SrcOver.derive(0.5F));
                    }

                    icon.paintIcon(c, g, x, y);
                    g2d.setComposite(oldComposite);
                }
            }
        }

        public void update(Graphics g, JComponent c) {
            if (c.isOpaque()) {
                g.setColor(c.getBackground());
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }

            if ((c instanceof JCTableHeader.CTableCellHeaderRenderer)) {
                JCTableHeader.CTableCellHeaderRenderer renderer = (JCTableHeader.CTableCellHeaderRenderer) c;
                this.pressMoveX = (this.pressMoveY = renderer.isPress() ? 1 : 0);
                Image image = renderer.getImage();
                Insets insets = c.getInsets();
                int width = c.getWidth();
                int height = c.getHeight();

                if (image != null) {
                    UIUtil.paintImage(g, image, this.IMAGE_INSETS,
                            new Rectangle(0, 0, width - insets.right, height
                                    - insets.bottom), c);
                }
            } else {
                this.pressMoveX = (this.pressMoveY = 0);
            }

            paint(g, c);
            paintIcon(g, c);
        }
    }

    private static final long serialVersionUID = -6295367316832337062L;
    private int height;

    private boolean packEnabled;

    private boolean pressable;

    private boolean showPopupMenu;

    public JCTableHeader() {
        this(null);
    }

    public JCTableHeader(TableColumnModel cm) {
        super(cm);
        setUI(new CTableHeaderUI());
        setFont(UIUtil.getDefaultFont());
        setBackground(UIResourceManager.getWhiteColor());
        setForeground(new Color(0, 28, 48));
        setOpaque(false);
        addMouseListener(new HeaderListener());
        this.pressable = true;
        this.showPopupMenu = true;
        this.packEnabled = true;
        this.height = 21;
    }

    protected TableCellRenderer createDefaultRenderer() {
        return new CTableCellHeaderRenderer();
    }

    public int getHeight() {
        return this.height;
    }

    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        size.height = this.height;
        return size;
    }

    private int getRolloverColumn() {
        TableHeaderUI ui = getUI();

        if (ui != null) {
            if ((ui instanceof CTableHeaderUI)) {
                return ((CTableHeaderUI) ui).getRolloverColumn();
            }

            try {
                Method getRolloverColumnMethod = BasicTableHeaderUI.class
                        .getDeclaredMethod("getRolloverColumn", new Class[0]);
                getRolloverColumnMethod.setAccessible(true);
                return ((Integer) getRolloverColumnMethod.invoke(ui,
                        new Object[0])).intValue();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return -1;
    }

    public boolean isPackEnabled() {
        return this.packEnabled;
    }

    public boolean isPressable() {
        return this.pressable;
    }

    public boolean isShowPopupMenu() {
        return this.showPopupMenu;
    }

    public void setHeight(int height) {
        this.height = height;
        revalidate();
    }

    public void setPackEnabled(boolean packEnabled) {
        this.packEnabled = packEnabled;
    }

    public void setPressable(boolean pressable) {
        this.pressable = pressable;
        repaint();
    }

    public void setShowPopupMenu(boolean showPopupMenu) {
        this.showPopupMenu = showPopupMenu;
    }

    @Deprecated
    public void updateUI() {
    }
}