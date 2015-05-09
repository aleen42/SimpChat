package com.java.ui.componentc;

import com.java.ui.component.JImagePane;
import com.java.ui.layout.LineLayout;
import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;
import com.java.ui.util.Util;
import sun.awt.SunToolkit;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class CTitlePane extends JComponent {
    private class CloseAction extends AbstractAction {
        private static final long serialVersionUID = -1022852041948761832L;

        public CloseAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {
            CTitlePane.this.close();
        }
    }

    private class IconifyAction extends AbstractAction {
        private static final long serialVersionUID = -1509739185778590162L;

        public IconifyAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {
            CTitlePane.this.iconify();
        }
    }

    private class MaximizeAction extends AbstractAction {
        private static final long serialVersionUID = -7252759733748229590L;

        public MaximizeAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {
            CTitlePane.this.maximize();
        }
    }

    private class PropertyChangeHandler implements PropertyChangeListener {
        private PropertyChangeHandler() {
        }

        public void propertyChange(PropertyChangeEvent e) {
            String name = e.getPropertyName();

            if (("resizable".equals(name)) || ("state".equals(name))) {
                Frame frame = CTitlePane.this.getFrame();

                if (frame != null) {
                    CTitlePane.this.setState(frame.getExtendedState(), true);
                }

                if ("resizable".equals(name)) {
                    CTitlePane.this.rootPane.repaint();
                }
            } else if ("title".equals(name)) {
                CTitlePane.this.repaint();
            } else if ("iconImage" == name) {
                CTitlePane.this.updateSystemIcon();
                CTitlePane.this.repaint();
            }
        }
    }

    private class RestoreAction extends AbstractAction {
        private static final long serialVersionUID = 7027403948364374755L;

        public RestoreAction() {
            super();
        }

        public void actionPerformed(ActionEvent e) {
            CTitlePane.this.restore();
        }
    }

    private class WindowHandler extends WindowAdapter {
        private WindowHandler() {
        }

        public void windowActivated(WindowEvent ev) {
            CTitlePane.this.setActive(true);
        }

        public void windowDeactivated(WindowEvent ev) {
            CTitlePane.this.setActive(false);
        }
    }

    private static final long serialVersionUID = -8078184400171961600L;
    private static final Font TITLE_FONT_CH = UIUtil.getTitleFont(true);
    private static final Font TITLE_FONT_EN = UIUtil.getTitleFont(false);
    private JCButton btnClose;
    private JCButton btnIconify;
    private JCButton btnMaxOrRestore;
    private boolean[] chFlags;
    private Action closeAction;
    private JImagePane contentPane;
    private Action iconifyAction;
    private Action maximizeAction;
    private PropertyChangeListener propertyChangeListener;
    private Action restoreAction;
    private JRootPane rootPane;
    private CRootPaneUI rootPaneUI;
    private Image shadowImage;
    private int state;
    private Image systemIcon;

    private int textX;

    private int textY;

    private String title;

    private String[] titleChars;

    private Window window;

    private WindowListener windowListener;

    public CTitlePane(JRootPane root, CRootPaneUI ui) {
        this.rootPane = root;
        this.rootPaneUI = ui;
        this.state = -1;
        setLayout(new LineLayout(0, 10, 10, 0));
        installSubcomponents();
        setPreferredSize(new Dimension(-1, 28));
        setOpaque(false);
    }

    public void addNotify() {
        super.addNotify();
        uninstallListeners();
        this.window = SwingUtilities.getWindowAncestor(this);

        if (this.window != null) {
            if ((this.window instanceof Frame)) {
                setState(((Frame) this.window).getExtendedState());
            } else {
                setState(0);
            }

            setActive(this.window.isActive());
            installListeners();
            updateSystemIcon();
        }
    }

    void changeCloseButtonState() {
        ButtonModel model = this.btnClose.getModel();
        Point mouseLocation = Util.getMouseLocation();
        Point buttonLocation = this.btnClose.getLocationOnScreen();
        int x = mouseLocation.x - buttonLocation.x;
        int y = mouseLocation.y - buttonLocation.y;

        if ((model.isRollover())
                && ((mouseLocation.x > buttonLocation.x
                + this.btnClose.getWidth()) || (mouseLocation.y < buttonLocation.y))) {
            this.btnClose.dispatchEvent(new MouseEvent(this.btnClose, 505,
                    System.currentTimeMillis(), 0, x, y, 0, false));
        } else if ((!model.isPressed())
                && (!model.isRollover())
                && (this.btnClose.getBounds().contains(
                this.btnClose.getX() + x, this.btnClose.getY() + y))) {
            this.btnClose.dispatchEvent(new MouseEvent(this.btnClose, 504,
                    System.currentTimeMillis(), 0, x, y, 0, false));
        }
    }

    private void close() {
        if (this.window != null) {
            this.window.dispatchEvent(new WindowEvent(this.window, 201));
        }
    }

    private void createActions() {
        this.closeAction = new CloseAction();

        if (getWindowDecorationStyle() == 1) {
            this.iconifyAction = new IconifyAction();
            this.restoreAction = new RestoreAction();
            this.maximizeAction = new MaximizeAction();
        }
    }

    private void createButtons() {
        this.btnClose = new JCButton(this.closeAction);
        this.btnIconify = new JCButton(this.iconifyAction);
        this.btnMaxOrRestore = new JCButton(this.restoreAction);
        JCButton[] buttons = {this.btnClose, this.btnIconify,
                this.btnMaxOrRestore};
        String[] names = {"Close", "Iconify", "Maximize"};
        int index = 0;

        this.btnClose.setImage(UIResourceManager.getImage("WindowCloseImage"));
        this.btnClose.setRolloverImage(UIResourceManager
                .getImage("WindowCloseRolloverImage"));
        this.btnClose.setPressedImage(UIResourceManager
                .getImage("WindowClosePressedImage"));
        this.btnClose.setPreferredSize(new Dimension(this.btnClose.getImage()
                .getWidth(null), this.btnClose.getImage().getHeight(null)));

        this.btnIconify.setImage(UIResourceManager.getImage("WindowMinImage"));
        this.btnIconify.setRolloverImage(UIResourceManager
                .getImage("WindowMinRolloverImage"));
        this.btnIconify.setPressedImage(UIResourceManager
                .getImage("WindowMinPressedImage"));
        this.btnIconify.setPreferredSize(new Dimension(this.btnIconify
                .getImage().getWidth(null), this.btnIconify.getImage()
                .getHeight(null)));

        for (JCButton button : buttons) {
            button.setToolTipText(button.getText());
            button.setText(null);
            button.setFocusable(false);
            button.setFocusPainted(false);
            button.putClientProperty("paintActive", Boolean.TRUE);
            button.putClientProperty("AccessibleName", names[(index++)]);
        }
    }

    private void createTitleShadow(Graphics g) {
        int titleLength = this.title.length();
        Map<Shape, Integer> shapeMap = new LinkedHashMap<Shape, Integer>();
        this.titleChars = new String[titleLength];
        this.chFlags = new boolean[titleLength];

        int shadowX = 0;
        int shadowY = 0;
        int shadowWidth = 0;
        int shadowHeight = 0;
        int textY = 0;
        int textWidth = 0;
        int textHeight = 0;
        Shape textShape;
        for (int i = 0; i < titleLength; i++) {
            this.titleChars[i] = String.valueOf(this.title.charAt(i));
            this.chFlags[i] = Util.isChinese(this.titleChars[i]);
            Font font = this.chFlags[i] ? TITLE_FONT_CH : TITLE_FONT_EN;
            FontMetrics fm = g.getFontMetrics(font);
            GlyphVector vector = font.createGlyphVector(
                    fm.getFontRenderContext(), this.titleChars[i]);
            Stroke stroke = new BasicStroke(12.0F, 1, 1);
            textShape = vector.getOutline();
            Shape shadowShape = stroke.createStrokedShape(textShape);
            Rectangle textRect = textShape.getBounds();
            Rectangle shadowRect = shadowShape.getBounds();
            shadowY = Math.min(shadowRect.y, shadowY);
            textY = Math.min(textRect.y, textY);
            shadowHeight = Math.max(shadowRect.y + shadowRect.height,
                    shadowHeight);
            textHeight = Math.max(textRect.y + textRect.height, textHeight);

            if (i == titleLength - 1) {
                shadowWidth = shadowX + shadowRect.width;
            }

            shapeMap.put(shadowShape, Integer.valueOf(shadowX));
            shadowX += fm.stringWidth(this.titleChars[i]);
        }

        shadowHeight -= shadowY;
        textWidth = shadowX;
        textHeight -= textY;
        GraphicsConfiguration gc = UIUtil.getGraphicsConfiguration(this);
        BufferedImage image = gc.createCompatibleImage(shadowWidth,
                shadowHeight, 3);
        Graphics2D imageG2D = image.createGraphics();
        imageG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        imageG2D.setColor(Color.WHITE);

        for (Object _shape : shapeMap.keySet()) {
            Shape shape = (Shape) _shape;
            Rectangle rect = shape.getBounds();
            int deltaX = ((Integer) shapeMap.get(shape)).intValue();
            imageG2D.translate(-rect.x + deltaX, -shadowY);
            imageG2D.fill(shape);
            imageG2D.translate(rect.x - deltaX, shadowY);
        }

        imageG2D.dispose();

        BufferedImage shadowImage = gc.createCompatibleImage(shadowWidth,
                shadowHeight, 3);
        imageG2D = shadowImage.createGraphics();
        int width = shadowWidth;
        int height = shadowHeight;
        int x = 0;
        int y = 0;
        imageG2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int i = 0; i < 12; i++) {
            imageG2D.setComposite(AlphaComposite.SrcOver.derive(0.075F));
            imageG2D.drawImage(image, x, y, width, height, null);
            x++;
            y++;
            width -= 2;
            height -= 2;
        }

        imageG2D.dispose();
        this.textX = Math.round((shadowWidth - textWidth) / 2.0F);
        this.textY = (-textY + Math.round((shadowHeight - textHeight) / 2.0F) + (1 - (shadowHeight - textHeight) % 2));
        this.shadowImage = shadowImage;
    }

    private WindowListener createWindowListener() {
        return new WindowHandler();
    }

    private PropertyChangeListener createWindowPropertyChangeListener() {
        return new PropertyChangeHandler();
    }

    public JImagePane getContentPane() {
        return this.contentPane;
    }

    private Frame getFrame() {
        if ((this.window instanceof Frame)) {
            return (Frame) this.window;
        }

        return null;
    }

    public JRootPane getRootPane() {
        return this.rootPane;
    }

    private String getTitle() {
        if ((this.window instanceof Frame)) {
            return ((Frame) this.window).getTitle();
        }

        if ((this.window instanceof Dialog)) {
            return ((Dialog) this.window).getTitle();
        }

        return null;
    }

    private int getWindowDecorationStyle() {
        return this.rootPane.getWindowDecorationStyle();
    }

    private void iconify() {
        Frame frame = getFrame();

        if (frame != null) {
            frame.setExtendedState(this.state | 0x1);
        }
    }

    private void installListeners() {
        if (this.window != null) {
            this.windowListener = createWindowListener();
            this.window.addWindowListener(this.windowListener);
            this.propertyChangeListener = createWindowPropertyChangeListener();
            this.window.addPropertyChangeListener(this.propertyChangeListener);
        }
    }

    private void installSubcomponents() {
        int decorationStyle = getWindowDecorationStyle();

        if (decorationStyle == 1) {
            createActions();
            createButtons();
            add(this.btnIconify, "End");
            add(this.btnMaxOrRestore, "End");
            add(this.btnClose, "End");
        } else if (decorationStyle != 0) {
            createActions();
            createButtons();
            add(this.btnClose, "End");
        }

        this.contentPane = new JImagePane();
        this.contentPane.setBackground(UIResourceManager.getEmptyColor());
        this.contentPane.setImageOnly(true);
        add(this.contentPane, "MiddleFill");
    }

    private void maximize() {
        Frame frame = getFrame();

        if (frame != null) {
            frame.setExtendedState(this.state | 0x6);
        }
    }

    public void paintComponent(Graphics g) {
        Frame frame = getFrame();
        String title = getTitle();
        int x = 5;
        int y = 5;

        if (this.systemIcon != null) {
            g.drawImage(this.systemIcon, x, y, 16, 16, this.rootPane);
            x += 19;
        }

        if ((title != null) && (!title.trim().isEmpty())) {
            if (!title.equals(this.title)) {
                this.title = title;
                createTitleShadow(g);
            }

            int shadowX = x - 3;
            int shadowY = (getHeight() - this.shadowImage
                    .getHeight(this.rootPane)) / 2;
            x = this.textX;

            g.translate(shadowX, shadowY);
            g.setColor(this.rootPane.getForeground());
            g.drawImage(this.shadowImage, 0, 0, this.rootPane);

            for (int i = 0; i < title.length(); i++) {
                g.setFont(this.chFlags[i] ? TITLE_FONT_CH : TITLE_FONT_EN);
                SwingUtilities2.drawString(this.rootPane, g,
                        this.titleChars[i], x, this.textY);
                x += g.getFontMetrics().stringWidth(this.titleChars[i]);
            }

            g.translate(-shadowX, -shadowY);
        }

        if (frame != null) {
            setState(frame.getExtendedState());
        }
    }

    public void removeNotify() {
        super.removeNotify();
        uninstallListeners();
        this.window = null;
    }

    private void restore() {
        Frame frame = getFrame();

        if (frame == null) {
            return;
        }

        if ((this.state & 0x1) != 0) {
            frame.setExtendedState(this.state & 0xFFFFFFFE);
        } else {
            frame.setExtendedState(this.state & 0xFFFFFFF9);
        }
    }

    private void setActive(boolean isActive) {
        this.btnClose.putClientProperty("paintActive", Boolean.valueOf(isActive));

        if (getWindowDecorationStyle() == 1) {
            this.btnIconify.putClientProperty("paintActive", Boolean.valueOf(isActive));
            this.btnMaxOrRestore.putClientProperty("paintActive", Boolean.valueOf(isActive));
        }

        this.rootPane.repaint();
    }

    private void setState(int state) {
        setState(state, false);
    }

    private void setState(int state, boolean updateRegardless) {
        if ((this.window != null) && (getWindowDecorationStyle() == 1) && ((this.state != state) || (updateRegardless))) {
            Frame frame = getFrame();

            if (frame != null) {
                if (((state & 0x6) != 0) && (frame.isShowing())) {
                    this.rootPaneUI.uninstallBorder(this.rootPane);
                } else if ((state & 0x6) == 0) {
                    this.rootPaneUI.installBorder(this.rootPane);
                }

                if (frame.isResizable()) {
                    if ((state & 0x6) != 0) {
                        updateRestoreButton(this.restoreAction);
                        this.maximizeAction.setEnabled(false);
                        this.restoreAction.setEnabled(true);
                    } else {
                        updateRestoreButton(this.maximizeAction);
                        this.maximizeAction.setEnabled(true);
                        this.restoreAction.setEnabled(false);
                    }

                    if ((this.btnMaxOrRestore.getParent() == null) || (this.btnIconify.getParent() == null)) {
                        this.btnMaxOrRestore.setVisible(true);
                        this.btnIconify.setVisible(true);
                        revalidate();
                        repaint();
                    }
                } else {
                    this.maximizeAction.setEnabled(false);
                    this.restoreAction.setEnabled(false);

                    if (this.btnMaxOrRestore.getParent() != null) {
                        this.btnMaxOrRestore.setVisible(false);
                        revalidate();
                        repaint();
                    }
                }
            } else {
                this.maximizeAction.setEnabled(false);
                this.restoreAction.setEnabled(false);
                this.iconifyAction.setEnabled(false);
                this.btnMaxOrRestore.setVisible(false);
                this.btnIconify.setVisible(false);
                revalidate();
                repaint();
            }

            this.closeAction.setEnabled(true);
            this.state = state;
        }
    }

    private void uninstallListeners() {
        if (this.window != null) {
            this.window.removeWindowListener(this.windowListener);
            this.window
                    .removePropertyChangeListener(this.propertyChangeListener);
        }
    }

    private void updateRestoreButton(Action action) {
        if (action == this.restoreAction) {
            this.btnMaxOrRestore.setImage(UIResourceManager
                    .getImage("WindowRestoreImage"));
            this.btnMaxOrRestore.setRolloverImage(UIResourceManager
                    .getImage("WindowRestoreRolloverImage"));
            this.btnMaxOrRestore.setPressedImage(UIResourceManager
                    .getImage("WindowRestorePressedImage"));
            this.btnMaxOrRestore.setPreferredSize(new Dimension(
                    this.btnMaxOrRestore.getImage().getWidth(null),
                    this.btnMaxOrRestore.getImage().getHeight(null)));
        } else {
            this.btnMaxOrRestore.setImage(UIResourceManager
                    .getImage("WindowMaxImage"));
            this.btnMaxOrRestore.setRolloverImage(UIResourceManager
                    .getImage("WindowMaxRolloverImage"));
            this.btnMaxOrRestore.setPressedImage(UIResourceManager
                    .getImage("WindowMaxPressedImage"));
            this.btnMaxOrRestore.setPreferredSize(new Dimension(
                    this.btnMaxOrRestore.getImage().getWidth(null),
                    this.btnClose.getImage().getHeight(null)));
        }

        this.btnMaxOrRestore.setAction(action);
        this.btnMaxOrRestore.setToolTipText(this.btnMaxOrRestore.getText());
        this.btnMaxOrRestore.setText(null);
    }

    private void updateSystemIcon() {
        if (this.window == null) {
            this.systemIcon = null;
            return;
        }

        List<Image> icons = this.window.getIconImages();

        if ((icons == null) || (icons.isEmpty())) {
            this.systemIcon = null;
            return;
        }

        if (icons.size() == 1) {
            this.systemIcon = ((Image) icons.get(0));
        } else {
            this.systemIcon = SunToolkit.getScaledIconImage(icons, 16, 16);
        }
    }
}