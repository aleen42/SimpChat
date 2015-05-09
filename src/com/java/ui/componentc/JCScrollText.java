package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;

import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class JCScrollText extends JCScrollPane {
    private static final Color DISABLED_BG = UIResourceManager.getColor("TextDisabledBackground");
    private static final Color NON_EDITABLE_BG = UIResourceManager.getColor("TextNonEditableBackground");

    private static final long serialVersionUID = -8804672738482197370L;
    private Color background;
    private Border disabledBorder;
    private JCTextArea editor;
    private MouseListener listener;
    private Border nonEditableBorder;
    private Border nonEditableRolloverBorder;
    private Border normalBorder;
    private Border rolloverBorder;

    public JCScrollText() {
        this(new JCTextArea());
    }

    public JCScrollText(JCTextArea editor) {
        setViewportView(this.editor = editor);
        setBackground(UIResourceManager.getWhiteColor());
        setHeaderVisible(false);
        editor.clearBorderListener();
        editor.setBorder(UIResourceManager.getBorder("ScrollTextBorder"));
        editor.setVisibleInsets(0, 0, 0, 0);
        editor.setAlpha(0.0F);
        this.normalBorder = new ImageBorder(UIResourceManager.getImageByName("border_normal.png"), 2, 2, 2, 2);
        this.rolloverBorder = UIResourceManager.getBorder("CompoundTextRolloverBorder");
        this.nonEditableBorder = UIResourceManager.getBorder("CompoundTextNonEditableBorder");
        this.nonEditableRolloverBorder = UIResourceManager.getBorder("CompoundTextNonEditableRolloverBorder");
        this.disabledBorder = UIResourceManager.getBorder("CompoundTextDisabledBorder");
        installListener();
        setBorder(this.normalBorder);
    }

    public JCTextArea getEditor() {
        return this.editor;
    }

    private void installListener() {
        this.listener = new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                JCScrollText.this.mouseIn();
            }

            public void mouseExited(MouseEvent e) {
                JCScrollText.this.mouseOut();
            }
        };
        addMouseListener(this.listener);
        this.editor.addMouseListener(this.listener);
        this.viewport.addMouseListener(this.listener);
        this.horizontalScrollBar.addMouseListener(this.listener);
        this.verticalScrollBar.addMouseListener(this.listener);

        for (Component c : this.horizontalScrollBar.getComponents()) {
            c.addMouseListener(this.listener);
        }

        for (Component c : this.verticalScrollBar.getComponents()) {
            c.addMouseListener(this.listener);
        }
    }

    public boolean isEditable() {
        return this.editor.isEditable();
    }

    private void mouseIn() {
        if ((this.normalBorder != null) && (this.editor.isEnabled())) {
            setBorder(this.editor.isEditable() ? this.rolloverBorder : this.nonEditableRolloverBorder);
        }
    }

    private void mouseOut() {
        if ((this.normalBorder != null) && (this.editor.isEnabled())) {
            setBorder(this.editor.isEditable() ? this.normalBorder : this.nonEditableBorder);
        }
    }

    public void setBackground(Color bg) {
        this.background = bg;
        super.setBackground(bg);
    }

    public void setEditable(boolean editable) {
        this.editor.setEditable(editable);
        mouseOut();

        if (isEnabled()) {
            super.setBackground(editable ? this.background : NON_EDITABLE_BG);
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        super.setBackground(enabled ? NON_EDITABLE_BG : isEditable() ? this.background : DISABLED_BG);

        if (enabled) {
            mouseOut();
        } else if (this.normalBorder != null) {
            setBorder(this.disabledBorder);
        }
    }
}