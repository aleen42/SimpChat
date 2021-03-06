package com.java.ui.componentc;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.io.Serializable;

public class JCLimitedPassword extends JCPasswordField implements Serializable {
    private class PasswordDocument extends PlainDocument {
        private static final long serialVersionUID = 5555932492608826780L;
        private int length;
        private int newLength;
        private int oldLength;

        private PasswordDocument() {
        }

        public void insertString(int offset, String input, AttributeSet a)
                throws BadLocationException {
            this.oldLength = getLength();
            this.newLength = input.length();
            this.length = (this.oldLength + this.newLength);

            if ((JCLimitedPassword.this.maxLength > 0)
                    && (this.length > JCLimitedPassword.this.maxLength)) {
                this.newLength = (JCLimitedPassword.this.maxLength - this.oldLength);

                if (this.newLength > 0) {
                    super.insertString(offset,
                            input.substring(0, this.newLength), a);
                }
            } else {
                super.insertString(offset, input, a);
            }
        }
    }

    private static final long serialVersionUID = 8114410159759930019L;

    private int maxLength;

    public JCLimitedPassword() {
        this(0);
    }

    public JCLimitedPassword(int maxLength) {
        this.maxLength = maxLength;
        setDocument(new PasswordDocument());
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}