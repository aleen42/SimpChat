package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CScrollBarUI extends BasicScrollBarUI implements MouseListener {
    private static final Color BACKGROUND = UIResourceManager
            .getColor("ScrollBarBackground");

    private static final Color BORDER_COLOR = UIResourceManager
            .getColor("ScrollBarBorderColor");

    public static ComponentUI createUI(JComponent c) {
        return new CScrollBarUI();
    }

    protected JCButton decreaseButton;
    protected JCButton increaseButton;

    private boolean pressed;

    protected JButton createDecreaseButton(int orientation) {
        if (this.decreaseButton == null) {
            this.decreaseButton = new JCButton();
            this.decreaseButton.setFocusable(false);
            this.decreaseButton.setRequestFocusEnabled(false);

            if (this.scrollbar.getOrientation() == 1) {
                this.decreaseButton.setImage(UIResourceManager
                        .getImage("ScrollBarUpImage"));
                this.decreaseButton.setRolloverImage(UIResourceManager
                        .getImage("ScrollBarUpRolloverImage"));
                this.decreaseButton.setPressedImage(UIResourceManager
                        .getImage("ScrollBarUpPressedImage"));
            } else {
                this.decreaseButton.setImage(UIResourceManager
                        .getImage("ScrollBarLeftImage"));
                this.decreaseButton.setRolloverImage(UIResourceManager
                        .getImage("ScrollBarLeftRolloverImage"));
                this.decreaseButton.setPressedImage(UIResourceManager
                        .getImage("ScrollBarLeftPressedImage"));
            }

            Image image = this.decreaseButton.getImage();
            this.decreaseButton.setPreferredSize(new Dimension(image
                    .getWidth(null), image.getHeight(null)));
        }

        return this.decreaseButton;
    }

    protected JButton createIncreaseButton(int orientation) {
        if (this.increaseButton == null) {
            this.increaseButton = new JCButton();
            this.increaseButton.setFocusable(false);
            this.increaseButton.setRequestFocusEnabled(false);

            if (this.scrollbar.getOrientation() == 1) {
                this.increaseButton.setImage(UIResourceManager
                        .getImage("ScrollBarDownImage"));
                this.increaseButton.setRolloverImage(UIResourceManager
                        .getImage("ScrollBarDownRolloverImage"));
                this.increaseButton.setPressedImage(UIResourceManager
                        .getImage("ScrollBarDownPressedImage"));
            } else {
                this.increaseButton.setImage(UIResourceManager
                        .getImage("ScrollBarRightImage"));
                this.increaseButton.setRolloverImage(UIResourceManager
                        .getImage("ScrollBarRightRolloverImage"));
                this.increaseButton.setPressedImage(UIResourceManager
                        .getImage("ScrollBarRightPressedImage"));
            }

            Image image = this.increaseButton.getImage();
            this.increaseButton.setPreferredSize(new Dimension(image
                    .getWidth(null), image.getHeight(null)));
        }

        return this.increaseButton;
    }

    public Dimension getPreferredSize(JComponent c) {
        if (this.scrollbar.getOrientation() == 1) {
            return new Dimension(14, 52);
        }

        return new Dimension(52, 14);
    }

    public boolean getSupportsAbsolutePositioning() {
        return true;
    }

    private Image getThumbImage() {
        Image image = null;
        boolean vertical = this.scrollbar.getOrientation() == 1;

        if (this.pressed) {
            image = UIResourceManager
                    .getImage(vertical ? "ScrollBarVPressedImage"
                            : "ScrollBarHPressedImage");
        } else if (isThumbRollover()) {
            image = UIResourceManager
                    .getImage(vertical ? "ScrollBarVRolloverImage"
                            : "ScrollBarHRolloverImage");
        } else {
            image = UIResourceManager.getImage(vertical ? "ScrollBarVImage"
                    : "ScrollBarHImage");
        }

        return image;
    }

    protected void installDefaults() {
        this.minimumThumbSize = new Dimension(14, 14);
        this.maximumThumbSize = new Dimension(4096, 4096);
        this.trackHighlight = 0;

        if ((this.scrollbar.getLayout() == null)
                || ((this.scrollbar.getLayout() instanceof UIResource))) {
            this.scrollbar.setLayout(this);
        }
    }

    protected void installListeners() {
        super.installListeners();
        this.scrollbar.addMouseListener(this);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        boolean flag = (SwingUtilities.isRightMouseButton(e))
                || ((!getSupportsAbsolutePositioning()) && (SwingUtilities
                .isMiddleMouseButton(e)));

        if ((!flag) && (getThumbBounds().contains(e.getPoint()))) {
            boolean oldPress = this.pressed;
            this.pressed = true;

            if (oldPress != this.pressed) {
                this.scrollbar.repaint();
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        this.pressed = false;
    }

    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        if ((thumbBounds.isEmpty()) || (!this.scrollbar.isEnabled())) {
            return;
        }

        Image image = getThumbImage();
        int width = thumbBounds.width;
        int height = thumbBounds.height;
        int imageWidth = image.getWidth(this.scrollbar);
        int imageHeight = image.getHeight(this.scrollbar);
        g.translate(thumbBounds.x, thumbBounds.y);

        if (this.scrollbar.getOrientation() == 1) {
            g.drawImage(image, 0, 0, width, 3, 0, 0, imageWidth, 3, null);
            g.drawImage(image, 0, 3, width, height - 3, 0, 3, imageWidth,
                    imageHeight - 3, null);
            g.drawImage(image, 0, height - 3, width, height, 0,
                    imageHeight - 3, imageWidth, imageHeight, null);
        } else {
            g.drawImage(image, 0, 0, 3, height, 0, 0, 3, imageHeight, null);
            g.drawImage(image, 3, 0, width - 3, height, 3, 0, imageWidth - 3,
                    imageHeight, null);
            g.drawImage(image, width - 3, 0, width, height, imageWidth - 3, 0,
                    imageWidth, imageHeight, null);
        }

        g.translate(-thumbBounds.x, -thumbBounds.y);
    }

    protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        if (this.scrollbar.getOrientation() == 1) {
            g.setColor(BORDER_COLOR);
            g.drawRect(trackBounds.x, trackBounds.y, trackBounds.width - 1,
                    trackBounds.height - 1);
            g.setColor(BACKGROUND);
            g.fillRect(trackBounds.x + 1, trackBounds.y, trackBounds.width - 2,
                    trackBounds.height);
        } else {
            g.setColor(BORDER_COLOR);
            g.drawRect(trackBounds.x, trackBounds.y, trackBounds.width - 1,
                    trackBounds.height - 1);
            g.setColor(BACKGROUND);
            g.fillRect(trackBounds.x, trackBounds.y + 1, trackBounds.width,
                    trackBounds.height - 2);
        }
    }

    protected void uninstallDefaults() {
    }

    protected void uninstallListeners() {
        super.uninstallListeners();
        this.scrollbar.removeMouseListener(this);
    }
}