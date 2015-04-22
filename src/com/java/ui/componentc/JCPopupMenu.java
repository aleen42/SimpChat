package com.java.ui.componentc;

import com.java.ui.util.UIUtil;
import com.sun.awt.AWTUtilities;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class JCPopupMenu extends JPopupMenu {
    public static class Separator extends JCSeparator {
        private static final long serialVersionUID = 3460403283019419638L;

        public Separator() {
            setBorder(new EmptyBorder(1, 26, 0, 2));
        }
    }

    private static final long serialVersionUID = -3145232751724445736L;
    private JWindow heavyWeightWindow;

    private boolean optimizeTextWhenTransparent;

    public JCPopupMenu() {
        this(null);
    }

    public JCPopupMenu(String label) {
        super(label);
        setUI(new CPopupMenuUI());
        setOpaque(false);
        setBorder(new EmptyBorder(7, 7, 7, 7));
        setOptimizeTextWhenTransparent((!System.getProperty("os.name").toLowerCase().startsWith("windows")) ||
                (System.getProperty("os.version").compareTo("6") < 0));
    }

    public void addSeparator() {
        add(new Separator());
    }

    private JWindow getHeavyWeightWindow() {
        Container c = getParent();

        while ((c != null) && (!(c instanceof Window))) {
            c = c.getParent();
        }

        if ((c != null) && (c.getName().equalsIgnoreCase("###overrideRedirect###"))) {
            return (JWindow) c;
        }

        return null;
    }

    public boolean isOptimizeTextWhenTransparent() {
        return this.optimizeTextWhenTransparent;
    }

    public void setOptimizeTextWhenTransparent(boolean optimizeTextWhenTransparent) {
        this.optimizeTextWhenTransparent = optimizeTextWhenTransparent;
    }

    public void setVisible(boolean visible) {
        if (visible == isVisible()) {
            return;
        }

        super.setVisible(visible);

        if ((visible) && ((this.heavyWeightWindow = getHeavyWeightWindow()) != null) && (this.optimizeTextWhenTransparent)) {
            UIUtil.optimizeTextWithChildren(this);
        }

        if (this.heavyWeightWindow != null) {
            if ((!visible) && (this.optimizeTextWhenTransparent)) {
                UIUtil.undoOptimizeTextWithChildren(this);
            }

            AWTUtilities.setWindowOpaque(this.heavyWeightWindow, !visible);

            if (!visible) {
                this.heavyWeightWindow = null;
            }
        }
    }

    @Deprecated
    public void updateUI() {
        setUI(getUI());
    }
}