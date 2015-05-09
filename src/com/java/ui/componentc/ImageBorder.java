package com.java.ui.componentc;

import javax.swing.border.AbstractBorder;
import java.awt.*;

public class ImageBorder extends AbstractBorder {
    private static final long serialVersionUID = 2081328874316180356L;
    private int bottom;
    private Image image;
    private int left;
    private int right;
    private int top;

    public ImageBorder(Image image) {
        this(image, 5, 5, 5, 5);
    }

    public ImageBorder(Image image, int top, int left, int bottom, int right) {
        this.image = image;
        this.top = top;
        this.left = left;
        this.bottom = bottom;
        this.right = right;
    }

    public Insets getBorderInsets() {
        return new Insets(this.top, this.left, this.bottom, this.right);
    }

    public Insets getBorderInsets(Component c) {
        return getBorderInsets();
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = this.left;
        insets.top = this.top;
        insets.right = this.right;
        insets.bottom = this.bottom;
        return insets;
    }

    public boolean isBorderOpaque() {
        return false;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (this.image == null) {
            return;
        }

        int imgWidth = this.image.getWidth(c);
        int imgHeight = this.image.getHeight(c);
        g.drawImage(this.image, x, y, x + this.left, y + this.top, 0, 0, this.left, this.top, c);
        g.drawImage(this.image, this.left + x, y, x + width - this.right, y + this.top, this.left, 0, imgWidth - this.right, this.top, c);
        g.drawImage(this.image, x + width - this.right, y, x + width, y + this.top, imgWidth - this.right, 0, imgWidth, this.top, c);
        g.drawImage(this.image, x, y + this.top, x + this.left, y + height - this.bottom, 0, this.top, this.left, imgHeight - this.bottom, c);
        g.drawImage(this.image, x + width - this.right, this.top + y, x + width, y + height - this.bottom, imgWidth - this.right, this.top, imgWidth,
                imgHeight - this.bottom, c);
        g.drawImage(this.image, x, y + height - this.bottom, this.left + x, y + height, 0, imgHeight - this.bottom, this.left, imgHeight, c);
        g.drawImage(this.image, this.left + x, y + height - this.bottom, x + width - this.right, y + height, this.left, imgHeight - this.bottom,
                imgWidth - this.right, imgHeight, c);
        g.drawImage(this.image, x + width - this.right, y + height - this.bottom, x + width, y + height, imgWidth - this.right, imgHeight -
                this.bottom, imgWidth, imgHeight, c);
    }
}