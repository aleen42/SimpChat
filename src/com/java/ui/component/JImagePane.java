package com.java.ui.component;

import com.java.ui.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class JImagePane extends JPanel {
    public static final String CENTER = "Center";
    public static final String SCALED = "Scaled";
    private static final long serialVersionUID = -6914280571042537095L;
    public static final String TILED = "Tiled";
    private float alpha;
    private double angle;
    private int[] cornersSize;
    private BufferedImage currentImage;
    private BufferedImage displayImage;
    private boolean filledAll;
    private boolean filledBorderArea;
    private Color gridColor;
    private Image image;
    private boolean imageOnly;
    private boolean keepAspectRatio;
    private String mode;
    private boolean showGrid;

    public JImagePane() {
        this(null, "Center");
    }

    public JImagePane(Image image, String mode) {
        this.image = image;
        this.mode = mode;
        this.alpha = 1.0F;
        this.gridColor = Color.BLACK;
        this.cornersSize = new int[4];
        super.setOpaque(false);
    }

    private BufferedImage createImage() {
        if (this.image != null) {
            Dimension size = getImagePreferredSize();
            BufferedImage bufferedImage = UIUtil.getGraphicsConfiguration(this)
                    .createCompatibleImage(size.width, size.height, 3);
            Graphics2D imageG2d = (Graphics2D) bufferedImage.getGraphics();
            AffineTransform trans = imageG2d.getTransform();
            Object oldHint = imageG2d
                    .getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            Composite oldComposite = imageG2d.getComposite();
            double theta = Math.toRadians(this.angle % 360.0D);
            double anchorx = size.width / 2.0D;
            double anchory = size.height / 2.0D;
            int x = (size.width - this.image.getWidth(this)) / 2;
            int y = (size.height - this.image.getHeight(this)) / 2;

            imageG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            imageG2d.setComposite(AlphaComposite.SrcOver.derive(this.alpha));
            trans.rotate(theta, anchorx, anchory);
            imageG2d.setTransform(trans);
            imageG2d.drawImage(this.image, x, y, this);
            trans.rotate(-theta, anchorx, anchory);
            imageG2d.setTransform(trans);
            imageG2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHint);
            imageG2d.setComposite(oldComposite);
            imageG2d.dispose();
            return bufferedImage;
        }

        return null;
    }

    public float getAlpha() {
        return this.alpha;
    }

    public double getAngle() {
        return this.angle;
    }

    public int getCornerSizeAt(int cornerIndex) {
        if ((cornerIndex >= 1) && (cornerIndex <= 4)) {
            return this.cornersSize[(cornerIndex - 1)];
        }

        return -1;
    }

    public BufferedImage getCurrentImage(boolean allArea) {
        return allArea ? this.displayImage : this.currentImage;
    }

    public Color getGridColor() {
        return this.gridColor;
    }

    public Image getImage() {
        return this.image;
    }

    public Dimension getImagePreferredSize() {
        Dimension size = new Dimension();

        if (this.image != null) {
            double theta = Math.toRadians(this.angle % 360.0D);
            int imageWidth = this.image.getWidth(this);
            int imageHeight = this.image.getHeight(this);
            double frameWidth = Math.abs(Math.cos(theta) * imageWidth)
                    + Math.abs(Math.sin(theta) * imageHeight);
            double frameHeight = Math.abs(Math.sin(theta) * imageWidth)
                    + Math.abs(Math.cos(theta) * imageHeight);
            size.setSize(frameWidth, frameHeight);
        }

        return size;
    }

    public String getMode() {
        return this.mode;
    }

    public boolean isFilledAll() {
        return this.filledAll;
    }

    public boolean isFilledBorderArea() {
        return this.filledBorderArea;
    }

    public boolean isImageOnly() {
        return this.imageOnly;
    }

    public boolean isKeepAspectRatio() {
        return this.keepAspectRatio;
    }

    public boolean isShowGrid() {
        return this.showGrid;
    }

    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        if (!this.imageOnly) {
            Object oldHints = g2d
                    .getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getBackground());
            g2d.fillPolygon(UIUtil.createRoundRect(0, 0, getWidth(),
                    getHeight(), this.cornersSize[0], this.cornersSize[1],
                    this.cornersSize[2], this.cornersSize[3]));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHints);
        }

        this.currentImage = createImage();
        Insets insets = this.filledBorderArea ? new Insets(0, 0, 0, 0)
                : getInsets();
        int width = getWidth() - insets.left - insets.right;
        int height = getHeight() - insets.top - insets.bottom;

        if ((this.currentImage != null) && (width > 0) && (height > 0)) {
            this.displayImage = UIUtil.getGraphicsConfiguration(this)
                    .createCompatibleImage(width, height, 3);
            Graphics2D areaG2d = (Graphics2D) this.displayImage.getGraphics();
            int imageWidth = this.currentImage.getWidth(this);
            int imageHeight = this.currentImage.getHeight(this);
            Color color = new Color(this.gridColor.getRed(),
                    this.gridColor.getGreen(), this.gridColor.getBlue(),
                    Math.round(this.alpha * 255.0F));

            if (this.mode.equalsIgnoreCase("Center")) {
                int x = (width - imageWidth) / 2;
                int y = (height - imageHeight) / 2;
                areaG2d.drawImage(this.currentImage, x, y, this);

                if (this.showGrid) {
                    areaG2d.setColor(color);
                    areaG2d.drawRect(x, y, imageWidth - 1, imageHeight - 1);
                }

            } else if (this.mode.equalsIgnoreCase("Tiled")) {
                for (int ix = 0; ix < width; ix += imageWidth) {
                    for (int iy = 0; iy < height; iy += imageHeight) {
                        areaG2d.drawImage(this.currentImage, ix, iy, this);
                    }
                }

                if (this.showGrid) {
                    areaG2d.setColor(color);
                    int ix = 0;
                    int iy = 0;

                    for (ix = 0; ix < width; ix += imageWidth) {
                        areaG2d.drawLine(ix, 0, ix, height - 1);
                    }

                    for (iy = 0; iy < height; iy += imageHeight) {
                        areaG2d.drawLine(0, iy, width - 1, iy);
                    }

                    if (width % imageWidth == 0) {
                        areaG2d.drawLine(width - 1, 0, width - 1, height - 1);
                    }

                    if (height % imageHeight == 0) {
                        areaG2d.drawLine(0, height - 1, width - 1, height - 1);
                    }
                }

            } else if (this.mode.equalsIgnoreCase("Scaled")) {
                int x = 0;
                int y = 0;

                if (this.keepAspectRatio) {
                    float widthRatio = width / imageWidth;
                    float heightRatio = height / imageHeight;

                    if (this.filledAll) {
                        float ratio = Math.max(widthRatio, heightRatio);
                        imageWidth = (int) (imageWidth * ratio);
                        imageHeight = (int) (imageHeight * ratio);
                    } else {
                        float ratio = Math.min(widthRatio, heightRatio);
                        imageWidth = (int) (imageWidth * ratio);
                        imageHeight = (int) (imageHeight * ratio);
                        x = (width - imageWidth) / 2;
                        y = (height - imageHeight) / 2;
                    }
                } else {
                    imageWidth = width;
                    imageHeight = height;
                }

                areaG2d.drawImage(this.currentImage, x, y, imageWidth,
                        imageHeight, this);

                if (this.showGrid) {
                    areaG2d.setColor(color);
                    areaG2d.drawRect(x, y, imageWidth - 1, imageHeight - 1);
                }
            }

            areaG2d.dispose();
            Paint oldPaint = g2d.getPaint();
            Object oldHints = g2d
                    .getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setPaint(UIUtil.createTexturePaint(this.displayImage, this));
            g2d.translate(insets.left, insets.top);
            g2d.fillPolygon(UIUtil.createRoundRect(0, 0,
                    this.displayImage.getWidth(),
                    this.displayImage.getHeight(), this.cornersSize[0],
                    this.cornersSize[1], this.cornersSize[2],
                    this.cornersSize[3]));
            g2d.translate(-insets.left, -insets.top);
            g2d.setPaint(oldPaint);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldHints);
        } else {
            this.displayImage = null;
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

    public void setAngle(double angle) {
        this.angle = angle;
        repaint();
    }

    public void setCornerSizeAt(int cornerIndex, int size) {
        if ((cornerIndex >= 1) && (cornerIndex <= 4)) {
            this.cornersSize[(cornerIndex - 1)] = size;
            repaint();
        }
    }

    public void setFilledAll(boolean filledAll) {
        this.filledAll = filledAll;
        repaint();
    }

    public void setFilledBorderArea(boolean filledBorderArea) {
        this.filledBorderArea = filledBorderArea;
        repaint();
    }

    public void setGridColor(Color gridColor) {
        this.gridColor = (gridColor == null ? Color.BLACK : gridColor);
        repaint();
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

    public void setImageOnly(boolean imageOnly) {
        this.imageOnly = imageOnly;
        repaint();
    }

    public void setKeepAspectRatio(boolean keepAspectRatio) {
        this.keepAspectRatio = keepAspectRatio;
        repaint();
    }

    public void setMode(String mode) {
        this.mode = mode;
        repaint();
    }

    @Deprecated
    public void setOpaque(boolean isOpaque) {
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
        repaint();
    }
}