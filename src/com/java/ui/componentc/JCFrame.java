package com.java.ui.componentc;

import com.java.ui.component.JImagePane;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class JCFrame extends JFrame {
    private static final long serialVersionUID = 854652168416021730L;
    private Image backgroundImage;
    private boolean borderPainted;
    private float imageAlpha;
    private CRootPaneUI.ImageDisplayMode imageDisplayMode;
    private boolean isMaximizedBoundsSet;
    private boolean titleOpaque;
    private CRootPaneUI ui;

    public JCFrame() throws HeadlessException {
        init();
    }

    public JCFrame(GraphicsConfiguration gc) {
        super(gc);
        init();
    }

    public JCFrame(String title) throws HeadlessException {
        super(title);
        init();
    }

    public JCFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
        init();
    }

    public void copyBackgroundImage(Container c) {
        setBackgroundImage(UIUtil.getBackgroundImageFromContainer(c),
                UIUtil.getEdgeBlurImageFromContainer(c));
        setImageDisplayMode(UIUtil.getImageDisplayModeFromContainer(c));
        setImageAlpha(UIUtil.getImageAlphaFromContainer(c));
        repaint();
    }

    protected JRootPane createRootPane() {
        JRootPane rp = new JRootPane() {
            private static final long serialVersionUID = 6817397706458749155L;

            @Deprecated
            public void updateUI() {
            }
        };
        rp.setOpaque(true);
        return rp;
    }

    public Image getBackgroundImage() {
        return this.backgroundImage;
    }

    public BufferedImage getEdgeBlurImage() {
        return this.ui.getEdgeBlurImage();
    }

    public synchronized int getExtendedState() {
        int state = super.getExtendedState();
        Window fullWin = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getFullScreenWindow();
        return this == fullWin ? state | 0x6 : state;
    }

    public float getImageAlpha() {
        return this.imageAlpha;
    }

    public CRootPaneUI.ImageDisplayMode getImageDisplayMode() {
        return this.imageDisplayMode;
    }

    public JImagePane getTitleContentPane() {
        return this.ui.getTitleContentPane();
    }

    private void init() {
        this.borderPainted = true;
        this.imageDisplayMode = CRootPaneUI.ImageDisplayMode.TILED;
        this.imageAlpha = 1.0F;
        JRootPane root = getRootPane();
        Container contentPane = getContentPane();
        setFont(UIUtil.getDefaultFont());
        setUndecorated(true);
        root.setUI(this.ui = new CRootPaneUI());
        root.setForeground(Color.BLACK);
        root.setBackground(new Color(233, 242, 249));
        root.setWindowDecorationStyle(1);
        setBackground(root.getBackground());

        if ((contentPane instanceof JComponent)) {
            ((JComponent) contentPane).setOpaque(false);
        }
    }

    public boolean isBorderPainted() {
        return this.borderPainted;
    }

    public boolean isTitleOpaque() {
        return this.titleOpaque;
    }

    /**
     * 设置背景图片
     *
     * @param backgroundImage
     * @param edgeBlurImage
     */
    public void setBackgroundImage(Image backgroundImage) {
        setBackgroundImage(backgroundImage, null);
    }

    /**
     * 设置背景图片
     *
     * @param backgroundImage
     * @param edgeBlurImage
     */
    public void setBackgroundImage(Image backgroundImage,
                                   BufferedImage edgeBlurImage) {
        if (this.backgroundImage != backgroundImage) {
            this.backgroundImage = backgroundImage;
            this.ui.removeBlurImage();
            this.ui.setEdgeBlurImage(edgeBlurImage);
            getRootPane().repaint();
        }
    }

    /**
     * 是否绘制边框
     *
     * @param borderPainted
     */
    public void setBorderPainted(boolean borderPainted) {
        if (this.borderPainted != borderPainted) {
            this.borderPainted = borderPainted;
            getRootPane().repaint();
        }
    }

    public synchronized void setExtendedState(int state) {
        if (((state & 0x6) != 0) && (!this.isMaximizedBoundsSet)) {
            Rectangle bounds = getGraphicsConfiguration().getBounds();
            Rectangle maxBounds = null;

            if ((bounds.x == 0) && (bounds.y == 0)) {
                Insets screenInsets = getToolkit().getScreenInsets(
                        getGraphicsConfiguration());
                maxBounds = new Rectangle(screenInsets.left, screenInsets.top,
                        bounds.width - screenInsets.right - screenInsets.left,
                        bounds.height - screenInsets.bottom - screenInsets.top);
            }

            super.setMaximizedBounds(maxBounds);
        }

        super.setExtendedState(state);
    }

    /**
     * 设置透明度
     *
     * @param imageAlpha
     */
    public void setImageAlpha(float imageAlpha) {
        if ((imageAlpha >= 0.0F) && (imageAlpha <= 1.0F)) {
            this.imageAlpha = imageAlpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid alpha:" + imageAlpha);
        }
    }

    /**
     * 设置图形显示模式
     *
     * @param imageDisplayMode
     */
    public void setImageDisplayMode(
            CRootPaneUI.ImageDisplayMode imageDisplayMode) {
        if (this.imageDisplayMode != imageDisplayMode) {
            this.imageDisplayMode = imageDisplayMode;
            getRootPane().repaint();
        }
    }

    /**
     * 设置最大化尺寸
     */
    public synchronized void setMaximizedBounds(Rectangle bounds) {
        this.isMaximizedBoundsSet = (bounds != null);
        super.setMaximizedBounds(bounds);
    }

    /**
     * 设置透明度
     *
     * @param titleOpaque
     */
    public void setTitleOpaque(boolean titleOpaque) {
        if (this.titleOpaque != titleOpaque) {
            this.titleOpaque = titleOpaque;
            getRootPane().repaint();
        }
    }

    /**
     * 中心显示
     */
    public void centerWindow() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width / 2 - this.getWidth() / 2;
        int y = screenSize.height / 2 - this.getHeight() / 2;
        this.setLocation(x, y);
    }
}