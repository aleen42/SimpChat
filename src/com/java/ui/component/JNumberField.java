package com.java.ui.component;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JNumberField extends JTextField implements Serializable {
    private class NumberDocument extends PlainDocument {
        private static final long serialVersionUID = 973277174850216005L;
        private int length;
        private Matcher matcher;
        private int newLength;
        private int oldLength;
        private String oldText;
        private StringBuilder text = new StringBuilder();

        private NumberDocument() {
        }

        public void insertString(int offset, String input, AttributeSet a)
                throws BadLocationException {
            this.oldLength = getLength();
            this.newLength = input.length();
            this.length = (this.oldLength + this.newLength);
            this.oldText = getText(0, this.oldLength);
            this.text.delete(0, this.text.length());
            this.text.append(this.oldText);
            this.text.insert(offset, input);
            this.matcher = JNumberField.this.pattern.matcher(this.text);

            if (((JNumberField.this.maxLength > 0) && (this.length > JNumberField.this.maxLength))
                    || (!this.matcher.matches())) {
                return;
            }

            try {
                double num = JNumberField.this.getNumber(this.text.toString(),
                        Double.valueOf(-4.940656458412465E-324D)).doubleValue();

                if ((num > JNumberField.this.maxRange)
                        || ((JNumberField.this.nonNegative) && (num < 0.0D))) {
                    return;
                }
            } catch (Exception e) {
                return;
            }

            super.insertString(offset, input, a);
        }
    }

    private static final long serialVersionUID = -7025550739233386707L;
    private int decimalLength;
    private int maxLength;
    private double maxRange;
    private boolean nonNegative;

    private Pattern pattern;

    public JNumberField() {
        this(0);
    }

    public JNumberField(int maxLength) {
        this(maxLength, 0);
    }

    public JNumberField(int maxLength, int decimalLength) {
        this(maxLength, decimalLength, 1.7976931348623157E+308D);
    }

    public JNumberField(int maxLength, int decimalLength, double maxRange) {
        this.maxLength = maxLength;
        this.maxRange = maxRange;
        setDecimalLength(decimalLength);
        setDocument(new NumberDocument());
        setHorizontalAlignment(4);
    }

    public int getDecimalLength() {
        return this.decimalLength;
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public double getMaxRange() {
        return this.maxRange;
    }

    public Number getNumber() {
        try {
            return getNumber(getText(), Integer.valueOf(0));
        } catch (Exception e) {
        }
        return null;
    }

    private Number getNumber(String numStr, Number defaultNum) throws Exception {
        if (numStr.endsWith(".")) {
            numStr = numStr.substring(0, numStr.length() - 1);
        }

        if ((numStr.isEmpty()) || (numStr.equals("-"))) {
            return defaultNum;
        }

        if (this.decimalLength > 0) {
            return Double.valueOf(Double.parseDouble(numStr));
        }

        return Long.valueOf(Long.parseLong(numStr));
    }

    public boolean isDecimal() {
        return this.decimalLength > 0;
    }

    public boolean isNonNegative() {
        return this.nonNegative;
    }

    public void setDecimalLength(int decimalLength) {
        this.decimalLength = decimalLength;

        if (decimalLength > 0) {
            String decimalRegex = "-?(0|(0\\.\\d{0," + decimalLength
                    + "})|([1-9]\\d*\\.?\\d{0," + decimalLength + "}))?";
            this.pattern = Pattern.compile(decimalRegex);
        } else {
            this.pattern = Pattern.compile("0|(-?([1-9]\\d*)?)");
        }
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setMaxRange(double maxRange) {
        this.maxRange = maxRange;
    }

    public void setNonNegative(boolean nonNegative) {
        this.nonNegative = nonNegative;
    }
}