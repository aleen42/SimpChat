package com.java.ui.componentc;

import com.java.ui.util.UIResourceManager;
import com.java.ui.util.UIUtil;

import sun.awt.AppContext;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;

public class CListCellRenderer extends JLabel implements ListCellRenderer, Serializable 
{
	private static final java.awt.Color mouseEnter_Font_Color = Color.BLACK;
	private static final java.awt.Color mousePressed_Font_Color = Color.WHITE;
	private static final java.awt.Color static_Font_Color = new Color(140, 140, 140);
    private static class RendererUI extends BasicLabelUI 
    {
        private static final Image BG_IMAGE = UIResourceManager
                .getImage("SelectedItemBackgroundImage");

        private static final Image BG_IMAGE_DISABLED = UIResourceManager
                .getImage("SelectedItemDisabledBackgroundImage");

        private static final Object RENDERER_UI_KEY = new Object();
        

        protected static RendererUI rendererUI = new RendererUI();

        public static ComponentUI createUI(JComponent c) {
            if (System.getSecurityManager() != null) {
                AppContext appContext = AppContext.getAppContext();
                RendererUI safeRendererUI = (RendererUI) appContext
                        .get(RENDERER_UI_KEY);

                if (safeRendererUI == null) {
                    safeRendererUI = new RendererUI();
                    appContext.put(RENDERER_UI_KEY, safeRendererUI);
                }

                return safeRendererUI;
            }

            return rendererUI;
        }

        protected void installDefaults(JLabel c) {
        }

        private void paintBackground(Graphics g, JComponent c) {
            if (((CListCellRenderer) c).selected) {
                Rectangle paintRect = new Rectangle(0, 0, c.getWidth(),
                        c.getHeight());

                JList list = ((CListCellRenderer) c).list;
                Image image;

                if ((list != null) && (!list.isEnabled())) {
                    image = BG_IMAGE_DISABLED;
                } else {
                    image = BG_IMAGE;
                }

                UIUtil.paintImage(g, image, new Insets(1, 1, 1, 1), paintRect, c);
            } else {
                g.setColor(c.getBackground());
                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        }

        public void update(Graphics g, JComponent c) {
            if (c.isOpaque()) {
                paintBackground(g, c);
            }

            paint(g, c);
        }
    }

    private static final long serialVersionUID = 3622492300888739118L;
    private JList list;

    private boolean selected;

    public CListCellRenderer() {
        setUI(new RendererUI());
        setBorder(UIResourceManager.getBorder("ListRendererBorder"));
//        this.addMouseListener(new MouseAdapter() {					/* Listen to the select */
//        	public void mouseEntered(MouseEvent e)
//        	{
//        		setForeground(mouseEnter_Font_Color);
//        	}
//        	public void mouseExited(MouseEvent e)
//        	{
//        		setForeground(static_Font_Color);
//        	}	
//		});
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
    	if ((value instanceof Icon)) {
            setIcon((Icon) value);
        } else {
            setText(value == null ? "" : value.toString());
        }

    	if(value.equals(" "))					// for empty element
    	{
    		this.selected = false;
    		return this;
    	}
    	
        this.list = list;
        this.selected = isSelected;
//        Color fg = list.getForeground();
//        if (((list instanceof JCList)) && (!list.isEnabled())) {
//            fg = ((JCList) list).getDisabledForeground();
//        }

        if ((list instanceof JCList)) {
            JCList cList = (JCList) list;
            Color color1 = cList.getRendererBackground1();
            Color color2 = cList.getRendererBackground2();
            setOpaque((isSelected)
                    || ((cList.isRendererOpaque()) && (color1 != null) && (color2 != null)));
            setBackground(index % 2 == 0 ? color1 : color2);
        } else {
            setOpaque(isSelected);
        }

        setFont(list.getFont());
        setForeground(isSelected ? mousePressed_Font_Color : static_Font_Color);
        return this;
    }

    @Deprecated
    public void updateUI() {
    }
}