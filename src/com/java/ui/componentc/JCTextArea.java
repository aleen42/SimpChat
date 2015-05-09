package com.java.ui.componentc;

import com.java.ui.util.TextExtender;
import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JCTextArea extends JTextArea {
    private static final long serialVersionUID = -7145841694314317852L;
    private float alpha;
    private boolean borderChange;
    private Border disabledBorder;
    private TextExtender extender;
    private Image image;
    private boolean imageOnly;
    private boolean leadingTextVisible;
    private MouseListener listener;
    private Border nonEditableBorder;
    private Border nonEditableRolloverBorder;
    private Border normalBorder;
    private Border rolloverBorder;
    private Insets visibleInsets;

    public JCTextArea() {
        this(null, null, 0, 0);
    }

    public JCTextArea(Document doc) {
        this(doc, null, 0, 0);
    }

    public JCTextArea(Document doc, String text, int rows, int columns) {
        super(doc, text, rows, columns);
        setUI(new CTextAreaUI());
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
        setEditable(false);
        super.setBorder(this.normalBorder = new ImageBorder(UIResourceManager.getImageByName("border_normal.png"), 5, 6, 3, 4));
        this.extender = new TextExtender(this);
        this.rolloverBorder = UIResourceManager.getBorder("TextRolloverBorder");
        this.nonEditableBorder = UIResourceManager.getBorder("TextNonEditableBorder");
        this.nonEditableRolloverBorder = UIResourceManager.getBorder("TextNonEditableRolloverBorder");
        this.disabledBorder = UIResourceManager.getBorder("TextDisabledBorder");
        this.alpha = 1.0F;
        this.visibleInsets = new Insets(1, 1, 1, 1);
        this.borderChange = true;
        this.leadingTextVisible = true;
//        this.listener = new MouseAdapter() {
//            public void mouseEntered(MouseEvent e) {
//                JCTextArea.this.mouseIn();
//            }
//
//            public void mouseExited(MouseEvent e) {
//                JCTextArea.this.mouseOut();
//            }
//        };
        addMouseListener(this.listener);
    }

    public JCTextArea(int rows, int columns) {
        this(null, null, rows, columns);
    }

    public JCTextArea(String text) {
        this(null, text, 0, 0);
    }

    public JCTextArea(String text, int rows, int columns) {
        this(null, text, rows, columns);
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

    public boolean isImageOnly() {
        return this.imageOnly;
    }

    public boolean isLeadingTextVisible() {
        return this.leadingTextVisible;
    }

    public boolean isPopupMenuEnabled() {
        return this.extender.isPopupMenuEnabled();
    }

    private void mouseIn() {
        if ((this.normalBorder != null) && (isEnabled())) {
            super.setBorder(isEditable() ? this.rolloverBorder : this.nonEditableRolloverBorder);
        }
    }

    private void mouseOut() {
        if ((this.normalBorder != null) && (isEnabled())) {
            super.setBorder(isEditable() ? this.normalBorder : this.nonEditableBorder);
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
//        if (this.borderChange) {
//            mouseOut();
//        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

//        if (this.borderChange) {
//            if (enabled) {
//                mouseOut();
//            } else if (this.normalBorder != null) {
//                super.setBorder(this.disabledBorder);
//            }
//        }
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setImageOnly(boolean imageOnly) {
        this.imageOnly = imageOnly;
        repaint();
    }

    public void setLeadingTextVisible(boolean leadingTextVisible) {
        this.leadingTextVisible = leadingTextVisible;
    }

    @Deprecated
    public void setOpaque(boolean isOpaque) {
    }

    public void setPopupMenuEnabled(boolean popupMenuEnabled) {
        this.extender.setPopupMenuEnabled(popupMenuEnabled);
    }

    public void setText(String text) {
        super.setText(text);

        if (this.leadingTextVisible) {
            setSelectionStart(0);
            setSelectionEnd(0);
        }
    }

    public void setVisibleInsets(int top, int left, int bottom, int right) {
        this.visibleInsets.set(top, left, bottom, right);
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}