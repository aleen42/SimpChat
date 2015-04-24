package com.Simp;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Button extends JButton 
{ 
	
    public Button(ImageIcon icon, ImageIcon mouseover, ImageIcon pressed, int type) 
    { 
        setSize(icon.getImage().getWidth(null),icon.getImage().getHeight(null)); 
        setIcon(icon); 
        setMargin(new Insets(0,0,0,0)); 
        setIconTextGap(0); 
        setBorderPainted(false); 
        setBorder(null); 
        setText(null); 
        
        addMouseListener(new MouseAdapter() {		
        	public void mouseEntered(MouseEvent e)
        	{
				setIcon(mouseover);
        	}
        	public void mouseExited(MouseEvent e)
        	{
        		setIcon(icon);
        	}	
			public void mousePressed(MouseEvent e) 
			{
				setIcon(pressed);	
			}
			public void mouseReleased(MouseEvent e) 
			{
				switch(type)
				{
				case 0: 
					setIcon(icon);	
					break;
				case 1:
					setIcon(mouseover);
					break;
				default:
					break;
				}
				
			}
		});
    } 
}