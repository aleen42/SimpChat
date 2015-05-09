package com.java.ui.componentc;

import javax.swing.*;

public class JCSeparator extends JSeparator {
    private static final long serialVersionUID = -2404686601191054374L;

    public JCSeparator() {
        this(0);
    }

    public JCSeparator(int orientation) {
        super(orientation);
        setUI(new CSeparatorUI());
        setOpaque(false);
    }

    @Deprecated
    public void updateUI() {
    }
}