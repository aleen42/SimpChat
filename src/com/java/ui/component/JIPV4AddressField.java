package com.java.ui.component;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JIPV4AddressField extends JTextField implements Serializable {
    private class Dot extends JLabel {
        private static final long serialVersionUID = -2704811830155290868L;

        public Dot() {
            super();
            setOpaque(false);
            setBorder(JIPV4AddressField.EMPTY_BORDER);
            setForeground(JIPV4AddressField.this.getForeground());
            setEnabled(JIPV4AddressField.this.isEnabled());
            setHorizontalAlignment(0);
        }

        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            setForeground(enabled ? JIPV4AddressField.this.getForeground()
                    : JIPV4AddressField.this.getDisabledTextColor());
        }

        public void updateUI() {
            super.updateUI();
            setFont(JIPV4AddressField.this.getFont());
        }
    }

    private class JIPV4Field extends JTextField implements ActionListener,
            FocusListener, Serializable {
        private class IPBlockDocument extends PlainDocument {
            private static final long serialVersionUID = -2645957214215338331L;
            private final Pattern INT_PATTERN = Pattern.compile("0|([1-9]\\d*)?");

            private int ipBlockInt;
            private int length;
            private Matcher matcher;
            private int newLength;
            private int oldLength;
            private StringBuilder text = new StringBuilder();

            private IPBlockDocument() {
            }

            public void insertString(int offset, String input, AttributeSet a)
                    throws BadLocationException {
                this.oldLength = getLength();
                this.newLength = input.length();
                this.length = (this.oldLength + this.newLength);
                this.text.delete(0, this.text.length());
                this.text.append(getText(0, this.oldLength));
                this.text.insert(offset, input);
                this.matcher = this.INT_PATTERN.matcher(this.text);

                if ((this.length > 3) || (!this.matcher.matches())) {
                    return;
                }

                this.ipBlockInt = (this.text.length() == 0 ? 0 : Integer
                        .parseInt(this.text.toString()));

                if (this.ipBlockInt > 255) {
                    return;
                }

                super.insertString(offset, input, a);
            }
        }

        private static final long serialVersionUID = 1411564647463716520L;

        private boolean selectAll;

        public JIPV4Field() {
            this.selectAll = true;
            setHorizontalAlignment(0);
            setBorder(JIPV4AddressField.EMPTY_BORDER);
            setOpaque(false);
            setMargin(new Insets(0, 0, 0, 0));
            setDocument(new IPBlockDocument());
            setFont(JIPV4AddressField.this.getFont());
            setSelectionColor(JIPV4AddressField.this.getSelectionColor());
            setSelectedTextColor(JIPV4AddressField.this.getSelectedTextColor());
            setEditable(JIPV4AddressField.this.isEditable());
            setDisabledTextColor(JIPV4AddressField.this.getDisabledTextColor());
            setCaretColor(JIPV4AddressField.this.getCaretColor());
            setForeground(JIPV4AddressField.this.getForeground());
            setEnabled(JIPV4AddressField.this.isEnabled());
            addActionListener(this);
            addFocusListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            transferFocus();
        }

        public void focusGained(FocusEvent e) {
            if (this.selectAll) {
                selectAll();
            } else {
                this.selectAll = true;
            }
        }

        public void focusLost(FocusEvent e) {
        }

        public void unSelectAllWhenFocusGained() {
            if (!isFocusOwner()) {
                this.selectAll = false;
            }
        }
    }

    private class KeyPressListener implements PropertyChangeListener {
        private JIPV4AddressField.JIPV4Field leftField;
        private JIPV4AddressField.JIPV4Field rightField;

        public KeyPressListener(JIPV4AddressField.JIPV4Field leftField,
                                JIPV4AddressField.JIPV4Field rightField) {
            this.leftField = leftField;
            this.rightField = rightField;
        }

        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();

            if ((name == "Left") && (this.leftField != null)) {
                this.leftField.requestFocus();
            } else if ((name == "Right") && (this.rightField != null)) {
                this.rightField.requestFocus();
            } else if ((name == "BackSpace") && (this.leftField != null)) {
                this.leftField.unSelectAllWhenFocusGained();
                this.leftField.requestFocus();
                this.leftField.setCaretPosition(this.leftField.getText()
                        .length());
            } else if ((name == "Delete") && (this.rightField != null)) {
                this.rightField.unSelectAllWhenFocusGained();
                this.rightField.requestFocus();
                this.rightField.setCaretPosition(0);
            }
        }
    }

    private static final Border EMPTY_BORDER = BorderFactory
            .createEmptyBorder();
    private static final long serialVersionUID = -2754807884601930339L;

    private Dot[] dots;

    private JIPV4Field[] ipFields;

    private KeyAdapter KeyListener = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            JTextComponent field = (JTextComponent) e.getComponent();
            int keyCode = e.getKeyCode();
            char keyChar = e.getKeyChar();
            String text = field.getText();
            String selText = field.getSelectedText();
            int caretPos = field.getCaretPosition();
            int textLength = text.length();

            if ((keyCode == 37) && (caretPos == 0) && (selText == null)) {
                field.firePropertyChange("Left", 0, 1);
            } else if (((keyCode == 39) && (caretPos == textLength) && (selText == null))
                    || ((keyChar == '.') && (!text.isEmpty()) && (selText == null))) {
                field.firePropertyChange("Right", 0, 1);
            } else if ((keyCode == 8) && (caretPos == 0) && (selText == null)) {
                field.firePropertyChange("BackSpace", 0, 1);
            } else if ((keyCode == 127) && (caretPos == textLength)
                    && (selText == null)) {
                field.firePropertyChange("Delete", 0, 1);
            } else if (keyCode == 36) {
                JIPV4AddressField.this.ipFields[0].unSelectAllWhenFocusGained();
                JIPV4AddressField.this.ipFields[0].requestFocus();
                JIPV4AddressField.this.ipFields[0].setCaretPosition(0);
            } else if (keyCode == 35) {
                int last = JIPV4AddressField.this.ipFields.length - 1;
                textLength = JIPV4AddressField.this.ipFields[last].getText()
                        .length();
                JIPV4AddressField.this.ipFields[last]
                        .unSelectAllWhenFocusGained();
                JIPV4AddressField.this.ipFields[last].requestFocus();
                JIPV4AddressField.this.ipFields[last]
                        .setCaretPosition(textLength);
            } else if (("0123456789".indexOf(keyChar) >= 0)
                    && (((selText == null) && (caretPos == 2)) || ((selText != null)
                    && (field.getSelectionStart() == 2) && (field
                    .getSelectionEnd() == 3)))) {
                field.firePropertyChange("Right", 0, 1);
            }
        }
    };

    public JIPV4AddressField() {
        this(null);
    }

    public JIPV4AddressField(String ipAddress) {
        setLayout(new GridLayout(1, 4, 0, 0));
        setFocusable(false);
        createIPFields();
        setIpAddress(ipAddress);
    }

    public void addFocusListener(FocusListener listener) {
        if (this.ipFields != null) {
            for (JIPV4Field field : this.ipFields) {
                field.addFocusListener(listener);
            }
        }
    }

    private void createIPFields() {
        this.ipFields = new JIPV4Field[4];
        this.dots = new Dot[3];
        JPanel[] fieldPanes = new JPanel[4];

        for (int i = 0; i < 4; i++) {
            this.ipFields[i] = new JIPV4Field();
            fieldPanes[i] = new JPanel();

            this.ipFields[i].addKeyListener(this.KeyListener);
            fieldPanes[i].setOpaque(false);
            fieldPanes[i].setLayout(new BorderLayout());
            fieldPanes[i].add(this.ipFields[i], "Center");

            if (i != 3) {
                fieldPanes[i].add(new Dot(), "East");
            }

            add(fieldPanes[i]);
        }

        for (int i = 0; i < 4; i++) {
            if (i == 0) {
                this.ipFields[i]
                        .addPropertyChangeListener(new KeyPressListener(null,
                                this.ipFields[(i + 1)]));
            } else if (i == 3) {
                this.ipFields[i]
                        .addPropertyChangeListener(new KeyPressListener(
                                this.ipFields[(i - 1)], null));
            } else {
                this.ipFields[i]
                        .addPropertyChangeListener(new KeyPressListener(
                                this.ipFields[(i - 1)], this.ipFields[(i + 1)]));
            }
        }
    }

    public JTextField[] getFieldComponents() {
        return this.ipFields;
    }

    public String getIpAddress() {
        StringBuilder ip = new StringBuilder();
        int emptyCount = 0;

        if (this.ipFields != null) {
            for (JIPV4Field field : this.ipFields) {
                String str = field.getText();

                if (str.isEmpty()) {
                    emptyCount++;
                }

                ip.append('.');
                ip.append(str);
            }

            ip.deleteCharAt(0);
        }

        return emptyCount == 4 ? "" : ip.toString();
    }

    public String getText() {
        return getIpAddress();
    }

    public void removeFocusListener(FocusListener listener) {
        if (this.ipFields != null) {
            for (JIPV4Field field : this.ipFields) {
                field.removeFocusListener(listener);
            }
        }
    }

    public void setCaretColor(Color color) {
        super.setCaretColor(color);

        if (this.ipFields != null) {
            for (JIPV4Field field : this.ipFields) {
                field.setCaretColor(color);
            }
        }
    }

    public void setDisabledTextColor(Color color) {
        super.setDisabledTextColor(color);

        if (this.ipFields != null) {
            for (JIPV4Field field : this.ipFields) {
                field.setDisabledTextColor(color);

                if (field.isEnabled())
                    continue;
                field.repaint();
            }

        }

        if ((this.dots != null) && (!isEnabled())) {
            for (Dot dot : this.dots) {
                dot.setForeground(color);
            }
        }
    }

    public void setEditable(boolean isEditable) {
        super.setEditable(isEditable);

        if (this.ipFields != null) {
            for (JIPV4Field field : this.ipFields) {
                field.setEditable(isEditable);
            }
        }
    }

    public void setEnabled(boolean isEnable) {
        super.setEnabled(isEnable);

        if (this.ipFields != null) {
            for (JIPV4Field field : this.ipFields) {
                field.setEnabled(isEnable);
            }
        }

        if (this.dots != null) {
            for (Dot dot : this.dots) {
                dot.setForeground(isEnable ? getForeground()
                        : getDisabledTextColor());
            }
        }
    }

    public void setFont(Font font) {
        super.setFont(font);

        if (this.ipFields != null) {
            for (JIPV4Field field : this.ipFields) {
                field.setFont(font);
            }
        }

        if (this.dots != null) {
            for (Dot dot : this.dots) {
                dot.setFont(font);
            }
        }
    }

    public void setForeground(Color color) {
        super.setForeground(color);

        if (this.ipFields != null) {
            for (JIPV4Field field : this.ipFields) {
                field.setForeground(color);
            }
        }

        if ((this.dots != null) && (isEnabled())) {
            for (Dot dot : this.dots) {
                dot.setForeground(color);
            }
        }
    }

    public void setIpAddress(String ipAddress) {
        int index;

        if ((ipAddress != null) && (!ipAddress.isEmpty())) {
            if (!Pattern
                    .matches(
                            "((2[0-4]\\d|25[0-5]|[01]?\\d?\\d)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d?\\d)",
                            ipAddress)) {
                throw new IllegalArgumentException("Invalid IP Address:"
                        + ipAddress);
            }

            String[] ipBit = ipAddress.split("\\.");
            index = 0;

            for (JIPV4Field field : this.ipFields) {
                field.setText(Integer.parseInt(ipBit[(index++)]) + "");
            }
        } else {
            for (JIPV4Field field : this.ipFields) {
                field.setText("");
            }
        }
    }

    public void setSelectedTextColor(Color color) {
        super.setSelectedTextColor(color);

        if (this.ipFields != null) {
            for (JIPV4Field field : this.ipFields) {
                field.setSelectedTextColor(color);
            }
        }
    }

    public void setSelectionColor(Color color) {
        super.setSelectionColor(color);

        if (this.ipFields != null) {
            for (JIPV4Field field : this.ipFields) {
                field.setSelectionColor(color);
            }
        }
    }

    public void setText(String text) {
        setIpAddress(text);
    }

    public void updateUI() {
        Component[] children = getComponents();

        for (Component child : children) {
            remove(child);
        }

        super.updateUI();

        for (Component child : children) {
            add(child);
        }

        if (getCaret() != null) {
            getCaret().deinstall(this);
        }
    }
}