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
	private TrayIcon trayIcon = null; // Icon
	private SystemTray tray = null; // Task Bar
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

		setUndecorated(true);	 																	//Without Border
		AWTUtilities.setWindowOpaque(this, false);													//Set Background Opaque to false
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();  							//Get the size of current screen
		setBounds(100, 100, (int)(scrSize.width * 0.3), (int)(scrSize.height * 0.8));	
		setLocation((scrSize.width - getWidth()) / 2, (scrSize.height - getHeight()) / 2);			//Startup location set at the center of current screen 
		
		Image bg = this.getToolkit().getImage("./Pic/bg.png");										//Set a JPanel with a background
		contentPane = new Background(bg);
		contentPane.setOpaque(false);
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		/* Close Button */
		ImageIcon close_button_bg = new ImageIcon("./Pic/closebutton_static.png");
		ImageIcon close_button_mouseover = new ImageIcon("./Pic/closebutton_mouseover.png");
		ImageIcon close_button_pressed = new ImageIcon("./Pic/closebutton_pressed.png");
		
		JButton Close_Button = new Button(close_button_bg, close_button_mouseover, close_button_pressed);
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
		JButton Minimize_Button = new Button(minimize_button_bg, minimize_button_mouseover, minimize_button_pressed);
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
	
//	void tray() 		
//	{
//		tray = SystemTray.getSystemTray(); // 获得本操作系统托盘的实例
//		ImageIcon icon = new ImageIcon("./Pic/closebutton_static.png"); // 将要显示到托盘中的图标
//		
//		PopupMenu pop = new PopupMenu(); // 构造一个右键弹出式菜单
//		MenuItem show = new MenuItem("打开程序");
//		MenuItem exit = new MenuItem("退出程序");
//		pop.add(show);
//		pop.add(exit);
//		trayIcon = new TrayIcon(icon.getImage(), "托盘", pop);
//
//		/* 添加鼠标监听器，当鼠标在托盘图标上双击时，默认显示窗口 */
//		trayIcon.addMouseListener(new MouseAdapter() 
//		{
//			public void mouseClicked(MouseEvent e) 
//			{
//			    if (e.getClickCount() == 2) 
//			    { // 鼠标双击
//			    	tray.remove(trayIcon); // 从系统的托盘实例中移除托盘图标
//			    	setExtendedState(JFrame.NORMAL);
//			    	setVisible(true); // 显示窗口
//			    	toFront();
//			    }
//		   }
//		});
//		show.addActionListener(new ActionListener() 
//		{ // 点击“显示窗口”菜单后将窗口显示出来
//			public void actionPerformed(ActionEvent e) 
//			{
//				tray.remove(trayIcon); // 从系统的托盘实例中移除托盘图标
//				setExtendedState(JFrame.NORMAL);
//				setVisible(true); // 显示窗口
//				toFront();
//		    }
//		});
//		exit.addActionListener(new ActionListener() 
//		{ // 点击“退出演示”菜单后退出程序
//			public void actionPerformed(ActionEvent e) 
//			{
//				System.exit(0); // 退出程序
//		    }
//		});
//	}

}
