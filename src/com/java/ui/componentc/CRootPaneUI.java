package com.java.ui.componentc;

import com.java.ui.component.JImagePane;
import com.java.ui.util.StackBlurFilter;
import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import com.sun.awt.AWTUtilities;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicRootPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;

public class CRootPaneUI extends BasicRootPaneUI {
    private class ComponentHandler extends ComponentAdapter {
        private ComponentHandler() {
        }

        public void componentResized(ComponentEvent e) {
            Window win = (Window) e.getSource();
            Frame frame = (win instanceof Frame) ? (Frame) win : null;

            if ((frame != null) && ((frame.getExtendedState() & 0x6) != 0)) {
                AWTUtilities.setWindowShape(win, null);
            } else {
                AWTUtilities.setWindowShape(win,
                        new RoundRectangle2D.Double(0.0D, 0.0D, win.getWidth(),
                                win.getHeight(), 6.0D, 6.0D));
            }
        }
    }

    private static class CRootLayout implements LayoutManager2 {
        public void addLayoutComponent(Component comp, Object constraints) {
        }

        public void addLayoutComponent(String name, Component comp) {
        }

        public float getLayoutAlignmentX(Container target) {
            return 0.0F;
        }

        public float getLayoutAlignmentY(Container target) {
            return 0.0F;
        }

        public void invalidateLayout(Container target) {
        }

        public void layoutContainer(Container parent) {
            JRootPane root = (JRootPane) parent;
            Rectangle rect = root.getBounds();
            Insets insets = root.getInsets();
            int nextY = 0;
            int width = rect.width - insets.right - insets.left;
            int height = rect.height - insets.top - insets.bottom;

            if (root.getLayeredPane() != null) {
                root.getLayeredPane().setBounds(insets.left, insets.top, width,
                        height);
            }

            if (root.getGlassPane() != null) {
                root.getGlassPane().setBounds(insets.left, insets.top, width,
                        height);
            }

            if ((root.getWindowDecorationStyle() != 0)
                    && ((root.getUI() instanceof CRootPaneUI))) {
                JComponent titlePane = ((CRootPaneUI) root.getUI()).titlePane;

                if (titlePane != null) {
                    Dimension titlePaneSize = titlePane.getPreferredSize();

                    if (titlePaneSize != null) {
                        int titlePaneHeight = titlePaneSize.height;
                        titlePane.setBounds(0, 0, width, titlePaneHeight);
                        nextY += titlePaneHeight;
                    }
                }
            }

            if (root.getJMenuBar() != null) {
                Dimension menuBarSize = root.getJMenuBar().getPreferredSize();
                root.getJMenuBar().setBounds(0, nextY, width,
                        menuBarSize.height);
                nextY += menuBarSize.height;
            }

            if (root.getContentPane() != null) {
                root.getContentPane().setBounds(0, nextY, width,
                        height < nextY ? 0 : height - nextY);
            }
        }

        public Dimension maximumLayoutSize(Container target) {
            int contentPaneWidth = 2147483647;
            int contentPaneHeight = 2147483647;
            int menuBarWidth = 2147483647;
            int menuBarHeight = 2147483647;
            int titlePaneWidth = 2147483647;
            int titlePaneHeight = 2147483647;
            Insets insets = target.getInsets();
            JRootPane root = (JRootPane) target;

            if (root.getContentPane() != null) {
                Dimension contentPaneSize = root.getContentPane()
                        .getMaximumSize();

                if (contentPaneSize != null) {
                    contentPaneWidth = contentPaneSize.width;
                    contentPaneHeight = contentPaneSize.height;
                }
            }

            if (root.getJMenuBar() != null) {
                Dimension menuBarSize = root.getJMenuBar().getMaximumSize();

                if (menuBarSize != null) {
                    menuBarWidth = menuBarSize.width;
                    menuBarHeight = menuBarSize.height;
                }
            }

            if ((root.getWindowDecorationStyle() != 0)
                    && ((root.getUI() instanceof CRootPaneUI))) {
                JComponent titlePane = ((CRootPaneUI) root.getUI()).titlePane;

                if (titlePane != null) {
                    Dimension titlePaneSize = titlePane.getMaximumSize();

                    if (titlePaneSize != null) {
                        titlePaneWidth = titlePaneSize.width;
                        titlePaneHeight = titlePaneSize.height;
                    }
                }
            }

            int maxWidth = Math.max(Math.max(contentPaneWidth, menuBarWidth),
                    titlePaneWidth);
            int maxHeight = Math
                    .max(Math.max(contentPaneHeight, menuBarHeight),
                            titlePaneHeight);

            if (maxWidth != 2147483647) {
                maxWidth += insets.left + insets.right;
            }

            if (maxHeight != 2147483647) {
                maxHeight = contentPaneHeight + menuBarHeight + titlePaneHeight
                        + insets.top + insets.bottom;
            }

            return new Dimension(maxWidth, maxHeight);
        }

        public Dimension minimumLayoutSize(Container parent) {
            int contentPaneWidth = 0;
            int contentPaneHeight = 0;
            int menuBarWidth = 0;
            int menuBarHeight = 0;
            int titlePaneWidth = 0;
            Insets insets = parent.getInsets();
            JRootPane root = (JRootPane) parent;
            Dimension contentPaneSize;

            if (root.getContentPane() != null) {
                contentPaneSize = root.getContentPane().getMinimumSize();
            } else {
                contentPaneSize = root.getSize();
            }

            if (contentPaneSize != null) {
                contentPaneWidth = contentPaneSize.width;
                contentPaneHeight = contentPaneSize.height;
            }

            if (root.getJMenuBar() != null) {
                Dimension menuBarSize = root.getJMenuBar().getMinimumSize();

                if (menuBarSize != null) {
                    menuBarWidth = menuBarSize.width;
                    menuBarHeight = menuBarSize.height;
                }
            }

            if ((root.getWindowDecorationStyle() != 0)
                    && ((root.getUI() instanceof CRootPaneUI))) {
                JComponent titlePane = ((CRootPaneUI) root.getUI()).titlePane;

                if (titlePane != null) {
                    Dimension titlePaneSize = titlePane.getMinimumSize();

                    if (titlePaneSize != null) {
                        titlePaneWidth = titlePaneSize.width;
                    }
                }
            }

            return new Dimension(Math.max(
                    Math.max(contentPaneWidth, menuBarWidth), titlePaneWidth)
                    + insets.left + insets.right, contentPaneHeight
                    + menuBarHeight + titlePaneWidth + insets.top
                    + insets.bottom);
        }

        public Dimension preferredLayoutSize(Container parent) {
            int contentPaneWidth = 0;
            int contentPaneHeight = 0;
            int menuBarWidth = 0;
            int menuBarHeight = 0;
            int titlePaneWidth = 0;
            Insets insets = parent.getInsets();
            JRootPane root = (JRootPane) parent;
            Dimension contentPaneSize;

            if (root.getContentPane() != null) {
                contentPaneSize = root.getContentPane().getPreferredSize();
            } else {
                contentPaneSize = root.getSize();
            }

            if (contentPaneSize != null) {
                contentPaneWidth = contentPaneSize.width;
                contentPaneHeight = contentPaneSize.height;
            }

            if (root.getJMenuBar() != null) {
                Dimension menuBarSize = root.getJMenuBar().getPreferredSize();

                if (menuBarSize != null) {
                    menuBarWidth = menuBarSize.width;
                    menuBarHeight = menuBarSize.height;
                }
            }

            if ((root.getWindowDecorationStyle() != 0)
                    && ((root.getUI() instanceof CRootPaneUI))) {
                JComponent titlePane = ((CRootPaneUI) root.getUI()).titlePane;

                if (titlePane != null) {
                    Dimension titlePaneSize = titlePane.getPreferredSize();

                    if (titlePaneSize != null) {
                        titlePaneWidth = titlePaneSize.width;
                    }
                }
            }

            return new Dimension(Math.max(
                    Math.max(contentPaneWidth, menuBarWidth), titlePaneWidth)
                    + insets.left + insets.right, contentPaneHeight
                    + menuBarHeight + titlePaneWidth + insets.top
                    + insets.bottom);
        }

        public void removeLayoutComponent(Component comp) {
        }
    }

    public static enum ImageDisplayMode {
        FILL, SCALED, TILED;
    }

    private class MouseInputHandler extends MouseInputAdapter {
        private final int[] CURSOR_MAPPING = {6, 6, 8, 7, 7, 6, 0, 0, 0, 7,
                10, 0, 0, 0, 11, 4, 0, 0, 0, 5, 4, 4, 9, 5, 5};
        private int dragCursor;
        private boolean dragging;
        private int dragHeight;
        private int dragOffsetX;
        private int dragOffsetY;
        private int dragWidth;
        private boolean isMovingWindow;

        private MouseInputHandler() {
        }

        private void adjust(Rectangle bounds, Dimension min, Dimension max,
                            int deltaX, int deltaY, int deltaWidth, int deltaHeight) {
            bounds.x += deltaX;
            bounds.y += deltaY;
            bounds.width += deltaWidth;
            bounds.height += deltaHeight;

            if (min != null) {
                if (bounds.width < min.width) {
                    int correction = min.width - bounds.width;

                    if (deltaX != 0) {
                        bounds.x -= correction;
                    }

                    bounds.width = min.width;
                }

                if (bounds.height < min.height) {
                    int correction = min.height - bounds.height;

                    if (deltaY != 0) {
                        bounds.y -= correction;
                    }

                    bounds.height = min.height;
                }
            }

            if (max != null) {
                if (bounds.width > max.width) {
                    int correction = max.width - bounds.width;

                    if (deltaX != 0) {
                        bounds.x -= correction;
                    }

                    bounds.width = max.width;
                }

                if (bounds.height > max.height) {
                    int correction = max.height - bounds.height;

                    if (deltaY != 0) {
                        bounds.y -= correction;
                    }

                    bounds.height = max.height;
                }
            }
        }

        private int calculateCorner(Window win, int x, int y) {
            Insets insets = win.getInsets();
            int xPosition = calculatePosition(x - insets.left, win.getWidth()
                    - insets.left - insets.right);
            int yPosition = calculatePosition(y - insets.top, win.getHeight()
                    - insets.top - insets.bottom);

            if ((xPosition == -1) || (yPosition == -1)) {
                return -1;
            }

            return yPosition * 5 + xPosition;
        }

        private int calculatePosition(int spot, int width) {
            if (spot < 5) {
                return 0;
            }
            if (spot < 10) {
                return 1;
            }
            if (spot >= width - 5) {
                return 4;
            }
            if (spot >= width - 10) {
                return 3;
            }

            return 2;
        }

        private int getCursor(int corner) {
            if (corner == -1) {
                return 0;
            }

            return this.CURSOR_MAPPING[corner];
        }

        public void mouseClicked(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }

            Window win = (Window) e.getSource();
            Frame frame = (win instanceof Frame) ? (Frame) win : null;
            Point point = e.getPoint();
            int clickCount = e.getClickCount();

            if ((clickCount % 2 == 0) && (point.x <= 22) && (point.y <= 22)
                    && (CRootPaneUI.this.root.getWindowDecorationStyle() != 0)) {
                CRootPaneUI.this.window.dispatchEvent(new WindowEvent(
                        CRootPaneUI.this.window, 201));
                return;
            }

            if (frame == null) {
                return;
            }

            int state = frame.getExtendedState();
            Window fullWin = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice().getFullScreenWindow();

            if ((frame.isResizable()) && (frame != fullWin)
                    && (clickCount % 2 == 0)) {
                if ((state & 0x6) != 0) {
                    frame.setExtendedState(state & 0xFFFFFFF9);
                } else {
                    frame.setExtendedState(state | 0x6);
                }
            }
        }

        public void mouseDragged(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                if (CRootPaneUI.this.window.getBounds().contains(e.getPoint())) {
                    mouseMoved(e);
                } else {
                    mouseExited(e);
                }

                return;
            }

            Window win = (Window) e.getSource();
            Point point = e.getPoint();

            if (this.isMovingWindow) {
                Point eventLocationOnScreen = e.getLocationOnScreen();
                win.setLocation(eventLocationOnScreen.x - this.dragOffsetX,
                        eventLocationOnScreen.y - this.dragOffsetY);
            } else if (this.dragCursor != 0) {
                Rectangle rect = win.getBounds();
                Rectangle startBounds = new Rectangle(rect);
                Dimension min = win.getMinimumSize();
                Dimension max = win.getMaximumSize();

                switch (this.dragCursor) {
                    case 11:
                        adjust(rect, min, max, 0, 0, point.x
                                + (this.dragWidth - this.dragOffsetX) - rect.width,
                                0);
                        break;
                    case 9:
                        adjust(rect, min, max, 0, 0, 0, point.y
                                + (this.dragHeight - this.dragOffsetY)
                                - rect.height);
                        break;
                    case 8:
                        adjust(rect, min, max, 0, point.y - this.dragOffsetY, 0,
                                -(point.y - this.dragOffsetY));
                        break;
                    case 10:
                        adjust(rect, min, max, point.x - this.dragOffsetX, 0,
                                -(point.x - this.dragOffsetX), 0);
                        break;
                    case 7:
                        adjust(rect, min, max, 0, point.y - this.dragOffsetY,
                                point.x + (this.dragWidth - this.dragOffsetX)
                                        - rect.width, -(point.y - this.dragOffsetY));
                        break;
                    case 5:
                        adjust(rect, min, max, 0, 0, point.x
                                + (this.dragWidth - this.dragOffsetX) - rect.width,
                                point.y + (this.dragHeight - this.dragOffsetY)
                                        - rect.height);
                        break;
                    case 6:
                        adjust(rect, min, max, point.x - this.dragOffsetX, point.y
                                - this.dragOffsetY, -(point.x - this.dragOffsetX),
                                -(point.y - this.dragOffsetY));
                        break;
                    case 4:
                        adjust(rect, min, max, point.x - this.dragOffsetX, 0,
                                -(point.x - this.dragOffsetX), point.y
                                + (this.dragHeight - this.dragOffsetY)
                                - rect.height);
                        break;
                }

                if (!rect.equals(startBounds)) {
                    win.setBounds(rect);

                    if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
                        win.validate();
                        CRootPaneUI.this.root.repaint();
                    }
                }
            }
        }

        public void mouseExited(MouseEvent e) {
            if (!this.dragging) {
                Window win = (Window) e.getSource();
                win.setCursor(CRootPaneUI.DEFAULT_CURSOR);
            }
        }

        public void mouseMoved(MouseEvent e) {
            Window win = (Window) e.getSource();
            Frame frame = (win instanceof Frame) ? (Frame) win : null;
            Dialog dialog = (win instanceof Dialog) ? (Dialog) win : null;
            int cursor = getCursor(calculateCorner(win, e.getX(), e.getY()));

            if ((cursor != 0)
                    && (win.getBounds().contains(e.getLocationOnScreen()))
                    && (((frame != null) && (frame.isResizable()) && ((frame
                    .getExtendedState() & 0x6) == 0)) || ((dialog != null) && (dialog
                    .isResizable())))) {
                win.setCursor(Cursor.getPredefinedCursor(cursor));
            } else {
                win.setCursor(CRootPaneUI.DEFAULT_CURSOR);
            }
        }

        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }

            Point dragWindowOffset = e.getPoint();
            Window win = (Window) e.getSource();
            this.dragging = true;

            if (win != null) {
                win.toFront();
            }

            Frame frame = (win instanceof Frame) ? (Frame) win : null;
            Dialog dialog = (win instanceof Dialog) ? (Dialog) win : null;
            int frameState = frame != null ? frame.getExtendedState() : 0;

            if (((frame != null) && ((frameState & 0x6) == 0))
                    || ((dialog != null) && (dragWindowOffset.y >= 5)
                    && (dragWindowOffset.y < win.getHeight() - 5)
                    && (dragWindowOffset.x >= 5) && (dragWindowOffset.x < win
                    .getWidth() - 5))) {
                this.isMovingWindow = true;
                this.dragOffsetX = dragWindowOffset.x;
                this.dragOffsetY = dragWindowOffset.y;
            }

            if ((!this.isMovingWindow)
                    && (((frame != null) && (frame.isResizable()) && ((frameState & 0x6) == 0)) || ((dialog != null) && (dialog
                    .isResizable())))) {
                this.dragOffsetX = dragWindowOffset.x;
                this.dragOffsetY = dragWindowOffset.y;
                this.dragWidth = win.getWidth();
                this.dragHeight = win.getHeight();
                this.dragCursor = getCursor(calculateCorner(win,
                        dragWindowOffset.x, dragWindowOffset.y));
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                return;
            }

            if ((this.dragCursor != 0) && (CRootPaneUI.this.window != null)
                    && (!CRootPaneUI.this.window.isValid())) {
                CRootPaneUI.this.window.validate();
                CRootPaneUI.this.root.repaint();
            }

            this.dragging = false;
            this.isMovingWindow = false;
            this.dragCursor = 0;
            mouseMoved(e);
        }
    }

    private static final Image BG_IMAGE = UIResourceManager
            .getImage("WindowBackgroundImage");

    private static final Image BG_IMAGE_MAX = UIUtil.cutImage(
            BG_IMAGE,
            new Rectangle(2, 2, BG_IMAGE.getWidth(null) - 4, BG_IMAGE
                    .getHeight(null) - 4), null);
    public static final StackBlurFilter BLUR_FILTER = new StackBlurFilter(100,
            15);
    public static final int BLUR_SIZE = 30;
    private static final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(0);
    private static final Border MAX_BORDER = new EmptyBorder(-2, -2, -2, -2);
    private static final Image TITLE_IMAGE = UIResourceManager
            .getImage("WindowTitleImage");

    public static ComponentUI createUI(JComponent c) {
        return new CRootPaneUI();
    }

    private Image bottomEdge;
    private ComponentListener componentListener;
    private BufferedImage edgeBlurImage;
    private LayoutManager layoutManager;
    private MouseInputListener mouseInputListener;

    private Image rightEdge;

    private JRootPane root;

    private LayoutManager savedOldLayout;

    private JComponent titlePane;

    private Window window;

    void changeCloseButtonState() {
        if ((this.titlePane instanceof CTitlePane)) {
            ((CTitlePane) this.titlePane).changeCloseButtonState();
        }
    }

    private LayoutManager createLayoutManager() {
        return new CRootLayout();
    }

    private JComponent createTitlePane(JRootPane root) {
        return new CTitlePane(root, this);
    }

    private ComponentListener createWindowComponentListener(JRootPane root) {
        return new ComponentHandler();
    }

    private MouseInputListener createWindowMouseInputListener(JRootPane root) {
        return new MouseInputHandler();
    }

    BufferedImage getEdgeBlurImage() {
        return this.edgeBlurImage;
    }

    JImagePane getTitleContentPane() {
        if ((this.titlePane instanceof CTitlePane)) {
            return ((CTitlePane) this.titlePane).getContentPane();
        }

        return null;
    }

    void installBorder(JRootPane root) {
        root.setBorder(null);
    }

    private void installClientDecorations(JRootPane root) {
        installBorder(root);

        if (root.getWindowDecorationStyle() != 0) {
            setTitlePane(root, createTitlePane(root));
        }

        installWindowListeners(root);
        installLayout(root);

        if (this.window != null) {
            root.revalidate();
            root.repaint();
        }
    }

    private void installLayout(JRootPane root) {
        if (this.layoutManager == null) {
            this.layoutManager = createLayoutManager();
        }

        this.savedOldLayout = root.getLayout();
        root.setLayout(this.layoutManager);
    }

    public void installUI(JComponent c) {
        super.installUI(c);
        this.root = ((JRootPane) c);
        Container parent = this.root.getParent();

        if ((parent instanceof Window)) {
            this.window = ((Window) parent);
        } else {
            this.window = SwingUtilities.getWindowAncestor(parent);
        }

        installClientDecorations(this.root);
    }

    private void installWindowListeners(JRootPane root) {
        if (this.window != null) {
            if (this.mouseInputListener == null) {
                this.mouseInputListener = createWindowMouseInputListener(root);
            }

            if (this.componentListener == null) {
                this.componentListener = createWindowComponentListener(root);
            }

            this.window.addMouseListener(this.mouseInputListener);
            this.window.addMouseMotionListener(this.mouseInputListener);
            this.window.addComponentListener(this.componentListener);
        }
    }

    public void paint(Graphics g, JComponent c) {
        boolean borderPainted = false;
        ImageDisplayMode mode = null;
        boolean titleOpaque = this.root.getWindowDecorationStyle() != 0;
        Image image = null;
        float imageAlpha = 1.0F;

        if ((this.window instanceof JCFrame)) {
            JCFrame frame = (JCFrame) this.window;
            image = frame.getBackgroundImage();
            borderPainted = frame.isBorderPainted();
            mode = frame.getImageDisplayMode();
            titleOpaque = (titleOpaque) && (frame.isTitleOpaque());
            imageAlpha = frame.getImageAlpha();
        } else if ((this.window instanceof JCDialog)) {
            JCDialog dialog = (JCDialog) this.window;
            image = dialog.getBackgroundImage();
            borderPainted = dialog.isBorderPainted();
            mode = dialog.getImageDisplayMode();
            titleOpaque = (titleOpaque) && (dialog.isTitleOpaque());
            imageAlpha = dialog.getImageAlpha();
        }

        int titleHeight = titleOpaque ? this.root.getInsets().top
                + this.titlePane.getHeight() : 0;

        if ((titleOpaque) && (titleHeight > 0)) {
            Rectangle rect = new Rectangle(0, 0, this.root.getWidth(),
                    titleHeight);
            UIUtil.paintImage(g, TITLE_IMAGE, new InsetsUIResource(5, 3, 1, 3),
                    rect, c);
        }

        if ((image != null) && (imageAlpha > 0.0F)) {
            Graphics2D g2d = (Graphics2D) g;
            Composite oldComposite = g2d.getComposite();

            if (imageAlpha < 1.0F) {
                g2d.setComposite(AlphaComposite.SrcOver.derive(imageAlpha));
            }

            paintBackgroundImage(g, c, image, mode, titleHeight);
            g2d.setComposite(oldComposite);
        }

        if (borderPainted) {
            Frame frame = (this.window instanceof Frame) ? (Frame) this.window
                    : null;

            if ((frame == null)
                    || ((frame != null) && ((frame.getExtendedState() & 0x6) == 0))) {
                UIUtil.paintImage(g, BG_IMAGE, new Insets(3, 3, 3, 3),
                        new Rectangle(0, 0, c.getWidth(), c.getHeight()), c);
            } else {
                UIUtil.paintImage(g, BG_IMAGE_MAX, new Insets(1, 1, 1, 1),
                        new Rectangle(0, 0, c.getWidth(), c.getHeight()), c);
            }
        }
    }

    protected void paintBackgroundImage(Graphics g, JComponent c, Image image,
                                        ImageDisplayMode mode, int titleHeight) {
        int width = c.getWidth();
        int height = c.getHeight() - titleHeight;
        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);
        g.translate(0, titleHeight);

        if (mode == ImageDisplayMode.SCALED) {
            g.drawImage(image, 0, 0, width, height, c);
        } else if (mode == ImageDisplayMode.TILED) {
            for (int x = 0; x < width; x += imageWidth) {
                for (int y = 0; y < height; y += imageHeight) {
                    g.drawImage(image, x, y, c);
                }
            }
        } else {
            if (this.edgeBlurImage == null) {
                this.edgeBlurImage = UIUtil.createEdgeBlurryImage(image, 30,
                        BLUR_FILTER, c);
            }

            imageWidth = this.edgeBlurImage.getWidth();
            imageHeight = this.edgeBlurImage.getHeight();

            if ((width > imageWidth) || (height > imageHeight)) {
                this.rightEdge = UIUtil.cutImage(this.edgeBlurImage,
                        new Rectangle(imageWidth - 1, 0, 1, imageHeight), c);
                this.bottomEdge = UIUtil.cutImage(this.edgeBlurImage,
                        new Rectangle(0, imageHeight - 1, imageWidth, 1), c);
                width = Math.max(width, imageWidth);
                height = Math.max(height, imageHeight);
                BufferedImage tempBlurImage = UIUtil
                        .getGraphicsConfiguration(c).createCompatibleImage(
                                width, height, 3);
                Graphics2D tempG2d = tempBlurImage.createGraphics();
                tempG2d.drawImage(this.edgeBlurImage, 0, 0, c);

                if (width > imageWidth) {
                    int x1 = imageWidth;
                    int y2 = Math.min(height, imageHeight);

                    for (int i = 0; i < width - imageWidth; i++) {
                        tempG2d.drawImage(this.rightEdge, x1, 0, x1 + 1, y2, 0,
                                0, 1, Math.min(height, imageHeight), c);
                        x1++;

                        if (y2 >= height)
                            continue;
                        y2++;
                    }

                }

                if (height > imageHeight) {
                    int x2 = Math.min(width, imageWidth + 1);
                    int y1 = imageHeight;

                    for (int i = 0; i < height - imageHeight; i++) {
                        tempG2d.drawImage(this.bottomEdge, 0, y1, x2, y1 + 1,
                                0, 0, Math.min(width, imageWidth), 1, c);
                        y1++;

                        if (x2 >= width)
                            continue;
                        x2++;
                    }

                }

                tempG2d.dispose();
                this.edgeBlurImage = tempBlurImage;
                tempBlurImage = null;
            }

            g.drawImage(this.edgeBlurImage, 0, 0, c);
        }

        g.translate(0, -titleHeight);
    }

    public void propertyChange(PropertyChangeEvent e) {
        super.propertyChange(e);
        String propertyName = e.getPropertyName();

        if ("windowDecorationStyle".equals(propertyName)) {
            JRootPane root = (JRootPane) e.getSource();
            uninstallClientDecorations(root);
            installClientDecorations(root);
        } else if ("ancestor".equals(propertyName)) {
            uninstallWindowListeners(this.root);
            installWindowListeners(this.root);
        }
    }

    public void removeBlurImage() {
        this.edgeBlurImage = null;
        this.rightEdge = null;
        this.bottomEdge = null;
    }

    void setEdgeBlurImage(BufferedImage edgeBlurImage) {
        this.edgeBlurImage = edgeBlurImage;
    }

    private void setTitlePane(JRootPane root, JComponent titlePane) {
        JLayeredPane layeredPane = root.getLayeredPane();

        if (this.titlePane != null) {
            this.titlePane.setVisible(false);
            layeredPane.remove(this.titlePane);
        }

        if (titlePane != null) {
            layeredPane.add(titlePane, JLayeredPane.FRAME_CONTENT_LAYER);
            titlePane.setVisible(true);
        }

        this.titlePane = titlePane;
    }

    void uninstallBorder(JRootPane root) {
        root.setBorder(MAX_BORDER);
    }

    private void uninstallClientDecorations(JRootPane root) {
        uninstallBorder(root);
        uninstallWindowListeners(root);
        setTitlePane(root, null);
        uninstallLayout(root);
        root.repaint();
        root.revalidate();
    }

    private void uninstallLayout(JRootPane root) {
        if (this.savedOldLayout != null) {
            root.setLayout(this.savedOldLayout);
            this.savedOldLayout = null;
        }
    }

    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        uninstallClientDecorations(this.root);
        this.layoutManager = null;
        this.mouseInputListener = null;
        this.componentListener = null;
        this.root = null;

        if (this.window != null) {
            this.window.setCursor(DEFAULT_CURSOR);
        }

        this.window = null;
    }

    private void uninstallWindowListeners(JRootPane root) {
        if (this.window != null) {
            this.window.removeMouseListener(this.mouseInputListener);
            this.window.removeMouseMotionListener(this.mouseInputListener);
            this.window.removeComponentListener(this.componentListener);
        }
    }
}