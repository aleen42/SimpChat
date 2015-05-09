package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JCToggleButton extends JToggleButton {
    private static final long serialVersionUID = -393947872129394154L;
    private float alpha;
    private Image disabledImage;
    private Image disabledSelectedImage;
    private Color disabledTextColor;
    private Image image;
    private Insets imageInsets;
    private boolean imageOnly;
    private boolean paintPressDown;
    private Image pressedImage;
    private Image rolloverImage;
    private Image rolloverSelectedImage;
    private Image selectedImage;

    public JCToggleButton() {
        this(null, null, false);
    }

    public JCToggleButton(Action a) {
        this();
        setAction(a);
    }

    public JCToggleButton(Icon icon) {
        this(null, icon, false);
    }

    public JCToggleButton(Icon icon, boolean selected) {
        this(null, icon, selected);
    }

    public JCToggleButton(String text) {
        this(text, null, false);
    }

    public JCToggleButton(String text, boolean selected) {
        this(text, null, selected);
    }

    public JCToggleButton(String text, Icon icon) {
        this(text, icon, false);
    }

    public JCToggleButton(String text, Icon icon, boolean selected) {
        super(text, icon, selected);
        setUI(new CToggleButtonUI());
        this.alpha = 1.0F;
        this.imageOnly = true;
        this.paintPressDown = true;
        this.imageInsets = new Insets(3, 3, 3, 3);
        this.disabledTextColor = new Color(103, 117, 127);
        setForeground(new Color(0, 28, 48));
        setBackground(Color.GRAY);
        setBorder(new EmptyBorder(0, 0, 0, 0));
        setContentAreaFilled(false);
        setFont(UIUtil.getDefaultFont());
        setFocusable(false);
        setRolloverEnabled(true);
        setIconTextGap(5);
        setMargin(new Insets(0, 0, 0, 0));
        super.setOpaque(false);
        this.image = UIResourceManager
                .getImageByName("button_normal.png", true);
        this.disabledImage = UIResourceManager.getImageByName(
                "button_disabled.png", true);
        this.disabledSelectedImage = UIResourceManager.getImageByName(
                "togglebutton_selected.png", true);
        this.selectedImage = UIResourceManager.getImageByName(
                "togglebutton_selected.png", true);
        this.rolloverImage = UIResourceManager.getImageByName(
                "button_rollover.png", true);
    }

    public float getAlpha() {
        return this.alpha;
    }

    public Image getDisabledImage() {
        return this.disabledImage;
    }

    public Image getDisabledSelectedImage() {
        return this.disabledSelectedImage;
    }

    public Color getDisabledTextColor() {
        return this.disabledTextColor;
    }

    public Image getImage() {
        return this.image;
    }

    public Insets getImageInsets() {
        return this.imageInsets;
    }

    public Image getPressedImage() {
        return this.pressedImage;
    }

    public Image getRolloverImage() {
        return this.rolloverImage;
    }

    public Image getRolloverSelectedImage() {
        return this.rolloverSelectedImage;
    }

    public Image getSelectedImage() {
        return this.selectedImage;
    }

    public boolean isImageOnly() {
        return this.imageOnly;
    }

    public boolean isPaintPressDown() {
        return this.paintPressDown;
    }

    public void setAlpha(float alpha) {
        if ((alpha >= 0.0F) && (alpha <= 1.0F)) {
            this.alpha = alpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid alpha:" + alpha);
        }
    }

    public void setDisabledImage(Image disabledImage) {
        this.disabledImage = disabledImage;
        repaint();
    }

    public void setDisabledSelectedImage(Image disabledSelectedImage) {
        this.disabledSelectedImage = disabledSelectedImage;
        repaint();
    }

    public void setDisabledTextColor(Color disabledTextColor) {
        this.disabledTextColor = disabledTextColor;

        if (!isEnabled()) {
            repaint();
        }
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setImageInsets(int top, int left, int bottom, int right) {
        this.imageInsets.set(top, left, bottom, right);
        repaint();
    }

    public void setImageOnly(boolean imageOnly) {
        this.imageOnly = imageOnly;
        repaint();
    }

    @Deprecated
    public void setOpaque(boolean opaque) {
    }

    public void setPaintPressDown(boolean paintPressDown) {
        this.paintPressDown = paintPressDown;
    }

    public void setPressedImage(Image pressedImage) {
        this.pressedImage = pressedImage;
        repaint();
    }

    public void setRolloverImage(Image rolloverImage) {
        this.rolloverImage = rolloverImage;
        repaint();
    }

    public void setRolloverSelectedImage(Image rolloverSelectedImage) {
        this.rolloverSelectedImage = rolloverSelectedImage;
        repaint();
    }

    public void setSelectedImage(Image selectedImage) {
        this.selectedImage = selectedImage;
        repaint();
    }

    @Deprecated
    public void updateUI() {
    }
}