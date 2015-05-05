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

import javax.swing.GroupLayout.Alignment;

public class UserList extends JPanel {
	
	private Image img;
	private JList list;
	private int index;
	
	private static final int empty_size = 2;
	private DataBase db = new DataBase();
	private static DefaultListModel listItem;
	public static String User_name_store;
	public static boolean isAddUserPanelShown = false;
	public static void add_item()
	{
		listItem.addElement(User_name_store);
	}
	
	public UserList(Image img) 
    { 
		/* paint background */
        this.img = img; 
        Dimension size = new Dimension(img.getWidth(null),img.getHeight(null)); 
        setSize(new Dimension(533, 400));
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
				SimpChat.update_AddUser_Label(isAddUserPanelShown);
				isAddUserPanelShown = !isAddUserPanelShown;
				SimpChat.User_name_textbox.requestFocus();							//the component gets focus
			}
		});
		Useradd_Button.setBounds(128, 361, 25, 29);
		Useradd_Button.setVisible(false);
		add(Useradd_Button);
		
		/* UserDelete Button */
		ImageIcon userdelete_button_bg = new ImageIcon("./Pic/UserDelete_static.png");
		ImageIcon userdelete_button_mouseover = new ImageIcon("./Pic/UserDelete_mouseover.png");
		ImageIcon userdelete_button_pressed = new ImageIcon("./Pic/UserDelete_pressed.png");
		JButton Userdelete_Button = new Button(userdelete_button_bg, userdelete_button_mouseover, userdelete_button_pressed, 1);
		Userdelete_Button.setOpaque(false);
		Userdelete_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{								
				if(index - empty_size + 1 <= 0)//no select
					return;
				db.delet_User(index - empty_size + 1);
				listItem.remove(index);							//delete selected user
				/* hide label */
				SimpChat.update_UserName_Label(false);	
				SimpChat.update_IPV4_Label(false);
				index = 0;
			}
		});
		Userdelete_Button.setVisible(false);
		Userdelete_Button.setBounds(165, 361, 25, 29);
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

        /* List_panel */
        listItem = new DefaultListModel();
        UpdateList();
        list = new JList(listItem);
        list.setCellRenderer(new CListCellRenderer());
        list.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
        list.addMouseListener(new MouseAdapter() {					/* Listen to the select */
        	public void mouseReleased(MouseEvent e)
        	{
        		index = list.getSelectedIndex();
        		SimpChat.User_name = list.getSelectedValue().toString();
        		if(SimpChat.User_name == " ")
        		{
        			SimpChat.update_UserName_Label(false);
        			SimpChat.update_IPV4_Label(false);
        			SimpChat.update_Status_Label(false);
        			return;
        		}	
        		SimpChat.update_UserName_Label(true);
        		SimpChat.IPV4_adress = db.get_ipv4(index - empty_size + 1);
        		SimpChat.update_IPV4_Label(true);
        		SimpChat.Status = db.get_status(index - empty_size + 1);
        		SimpChat.update_Status_Label(true);
        	}
        	
		});
        ScrollBox scroll_list = new ScrollBox(list);
//        JCScrollPane scroll_list = new JCScrollPane(list);
        scroll_list.setBounds(20, 40, 170, 300);
//        scroll_list.setImage(img);
        add(scroll_list);
        SwingUtilities.updateComponentTreeUI (scroll_list);
          
        setVisible(false);
    } 
	
	
	public void UpdateList()
	{
		listItem.clear();
		for(int i = 1; i <= empty_size; i++)
	    {
			listItem.addElement(" ");
	    }
		db.get_User_name();
		for(int i = 1; i <= empty_size; i++)
	    {
			listItem.addElement(" ");
	    }
	}
	//Override paintComponent 
    public void paintComponent(Graphics g) 
    { 
        g.drawImage(img, 0, 0, null); 
    } 
}
