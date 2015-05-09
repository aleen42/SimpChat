package com.java.ui.componentc;

import com.java.ui.component.JImagePane;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class JCDialog extends JDialog {
    private class ActionHandler implements ActionListener {
        private ActionHandler() {
        }

        public void actionPerformed(ActionEvent e) {
            if ((!JCDialog.this.isVisible()) && (JCDialog.this.timer != null)
                    && (JCDialog.this.timer.isRunning())) {
                JCDialog.this.timer.stop();
                JCDialog.this.timer = null;
            } else {
                JCDialog.this.ui.changeCloseButtonState();
            }
        }
    }

    private static final long serialVersionUID = -1642900259343757988L;
    private Image backgroundImage;
    private boolean borderPainted;
    private float imageAlpha;
    private CRootPaneUI.ImageDisplayMode imageDisplayMode;
    private Timer timer;
    private boolean titleOpaque;

    private CRootPaneUI ui;

    public JCDialog() {

    }

    public JCDialog(Dialog owner) {
        this(owner, false);
    }

    public JCDialog(Dialog owner, boolean modal) {
        this(owner, null, modal);
    }

    public JCDialog(Dialog owner, String title) {
        this(owner, title, false);
    }

    public JCDialog(Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        init();
    }

    public JCDialog(Dialog owner, String title, boolean modal,
                    GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        init();
    }

    public JCDialog(Frame owner) {
        this(owner, false);
    }

    public JCDialog(Frame owner, boolean modal) {
        this(owner, null, modal);
    }

    public JCDialog(Frame owner, String title) {
        this(owner, title, false);
    }

    public JCDialog(Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        init();
    }

    public JCDialog(Frame owner, String title, boolean modal,
                    GraphicsConfiguration gc) {
        super(owner, title, modal, gc);
        init();
    }

    public JCDialog(Window owner) {
        this(owner, Dialog.ModalityType.MODELESS);
    }

    public JCDialog(Window owner, Dialog.ModalityType modalityType) {
        this(owner, null, modalityType);
    }

    public JCDialog(Window owner, String title) {
        this(owner, title, Dialog.ModalityType.MODELESS);
    }

    public JCDialog(Window owner, String title, Dialog.ModalityType modalityType) {
        super(owner, title, modalityType);
        init();
    }

    public JCDialog(Window owner, String title,
                    Dialog.ModalityType modalityType, GraphicsConfiguration gc) {
        super(owner, title, modalityType, gc);
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
        Container parent = getParent();
        setFont(UIUtil.getDefaultFont());
        setUndecorated(true);
        root.setUI(this.ui = new CRootPaneUI());
        root.setForeground(Color.BLACK);
        root.setBackground(new Color(233, 242, 249));
        root.setWindowDecorationStyle(2);
        setBackground(root.getBackground());

        if ((parent != null) && ((parent instanceof Window))) {
            copyBackgroundImage(parent);
            setIconImages(((Window) parent).getIconImages());
        }

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

    public void setBackgroundImage(Image backgroundImage) {
        setBackgroundImage(backgroundImage, null);
    }

    public void setBackgroundImage(Image backgroundImage,
                                   BufferedImage edgeBlurImage) {
        if (this.backgroundImage != backgroundImage) {
            this.backgroundImage = backgroundImage;
            this.ui.removeBlurImage();
            this.ui.setEdgeBlurImage(edgeBlurImage);
            getRootPane().repaint();
        }
    }

    public void setBorderPainted(boolean borderPainted) {
        if (this.borderPainted != borderPainted) {
            this.borderPainted = borderPainted;
            getRootPane().repaint();
        }
    }

    public void setImageAlpha(float imageAlpha) {
        if ((imageAlpha >= 0.0F) && (imageAlpha <= 1.0F)) {
            this.imageAlpha = imageAlpha;
            repaint();
        } else {
            throw new IllegalArgumentException("Invalid alpha:" + imageAlpha);
        }
    }

    public void setImageDisplayMode(
            CRootPaneUI.ImageDisplayMode imageDisplayMode) {
        if (this.imageDisplayMode != imageDisplayMode) {
            this.imageDisplayMode = imageDisplayMode;
            getRootPane().repaint();
        }
    }

    public void setTitleOpaque(boolean titleOpaque) {
        if (this.titleOpaque != titleOpaque) {
            this.titleOpaque = titleOpaque;
            getRootPane().repaint();
        }
    }

    public void setVisible(boolean visible) {
        if ((isUndecorated()) && (isModal())) {
            if (visible) {
                if (this.timer == null) {
                    this.timer = new Timer(100, new ActionHandler());
                }

                if (!this.timer.isRunning()) {
                    this.timer.start();
                }
            } else if ((this.timer != null) && (this.timer.isRunning())) {
                this.timer.stop();
            }
        }

        super.setVisible(visible);
    }
}