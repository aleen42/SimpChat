package com.Simp;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class UIPanel extends JPanel{
	
	private Image img;
	
	public UIPanel(Image img)
	{
		/* paint background */
		this.img = img; 
        Dimension size = new Dimension(img.getWidth(null),img.getHeight(null)); 
        
        setSize(new Dimension(200, 400));
        setMinimumSize(size); 
        setMaximumSize(size); 
        setLayout(null);
	}
	
	
	//Override paintComponent 
    public void paintComponent(Graphics g) 
    { 
        g.drawImage(img, 0, 0, null); 
    } 

}
