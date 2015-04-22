package com.java.ui.componentc;

import com.java.ui.component.JImagePane;
import com.java.ui.util.UIResourceManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.util.Vector;

public class JCScrollTable extends JCScrollPane
        implements ActionListener {
    public static final String COMMAND_SHOW_MENU = "ShowMenu";
    private static final Color DISABLED_BG = UIResourceManager.getColor("TextDisabledBackground");
    private static final long serialVersionUID = 4276053919432949896L;
    private Color background;
    private Border border;
    private JCButton btnUpperRight;
    private Border disabledBorder;
    private JCTable table;

    public JCScrollTable() {
        this(new JCTable());
    }

    public JCScrollTable(JCTable table) {
        setViewportView(this.table = table);
        init();
    }

    public JCScrollTable(Object[][] rowData, Object[] columnNames) {
        this(new JCTable(rowData, columnNames));
    }

    public JCScrollTable(TableModel dm) {
        this(new JCTable(dm));
    }

    public JCScrollTable(Vector<?> rowData, Vector<?> columnNames) {
        this(new JCTable(rowData, columnNames));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ShowMenu")) {
            JPopupMenu menu = this.table.getColumnControlMenu();

            if ((menu != null) && (menu.getComponentCount() > 0)) {
                Dimension buttonSize = this.btnUpperRight.getSize();
                menu.show(this.btnUpperRight, buttonSize.width - menu.getPreferredSize().width, buttonSize.height);
            }
        }
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
    }

    private JComponent createUpperRightCorner() {
        Image defaultImage = UIResourceManager.getImage("ScrollTableShowMenuButtonImage");
        JImagePane pane = new JImagePane();
        this.btnUpperRight = new JCButton();
        pane.setImageOnly(true);
        pane.setBorder(new EmptyBorder(0, 0, 1, 0));
        pane.setLayout(new BorderLayout());
        pane.setFilledBorderArea(false);
        pane.setMode("Scaled");
        pane.setImage(defaultImage);
        pane.setAlpha(0.5F);
        this.btnUpperRight.setImage(defaultImage);
        this.btnUpperRight.setRolloverImage(UIResourceManager.getImage("ScrollTableShowMenuButtonRolloverImage"));
        this.btnUpperRight.setPressedImage(UIResourceManager.getImage("ScrollTableShowMenuButtonPressedImage"));
        this.btnUpperRight.setDisabledImage(this.btnUpperRight.getImage());
        this.btnUpperRight.setFocusable(false);
        this.btnUpperRight.setActionCommand("ShowMenu");
        this.btnUpperRight.addActionListener(this);
        pane.add(this.btnUpperRight);
        return pane;
    }

    public Border getDisabledBorder() {
        return this.disabledBorder;
    }

    public Color getDisabledForeground() {
        return this.table.getDisabledForeground();
    }

    @Deprecated
    public HeaderPane getHeader() {
        return null;
    }

    @Deprecated
    public JLabel getHeaderLabel() {
        return null;
    }

    @Deprecated
    public String getHeaderText() {
        return null;
    }

    public JCTable getTable() {
        return this.table;
    }

    private void init() {
        setBorder(new LineBorder(new Color(84, 165, 213)));
        setDisabledBorder(new LineBorder(new Color(84, 165, 213, 128)));
        setBackground(UIResourceManager.getWhiteColor());
        setCorner("UPPER_RIGHT_CORNER", createUpperRightCorner());
        setHeaderDisabledForeground(new Color(123, 123, 122));
        this.table.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.table.setDisabledBorder(this.table.getBorder());
        this.table.setVisibleInsets(0, 0, 0, 0);
        this.table.setAlpha(0.0F);
        this.table.setBackground(UIResourceManager.getEmptyColor());
        this.table.getTableHeader().setBackground(UIResourceManager.getEmptyColor());
    }

    protected void initHeader() {
    }

    public boolean isColumnControlEnabled() {
        return this.btnUpperRight.isEnabled();
    }

    public void setBackground(Color background) {
        this.background = background;
        super.setBackground(background);
    }

    public void setBorder(Border border) {
        this.border = border;
        super.setBorder(border);
    }

    public void setColumnControlEnabled(boolean enabled) {
        this.btnUpperRight.setEnabled(enabled);
    }

    public void setDisabledBorder(Border disabledBorder) {
        this.disabledBorder = disabledBorder;

        if (!isEnabled()) {
            super.setBorder(disabledBorder);
        }
    }

    public void setDisabledForeground(Color disabledForeground) {
        this.table.setDisabledForeground(disabledForeground);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        super.setBorder(enabled ? this.border : this.disabledBorder);
        super.setBackground(enabled ? this.background : DISABLED_BG);
        this.btnUpperRight.setVisible(enabled);
    }

    @Deprecated
    public void setHeaderText(String text) {
    }
}