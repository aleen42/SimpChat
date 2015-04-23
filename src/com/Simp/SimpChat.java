package com.Simp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import com.sun.awt.AWTUtilities;

public class SimpChat extends JFrame
{
	private JPanel contentPane;
	private int point_x, point_y;
	private boolean isDragging = false;
	private boolean isUserListShown = false;
//	private TrayIcon trayIcon = null; // Icon
//	private SystemTray tray = null; // Task Bar

	private final JPanel panel = new JPanel();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					SimpChat frame = new SimpChat();
					frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SimpChat() 
	{
		panel.setLayout(null);

		setUndecorated(true);	 																	//Without Border
		AWTUtilities.setWindowOpaque(this, false);													//Set Background Opaque to false
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();  							//Get the size of current screen
		setBounds(0, 0, (int)(scrSize.width * 0.3) + 200, (int)(scrSize.height * 0.8));	
		setLocation((scrSize.width - getWidth()) / 2, (scrSize.height - getHeight()) / 2);			//Startup location set at the center of current screen 
		
		/* Background */
		Image bg = this.getToolkit().getImage("./Pic/bg.png");										//Set a JPanel with a background
		contentPane = new Background(bg);
		contentPane.setOpaque(false);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/* User List */
		
			/* JPanel */
		Image userlist_bg = this.getToolkit().getImage("./Pic/userlist_bg.png");
		JPanel userlist = new UserList(userlist_bg);
		userlist.setBounds((int)(scrSize.width * 0.3) - 4, ((int)(scrSize.height * 0.8) - 400) / 2, 200, 400);
		contentPane.add(userlist);
		
			/* Button */
		ImageIcon userlist_button_bg = new ImageIcon("./Pic/userlistbutton_static.png");
		ImageIcon userlist_button_mouseover = new ImageIcon("./Pic/userlistbutton_mouseover.png");
		ImageIcon userlist_button_pressed = new ImageIcon("./Pic/userlistbutton_pressed.png");
		JButton UserList_Button = new Button(userlist_button_bg, userlist_button_mouseover, userlist_button_pressed, 1);
		UserList_Button.setOpaque(false);
		UserList_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				Show_UserList(isUserListShown, userlist);
//				setVisible(true);	//set the Z order of JFrames
			}
		});
		UserList_Button.setBounds(379, getHeight() / 2 - 84 / 2, 28, 84);
		contentPane.add(UserList_Button);		
		
		/* Close Button */
		ImageIcon close_button_bg = new ImageIcon("./Pic/closebutton_static.png");
		ImageIcon close_button_mouseover = new ImageIcon("./Pic/closebutton_mouseover.png");
		ImageIcon close_button_pressed = new ImageIcon("./Pic/closebutton_pressed.png");
		
		JButton Close_Button = new Button(close_button_bg, close_button_mouseover, close_button_pressed, 0);
		Close_Button.setOpaque(false);
		Close_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(0);
			}
		});
		Close_Button.setBounds(379, 4, 28, 28);
		contentPane.add(Close_Button);
		
		/* Minimize Button */ 
		ImageIcon minimize_button_bg = new ImageIcon("./Pic/minimizebutton_static.png");
		ImageIcon minimize_button_mouseover = new ImageIcon("./Pic/minimizebutton_mouseover.png");
		ImageIcon minimize_button_pressed = new ImageIcon("./Pic/minimizebutton_pressed.png");
		JButton Minimize_Button = new Button(minimize_button_bg, minimize_button_mouseover, minimize_button_pressed, 0);
		Minimize_Button.setOpaque(false);
		Minimize_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				setExtendedState(ICONIFIED);
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
		Minimize_Button.setBounds(351, 4, 28, 28);
		contentPane.add(Minimize_Button);
	
//		if (SystemTray.isSupported()) 
//		{ // 如果操作系统支持托盘
//			this.tray();
//		}
		
		
		/* Drag Handler */
		this.addMouseListener(new MouseAdapter() 
		{													//Getting mouse_point and judge whether it's dragging
			public void mousePressed(MouseEvent e) 
			{
			    isDragging = true;
			    point_x = e.getX();
			    point_y = e.getY();
			}
			public void mouseReleased(MouseEvent e) 
			{
			    isDragging = false;
			}
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e) 
			{
			    if (isDragging) 
			    { 
			    	int left = getLocation().x;
			    	int top = getLocation().y;
			    	setLocation(left + e.getX() - point_x, top + e.getY() - point_y);
			    } 
			}
		}); 
	}
	
	public void Show_UserList(boolean isthere, JPanel userlist_window)
	{
		userlist_window.setVisible(!isthere);
		isUserListShown = !isthere;
	}
}
