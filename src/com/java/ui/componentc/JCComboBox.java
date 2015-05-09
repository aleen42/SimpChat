package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import com.java.ui.util.Util;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

public class JCComboBox extends JComboBox {
    private static final long serialVersionUID = 2902489693121071103L;
    private float alpha;
    private boolean borderChange;
    private Border disabledBorder;
    private Color disabledTextColor;
    private boolean editableAll;
    private Image image;
    private boolean imageOnly;
    private MouseListener listener;
    private Border normalBorder;
    private Border rolloverBorder;
    private CComboBoxUI ui;
    private Insets visibleInsets;

    public JCComboBox() {
        init();
    }

    public JCComboBox(ComboBoxModel model) {
        super(model);
        init();
    }

    public JCComboBox(Object[] items) {
        super(items);
        init();
    }

    public JCComboBox(Vector<?> items) {
        super(items);
        init();
    }

    public float getAlpha() {
        return this.alpha;
    }

    public Color getDisabledTextColor() {
        return this.disabledTextColor;
    }

    public Image getImage() {
        return this.image;
    }

    public Insets getVisibleInsets() {
        return this.visibleInsets;
    }

    private void init() {
        this.alpha = 1.0F;
        this.visibleInsets = new Insets(1, 1, 1, 1);
        this.borderChange = true;
        this.editableAll = true;
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
                JCComboBox.this.mouseIn();
            }

            public void mouseExited(MouseEvent e) {
                JCComboBox.this.mouseOut();
            }
        };
        setUI(new CComboBoxUI());
        super.setBorder(this.normalBorder);
        super.setOpaque(false);
        setFont(UIUtil.getDefaultFont());
        setBackground(UIResourceManager.getWhiteColor());
        setForeground(Color.BLACK);
        setRenderer(new CComboBoxRenderer(this));
    }

    public boolean isEditableAll() {
        return this.editableAll;
    }

    public boolean isImageOnly() {
        return this.imageOnly;
    }

    protected void mouseIn() {
        if (isEnabled()) {
            if (this.normalBorder != null) {
                super.setBorder(this.rolloverBorder);
            }

            if (this.ui != null) {
                this.ui.changeButtonBorder(true);
            }
        }
    }

    protected void mouseOut() {
        if ((isEnabled()) && (!isPopupVisible())) {
            if (this.normalBorder != null) {
                super.setBorder(this.normalBorder);
            }

            if (this.ui != null) {
                this.ui.changeButtonBorder(false);
            }
        }
    }

    public void resetBorder() {
        if ((!isShowing())
                || (!new Rectangle(getLocationOnScreen(), getSize())
                .contains(Util.getMouseLocation()))) {
            super.setBorder(this.normalBorder);

            if (this.ui != null) {
                this.ui.changeButtonBorder(false);
            }
        }
    }

    protected void resetShortcutKeys() {
        InputMap inputMap = getInputMap(1);

        for (KeyStroke ks : inputMap.allKeys()) {
            Object value = inputMap.get(ks);

            if (value.equals("selectNext")) {
                inputMap.put(ks, "selectNext2");
            } else {
                if (!value.equals("selectPrevious"))
                    continue;
                inputMap.put(ks, "selectPrevious2");
            }
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
        ComboBoxEditor editor = getEditor();
        Component field;
        if ((editor != null)
                && (((field = editor.getEditorComponent()) instanceof JTextComponent))) {
            ((JTextComponent) field).setDisabledTextColor(disabledTextColor);
        }

        if (!isEnabled()) {
            repaint();
        }
    }

    public void setEditable(boolean editable) {
        super.setEditable((editable) && (isEditableAll()));
    }

    public void setEditableAll(boolean editableAll) {
        this.editableAll = editableAll;
        this.ui.getArrowButton().setEnabled((editableAll) && (isEnabled()));
        repaint();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        this.ui.getArrowButton().setEnabled((enabled) && (isEditableAll()));

        if (this.borderChange) {
            if (enabled) {
                mouseOut();
            } else if (this.normalBorder != null) {
                super.setBorder(this.disabledBorder);
            }
        }
    }

    public void setForeground(Color color) {
        super.setForeground(color);

        if (getEditor() != null) {
            getEditor().getEditorComponent().setForeground(color);
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

    public void setUI(ComboBoxUI ui) {
        removeMouseListener(this.listener);
        ComboBoxEditor editor = getEditor();

        if (editor != null) {
            editor.getEditorComponent().removeMouseListener(this.listener);
        }

        if ((this.renderer != null) && ((this.renderer instanceof Component))) {
            ((Component) this.renderer).removeMouseListener(this.listener);
        }

        for (Component c : getComponents()) {
            c.removeMouseListener(this.listener);
        }

        if ((ui instanceof CComboBoxUI)) {
            this.ui = ((CComboBoxUI) ui);
        }

        super.setUI(ui);
        putClientProperty("JComboBox.isTableCellEditor", Boolean.valueOf(false));
        resetShortcutKeys();
        addMouseListener(this.listener);
        getEditor().getEditorComponent().addMouseListener(this.listener);

        if ((this.renderer instanceof Component)) {
            ((Component) this.renderer).addMouseListener(this.listener);
        }

        for (Component c : getComponents()) {
            c.addMouseListener(this.listener);
        }
    }

    public void setVisibleInsets(int top, int left, int bottom, int right) {
        this.visibleInsets.set(top, left, bottom, right);
        repaint();
    }

    @Deprecated
    public void updateUI() {
        ComboPopup popup;
        if ((this.ui != null) && ((popup = this.ui.getPopup()) != null)
                && ((popup instanceof Component))) {
            SwingUtilities.updateComponentTreeUI((Component) popup);
        }
    }
}