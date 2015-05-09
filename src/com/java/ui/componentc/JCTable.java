package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.table.*;
import java.awt.*;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Vector;

public class JCTable extends JTable {
    static class BooleanEditor extends DefaultCellEditor {
        private static final long serialVersionUID = 7383268047934155911L;

        public BooleanEditor(JCCheckBox checkBox) {
            super(checkBox);
            // JCCheckBox checkBox = (JCCheckBox) getComponent();
            checkBox.setHorizontalAlignment(0);
            checkBox.setBorderPainted(true);
            checkBox.setFocusPainted(false);
        }

        public Component getTableCellEditorComponent(JTable table,
                                                     Object value, boolean isSelected, int row, int column) {
            JCCheckBox checkBox = (JCCheckBox) super
                    .getTableCellEditorComponent(table, value, isSelected, row,
                            column);
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component c = renderer.getTableCellRendererComponent(table, value,
                    isSelected, true, row, column);
            checkBox.setBackground(JCTable.createEditorBackground(table, c));
            checkBox.setOpaque(checkBox.getBackground() != null);
            return checkBox;
        }
    }

    static class DateRenderer extends CTableCellRenderer.UIResource {
        private static final long serialVersionUID = -155254367088760418L;
        DateFormat formatter;

        public void setValue(Object value) {
            if (this.formatter == null) {
                this.formatter = DateFormat.getDateInstance();
            }

            setText(value == null ? "" : this.formatter.format(value));
        }
    }

    static class DoubleRenderer extends JCTable.NumberRenderer {
        private static final long serialVersionUID = -634858611608640293L;
        NumberFormat formatter;

        public void setValue(Object value) {
            if (this.formatter == null) {
                this.formatter = NumberFormat.getInstance();
            }

            setText(value == null ? "" : this.formatter.format(value));
        }
    }

    public static class GenericEditor extends DefaultCellEditor {
        private static final Border EDITOR_BORDER = UIResourceManager
                .getBorder("TableEditorBorder");
        private static final Border ERROR_BORDER = new CompoundBorder(
                new LineBorder(Color.RED, 2), new EmptyBorder(0, 1, 0, 1));

        private static final Color SELECTION_COLOR = UIResourceManager
                .getColor("TextSelectionColor");

        private static final long serialVersionUID = 2204159572123005700L;

        private Class<?>[] argTypes = {String.class};
        private Constructor<?> constructor;
        private Object value;

        public GenericEditor(JTextField field) {
            super(field);

            field.setUI(new BasicTextFieldUI());
            field.setMargin(new Insets(0, 0, 0, 0));
            field.setCaretColor(Color.BLACK);
            field.setSelectionColor(SELECTION_COLOR);
            field.setSelectedTextColor(UIResourceManager
                    .getColor("TextSelectionForeground"));
            getComponent().setName("Table.editor");
        }

        public Object getCellEditorValue() {
            return this.value;
        }

        public Component getTableCellEditorComponent(JTable table,
                                                     Object value, boolean isSelected, int row, int column) {
            this.value = null;
            JComponent editor = (JComponent) getComponent();
            TableCellRenderer renderer = table.getCellRenderer(row, column);
            Component c = renderer.getTableCellRendererComponent(table, value,
                    isSelected, true, row, column);

            editor.setBorder(EDITOR_BORDER);
            editor.setFont(table.getFont());
            editor.setBackground(JCTable.createEditorBackground(table, c));
            editor.setForeground(table.getForeground());
            editor.setOpaque(editor.getBackground() != null);

            if (((editor instanceof JTextField)) && ((c instanceof JLabel))) {
                ((JTextField) editor).setHorizontalAlignment(((JLabel) c)
                        .getHorizontalAlignment());
            }

            try {
                Class<?> type = table.getColumnClass(column);

                if (type == Object.class) {
                    type = String.class;
                }

                this.constructor = type.getConstructor(this.argTypes);
            } catch (Exception e) {
                return null;
            }

            return super.getTableCellEditorComponent(table, value, isSelected,
                    row, column);
        }

        public boolean stopCellEditing() {
            String str = (String) super.getCellEditorValue();

            if ("".equals(str)) {
                if (this.constructor.getDeclaringClass() == String.class) {
                    this.value = str;
                }

                super.stopCellEditing();
            }

            try {
                this.value = this.constructor.newInstance(new Object[]{str});
            } catch (Exception e) {
                ((JComponent) getComponent()).setBorder(ERROR_BORDER);
                return false;
            }

            return super.stopCellEditing();
        }
    }

    static class IconRenderer extends CTableCellRenderer.UIResource {
        private static final long serialVersionUID = 1317588288028835312L;

        public IconRenderer() {
            setHorizontalAlignment(0);
        }

        public void setValue(Object value) {
            setIcon((value instanceof Icon) ? (Icon) value : null);
        }
    }

    static class NumberRenderer extends CTableCellRenderer.UIResource {
        private static final long serialVersionUID = 4026292660464065849L;

        public NumberRenderer() {
            setHorizontalAlignment(4);
        }
    }

    private static final Color DISABLED_BG = UIResourceManager
            .getColor("TextDisabledBackground");
    private static final long serialVersionUID = -3423702493595092240L;

    public static Color createEditorBackground(JTable table, Component renderer) {
        Color color = renderer.getBackground();
        JCTable cTable;
        if (((table instanceof JCTable))
                && ((!(cTable = (JCTable) table).isRendererOpaque())
                || (cTable.getRendererBackground1() == null) || (cTable
                .getRendererBackground2() == null))) {
            color = null;
        }

        return color;
    }

    private float alpha;
    private Color background;
    private Border border;
    private Border cellInsideBorder;
    private JPopupMenu columnControlMenu;
    private Border disabledBorder;
    private Color disabledForeground;
    private Color disabledGridColor;
    private Color gridColor;
    private Image image;
    private boolean imageOnly;
    private boolean inLayout;
    private boolean intelliMode;

    private int oldAutoResizeMode;

    private Color rendererBackground1;

    private Color rendererBackground2;

    private boolean rendererOpaque;

    private boolean showColumnLines;

    private boolean showRowLines;

    private Insets visibleInsets;

    public JCTable() {
        this(null, null, null);
    }

    public JCTable(int numRows, int numColumns) {
        this(new DefaultTableModel(numRows, numColumns));
    }

    public JCTable(final Object[][] rowData, final Object[] columnNames) {
        this(new AbstractTableModel() {
            private static final long serialVersionUID = -4672208434155296416L;

            public int getColumnCount() {
                return columnNames.length;
            }

            public String getColumnName(int column) {
                return rowData[column].toString();
            }

            public int getRowCount() {
                return rowData.length;
            }

            public Object getValueAt(int row, int col) {
                return rowData[row][col];
            }

            public boolean isCellEditable(int row, int column) {
                return true;
            }

            public void setValueAt(Object value, int row, int col) {
                rowData[row][col] = value;
                fireTableCellUpdated(row, col);
            }
        });
    }

    public JCTable(TableModel dm) {
        this(dm, null, null);
    }

    public JCTable(TableModel dm, TableColumnModel cm) {
        this(dm, cm, null);
    }

    public JCTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
        super(dm, cm, sm);
        setUI(new CTableUI());
        setBorder(new CompoundBorder(new LineBorder(new Color(84, 165, 213)),
                new EmptyBorder(1, 1, 1, 1)));
        setDisabledBorder(new CompoundBorder(new LineBorder(new Color(84, 165,
                213, 128)), new EmptyBorder(1, 1, 1, 1)));
        setCellInsideBorder(new EmptyBorder(0, 3, 0, 3));
        setColumnModel(new CTableColumnModel(
                this.columnControlMenu = new JCPopupMenu()));
        setRowHeight(20);
        setRowMargin(0);
        this.columnModel.setColumnMargin(0);
        setFont(UIUtil.getDefaultFont());
        setBackground(UIResourceManager.getWhiteColor());
        setForeground(Color.BLACK);
        setSelectionForeground(UIResourceManager.getWhiteColor());
        setDisabledForeground(new Color(123, 123, 122));
        setGridColor(new Color(84, 165, 213));
        setDisabledGridColor(new Color(84, 165, 213, 128));
        setHorizontalScrollEnabled(true);
        super.setShowVerticalLines(false);
        super.setShowHorizontalLines(false);
        super.setOpaque(false);
        setFillsViewportHeight(true);
        this.alpha = 1.0F;
        this.visibleInsets = new Insets(1, 1, 1, 1);
        this.rendererOpaque = true;
        this.rendererBackground1 = new Color(251, 251, 255);
        this.rendererBackground2 = new Color(243, 248, 251);
    }

    public JCTable(Vector<?> rowData, Vector<?> columnNames) {
        this(new DefaultTableModel(rowData, columnNames));
    }

    public void columnMarginChanged(ChangeEvent e) {
        if (isEditing()) {
            removeEditor();
        }

        TableColumn resizingColumn = getResizingColumn();

        if ((resizingColumn != null) && (this.autoResizeMode == 0)
                && (!this.inLayout)) {
            resizingColumn.setPreferredWidth(resizingColumn.getWidth());
        }

        resizeAndRepaint();
    }

    protected void configureEnclosingScrollPane() {
        super.configureEnclosingScrollPane();
        Container p = getParent();

        if ((p instanceof JViewport)) {
            Container gp = p.getParent();

            if ((gp instanceof JScrollPane)) {
                JScrollPane scrollPane = (JScrollPane) gp;
                JViewport viewport = scrollPane.getColumnHeader();

                if ((viewport != null) && (viewport.isOpaque())) {
                    viewport.setOpaque(false);
                }
            }
        }
    }

    protected void createDefaultEditors() {
        this.defaultEditorsByColumnClass = new UIDefaults(3, 0.75F);
        // setDefaultEditor(Object.class, new GenericEditor());
        // setDefaultEditor(Number.class, new GenericEditor());
        // setDefaultEditor(Boolean.class, new BooleanEditor());
    }

    protected void createDefaultRenderers() {
        this.defaultRenderersByColumnClass = new UIDefaults(8, 0.75F);
        setDefaultRenderer(Object.class, new CTableCellRenderer.UIResource());
        setDefaultRenderer(Boolean.class,
                new CTableBooleanRenderer.UIResource());
        setDefaultRenderer(Number.class, new NumberRenderer());
        setDefaultRenderer(Float.class, new DoubleRenderer());
        setDefaultRenderer(Double.class, new DoubleRenderer());
        setDefaultRenderer(Date.class, new DateRenderer());
        setDefaultRenderer(Icon.class, new IconRenderer());
        setDefaultRenderer(ImageIcon.class, new IconRenderer());
    }

    protected JTableHeader createDefaultTableHeader() {
        return new JCTableHeader(this.columnModel);
    }

    public void doLayout() {
        int resizeMode = getAutoResizeMode();

        if (isHorizontalScrollEnabled()) {
            this.autoResizeMode = this.oldAutoResizeMode;
        }

        this.inLayout = true;
        super.doLayout();
        this.inLayout = false;
        this.autoResizeMode = resizeMode;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public Border getCellInsideBorder() {
        return this.cellInsideBorder;
    }

    public TableCellRenderer getCellRenderer(TableColumn column) {
        int viewColumnIndex = convertColumnIndexToView(column.getModelIndex());

        if (viewColumnIndex >= 0) {
            return getCellRenderer(0, viewColumnIndex);
        }

        TableCellRenderer renderer = column.getCellRenderer();

        if (renderer == null) {
            renderer = getDefaultRenderer(getModel().getColumnClass(
                    column.getModelIndex()));
        }

        return renderer;
    }

    public JPopupMenu getColumnControlMenu() {
        return this.columnControlMenu;
    }

    public Border getDisabledBorder() {
        return this.disabledBorder;
    }

    public Color getDisabledForeground() {
        return this.disabledForeground;
    }

    public Color getDisabledGridColor() {
        return this.disabledGridColor;
    }

    public TableCellRenderer getHeaderRenderer(TableColumn column) {
        TableCellRenderer renderer = column.getHeaderRenderer();

        if (renderer == null) {
            JTableHeader header = getTableHeader();

            if (header != null) {
                renderer = header.getDefaultRenderer();
            }
        }

        return renderer;
    }

    public Image getImage() {
        return this.image;
    }

    public Color getRendererBackground1() {
        return this.rendererBackground1;
    }

    public Color getRendererBackground2() {
        return this.rendererBackground2;
    }

    private TableColumn getResizingColumn() {
        return this.tableHeader == null ? null : this.tableHeader
                .getResizingColumn();
    }

    public boolean getScrollableTracksViewportWidth() {
        boolean shouldTrack = super.getScrollableTracksViewportWidth();

        if (isHorizontalScrollEnabled()) {
            return getPreferredSize().width < getParent().getWidth();
        }

        return shouldTrack;
    }

    public Insets getVisibleInsets() {
        return this.visibleInsets;
    }

    public boolean isHorizontalScrollEnabled() {
        return (this.intelliMode) && (getAutoResizeMode() == 0);
    }

    public boolean isImageOnly() {
        return this.imageOnly;
    }

    public boolean isRendererOpaque() {
        return this.rendererOpaque;
    }

    public boolean isShowColumnLines() {
        return this.showColumnLines;
    }

    public boolean isShowRowLines() {
        return this.showRowLines;
    }

    public void packAllColumns() {
        for (int col = 0; col < getColumnCount(); col++) {
            packColumn(getColumnModel().getColumn(col), -1);
        }
    }

    public void packColumn(Object identifier, int margin) {
        packColumn(getColumn(identifier), margin);
    }

    public void packColumn(TableColumn column, int margin) {
        TableColumnModel colModel = getColumnModel();

        if (((colModel instanceof CTableColumnModel))
                && (!((CTableColumnModel) colModel).isColumnVisible(column))) {
            throw new IllegalStateException("column must be visible to pack");
        }

        int columnIndex = convertColumnIndexToView(column.getModelIndex());
        int width = 0;
        TableCellRenderer headerRenderer = getHeaderRenderer(column);
        TableCellRenderer renderer = getCellRenderer(column);
        margin = margin < 0 ? 3 : margin;

        if (headerRenderer != null) {
            Component comp = headerRenderer.getTableCellRendererComponent(this,
                    column.getHeaderValue(), false, false, 0, columnIndex);
            width = comp.getPreferredSize().width;
        }

        for (int row = 0; row < getRowCount(); row++) {
            Component comp = renderer.getTableCellRendererComponent(this,
                    getValueAt(row, columnIndex), false, false, row,
                    columnIndex);
            width = Math.max(width, comp.getPreferredSize().width);
        }

        width += 2 * margin;
        column.setPreferredWidth(width);
    }

    public void setAlpha(float alpha) {
        if ((alpha >= 0.0F) && (alpha <= 1.0F)) {
            this.alpha = alpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid alpha:" + alpha);
        }
    }

    public void setAutoResizeMode(int mode) {
        if (mode != 0) {
            this.oldAutoResizeMode = mode;
        }

        this.intelliMode = false;
        super.setAutoResizeMode(mode);
    }

    public void setBackground(Color background) {
        this.background = background;
        super.setBackground(background);
    }

    public void setBorder(Border border) {
        this.border = border;
        super.setBorder(border);
    }

    public void setCellInsideBorder(Border cellInsideBorder) {
        this.cellInsideBorder = cellInsideBorder;
        repaint();
    }

    public void setDisabledBorder(Border disabledBorder) {
        this.disabledBorder = disabledBorder;

        if (!isEnabled()) {
            super.setBorder(disabledBorder);
        }
    }

    public void setDisabledForeground(Color disabledForeground) {
        this.disabledForeground = disabledForeground;

        if (!isEnabled()) {
            repaint();
        }
    }

    public void setDisabledGridColor(Color disabledGridColor) {
        this.disabledGridColor = disabledGridColor;

        if (!isEnabled()) {
            repaint();
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getTableHeader().setEnabled(enabled);
        super.setBorder(enabled ? this.border : this.disabledBorder);
        super.setBackground(enabled ? this.background : DISABLED_BG);
        super.setGridColor(enabled ? this.gridColor : this.disabledGridColor);
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = gridColor;
        super.setGridColor(gridColor);
    }

    public void setHorizontalScrollEnabled(boolean enabled) {
        if (enabled == isHorizontalScrollEnabled()) {
            return;
        }

        if (enabled) {
            if (getAutoResizeMode() != 0) {
                this.oldAutoResizeMode = getAutoResizeMode();
            }

            setAutoResizeMode(0);
            this.intelliMode = true;
        } else {
            setAutoResizeMode(this.oldAutoResizeMode);
        }
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setImageOnly(boolean imageOnly) {
        this.imageOnly = imageOnly;
        repaint();
    }

    @Deprecated
    public void setOpaque(boolean isOpaque) {
    }

    public void setRendererBackground1(Color rendererBackground1) {
        this.rendererBackground1 = rendererBackground1;
        repaint();
    }

    public void setRendererBackground2(Color rendererBackground2) {
        this.rendererBackground2 = rendererBackground2;
        repaint();
    }

    public void setRendererOpaque(boolean rendererOpaque) {
        this.rendererOpaque = rendererOpaque;
        repaint();
    }

    public void setShowColumnLines(boolean showColumnLines) {
        this.showColumnLines = showColumnLines;
        repaint();
    }

    public void setShowGrid(boolean showGrid) {
        setShowRowLines(showGrid);
        setShowColumnLines(showGrid);
        repaint();
    }

    @Deprecated
    public void setShowHorizontalLines(boolean showHorizontalLines) {
    }

    public void setShowRowLines(boolean showRowLines) {
        this.showRowLines = showRowLines;
        repaint();
    }

    @Deprecated
    public void setShowVerticalLines(boolean showVerticalLines) {
    }

    public void setVisibleInsets(int top, int left, int bottom, int right) {
        this.visibleInsets.set(top, left, bottom, right);
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}