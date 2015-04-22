package com.Simp;

import java.awt.Color;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicLabelUI;

import com.java.ui.componentc.CScrollBarUI;
import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

public class ScrollBox extends JScrollPane {
	
	private Border insideBorder;
    private Border outsideBorder;
	
	protected class CScrollBar extends JScrollPane.ScrollBar {
        private static final long serialVersionUID = -8174518362746135594L;

        public CScrollBar(int orientation) {
            super(orientation);
            setUI(new CScrollBarUI());
            setOpaque(false);
            setBorder(null);
        }

        @Deprecated
        public void updateUI() {
        }
    }
	
	ScrollBox()
	{
		super(null, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	ScrollBox(JList list)
	{
		super(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		super.setBorder(new CompoundBorder(this.outsideBorder = super
                .getBorder(), this.insideBorder = new EmptyBorder(1, 1, 1, 1)));											//Clear Border
		super.setOpaque(false);
		setBackground(Color.GRAY);
        setForeground(Color.BLACK);
        setFont(UIUtil.getDefaultFont());
        setCorner("LOWER_RIGHT_CORNER", createLowerRightCorner());
	}
	
	private JComponent createLowerRightCorner() {
        JLabel label = new JLabel(
                UIResourceManager.getIcon("ScrollPaneLowerRightCornerIcon")) {
            private static final long serialVersionUID = 5657359502965563304L;

            @Deprecated
            public void updateUI() {
            }
        };
        label.setUI(new BasicLabelUI() {
            protected void installDefaults(JLabel c) {
            }
        });
        label.setOpaque(false);
        return label;
    }
}
