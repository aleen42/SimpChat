package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import com.java.ui.util.Util;
import com.sun.awt.AWTUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicPopupMenuUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Calendar;

public class JCCalendarComboBox extends JCComboBox {
    private class CCalendarComboBoxUI extends CComboBoxUI {
        private KeyListener keyListener;
        private MouseListener mouseListener;

        private CCalendarComboBoxUI() {
        }

        private void changeDate(int day) {
            if ((this.comboBox instanceof JCCalendarComboBox)) {
                JCCalendarComboBox calendarBox = (JCCalendarComboBox) this.comboBox;
                Calendar date = calendarBox.getDate();

                if (date == null) {
                    date = Calendar.getInstance();
                } else {
                    date.add(6, day);
                }

                calendarBox.setDate(date);
            }
        }

        public void configureArrowButton() {
            super.configureArrowButton();

            if (this.arrowButton != null) {
                this.arrowButton.addMouseListener(getMouseListener());
            }
        }

        protected JButton createArrowButton() {
            JCButton button = new JCButton();
            button.setName("CalendarComboBox.arrowButton");
            button.setImage(this.buttonImage = UIResourceManager
                    .getImage("CalendarComboBoxButtonImage"));
            button.setDisabledImage(UIUtil.toBufferedImage(this.buttonImage,
                    0.5F, button));
            button.setRolloverImage(this.buttonRolloverImage = UIResourceManager
                    .getImage("CalendarComboBoxButtonRolloverImage"));
            button.setPressedImage(UIResourceManager
                    .getImage("CalendarComboBoxButtonPressedImage"));
            button.setImageInsets(2, 2, 0, 0);
            return button;
        }

        protected ComboPopup createPopup() {
            return new JCCalendarComboBox.CCalendarPopup(comboBox);
        }

        private KeyListener getKeyListener() {
            if (this.keyListener == null) {
                this.keyListener = new KeyAdapter() {
                    public void keyPressed(KeyEvent e) {
                        int keyCode = e.getKeyCode();

                        if (e.getModifiers() == 0) {
                            if (keyCode == 38) {
                                JCCalendarComboBox.CCalendarComboBoxUI.this
                                        .selectPreviousPossibleValue();
                            } else if (keyCode == 40) {
                                JCCalendarComboBox.CCalendarComboBoxUI.this
                                        .selectNextPossibleValue();
                            }
                        }
                    }
                };
            }
            return this.keyListener;
        }

        private MouseListener getMouseListener() {
            if (this.mouseListener == null) {
                this.mouseListener = new MouseAdapter() {
                    private boolean visible;

                    public void mousePressed(MouseEvent e) {
                        this.visible = JCCalendarComboBox.CCalendarComboBoxUI.this.comboBox
                                .isPopupVisible();
                        MenuSelectionManager.defaultManager()
                                .clearSelectedPath();
                    }

                    public void mouseReleased(MouseEvent e) {
                        if ((this.visible)
                                || (!(JCCalendarComboBox.CCalendarComboBoxUI.this.comboBox instanceof JCComboBox))
                                || ((JCCalendarComboBox.CCalendarComboBoxUI.this.comboBox
                                .isEnabled()) && (((JCComboBox) JCCalendarComboBox.CCalendarComboBoxUI.this.comboBox)
                                .isEditableAll()))) {
                            JCCalendarComboBox.CCalendarComboBoxUI.this.comboBox
                                    .setPopupVisible(!this.visible);
                        }
                    }
                };
            }
            return this.mouseListener;
        }

        protected void installListeners() {
            super.installListeners();
            this.comboBox.getEditor().getEditorComponent()
                    .addKeyListener(getKeyListener());
        }

        protected void selectNextPossibleValue() {
            if (((this.comboBox instanceof JCCalendarComboBox))
                    && (!((JCCalendarComboBox) this.comboBox).isEditableAll())) {
                return;
            }

            changeDate(1);
        }

        protected void selectPreviousPossibleValue() {
            if (((this.comboBox instanceof JCCalendarComboBox))
                    && (!((JCCalendarComboBox) this.comboBox).isEditableAll())) {
                return;
            }

            changeDate(-1);
        }

        public void unconfigureArrowButton() {
            super.unconfigureArrowButton();

            if (this.arrowButton != null) {
                this.arrowButton.removeMouseListener(getMouseListener());
            }
        }

        protected void uninstallListeners() {
            super.uninstallListeners();
            this.comboBox.getEditor().getEditorComponent()
                    .removeKeyListener(this.keyListener);
        }
    }

    private class CCalendarPopup extends BasicComboPopup {
        private static final long serialVersionUID = -2699306734874068484L;
        private JWindow heavyWeightWindow;
        private RoundRectangle2D.Double roundRect;
        private final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit()
                .getScreenSize().height;

        public CCalendarPopup(JComboBox comboBox) {
            super(comboBox);
            setUI(new BasicPopupMenuUI() {
                public void installDefaults() {
                }

                protected void uninstallDefaults() {
                }
            });
            setBorder(new EmptyBorder(0, 0, 0, 0));
            setOpaque(false);
            remove(this.scroller);
            setLayout(new BorderLayout());
            add(JCCalendarComboBox.this.calendarPane = new JCCalendarPane(),
                    "Center");
        }

        private JWindow getHeavyWeightWindow() {
            Container c = getParent();

            while ((c != null) && (!(c instanceof Window))) {
                c = c.getParent();
            }

            if ((c != null)
                    && (c.getName().equalsIgnoreCase("###overrideRedirect###"))) {
                return (JWindow) c;
            }

            return null;
        }

        public void setVisible(boolean visible) {
            if (visible == isVisible()) {
                return;
            }

            if (visible) {
                Object value = JCCalendarComboBox.this.getSelectedItem();
                JCCalendarComboBox.this.calendarPane
                        .setDateString(value == null ? null : value.toString());
            } else {
                JCCalendarComboBox.this.calendarPane.clearMouseStatus();
            }

            super.setVisible(visible);

            if ((visible)
                    && ((this.heavyWeightWindow = getHeavyWeightWindow()) != null)) {
                if (this.roundRect == null) {
                    this.roundRect = new RoundRectangle2D.Double(0.0D, 0.0D,
                            getWidth(), getHeight(), 3.0D, 3.0D);
                } else {
                    this.roundRect.width = getWidth();
                    this.roundRect.height = getHeight();
                }

                AWTUtilities.setWindowShape(this.heavyWeightWindow,
                        this.roundRect);
            } else if (!visible) {
                if (this.heavyWeightWindow != null) {
                    AWTUtilities.setWindowShape(this.heavyWeightWindow, null);
                    this.heavyWeightWindow = null;
                }

                JCCalendarComboBox.this.resetBorderAfterPopupHidden();
            }
        }

        public void show() {
            if (((this.comboBox instanceof JCComboBox))
                    && (!((JCComboBox) this.comboBox).isEditableAll())) {
                return;
            }

            super.show();
        }

        public void show(Component invoker, int x, int y) {
            if (invoker != null) {
                int parentWidth = invoker.getWidth();
                Dimension size = getPreferredSize();
                int width = size.width;
                int height = size.height;
                x = parentWidth > width ? parentWidth - width : 0;
                y = this.SCREEN_HEIGHT
                        - (invoker.getLocationOnScreen().y + invoker
                        .getHeight()) < height ? -height : invoker
                        .getHeight();
            }

            super.show(invoker, x, y);
        }

        protected void togglePopup() {
        }

        @Deprecated
        public void updateUI() {
            setUI(getUI());
        }
    }

    private class EditorListener extends MouseAdapter {
        private EditorListener() {
        }

        public void mousePressed(MouseEvent e) {
            JCCalendarComboBox.this.calendarPane.close();
        }
    }

    private static final long serialVersionUID = -3823377723957390277L;

    private JCCalendarPane calendarPane;

    public JCCalendarComboBox() {
        setUI(new CCalendarComboBoxUI());
        super.setEditable(true);
        JTextComponent editor = (JTextComponent) getEditor()
                .getEditorComponent();
        editor.setEditable(false);
        editor.setCursor(new Cursor(2));
        editor.addMouseListener(new EditorListener());
        this.calendarPane.setClosable(true);
        this.calendarPane.setCloseAction(new AbstractAction() {
            private static final long serialVersionUID = 4087888078109547665L;

            public void actionPerformed(ActionEvent e) {
                JCCalendarComboBox.this.updateSelected();
            }
        });
    }

    public JCCalendarPane getCalendarPane() {
        return this.calendarPane;
    }

    public Calendar getDate() {
        return this.calendarPane.getDate();
    }

    public String getDateString() {
        return this.calendarPane.getDateString();
    }

    private void resetBorderAfterPopupHidden() {
        Rectangle rect = new Rectangle(getLocationOnScreen(), getSize());

        if (!rect.contains(Util.getMouseLocation())) {
            mouseOut();
        }
    }

    protected void resetShortcutKeys() {
        InputMap inputMap = getInputMap(1);

        for (KeyStroke ks : inputMap.allKeys()) {
            Object value = inputMap.get(ks);

            if ((!value.equals("selectNext")) && (!value.equals("selectNext2"))
                    && (!value.equals("selectPrevious"))
                    && (!value.equals("selectPrevious2")))
                continue;
            inputMap.put(ks, "");
        }
    }

    public void setDate(Calendar date) {
        this.calendarPane.setDate(date);
        updateSelected();
    }

    public void setDateString(String dateString) {
        this.calendarPane.setDateString(dateString);
        updateSelected();
    }

    @Deprecated
    public void setEditable(boolean editable) {
    }

    private void updateSelected() {
        String date = this.calendarPane.getDateString();
        Object oldDate = getSelectedItem();

        if (((date != null) && (!date.equals(oldDate)))
                || ((oldDate != null) && (!oldDate.equals(date)))) {
            setSelectedItem(date);
        }

        if (isPopupVisible()) {
            hidePopup();
        }
    }
}