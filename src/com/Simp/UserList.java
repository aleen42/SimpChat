package com.Simp;

import javax.swing.JList;
import javax.swing.JPanel;

import java.awt.*;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.java.ui.componentc.*;

public class UserList extends JPanel {

	
	private Image img;  
	public UserList(Image img) 
    { 
        this.img = img; 
        Dimension size = new Dimension(img.getWidth(null),img.getHeight(null)); 
        setSize(new Dimension(200, 400));
        setMinimumSize(size); 
        setMaximumSize(size); 
        setLayout(null);
        
        
        
//        DefaultListModel model = new DefaultListModel(); 
        
//        list.setBounds(10, 34, 180, 356);
//        list.setOpaque(false);					//Set a transparent background
//        list.setModel(model);
        
        
        Vector<UserList_Item> rows = new Vector<UserList_Item>();
        for(int i = 1; i <= 20; i++)
        {
        	UserList_Item item = new UserList_Item("Aleen"+i);
        	rows.add(item); 
        }
        
        JList<UserList_Item> list = new JList<UserList_Item>(rows);
//        list.setBackground(Color.BLACK);
        list.setBorder(new EmptyBorder(0, 0, 0, 0));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setBounds(0, 0, 180, 700);
        list.setOpaque(false);
        list.setCellRenderer(new ListCellRenderer<UserList_Item>(){					/* Sets the delegate that is used to paint each cell in the list. */
        	@Override
        	public Component getListCellRendererComponent(
        	JList<? extends UserList_Item> list, UserList_Item value, int index,
        	boolean isSelected, boolean cellHasFocus) {
        	return value;
        	}
        	});
        JCScrollPane scroll_list = new JCScrollPane(list);
        scroll_list.setBounds(10, 40, 180, 350);
        
        add(scroll_list);
//        JCScrollPane jscrollPanel = new JCScrollPane(list);
//        jscrollPanel.setOpaque(false);
//        jscrollPanel.getViewport().setOpaque(false);
//        jscrollPanel.setBounds(10, 59, 180, 331);
//        setVisible(false);
    } 
	//Override paintComponent 
    public void paintComponent(Graphics g) 
    { 
        g.drawImage(img, 0, 0, null); 
    } 
}
