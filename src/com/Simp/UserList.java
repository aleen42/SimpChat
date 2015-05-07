package com.Simp;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
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
	
	private String Status = "null";
	private String User_name = "Name";
	private String IPV4_adress = "0.0.0.0";
	private JPanel Add_user_panel;
	private JLabel Name_Label;
	private JLabel IPV4_Label;
	private JLabel Status_Label;
	private JTextField User_name_textbox;
	
	private static final int empty_size = 2;
	private DataBase db = new DataBase();
	private DefaultListModel listItem;
	public boolean isAddUserPanelShown = false;
	public String User_name_store = "";
	public void add_item()
	{
		listItem.addElement(User_name_store);
	}
	
	public DefaultListModel get_listItem()
	{
		return listItem;
	}
	
	public UserList(Image img, JPanel add_user_panel, JLabel name_label, JLabel ipv4_label, JLabel status_label, JTextField user_name_textbox) 
    { 
		/* paint background */
        this.img = img; 
        this.Add_user_panel = add_user_panel;
        this.Name_Label = name_label;
        this.IPV4_Label = ipv4_label;
        this.Status_Label = status_label;
        this.User_name_textbox = user_name_textbox;
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
				update_AddUser_Label(isAddUserPanelShown);
				isAddUserPanelShown = !isAddUserPanelShown;
				User_name_textbox.requestFocus();							//the component gets focus
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
				update_UserName_Label(false);	
				update_IPV4_Label(false);
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
        list = new JList(listItem);
        list.setCellRenderer(new CListCellRenderer());
        list.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
//        UpdateList();
        list.addMouseListener(new MouseAdapter() {					/* Listen to the select */
        	public void mouseReleased(MouseEvent e)
        	{
        		if(list.getSelectedValue().toString().equals(" "))
        		{
        			update_UserName_Label(false);
        			update_IPV4_Label(false);
        			update_Status_Label(false);
        			return;
        		}
        		User_name = list.getSelectedValue().toString().substring(0, list.getSelectedValue().toString().indexOf("/"));
        		update_UserName_Label(true);
        		IPV4_adress = list.getSelectedValue().toString().substring(list.getSelectedValue().toString().indexOf("/") + 1, list.getSelectedValue().toString().length());
        		update_IPV4_Label(true);
        		Status = db.get_status(User_name, IPV4_adress);
        	    update_Status_Label(true);
        	    
        	}
        	
		});
        ScrollBox scroll_list = new ScrollBox(list);
//        JCScrollPane scroll_list = new JCScrollPane(list);
        scroll_list.setBounds(20, 40, 170, 300);

        add(scroll_list);
//        SwingUtilities.updateComponentTreeUI (scroll_list);
          
        setVisible(false);
    } 
	
	public void ClearList()
	{
		listItem.clear();
	}
	
//	public void UpdateList()
//	{
//		listItem.clear();
//		for(int i = 1; i <= empty_size; i++)
//	    {
//			listItem.addElement(" ");
//	    }
//		db.get_User_name(listItem);
//		for(int i = 1; i <= empty_size; i++)
//	    {
//			listItem.addElement(" ");
//	    }
//		update_UserName_Label(false);
//		update_IPV4_Label(false);
//		update_Status_Label(false);
//		
//	}
	//Override paintComponent 
    public void paintComponent(Graphics g) 
    { 
        g.drawImage(img, 0, 0, null); 
    } 
    
    public void update_AddUser_Label(boolean visible)
	{
		Add_user_panel.setVisible(!visible);
	}
	
	public void update_UserName_Label(boolean visible)
	{
		Name_Label.setVisible(visible);
		Name_Label.setText(User_name);
	}
	
	public void update_Status_Label(boolean visible)
	{
		Status_Label.setVisible(visible);
		Status_Label.setText("(" + Status + ")");
	}
	
	public void update_IPV4_Label(boolean visible)
	{
		IPV4_Label.setVisible(visible);
		IPV4_Label.setText("IP: " + IPV4_adress);
	}
}
