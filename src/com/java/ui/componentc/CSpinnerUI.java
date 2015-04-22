package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;
import java.awt.*;

public class CSpinnerUI extends BasicSpinnerUI {
    private class SpinnerUILayout implements LayoutManager {
        private Component editor;
        private Component nextButton;
        private Component previousButton;
        private final Dimension ZERO_SIZE = new Dimension(0, 0);

        private SpinnerUILayout() {
        }

        public void addLayoutComponent(String name, Component c) {
            if ("Next".equals(name)) {
                this.nextButton = c;
            } else if ("Previous".equals(name)) {
                this.previousButton = c;
            } else if ("Editor".equals(name)) {
                this.editor = c;
            }
        }

        public void layoutContainer(Container parent) {
            int width = parent.getWidth();
            int height = parent.getHeight();
            Insets insets = parent.getInsets();
            Dimension nextD = preferredSize(this.nextButton);
            Dimension previousD = preferredSize(this.previousButton);
            int buttonsWidth = Math.max(nextD.width, previousD.width);
            int editorHeight = height - (insets.top + insets.bottom);
            int buttonsX;

            int editorX;
            int editorWidth;
            if (parent.getComponentOrientation().isLeftToRight()) {
                editorX = insets.left;
                editorWidth = width - buttonsWidth - insets.left - insets.right;
                buttonsX = width - buttonsWidth - insets.right;
            } else {
                buttonsX = insets.left;
                editorX = buttonsX + buttonsWidth;
                editorWidth = width - buttonsWidth - insets.left - insets.right;
            }

            int nextY = insets.top;
            int nextHeight = editorHeight / 2;
            int previousY = insets.top + nextHeight + editorHeight % 2;
            int previousHeight = height - previousY - insets.bottom;

            setBounds(this.editor, editorX, insets.top, editorWidth,
                    editorHeight);
            setBounds(this.nextButton, buttonsX, nextY, buttonsWidth,
                    nextHeight);
            setBounds(this.previousButton, buttonsX, previousY, buttonsWidth,
                    previousHeight);
        }

        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        public Dimension preferredLayoutSize(Container parent) {
            Dimension nextD = preferredSize(this.nextButton);
            Dimension previousD = preferredSize(this.previousButton);
            Dimension editorD = preferredSize(this.editor);
            editorD.height = ((editorD.height + 1) / 2 * 2);
            Dimension size = new Dimension(editorD.width, editorD.height);
            size.width += Math.max(nextD.width, previousD.width);
            Insets insets = parent.getInsets();
            size.width += insets.left + insets.right;
            size.height += insets.top + insets.bottom;
            return size;
        }

        private Dimension preferredSize(Component c) {
            return c == null ? this.ZERO_SIZE : c.getPreferredSize();
        }

        public void removeLayoutComponent(Component c) {
            if (c == this.nextButton) {
                this.nextButton = null;
            } else if (c == this.previousButton) {
                this.previousButton = null;
            } else if (c == this.editor) {
                this.editor = null;
            }
        }

        private void setBounds(Component c, int x, int y, int width, int height) {
            if (c != null) {
                c.setBounds(x, y, width, height);
            }
        }
    }

    private static final Color DISABLED_BG = UIResourceManager
            .getColor("TextDisabledBackground");

    public static ComponentUI createUI(JComponent c) {
        return new CSpinnerUI();
    }

    private LayoutManager layout;

    protected LayoutManager createLayout() {
        if (this.layout == null) {
            this.layout = new SpinnerUILayout();
        }

        return this.layout;
    }

    protected Component createNextButton() {
        JCButton button = new JCButton();
        button.setPreferredSize(new Dimension(20, -1));
        button.setRequestFocusEnabled(false);
        installNextButtonListeners(button);
        button.setImage(UIResourceManager.getImage("SpinnerNextImage"));
        button.setPressedImage(UIResourceManager
                .getImage("SpinnerNextPressedImage"));
        button.setRolloverImage(UIResourceManager
                .getImage("SpinnerNextRolloverImage"));
        button.setDisabledImage(UIResourceManager
                .getImage("SpinnerNextDisabledImage"));
        button.setIcon(UIResourceManager.getIcon("SpinnerNextIcon"));
        button.setDisabledIcon(UIResourceManager
                .getIcon("SpinnerNextDisabledIcon"));
        button.setImageInsets(2, 2, 2, 2);
        return button;
    }

    protected Component createPreviousButton() {
        JCButton button = new JCButton();
        button.setPreferredSize(new Dimension(20, -1));
        button.setRequestFocusEnabled(false);
        installPreviousButtonListeners(button);
        button.setImage(UIResourceManager.getImage("SpinnerPreviousImage"));
        button.setPressedImage(UIResourceManager
                .getImage("SpinnerPreviousPressedImage"));
        button.setRolloverImage(UIResourceManager
                .getImage("SpinnerPreviousRolloverImage"));
        button.setDisabledImage(UIResourceManager
                .getImage("SpinnerPreviousDisabledImage"));
        button.setIcon(UIResourceManager.getIcon("SpinnerPreviousIcon"));
        button.setDisabledIcon(UIResourceManager
                .getIcon("SpinnerPreviousDisabledIcon"));
        button.setImageInsets(2, 2, 2, 2);
        return button;
    }

    protected void installDefaults() {
        this.spinner.setLayout(createLayout());
    }

    private void paintBackground(Graphics g, JComponent c) {
        if ((c instanceof JCSpinner)) {
            JCSpinner spinner = (JCSpinner) c;
            UIUtil.paintBackground(g, spinner, spinner.getBackground(),
                    DISABLED_BG, spinner.getImage(), spinner.isImageOnly(),
                    spinner.getAlpha(), spinner.getVisibleInsets());
        }
    }

    protected void uninstallDefaults() {
    }

    public void update(Graphics g, JComponent c) {
        paintBackground(g, c);
        super.update(g, c);
    }
}