package com.Simp;

import javax.swing.*; 
import java.awt.*; 
 
public class Background extends JPanel{ 
    private Image img;  
    public Background(Image img) 
    { 
        this.img = img; 
        Dimension size = new Dimension(img.getWidth(null),img.getHeight(null)); 
        setSize(size); 
        //Ensure fill the panel 
        setPreferredSize(size); 
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