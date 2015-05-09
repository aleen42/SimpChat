package com.java.ui.componentc;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.io.Serializable;

public class JCStringField extends JCTextField implements Serializable {
    private class StringDocument extends PlainDocument {
        private static final long serialVersionUID = -2645957214215338331L;
        private int length;
        private int newLength;
        private int oldLength;

        private StringDocument() {
        }

        public void insertString(int offset, String input, AttributeSet a)
                throws BadLocationException {
            this.oldLength = getLength();
            this.newLength = input.length();
            this.length = (this.oldLength + this.newLength);

            if ((JCStringField.this.maxLength > 0)
                    && (this.length > JCStringField.this.maxLength)) {
                this.newLength = (JCStringField.this.maxLength - this.oldLength);

                if (this.newLength > 0) {
                    super.insertString(offset,
                            input.substring(0, this.newLength), a);
                }
            } else {
                super.insertString(offset, input, a);
            }
        }
    }

    private static final long serialVersionUID = 7379430556435445023L;

    private int maxLength;

    public JCStringField() {
        this(0);
    }

    public JCStringField(int maxLength) {
        this.maxLength = maxLength;
        setDocument(new StringDocument());
    }

    public int getMaxLength() {
        return this.maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }
}