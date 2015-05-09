package com.java.ui.componentc;

import com.java.ui.layout.LineLayout;
import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.LabelUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class JCCalendarPane extends JComponent {
    private class ButtonListener implements ActionListener {
        private ButtonListener() {
        }

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();

            if (source == JCCalendarPane.this.btnClose) {
                JCCalendarPane.this.close();
            } else if (source == JCCalendarPane.this.btnNextMonth) {
                JCCalendarPane.this.currentDate.add(2, 1);
                JCCalendarPane.this.refreshDays();
            } else if (source == JCCalendarPane.this.btnNextYear) {
                JCCalendarPane.this.currentDate.add(1, 1);
                JCCalendarPane.this.refreshDays();
            } else if (source == JCCalendarPane.this.btnPreviousMonth) {
                JCCalendarPane.this.currentDate.add(2, -1);
                JCCalendarPane.this.refreshDays();
            } else if (source == JCCalendarPane.this.btnPreviousYear) {
                JCCalendarPane.this.currentDate.add(1, -1);
                JCCalendarPane.this.refreshDays();
            } else if (source == JCCalendarPane.this.btnClear) {
                JCCalendarPane.this.clear();
            } else {
                int amount = Integer.parseInt(e.getActionCommand());
                Calendar date = Calendar.getInstance();

                if (amount != 0) {
                    date.add(6, Integer.parseInt(e.getActionCommand()));
                }

                JCCalendarPane.this.setDate(date);
                JCCalendarPane.this.close();
            }
        }
    }

    private static class CalendarLabelUI extends BasicLabelUI {
        public static ComponentUI createUI(JComponent c) {
            return new CalendarLabelUI();
        }

        protected void installDefaults(JLabel c) {
        }

        public void update(Graphics g, JComponent c) {
            if (c.isOpaque()) {
                Insets insets = c.getInsets();
                g.setColor(c.getBackground());
                g.fillRect(insets.left, insets.top, c.getWidth() - insets.left
                        - insets.right, c.getHeight() - insets.top
                        - insets.bottom);
            }

            paint(g, c);
        }
    }

    private class CalendarMouseListener extends MouseAdapter {
        private CalendarMouseListener() {
        }

        private Rectangle getRolloverRect(Rectangle rect, Insets insets) {
            JCCalendarPane.this.rolloverRect.setBounds(rect);
            JCCalendarPane.this.rolloverRect.x += insets.left;
            JCCalendarPane.this.rolloverRect.y += insets.top;
            JCCalendarPane.this.rolloverRect.width -= insets.left
                    + insets.right;
            JCCalendarPane.this.rolloverRect.height -= insets.top
                    + insets.bottom;
            return JCCalendarPane.this.rolloverRect;
        }

        public void mouseExited(MouseEvent e) {
            if (e.getSource() == JCCalendarPane.this.dayTable) {
                int oldRow = JCCalendarPane.this.curRow;
                int oldColumn = JCCalendarPane.this.curColumn;
                JCCalendarPane.this.clearMouseStatus();

                if ((oldRow >= 0)
                        && (oldColumn >= 0)
                        && (JCCalendarPane.this.dayDatas[oldRow][oldColumn] != null)) {
                    JCCalendarPane.this.dayTable
                            .repaint(JCCalendarPane.this.dayTable.getCellRect(
                                    oldRow, oldColumn, true));
                }
            }
        }

        public void mouseMoved(MouseEvent e) {
            Point point = e.getPoint();
            int oldRow = JCCalendarPane.this.curRow;
            int oldColumn = JCCalendarPane.this.curColumn;
            JCCalendarPane.this.curRow = JCCalendarPane.this.dayTable
                    .rowAtPoint(point);
            JCCalendarPane.this.curColumn = JCCalendarPane.this.dayTable
                    .columnAtPoint(point);
            Rectangle rect = JCCalendarPane.this.dayTable.getCellRect(
                    JCCalendarPane.this.curRow, JCCalendarPane.this.curColumn,
                    true);
            boolean existOld = (oldRow >= 0)
                    && (oldColumn >= 0)
                    && (JCCalendarPane.this.dayDatas[oldRow][oldColumn] != null);
            boolean repaintOld = false;
            boolean repaintCur = false;

            if ((JCCalendarPane.this.curRow < 0)
                    || (JCCalendarPane.this.curColumn < 0)) {
                repaintOld = existOld;
            } else {
                boolean isEmptyDay = JCCalendarPane.this.dayDatas[JCCalendarPane.this.curRow][JCCalendarPane.this.curColumn] == null;
                JLabel label = (JLabel) JCCalendarPane.this.dayTable
                        .getCellRenderer(JCCalendarPane.this.curRow,
                                JCCalendarPane.this.curColumn);
                Insets insets = label.getInsets();
                boolean oldMouseIn = JCCalendarPane.this.mouseIn;
                JCCalendarPane.this.mouseIn = ((getRolloverRect(rect, insets)
                        .contains(point)) && (!isEmptyDay));

                if ((JCCalendarPane.this.curRow != oldRow)
                        || (JCCalendarPane.this.curColumn != oldColumn)) {
                    if (existOld) {
                        repaintOld = true;
                    }

                    if (JCCalendarPane.this.mouseIn) {
                        repaintCur = true;
                    }
                } else if ((oldMouseIn != JCCalendarPane.this.mouseIn)
                        && (!isEmptyDay)) {
                    repaintCur = true;
                }
            }

            if (repaintOld) {
                JCCalendarPane.this.dayTable
                        .repaint(JCCalendarPane.this.dayTable.getCellRect(
                                oldRow, oldColumn, true));
            }

            if ((repaintCur) && (!rect.isEmpty())) {
                JCCalendarPane.this.dayTable.repaint(rect);
            }
        }

        public void mousePressed(MouseEvent e) {
            if (JCCalendarPane.this.showShortcutMenu) {
                JCCalendarPane.this.hidePopupMenu();
            }
        }

        public void mouseReleased(MouseEvent e) {
            Object source = e.getSource();

            if (source == JCCalendarPane.this.lbYearAndMonth) {
                JCCalendarPane.this.showPopupMenu(e);
            } else if ((source == JCCalendarPane.this.dayTable)
                    && (SwingUtilities.isLeftMouseButton(e))
                    && (JCCalendarPane.this.mouseIn)) {
                Integer oldSelectedDay = JCCalendarPane.this.selectedDay;
                JCCalendarPane.this.selectedDay = JCCalendarPane.this.dayDatas[JCCalendarPane.this.curRow][JCCalendarPane.this.curColumn];

                if (!JCCalendarPane.this.isSelectedDay(oldSelectedDay)) {
                    JCCalendarPane.this.date = ((Calendar) JCCalendarPane.this.currentDate
                            .clone());
                    JCCalendarPane.this.date.set(5,
                            JCCalendarPane.this.selectedDay.intValue());
                    JCCalendarPane.this.dayTable
                            .repaint(JCCalendarPane.this.dayTable.getCellRect(
                                    JCCalendarPane.this.curRow,
                                    JCCalendarPane.this.curColumn, true));
                }

                if ((oldSelectedDay != null)
                        && (JCCalendarPane.this.isCurrentMonth())) {
                    int count = JCCalendarPane.this.firstWeekDayOfMonth
                            + oldSelectedDay.intValue() - 2;
                    JCCalendarPane.this.dayTable
                            .repaint(JCCalendarPane.this.dayTable.getCellRect(
                                    count / 7, count % 7, true));
                }

                JCCalendarPane.this.close();
            }
        }
    }

    private class CalendarPopup extends JPopupMenu {
        private class ListListener extends MouseAdapter {
            private ListListener() {
            }

            public void mouseMoved(MouseEvent e) {
                JCCalendarPane.CalendarPopup.this.list
                        .setSelectedIndex(JCCalendarPane.CalendarPopup.this.list
                                .locationToIndex(e.getPoint()));
            }

            public void mouseReleased(MouseEvent e) {
                JCCalendarPane.CalendarPopup.this.dateChanged();
            }
        }

        private class ListRenderer extends CListCellRenderer {
            private static final long serialVersionUID = -408969737197174965L;

            public ListRenderer() {
                setBorder(new EmptyBorder(0, 1, 0, 3));
            }

            public Component getListCellRendererComponent(JList list,
                                                          Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index,
                        isSelected, cellHasFocus);

                if ((value != null) && ((value instanceof Integer))) {
                    int intValue = ((Integer) value).intValue();

                    if (intValue < 10) {
                        setText("0" + value);
                    }
                }

                return this;
            }
        }

        private static final long serialVersionUID = -9198339742476660346L;
        private MenuElement[] elements;
        private Method getPopupMethod;
        public JCList list;

        public DefaultListModel listModel;

        private Field popupField;

        public CalendarPopup(String name, int minValue, int maxValue) {
            setUI(new BasicPopupMenuUI() {
                public void installDefaults() {
                }

                protected void uninstallDefaults() {
                }
            });
            JCScrollList scList = new JCScrollList(
                    this.listModel = new DefaultListModel());
            ListListener listener = new ListListener();
            this.list = scList.getList();
            this.list.setRendererOpaque(false);
            this.list.setFocusable(false);
            this.list.setSelectionMode(0);
            this.list.setCellRenderer(new ListRenderer());
            this.list.addMouseListener(listener);
            this.list.addMouseMotionListener(listener);
            scList.setHorizontalScrollBar(null);
            setName(name);
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setOpaque(false);
            setLayout(new BorderLayout());
            add(scList, "Center");
            resetValues(minValue, maxValue);
            try {
                this.popupField = JPopupMenu.class.getDeclaredField("popup");
                this.getPopupMethod = JPopupMenu.class.getDeclaredMethod(
                        "getPopup", new Class[0]);
                this.popupField.setAccessible(true);
                this.getPopupMethod.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void dateChanged() {
            String name = getName();
            Integer selectedValue = (Integer) this.list.getSelectedValue();

            if ((selectedValue != null) && ("Year".equals(name))) {
                JCCalendarPane.this.currentDate
                        .set(1, selectedValue.intValue());
                JCCalendarPane.this.refreshDays();
            } else if ((selectedValue != null) && ("Month".equals(name))) {
                JCCalendarPane.this.currentDate.set(2,
                        selectedValue.intValue() - 1);
                JCCalendarPane.this.refreshDays();
            }

            setVisible(false);
        }

        public MenuElement[] getParentElements() {
            if (this.elements == null) {
                initParentElements();
            }

            return this.elements;
        }

        public Dimension getPreferredSize() {
            Dimension size = super.getPreferredSize();
            int maxHeight = JCCalendarPane.this.weekPane.getHeight()
                    + JCCalendarPane.this.dayPane.getHeight();

            if (size.height > maxHeight) {
                size.height = maxHeight;
            }

            return size;
        }

        private Popup getThePopup() {
            try {
                return (Popup) this.popupField.get(this);
            } catch (Exception e) {
            }
            return null;
        }

        public void hide() {
            MenuSelectionManager manager = MenuSelectionManager
                    .defaultManager();
            MenuElement[] selection = manager.getSelectedPath();
            List<MenuElement> meList = new ArrayList<MenuElement>();

            for (MenuElement e : selection) {
                if (e == this)
                    continue;
                meList.add(e);
            }

            selection = new MenuElement[meList.size()];
            meList.toArray(selection);
            manager.setSelectedPath(selection);
        }

        private void initParentElements() {
            Container parent = JCCalendarPane.this.lbYearAndMonth.getParent();
            List<MenuElement> meList = new ArrayList<MenuElement>();

            while (parent != null) {
                if ((parent instanceof MenuElement)) {
                    meList.add((MenuElement) parent);
                }

                parent = parent.getParent();
            }

            meList.toArray(this.elements = new MenuElement[meList.size()]);
        }

        private Popup invokeGetPopup() {
            try {
                return (Popup) this.getPopupMethod.invoke(this, new Object[0]);
            } catch (Exception e) {
            }
            return null;
        }

        private boolean isPopupMenu() {
            Component invoker = getInvoker();
            return (invoker != null) && (!(invoker instanceof JMenu));
        }

        public void resetValues(int minValue, int maxValue) {
            this.listModel.removeAllElements();

            for (int value = minValue; value <= maxValue; value++) {
                this.listModel.addElement(Integer.valueOf(value));
            }
        }

        private void setThePopup(Popup popup) {
            try {
                this.popupField.set(this, popup);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setVisible(boolean visible) {
            if (visible == isVisible()) {
                return;
            }

            if (!visible) {
                Boolean doCanceled = (Boolean) getClientProperty("JPopupMenu.firePopupMenuCanceled");

                if ((doCanceled != null) && (doCanceled == Boolean.TRUE)) {
                    putClientProperty("JPopupMenu.firePopupMenuCanceled",
                            Boolean.FALSE);
                    firePopupMenuCanceled();
                }

                getSelectionModel().clearSelection();
            } else if (isPopupMenu()) {
                MenuElement[] me;
                if (getParentElements().length > 0) {
                    me = new MenuElement[this.elements.length + 1];
                    me[this.elements.length] = this;
                    System.arraycopy(this.elements, 0, me, 0,
                            this.elements.length);
                } else {
                    me = new MenuElement[]{this};
                }

                MenuSelectionManager.defaultManager().setSelectedPath(me);
            }

            Popup popup = getThePopup();

            if (visible) {
                firePopupMenuWillBecomeVisible();
                setThePopup(invokeGetPopup());
                firePropertyChange("visible", Boolean.FALSE, Boolean.TRUE);
            } else if (popup != null) {
                firePopupMenuWillBecomeInvisible();
                popup.hide();
                setThePopup(null);
                firePropertyChange("visible", Boolean.TRUE, Boolean.FALSE);
            }
        }

        public void show(Component invoker, int x, int y) {
            String name = getName();
            this.list.clearSelection();

            if ("Year".equals(name)) {
                this.list
                        .setSelectedValue(
                                Integer.valueOf(JCCalendarPane.this.currentDate
                                        .get(1)), true);
            } else if ("Month".equals(name)) {
                this.list.setSelectedValue(Integer
                        .valueOf(JCCalendarPane.this.currentDate.get(2) + 1),
                        true);
            }

            super.show(invoker, x, y);
        }

        @Deprecated
        public void updateUI() {
            setUI(getUI());
        }
    }

    private class CalendarTableCellRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1874605203390504635L;
        private boolean isDayTable;

        public CalendarTableCellRenderer(boolean isDayTable) {
            this.isDayTable = isDayTable;
            setUI(isDayTable ? new JCCalendarPane.CalendarLabelUI()
                    : new JCCalendarPane.DefaultLabelUI());
            setOpaque(false);
            setBorder(isDayTable ? JCCalendarPane.DAY_BORDER : null);
            setHorizontalAlignment(0);
            setVerticalAlignment(0);
            setBackground(UIResourceManager.getColor("CalendarDayBackground"));
        }

        public Component getTableCellRendererComponent(JTable table,
                                                       Object value, boolean isSelected, boolean hasFocus, int row,
                                                       int column) {
            setText(value == null ? null : value.toString());
            setFont(table.getFont());
            setForeground((column == 0) || (column == 6) ? UIResourceManager
                    .getColor("CalendarWeekendForeground") : UIResourceManager
                    .getColor("CalendarWeekForeground"));

            if (this.isDayTable) {
                if (JCCalendarPane.this.isSelectedDay((Integer) value)) {
                    setOpaque(true);
                    setBorder(JCCalendarPane.DAY_SELECTED_BORDER);
                } else {
                    setOpaque((value != null) && (JCCalendarPane.this.mouseIn)
                            && (JCCalendarPane.this.curRow == row)
                            && (JCCalendarPane.this.curColumn == column));
                    setBorder(JCCalendarPane.DAY_BORDER);
                }
            }

            return this;
        }

        @Deprecated
        public void updateUI() {
        }
    }

    private class CalendarTableModel extends AbstractTableModel {
        private static final long serialVersionUID = -3633867494696004985L;
        private String[] columnNames;
        private Object[][] rowData;

        public CalendarTableModel(Object[][] rowData) {
            this.rowData = rowData;
            this.columnNames = new String[rowData[0].length];
            Arrays.fill(this.columnNames, "");
        }

        public int getColumnCount() {
            return this.columnNames.length;
        }

        public String getColumnName(int column) {
            return this.columnNames[column].toString();
        }

        public int getRowCount() {
            return this.rowData.length;
        }

        public Object getValueAt(int row, int col) {
            return this.rowData[row][col];
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public void setValueAt(Object value, int row, int col) {
            this.rowData[row][col] = (value == null ? null : value.toString());
            fireTableCellUpdated(row, col);
        }
    }

    private static class DefaultLabelUI extends BasicLabelUI {
        public static ComponentUI createUI(JComponent c) {
            return new DefaultLabelUI();
        }

        protected void installDefaults(JLabel c) {
        }
    }

    private static class DefaultTableUI extends BasicTableUI {
        public static ComponentUI createUI(JComponent c) {
            return new DefaultTableUI();
        }

        protected void installDefaults() {
        }
    }

    private static final Border DAY_BORDER = UIResourceManager
            .getBorder("CalendarDayBorder");
    private static final Border DAY_SELECTED_BORDER = UIResourceManager
            .getBorder("CalendarTodaySelectedBorder");
    private static final Font DEFAULT_FONT = UIUtil.getDefaultFont();
    private static final Color DEFAULT_FOREGROUND = new Color(0, 28, 48);
    private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final Insets IMAGE_INSETS = new Insets(52, 3, 26, 3);
    private static final long serialVersionUID = -6243749367273378444L;
    private static final DateFormat YEAR_MONTH_FORMAT = new SimpleDateFormat(
            "yyyy-MM");
    private JPanel bottomPane;
    private JCButton btnAfterTomorrow;
    private JCButton btnBeforeYesterday;
    private JCButton btnClear;
    private JCButton btnClose;
    private JCButton btnNextMonth;
    private JCButton btnNextYear;
    private JCButton btnPreviousMonth;
    private JCButton btnPreviousYear;
    private JCButton btnToday;
    private JCButton btnTomorrow;
    private JCButton btnYesterday;
    private ButtonListener buttonListener;
    private Action closeAction;
    private int curColumn;
    private Calendar currentDate;
    private int curRow;
    private Calendar date;
    private Integer[][] dayDatas;
    private JPanel dayPane;
    private JTable dayTable;
    private int firstWeekDayOfMonth;
    private JLabel lbYearAndMonth;
    private CalendarPopup monthPopup;

    private boolean mouseIn;

    private CalendarMouseListener mouseListener;

    private Rectangle rolloverRect;

    private Integer selectedDay;

    private boolean showShortcutMenu;

    private JPanel topPane;

    private JPanel weekPane;

    private CalendarPopup yearPopup;

    public JCCalendarPane() {
        super();
    }

    public JCCalendarPane(Calendar date) {
        this.buttonListener = new ButtonListener();
        this.mouseListener = new CalendarMouseListener();
        this.curRow = (this.curColumn = -1);
        this.rolloverRect = new Rectangle();
        this.yearPopup = new CalendarPopup("Year", 1970, 2050);
        this.monthPopup = new CalendarPopup("Month", 1, 12);
        this.showShortcutMenu = true;
        setDate(date, false);
        setBorder(new EmptyBorder(3, 3, 3, 3));
        setLayout(new LineLayout(0, 10, 10, 1));
        setOpaque(false);
        setDoubleBuffered(true);
        setPreferredSize(new Dimension(180, 179));
        initUI();
        setFont(new Font("Arial", 0, 12));
        addMouseListener(this.mouseListener);
    }

    public JCCalendarPane(String dateString) {
        super();
        setDateString(dateString);
    }

    public void clear() {
        setDate(null);
        close();
    }

    public void clearMouseStatus() {
        this.mouseIn = false;
        this.curRow = -1;
        this.curColumn = -1;
    }

    public void close() {
        if (this.closeAction != null) {
            this.closeAction.actionPerformed(new ActionEvent(this, 0, ""));
        }
    }

    private Image createBottomButtonImage(boolean fill) {
        BufferedImage bufferedImage = UIUtil.getGraphicsConfiguration(this)
                .createCompatibleImage(10, 10, 3);

        if (fill) {
            Graphics g = bufferedImage.getGraphics();
            g.setColor(UIResourceManager.getColor("CalendarDayBackground"));
            g.fillRect(0, 0, bufferedImage.getWidth(),
                    bufferedImage.getHeight());
            g.dispose();
        }

        return bufferedImage;
    }

    private Integer[][] createDayDatas() {
        if (this.dayDatas == null) {
            this.dayDatas = new Integer[6][7];
        }

        int oldDay = this.currentDate.get(5);
        this.currentDate.set(5, 1);
        this.firstWeekDayOfMonth = this.currentDate.get(7);
        this.currentDate.set(5, oldDay);
        int maxDay = this.currentDate.getActualMaximum(5);
        int day = 0;

        for (int i = 0; i < 42; i++) {
            int row = i / 7;
            int col = i % 7;

            if ((i >= this.firstWeekDayOfMonth - 1) && (day < maxDay)) {
                day++;
                this.dayDatas[row][col] = Integer.valueOf(day);
            } else {
                this.dayDatas[row][col] = null;
            }
        }

        return this.dayDatas;
    }

    private JTable createTable(TableModel tableModel, int rowHeight,
                               boolean isDayTable) {
        final JTable table = new JTable(tableModel) {
            private static final long serialVersionUID = -771844785042284235L;

            @Deprecated
            public void updateUI() {
            }
        };
        table.setUI(new DefaultTableUI());
        table.setDefaultRenderer(Object.class, new CalendarTableCellRenderer(
                isDayTable));
        table.getTableHeader().setVisible(false);
        table.setFont(DEFAULT_FONT);
        table.setShowGrid(false);
        table.setFocusable(false);
        table.setRowSelectionAllowed(false);
        table.setOpaque(false);
        table.setBorder(new EmptyBorder(0, 0, 0, 0));
        table.setRowHeight(rowHeight);
        table.setRowMargin(0);
        table.getColumnModel().setColumnMargin(0);
        table.addMouseListener(this.mouseListener);

        if (isDayTable) {
            table.addMouseMotionListener(this.mouseListener);
            table.addComponentListener(new ComponentAdapter() {
                public void componentResized(ComponentEvent e) {
                    table.setRowHeight(table.getHeight() / table.getRowCount());
                }
            });
        }
        return table;
    }

    public Action getCloseAction() {
        return this.closeAction;
    }

    public Calendar getDate() {
        return this.date;
    }

    public String getDateString() {
        return this.date == null ? null : FORMAT.format(this.date.getTime());
    }

    private Rectangle getYearAndMonthTextRect(FontMetrics metrics) {
        Insets insets = this.lbYearAndMonth.getInsets(null);
        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();
        Rectangle viewRect = new Rectangle(insets.left, insets.top,
                this.lbYearAndMonth.getWidth() - (insets.left + insets.right),
                this.lbYearAndMonth.getHeight() - (insets.top + insets.bottom));
        SwingUtilities.layoutCompoundLabel(this.lbYearAndMonth, metrics,
                this.lbYearAndMonth.getText(), null,
                this.lbYearAndMonth.getVerticalAlignment(),
                this.lbYearAndMonth.getHorizontalAlignment(),
                this.lbYearAndMonth.getVerticalTextPosition(),
                this.lbYearAndMonth.getHorizontalTextPosition(), viewRect,
                iconRect, textRect, this.lbYearAndMonth.getIconTextGap());
        return textRect;
    }

    public void hidePopupMenu() {
        if (this.yearPopup.isVisible()) {
            this.yearPopup.setVisible(false);
        }

        if (this.monthPopup.isVisible()) {
            this.monthPopup.setVisible(false);
        }
    }

    private void initBottomPane() {
        Image image = createBottomButtonImage(false);
        Image rolloverImage = createBottomButtonImage(true);
        this.btnBeforeYesterday = new JCButton("鍓嶅ぉ");
        this.btnYesterday = new JCButton("鏄ㄥぉ");
        this.btnToday = new JCButton("浠婂ぉ");
        this.btnTomorrow = new JCButton("鏄庡ぉ");
        this.btnAfterTomorrow = new JCButton("鍚庡ぉ");
        this.btnClear = new JCButton("娓呴櫎");
        JCButton[] buttons = {this.btnBeforeYesterday, this.btnYesterday,
                this.btnToday, this.btnTomorrow, this.btnAfterTomorrow,
                this.btnClear};
        String[] cmds = {"-2", "-1", "0", "1", "2"};
        int index = 0;

        for (JCButton button : buttons) {
            button.setForeground(DEFAULT_FOREGROUND);
            button.setFocusable(false);
            button.setImage(image);
            button.setRolloverImage(rolloverImage);
            button.setPressedImage(rolloverImage);
            button.setActionCommand(cmds[(index++)]);
            button.addActionListener(this.buttonListener);
            button.addMouseListener(this.mouseListener);
            this.bottomPane.add(button);
        }
    }

    private void initPanes() {
        this.topPane = new JPanel(new LineLayout(3, 0, 0, 0, 0, 0, 0, 0));
        this.bottomPane = new JPanel(new GridLayout(1, 6));
        this.weekPane = new JPanel(new BorderLayout());
        this.dayPane = new JPanel(new BorderLayout());
        this.topPane.setOpaque(false);
        this.topPane.setBorder(new EmptyBorder(0, 2, 3, 2));
        this.topPane.setPreferredSize(new Dimension(-1, 23));
        this.weekPane.setOpaque(false);
        this.weekPane.setBorder(null);
        this.weekPane.setPreferredSize(new Dimension(-1, 20));
        this.dayPane.setOpaque(false);
        this.dayPane.setBorder(new EmptyBorder(1, 0, 1, 0));
        this.bottomPane.setOpaque(false);
        this.bottomPane.setBorder(null);
        this.bottomPane.setPreferredSize(new Dimension(-1, 21));
    }

    private void initTopPane() {
        Dimension buttonSize = new Dimension(16, 16);
        this.lbYearAndMonth = new JLabel(
                YEAR_MONTH_FORMAT.format(this.currentDate.getTime())) {
            private static final long serialVersionUID = -3899939777706194998L;

            @Deprecated
            public void updateUI() {
            }
        };
        this.btnClose = new JCButton();
        this.btnNextMonth = new JCButton();
        this.btnNextYear = new JCButton();
        this.btnPreviousMonth = new JCButton();
        this.btnPreviousYear = new JCButton();
        JCButton[] buttons = {this.btnPreviousYear, this.btnPreviousMonth,
                this.btnNextMonth, this.btnNextYear, this.btnClose};
        String[] layoutArgs = {"Start", "Start", "End", "End", "End"};
        int index = 0;

        this.lbYearAndMonth.setUI(new CalendarLabelUI());
        this.lbYearAndMonth.setOpaque(false);
        this.lbYearAndMonth.setBorder(null);
        this.lbYearAndMonth.setHorizontalAlignment(0);
        this.lbYearAndMonth.setVerticalAlignment(0);
        this.lbYearAndMonth.setForeground(DEFAULT_FOREGROUND);
        this.lbYearAndMonth.addMouseListener(this.mouseListener);
        this.btnClose.setVisible(false);
        this.btnClose
                .setImage(UIResourceManager.getImage("CalendarCloseImage"));
        this.btnClose.setRolloverImage(UIResourceManager
                .getImage("CalendarCloseRolloverImage"));
        this.btnClose.setPressedImage(UIResourceManager
                .getImage("CalendarClosePressedImage"));
        this.btnNextMonth.setImage(UIResourceManager
                .getImage("CalendarNextMonthImage"));
        this.btnNextMonth.setRolloverImage(UIResourceManager
                .getImage("CalendarNextMonthRolloverImage"));
        this.btnNextMonth.setPressedImage(UIResourceManager
                .getImage("CalendarNextMonthPressedImage"));
        this.btnNextYear.setImage(UIResourceManager
                .getImage("CalendarNextYearImage"));
        this.btnNextYear.setRolloverImage(UIResourceManager
                .getImage("CalendarNextYearRolloverImage"));
        this.btnNextYear.setPressedImage(UIResourceManager
                .getImage("CalendarNextYearPressedImage"));
        this.btnPreviousMonth.setImage(UIResourceManager
                .getImage("CalendarPreviousMonthImage"));
        this.btnPreviousMonth.setRolloverImage(UIResourceManager
                .getImage("CalendarPreviousMonthRolloverImage"));
        this.btnPreviousMonth.setPressedImage(UIResourceManager
                .getImage("CalendarPreviousMonthPressedImage"));
        this.btnPreviousYear.setImage(UIResourceManager
                .getImage("CalendarPreviousYearImage"));
        this.btnPreviousYear.setRolloverImage(UIResourceManager
                .getImage("CalendarPreviousYearRolloverImage"));
        this.btnPreviousYear.setPressedImage(UIResourceManager
                .getImage("CalendarPreviousYearPressedImage"));

        for (JCButton button : buttons) {
            button.addActionListener(this.buttonListener);
            button.addMouseListener(this.mouseListener);
            button.setFocusable(false);
            button.setPreferredSize(buttonSize);
            this.topPane.add(button, layoutArgs[(index++)]);
        }

        this.topPane.add(this.lbYearAndMonth, "MiddleFill");
    }

    private void initUI() {
        String[][] weekDatas = {{"鏃�", "涓�", "浜�", "涓�", "鍥�", "浜�", "鍏�"}};
        initPanes();
        initTopPane();
        initBottomPane();
        this.weekPane.add(
                createTable(new CalendarTableModel(weekDatas),
                        this.weekPane.getPreferredSize().height, false),
                "Center");
        this.dayPane.add(
                this.dayTable = createTable(new CalendarTableModel(
                        createDayDatas()), 18, true), "Center");
        add(this.topPane, "StartFill");
        add(this.weekPane, "StartFill");
        add(this.dayPane, "MiddleFill");
        add(this.bottomPane, "EedFill");
    }

    public boolean isClosable() {
        return this.btnClose.isVisible();
    }

    private boolean isCurrentMonth() {
        return (this.date != null)
                && (this.date.get(1) == this.currentDate.get(1))
                && (this.date.get(2) == this.currentDate.get(2));
    }

    private boolean isSelectedDay(Integer day) {
        return (isCurrentMonth()) && (day != null)
                && (this.selectedDay != null)
                && (this.selectedDay.intValue() == day.intValue());
    }

    public boolean isShowShortcutMenu() {
        return this.showShortcutMenu;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        UIUtil.paintImage(g,
                UIResourceManager.getImage("CalendarBackgroundImage"),
                IMAGE_INSETS, new Rectangle(0, 0, getWidth(), getHeight()),
                this);
    }

    private void refreshDays() {
        this.lbYearAndMonth.setText(YEAR_MONTH_FORMAT.format(this.currentDate
                .getTime()));
        createDayDatas();
        ((AbstractTableModel) this.dayTable.getModel()).fireTableDataChanged();
    }

    public void setClosable(boolean closable) {
        this.btnClose.setVisible(closable);
    }

    public void setCloseAction(Action closeAction) {
        this.closeAction = closeAction;
    }

    public void setDate(Calendar date) {
        setDate(date, true);
    }

    private void setDate(Calendar date, boolean refresh) {
        this.date = date;
        this.currentDate = (date == null ? Calendar.getInstance()
                : (Calendar) date.clone());
        this.selectedDay = (date == null ? null : Integer.valueOf(date.get(5)));

        if (refresh) {
            refreshDays();
        }
    }

    public void setDateString(String dateString) {
        try {
            if ((dateString == null) || (dateString.isEmpty())) {
                setDate(null);
            } else {
                Date newDate = FORMAT.parse(dateString);

                if (this.date == null) {
                    this.date = Calendar.getInstance();
                }

                this.date.setTime(newDate);
                setDate(this.date);
            }
        } catch (ParseException e) {
            setDate(null);
        }
    }

    public void setFont(Font font) {
        super.setFont(font);

        if (this.lbYearAndMonth != null) {
            this.lbYearAndMonth.setFont(font);
        }

        if (this.dayTable != null) {
            this.dayTable.setFont(font);
        }

        if (this.yearPopup != null) {
            this.yearPopup.list.setFont(font);
        }

        if (this.monthPopup != null) {
            this.monthPopup.list.setFont(font);
        }
    }

    public void setShowShortcutMenu(boolean showShortcutMenu) {
        this.showShortcutMenu = showShortcutMenu;
    }

    public void setYearInterval(int min, int max) {
        this.yearPopup.resetValues(min, max);
    }

    private void showPopupMenu(MouseEvent e) {
        LabelUI ui = this.lbYearAndMonth.getUI();

        if ((this.showShortcutMenu) && ((ui instanceof CalendarLabelUI))
                && (SwingUtilities.isLeftMouseButton(e))) {
            String text = this.lbYearAndMonth.getText();

            if ((text != null) && (!text.isEmpty())) {
                FontMetrics metrics = this.lbYearAndMonth
                        .getFontMetrics(this.lbYearAndMonth.getFont());
                Rectangle textRect = getYearAndMonthTextRect(metrics);
                Point p = e.getPoint();
                String year1 = text.substring(0, 4);
                String year2 = text.substring(0, 5);
                int yearWidth1 = metrics.stringWidth(year1);
                int yearWidth2 = metrics.stringWidth(year2);

                if ((p.y >= textRect.y)
                        && (p.y <= textRect.y + textRect.height)) {
                    int menuX = textRect.x;
                    int menuY = textRect.y + textRect.height;

                    if ((p.x >= textRect.x) && (p.x <= textRect.x + yearWidth1)) {
                        this.yearPopup.show(this.lbYearAndMonth, menuX, menuY);
                    } else if ((p.x >= textRect.x + yearWidth2)
                            && (p.x <= textRect.x + textRect.width)) {
                        menuX = textRect.x + yearWidth2;
                        this.monthPopup.show(this.lbYearAndMonth, menuX, menuY);
                    }
                }
            }
        }
    }

    @Deprecated
    public void updateUI() {
        if (this.yearPopup != null) {
            SwingUtilities.updateComponentTreeUI(this.yearPopup);
        }

        if (this.monthPopup != null) {
            SwingUtilities.updateComponentTreeUI(this.monthPopup);
        }
    }
}