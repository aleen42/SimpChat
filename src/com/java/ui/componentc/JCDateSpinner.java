package com.java.ui.componentc;

import javax.swing.*;
import java.util.Date;

public class JCDateSpinner extends JCSpinner {
    public static final String DEFAULT_FORMAT_PATTERN = "HH:mm:ss";
    private static final long serialVersionUID = -5461435392396837097L;
    private JSpinner.DateEditor editor;
    private JFormattedTextField field;

    public JCDateSpinner() {
        this("HH:mm:ss");
    }

    public JCDateSpinner(Date date) {
        this("HH:mm:ss", date);
    }

    public JCDateSpinner(String dateFormatPattern) {
        this(dateFormatPattern, new Date());
    }

    public JCDateSpinner(String dateFormatPattern, Date date) {
        super(new SpinnerDateModel());
        this.editor = createEditor(dateFormatPattern);
        this.field = this.editor.getTextField();
        setEditor(this.editor);
        this.field.setEditable(false);
        setHorizontalAlignment(0);
        setDate(date);
    }

    protected JSpinner.DateEditor createEditor(String dateFormatPattern) {
        JSpinner.DateEditor editor = new JSpinner.DateEditor(this,
                dateFormatPattern);
        changeEditorField(editor);
        return editor;
    }

    public Date getDate() {
        return getValue();
    }

    public String getText() {
        return this.field.getText();
    }

    public Date getValue() {
        return (Date) super.getValue();
    }

    public void setDate(Date date) {
        setValue(date);
    }

    public void setValue(Date value) {
        super.setValue(value);
    }
}