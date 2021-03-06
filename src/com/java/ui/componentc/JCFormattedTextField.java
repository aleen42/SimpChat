package com.java.ui.componentc;

import com.java.ui.util.TextExtender;
import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.Format;

public class JCFormattedTextField extends JFormattedTextField {
    private static final long serialVersionUID = -2877157868704955530L;
    private float alpha;
    private boolean borderChange;
    private Border disabledBorder;
    private TextExtender extender;
    private Image image;
    private boolean imageOnly;
    private MouseListener listener;
    private Border nonEditableBorder;
    private Border nonEditableRolloverBorder;
    private Border normalBorder;
    private Border rolloverBorder;
    private Insets visibleInsets;

    public JCFormattedTextField() {
        init();
    }

    public JCFormattedTextField(Format format) {
        super(format);
        init();
    }

    public JCFormattedTextField(JFormattedTextField.AbstractFormatter formatter) {
        super(formatter);
        init();
    }

    public JCFormattedTextField(
            JFormattedTextField.AbstractFormatterFactory factory) {
        super(factory);
        init();
    }

    public JCFormattedTextField(
            JFormattedTextField.AbstractFormatterFactory factory,
            Object currentValue) {
        super(factory, currentValue);
        init();
    }

    public JCFormattedTextField(Object value) {
        super(value);
        init();
    }

    public void clearBorderListener() {
        removeMouseListener(this.listener);
        this.borderChange = false;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public Image getImage() {
        return this.image;
    }

    public Insets getVisibleInsets() {
        return this.visibleInsets;
    }

    public void init() {
        setUI(new CFormattedTextFieldUI());
        super.setOpaque(false);
        setFont(UIUtil.getDefaultFont());
        setBackground(UIResourceManager.getWhiteColor());
        setForeground(Color.BLACK);
        setCaretColor(Color.BLACK);
        setSelectionColor(new Color(49, 106, 197));
        setSelectedTextColor(UIResourceManager.getWhiteColor());
        setDisabledTextColor(new Color(123, 123, 122));
        setCursor(new Cursor(2));
        setMargin(new Insets(0, 0, 0, 0));
        super.setBorder(this.normalBorder = new ImageBorder(UIResourceManager
                .getImageByName("border_normal.png"), 5, 6, 3, 4));
        this.extender = new TextExtender(this);
        this.rolloverBorder = UIResourceManager.getBorder("TextRolloverBorder");
        this.nonEditableBorder = UIResourceManager
                .getBorder("TextNonEditableBorder");
        this.nonEditableRolloverBorder = UIResourceManager
                .getBorder("TextNonEditableRolloverBorder");
        this.disabledBorder = UIResourceManager.getBorder("TextDisabledBorder");
        this.alpha = 1.0F;
        this.visibleInsets = new Insets(1, 1, 1, 1);
        this.borderChange = true;
        this.listener = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                JCFormattedTextField.this.mouseIn();
            }

            public void mouseExited(MouseEvent e) {
                JCFormattedTextField.this.mouseOut();
            }
        };
        addMouseListener(this.listener);
    }

    public boolean isImageOnly() {
        return this.imageOnly;
    }

    public boolean isPopupMenuEnabled() {
        return this.extender.isPopupMenuEnabled();
    }

    private void mouseIn() {
        if ((this.normalBorder != null) && (isEnabled())) {
            super.setBorder(isEditable() ? this.rolloverBorder
                    : this.nonEditableRolloverBorder);
        }
    }

    private void mouseOut() {
        if ((this.normalBorder != null) && (isEnabled())) {
            super.setBorder(isEditable() ? this.normalBorder
                    : this.nonEditableBorder);
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

    public void setEditable(boolean editable) {
        super.setEditable(editable);

        if (this.borderChange) {
            mouseOut();
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if (this.borderChange) {
            if (enabled) {
                mouseOut();
            } else if (this.normalBorder != null) {
                super.setBorder(this.disabledBorder);
            }
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

    public void setPopupMenuEnabled(boolean popupMenuEnabled) {
        this.extender.setPopupMenuEnabled(popupMenuEnabled);
    }

    public void setVisibleInsets(int top, int left, int bottom, int right) {
        this.visibleInsets.set(top, left, bottom, right);
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}