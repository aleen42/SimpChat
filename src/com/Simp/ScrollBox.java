package com.Simp;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.plaf.basic.BasicViewportUI;

import com.java.ui.componentc.*;
import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;


public class ScrollBox extends JScrollPane {
	
	private Border insideBorder;
    private Border outsideBorder;
    private float alpha;
    private Insets visibleInsets;
	
	protected class ScrollBox_ScrollBar extends JScrollPane.ScrollBar {

        public ScrollBox_ScrollBar(int orientation) {
            super(orientation);
            setUI(new CScrollBarUI(Color.WHITE, Color.WHITE));
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
		super(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		super.setBorder(new CompoundBorder(this.outsideBorder = super.getBorder(), this.insideBorder = new EmptyBorder(1, 1, 1, 1)));											//Clear Border
		super.setOpaque(false);
//		setBackground(Color.RED);
        setForeground(Color.WHITE);
        setFont(UIUtil.getDefaultFont());
        createVerticalScrollBar();
        createHorizontalScrollBar();
        createViewport();
        this.visibleInsets = new Insets(1, 1, 1, 1);				//without border
        setBorder(null);
//        getViewport().setOpaque(false);
	}
	
	public JScrollBar createVerticalScrollBar() 
	{
        return new ScrollBox_ScrollBar(1);
    }
	
	public JScrollBar createHorizontalScrollBar() 
	{
        return new ScrollBox_ScrollBar(0);
    }
	
	protected JViewport createViewport() {
        JViewport viewport = new JViewport();
        viewport.setUI(new BasicViewportUI() 
        {
            protected void installDefaults(JComponent c) 
            {
            }
        });
//        viewport.setFont(UIUtil.getDefaultFont());
//        viewport.setForeground(Color.BLACK);
//        viewport.setBackground(UIResourceManager.getEmptyColor());
//        viewport.setBackground(Color.BLACK);
        viewport.setOpaque(false);
        return viewport;
    }
	
	public void setBorder(Border border) 
	{									
        if ((border == null) && (this.visibleInsets != null)) 
        {
            this.visibleInsets.set(0, 0, 0, 0);
        }
        this.outsideBorder = border;
        super.setBorder(new CompoundBorder(this.outsideBorder, this.insideBorder));
    }
	
}
