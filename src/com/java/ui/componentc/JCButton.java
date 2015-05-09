package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JCButton extends JButton {
	private static final long serialVersionUID = -558139912606968740L;
	private float alpha;
	private Image disabledImage;
	private Color disabledTextColor;
	private Image focusImage;
	private Insets focusImageInsets;
	private Image image;
	private Insets imageInsets;
	private boolean imageOnly;
	private boolean paintPressDown;
	private Image pressedImage;
	private Image rolloverImage;

	public JCButton() {
		this(null, null);
	}

	public JCButton(Action action) {
		this(null, null);
		setAction(action);
	}

	public JCButton(Icon icon) {
		this(null, icon);
	}

	public JCButton(String text) {
		this(text, null);
	}

	public JCButton(String text, Icon icon) {
		super(text, icon);
		setUI(new CButtonUI());
		this.alpha = 1.0F;
		this.imageOnly = true;
		this.paintPressDown = true;
		this.imageInsets = new Insets(3, 3, 3, 3);
		this.focusImageInsets = new Insets(4, 4, 4, 4);
		this.disabledTextColor = new Color(103, 117, 127);
		setForeground(new Color(0, 28, 48));
		setBackground(Color.GRAY);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentAreaFilled(false);
		setFont(UIUtil.getDefaultFont());
		setRolloverEnabled(true);
		setIconTextGap(5);
		setMargin(new Insets(0, 0, 0, 0));
		super.setOpaque(false);
		this.image = UIResourceManager
				.getImageByName("button_normal.png", true);
		this.disabledImage = UIResourceManager.getImageByName(
				"button_disabled.png", true);
		this.pressedImage = UIResourceManager.getImageByName(
				"button_pressed.png", true);
		this.rolloverImage = UIResourceManager.getImageByName(
				"button_rollover.png", true);
		this.focusImage = UIResourceManager.getImageByName("button_foucs.png",
				true);
	}

	public float getAlpha() {
		return this.alpha;
	}

	public Image getDisabledImage() {
		return this.disabledImage;
	}

	public Color getDisabledTextColor() {
		return this.disabledTextColor;
	}

	public Image getFocusImage() {
		return this.focusImage;
	}

	public Insets getFocusImageInsets() {
		return this.focusImageInsets;
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

	public void setDisabledTextColor(Color disabledTextColor) {
		this.disabledTextColor = disabledTextColor;

		if (!isEnabled()) {
			repaint();
		}
	}

	public void setFocusImage(Image focusImage) {
		this.focusImage = focusImage;
		repaint();
	}

	public void setFocusImageInsets(int top, int left, int bottom, int right) {
		this.focusImageInsets.set(top, left, bottom, right);
		repaint();
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

	@Deprecated
	public void updateUI() {
	}
}