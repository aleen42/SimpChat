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
import java.util.regex.Pattern;

public class JMACAddressField extends JTextField implements Serializable {
    private class Colon extends JLabel {
        private static final long serialVersionUID = 301136113913078182L;

        public Colon() {
            super();
            setOpaque(false);
            setBorder(JMACAddressField.EMPTY_BORDER);
            setForeground(JMACAddressField.this.getForeground());
            setEnabled(JMACAddressField.this.isEnabled());
            setHorizontalAlignment(0);
        }

        public void setEnabled(boolean enabled) {
            super.setEnabled(enabled);
            setForeground(enabled ? JMACAddressField.this.getForeground()
                    : JMACAddressField.this.getDisabledTextColor());
        }

        public void updateUI() {
            super.updateUI();
            setFont(JMACAddressField.this.getFont());
        }
    }

    private class JMACField extends JTextField implements ActionListener,
            FocusListener, Serializable {
        private class MacBlockDocument extends PlainDocument {
            private static final long serialVersionUID = -2667829308055179550L;
            private final Pattern PATTERN = Pattern.compile("[0-9A-Fa-f]{0,2}");

            private StringBuilder text = new StringBuilder();

            private MacBlockDocument() {
            }

            public void insertString(int offset, String input, AttributeSet a)
                    throws BadLocationException {
                this.text.delete(0, this.text.length());
                this.text.append(getText(0, getLength()));
                this.text.insert(offset, input);

                if (!this.PATTERN.matcher(this.text).matches()) {
                    return;
                }

                super.insertString(offset, input, a);
            }
        }

        private static final long serialVersionUID = -5440880126904364761L;

        private boolean selectAll;

        public JMACField() {
            this.selectAll = true;
            setHorizontalAlignment(0);
            setBorder(JMACAddressField.EMPTY_BORDER);
            setOpaque(false);
            setMargin(new Insets(0, 0, 0, 0));
            setDocument(new MacBlockDocument());
            setFont(JMACAddressField.this.getFont());
            setSelectionColor(JMACAddressField.this.getSelectionColor());
            setSelectedTextColor(JMACAddressField.this.getSelectedTextColor());
            setEditable(JMACAddressField.this.isEditable());
            setDisabledTextColor(JMACAddressField.this.getDisabledTextColor());
            setCaretColor(JMACAddressField.this.getCaretColor());
            setForeground(JMACAddressField.this.getForeground());
            setEnabled(JMACAddressField.this.isEnabled());
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
        private JMACAddressField.JMACField leftField;
        private JMACAddressField.JMACField rightField;

        public KeyPressListener(JMACAddressField.JMACField leftField,
                                JMACAddressField.JMACField rightField) {
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
    private static final long serialVersionUID = -4711699807610552544L;

    private Colon[] colons;

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
                    || ((keyChar == ':') && (!text.isEmpty()) && (selText == null))) {
                field.firePropertyChange("Right", 0, 1);
            } else if ((keyCode == 8) && (caretPos == 0) && (selText == null)) {
                field.firePropertyChange("BackSpace", 0, 1);
            } else if ((keyCode == 127) && (caretPos == textLength)
                    && (selText == null)) {
                field.firePropertyChange("Delete", 0, 1);
            } else if (keyCode == 36) {
                JMACAddressField.this.macFields[0].unSelectAllWhenFocusGained();
                JMACAddressField.this.macFields[0].requestFocus();
                JMACAddressField.this.macFields[0].setCaretPosition(0);
            } else if (keyCode == 35) {
                int last = JMACAddressField.this.macFields.length - 1;
                textLength = JMACAddressField.this.macFields[last].getText()
                        .length();
                JMACAddressField.this.macFields[last]
                        .unSelectAllWhenFocusGained();
                JMACAddressField.this.macFields[last].requestFocus();
                JMACAddressField.this.macFields[last]
                        .setCaretPosition(textLength);
            } else if (("0123456789abcdefABCDEF".indexOf(keyChar) >= 0)
                    && (((selText == null) && (caretPos == 1)) || ((selText != null)
                    && (field.getSelectionStart() == 1) && (field
                    .getSelectionEnd() == 2)))) {
                field.firePropertyChange("Right", 0, 1);
            }
        }
    };

    private JMACField[] macFields;

    public JMACAddressField() {
        this(null);
    }

    public JMACAddressField(String macAddress) {
        setLayout(new GridLayout(1, 6, 0, 0));
        setFocusable(false);
        createMacFields();
        setMacAddress(macAddress);
    }

    public void addFocusListener(FocusListener listener) {
        if (this.macFields != null) {
            for (JMACField field : this.macFields) {
                field.addFocusListener(listener);
            }
        }
    }

    private void createMacFields() {
        this.macFields = new JMACField[6];
        this.colons = new Colon[5];
        JPanel[] fieldPanes = new JPanel[6];

        for (int i = 0; i < 6; i++) {
            this.macFields[i] = new JMACField();
            fieldPanes[i] = new JPanel();

            this.macFields[i].addKeyListener(this.KeyListener);
            fieldPanes[i].setOpaque(false);
            fieldPanes[i].setLayout(new BorderLayout());
            fieldPanes[i].add(this.macFields[i], "Center");

            if (i != 5) {
                fieldPanes[i].add(new Colon(), "East");
            }

            add(fieldPanes[i]);
        }

        for (int i = 0; i < 6; i++) {
            if (i == 0) {
                this.macFields[i]
                        .addPropertyChangeListener(new KeyPressListener(null,
                                this.macFields[(i + 1)]));
            } else if (i == 5) {
                this.macFields[i]
                        .addPropertyChangeListener(new KeyPressListener(
                                this.macFields[(i - 1)], null));
            } else {
                this.macFields[i]
                        .addPropertyChangeListener(new KeyPressListener(
                                this.macFields[(i - 1)],
                                this.macFields[(i + 1)]));
            }
        }
    }

    public JTextField[] getFieldComponents() {
        return this.macFields;
    }

    public String getMacAddress() {
        StringBuilder mac = new StringBuilder();
        int emptyCount = 0;

        if (this.macFields != null) {
            for (JMACField field : this.macFields) {
                String str = field.getText();

                if (str.isEmpty()) {
                    emptyCount++;
                }

                str = str.length() == 1 ? "0" + str : str;
                mac.append(':');
                mac.append(str);
            }

            mac.deleteCharAt(0);
        }

        return emptyCount == 6 ? "" : mac.toString();
    }

    public String getText() {
        return getMacAddress();
    }

    public void removeFocusListener(FocusListener listener) {
        if (this.macFields != null) {
            for (JMACField field : this.macFields) {
                field.removeFocusListener(listener);
            }
        }
    }

    public void setCaretColor(Color color) {
        super.setCaretColor(color);

        if (this.macFields != null) {
            for (JMACField field : this.macFields) {
                field.setCaretColor(color);
            }
        }
    }

    public void setDisabledTextColor(Color color) {
        super.setDisabledTextColor(color);

        if (this.macFields != null) {
            for (JMACField field : this.macFields) {
                field.setDisabledTextColor(color);

                if (field.isEnabled())
                    continue;
                field.repaint();
            }

        }

        if ((this.colons != null) && (!isEnabled())) {
            for (Colon colon : this.colons) {
                colon.setForeground(color);
            }
        }
    }

    public void setEditable(boolean isEditable) {
        super.setEditable(isEditable);

        if (this.macFields != null) {
            for (JMACField field : this.macFields) {
                field.setEditable(isEditable);
            }
        }
    }

    public void setEnabled(boolean isEnable) {
        super.setEnabled(isEnable);

        if (this.macFields != null) {
            for (JMACField field : this.macFields) {
                field.setEnabled(isEnable);
            }
        }

        if (this.colons != null) {
            for (Colon colon : this.colons) {
                colon.setForeground(isEnable ? getForeground()
                        : getDisabledTextColor());
            }
        }
    }

    public void setFont(Font font) {
        super.setFont(font);

        if (this.macFields != null) {
            for (JMACField field : this.macFields) {
                field.setFont(font);
            }
        }

        if (this.colons != null) {
            for (Colon colon : this.colons) {
                colon.setFont(font);
            }
        }
    }

    public void setForeground(Color color) {
        super.setForeground(color);

        if (this.macFields != null) {
            for (JMACField field : this.macFields) {
                field.setForeground(color);
            }
        }

        if ((this.colons != null) && (isEnabled())) {
            for (Colon colon : this.colons) {
                colon.setForeground(color);
            }
        }
    }

    public void setMacAddress(String macAddress) {
        int index;
        if ((macAddress != null) && (!macAddress.isEmpty())) {
            if (!Pattern.matches("([0-9A-Fa-f]{2}:){5}[0-9A-Fa-f]{2}",
                    macAddress)) {
                throw new IllegalArgumentException("Invalid Mac Address:"
                        + macAddress);
            }

            String[] macBit = macAddress.split("\\:");
            index = 0;

            for (JMACField macField1 : this.macFields) {
                macField1.setText(macBit[(index++)]);
            }
        } else {
            for (JMACField macField1 : this.macFields) {
                macField1.setText("");
            }
        }
    }

    public void setSelectedTextColor(Color color) {
        super.setSelectedTextColor(color);

        if (this.macFields != null) {
            for (JMACField field : this.macFields) {
                field.setSelectedTextColor(color);
            }
        }
    }

    public void setSelectionColor(Color color) {
        super.setSelectionColor(color);

        if (this.macFields != null) {
            for (JMACField field : this.macFields) {
                field.setSelectionColor(color);
            }
        }
    }

    public void setText(String text) {
        setMacAddress(text);
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