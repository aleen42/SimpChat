package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicFormattedTextFieldUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeListener;

public class JCSpinner extends JSpinner {
    private class SpinnerTextField extends JFormattedTextField {
        private static final long serialVersionUID = -4082862799280638287L;

        public SpinnerTextField() {
            setUI(new BasicFormattedTextFieldUI());
            setSelectionColor(UIResourceManager.getColor("TextSelectionColor"));
            setSelectedTextColor(UIResourceManager
                    .getColor("TextSelectionForeground"));
            setMargin(new Insets(0, 0, 0, 0));
            setCursor(new Cursor(2));
            setFont(UIUtil.getDefaultFont());
            setBackground(UIResourceManager.getWhiteColor());
            setCaretColor(Color.BLACK);
            setBorder(new EmptyBorder(0, 0, 0, 0));
        }

        @Deprecated
        public void updateUI() {
        }
    }

    private static final Border EDITOR_BORDER = UIResourceManager
            .getBorder("SpinnerEditorBorder");

    private static final Border EDITOR_DISABLED_BORDER = UIResourceManager
            .getBorder("SpinnerEditorDisabledBorder");
    private static final long serialVersionUID = -7216220112988325312L;
    private float alpha;
    private boolean borderChange;
    private Border disabledBorder;
    private Color disabledTextColor;
    private Image image;
    private boolean imageOnly;
    private MouseListener listener;
    private Border normalBorder;
    private Border rolloverBorder;

    private Insets visibleInsets;

    public JCSpinner() {
        this(new SpinnerNumberModel());
    }

    public JCSpinner(SpinnerModel model) {
        super(model);
        this.alpha = 1.0F;
        this.visibleInsets = new Insets(1, 1, 1, 1);
        this.borderChange = true;
        this.disabledTextColor = new Color(126, 126, 125);
        this.normalBorder = new ImageBorder(
                UIResourceManager.getImageByName("border_normal.png"), 2, 2, 2,
                2);
        this.rolloverBorder = UIResourceManager
                .getBorder("CompoundTextRolloverBorder");
        this.disabledBorder = UIResourceManager
                .getBorder("CompoundTextDisabledBorder");
        this.listener = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                JCSpinner.this.mouseIn();
            }

            public void mouseExited(MouseEvent e) {
                JCSpinner.this.mouseOut();
            }
        };
        setUI(new CSpinnerUI());
        super.setBorder(this.normalBorder);
        super.setOpaque(false);
        super.setForeground(Color.BLACK);
        setFont(UIUtil.getDefaultFont());
        setBackground(UIResourceManager.getWhiteColor());
        initEditor(getEditor());
        addMouseListener(this.listener);

        for (Component c : getComponents()) {
            if (!(c instanceof JButton))
                continue;
            c.addMouseListener(this.listener);
        }
    }

    protected void changeEditorField(JComponent c) {
        if ((c != null) && ((c instanceof JSpinner.DefaultEditor))) {
            JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) c;
            JFormattedTextField oldField = editor.getTextField();
            JFormattedTextField newField = new SpinnerTextField();
            newField.setName(oldField.getName());
            newField.setValue(oldField.getValue());
            newField.setEditable(oldField.isEditable());
            newField.setInheritsPopupMenu(oldField.getInheritsPopupMenu());
            newField.setToolTipText(oldField.getToolTipText());
            newField.setActionMap(oldField.getActionMap());
            newField.setFormatterFactory(oldField.getFormatterFactory());
            newField.setHorizontalAlignment(oldField.getHorizontalAlignment());
            newField.setColumns(oldField.getColumns());

            for (PropertyChangeListener pcl : oldField
                    .getPropertyChangeListeners()) {
                newField.addPropertyChangeListener(pcl);
            }

            editor.remove(oldField);
            editor.add(newField);
        }
    }

    protected JComponent createEditor(SpinnerModel model) {
        JComponent editor = super.createEditor(model);
        changeEditorField(editor);
        return editor;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public Color getDisabledTextColor() {
        return this.disabledTextColor;
    }

    public int getHorizontalAlignment() {
        JComponent editor = getEditor();

        if ((editor instanceof JSpinner.DefaultEditor)) {
            return ((JSpinner.DefaultEditor) editor).getTextField()
                    .getHorizontalAlignment();
        }

        return -1;
    }

    public Image getImage() {
        return this.image;
    }

    public Insets getVisibleInsets() {
        return this.visibleInsets;
    }

    private void initEditor(JComponent editor) {
        setForeground(getForeground());
        setDisabledTextColor(this.disabledTextColor);
        editor.setOpaque(false);
        editor.setBorder(isEnabled() ? EDITOR_BORDER : EDITOR_DISABLED_BORDER);
        editor.addMouseListener(this.listener);

        for (Component c : editor.getComponents()) {
            ((JComponent) c).setOpaque(false);
            c.addMouseListener(this.listener);
        }
    }

    public boolean isImageOnly() {
        return this.imageOnly;
    }

    private void mouseIn() {
        if ((isEnabled()) && (this.normalBorder != null)) {
            super.setBorder(this.rolloverBorder);
        }
    }

    private void mouseOut() {
        if ((isEnabled()) && (this.normalBorder != null)) {
            super.setBorder(this.normalBorder);
        }
    }

    public void setAlpha(float alpha) {
        if ((alpha >= 0.0F) && (alpha <= 1.0F)) {
            this.alpha = alpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid alpha:" + alpha);
        }
    }

    public void setBorder(Border border) {
        this.normalBorder = border;

        if ((border == null) && (this.visibleInsets != null)) {
            this.visibleInsets.set(0, 0, 0, 0);
        }

        super.setBorder(border);
    }

    public void setDisabledTextColor(Color disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
        JComponent editor = getEditor();

        if ((editor instanceof JSpinner.DefaultEditor)) {
            JFormattedTextField field = ((JSpinner.DefaultEditor) editor)
                    .getTextField();
            field.setDisabledTextColor(disabledTextColor);
        }
    }

    public void setEditor(JComponent editor) {
        JComponent oldEditor = getEditor();

        if (oldEditor != null) {
            oldEditor.removeMouseListener(this.listener);

            for (Component c : oldEditor.getComponents()) {
                c.removeMouseListener(this.listener);
            }
        }

        super.setEditor(editor);
        initEditor(editor);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        getEditor().setBorder(
                isEnabled() ? EDITOR_BORDER : EDITOR_DISABLED_BORDER);

        if (this.borderChange) {
            if (enabled) {
                mouseOut();
            } else if (this.normalBorder != null) {
                super.setBorder(this.disabledBorder);
            }
        }
    }

    public void setFont(Font font) {
        super.setFont(font);
        JComponent editor = getEditor();

        if ((editor instanceof JSpinner.DefaultEditor)) {
            ((JSpinner.DefaultEditor) editor).getTextField().setFont(font);
        }
    }

    public void setForeground(Color foreground) {
        super.setForeground(foreground);

        JComponent editor = getEditor();

        if ((editor instanceof JSpinner.DefaultEditor)) {
            JFormattedTextField field = ((JSpinner.DefaultEditor) editor)
                    .getTextField();
            field.setForeground(foreground);
        }
    }

    public void setHorizontalAlignment(int alignment) {
        JComponent editor = getEditor();

        if ((editor instanceof JSpinner.DefaultEditor)) {
            ((JSpinner.DefaultEditor) editor).getTextField()
                    .setHorizontalAlignment(alignment);
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

    public void setVisibleInsets(int top, int left, int bottom, int right) {
        this.visibleInsets.set(top, left, bottom, right);
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}