package com.Simp;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.java.ui.componentc.*;

public class UserList extends JPanel {

	
	private Image img;
	private DefaultListModel listItem;
	private JList list;
	
	
	public UserList(Image img) 
    { 
        this.img = img; 
        Dimension size = new Dimension(img.getWidth(null),img.getHeight(null)); 
        setSize(new Dimension(200, 400));
        setMinimumSize(size); 
        setMaximumSize(size); 
        setLayout(null);
        
        /* UserAdd Button */
		ImageIcon useradd_button_bg = new ImageIcon("./Pic/UserAdd_static.png");
		ImageIcon useradd_button_mouseover = new ImageIcon("./Pic/UserAdd_mouseover.png");
		ImageIcon useradd_button_pressed = new ImageIcon("./Pic/UserAdd_pressed.png");
		JButton Useradd_Button = new Button(useradd_button_bg, useradd_button_mouseover, useradd_button_pressed, 1);
		Useradd_Button.setOpaque(false);
		Useradd_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
//				setExtendedState(ICONIFIED);
//				try 
//				{
//					tray.add(trayIcon); // 将托盘图标添加到系统的托盘实例中
////					setVisible(false); // 使窗口不可视
//					dispose();
//			    } 
//				catch (AWTException e) 
//				{
//					e.printStackTrace();
//			    }
			}
		});
		Useradd_Button.setBounds(112, 41, 25, 29);
		add(Useradd_Button);
		
		/* UserDelete Button */
		ImageIcon userdelete = new ImageIcon("./Pic/UserDelete_static.png");
		ImageIcon userdelete_button_mouseover = new ImageIcon("./Pic/UserDelete_mouseover.png");
		ImageIcon userdelete_button_pressed = new ImageIcon("./Pic/UserDelete_pressed.png");
		JButton Userdelete_Button = new Button(useradd_button_bg, useradd_button_mouseover, useradd_button_pressed, 1);
		Userdelete_Button.setOpaque(false);
		Userdelete_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
//				setExtendedState(ICONIFIED);
//				try 
//				{
//					tray.add(trayIcon); // 将托盘图标添加到系统的托盘实例中
////					setVisible(false); // 使窗口不可视
//					dispose();
//			    } 
//				catch (AWTException e) 
//				{
//					e.printStackTrace();
//			    }
			}
		});
		Userdelete_Button.setBounds(149, 41, 25, 29);
		add(Userdelete_Button);
        
//        DefaultListModel model = new DefaultListModel(); 
//        for(int i = 1; i <= 20; i++)
//        {
//        	UserList_Item item = new UserList_Item("Aleen"+i);
//        	model.addElement(item);
//        }
//        JCList list = new JCList();
//        list.setBounds(10, 34, 180, 356);
//        list.setOpaque(false);					//Set a transparent background
//        list.setModel(model);
//        Vector<UserList_Item> rows = new Vector<UserList_Item>();
//        JList<UserList_Item> list = new JList<UserList_Item>(rows);
//        
//              
//        
//        
////        
////        list.setBackground(Color.BLACK);
//        list.setBorder(new EmptyBorder(0, 0, 0, 0));
//        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
////        list.setBounds(0, 0, 180, 700);
//        list.setOpaque(false);
//        list.setCellRenderer(new ListCellRenderer<UserList_Item>(){					/* Sets the delegate that is used to paint each cell in the list. */
//        	@Override
//        	public Component getListCellRendererComponent(
//        	JList<? extends UserList_Item> list, UserList_Item value, int index,
//        	boolean isSelected, boolean cellHasFocus) {
//        	return value;
//        	}
//        	});
        listItem = new DefaultListModel();
        UpdateList();
        list = new JList(listItem);
        list.setCellRenderer(new CListCellRenderer());
        list.addMouseListener(new MouseAdapter() {					/* Listen to the select */
        	public void mouseReleased(MouseEvent e)
        	{
        		SimpChat.User_name = list.getSelectedValue().toString();
        		SimpChat.update_UserName_Label();
        	}
        	
		});
        ScrollBox scroll_list = new ScrollBox(list);
//        JCScrollPane scroll_list = new JCScrollPane(list);
        scroll_list.setBounds(10, 80, 180, 310);
//        scroll_list.setImage(img);
        add(scroll_list);
        SwingUtilities.updateComponentTreeUI (scroll_list);
//        JCScrollPane jscrollPanel = new JCScrollPane(list);
//        jscrollPanel.setOpaque(false);
//        jscrollPanel.getViewport().setOpaque(false);
//        jscrollPanel.setBounds(10, 59, 180, 331);
        setVisible(false);
    } 
	
	
	protected void UpdateList()
	{
		for(int i = 1; i <= 20; i++)
	    {
			listItem.addElement("Aleen" + i);
	    }
	}
	//Override paintComponent 
    public void paintComponent(Graphics g) 
    { 
        g.drawImage(img, 0, 0, null); 
    } 
    
    
}
