package com.Simp;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.java.ui.componentc.JCPasswordField;
import com.java.ui.componentc.JCTextField;
import com.sun.awt.AWTUtilities;

public class Login extends JFrame{
	
	private JPanel contentPane;
	private int point_x, point_y;
	private boolean isDragging = false;
	private JTextField user_textbox;
	private String user_textbox_text_value = "";
	private JCTextField ip_textbox;
	private String ip_textbox_text_value = "";
	private JLabel error_label;
	private JButton Login_Button;
	private JButton SignUp_Button;
	private DataBase db = new DataBase();
	
	public Login() 
	{
		OptimalFont.setUI();	//抗锯齿字体
		setUndecorated(true);	 																	//Without Border
		AWTUtilities.setWindowOpaque(this, false);													//Set Background Opaque to false
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension scrSize = Toolkit.getDefaultToolkit().getScreenSize();  							//Get the size of current screen
		setBounds(0, 0, 400, 200);	
		setLocation((scrSize.width - getWidth()) / 2, (scrSize.height - getHeight()) / 2);			//Startup location set at the center of current screen 
	
		/* Background */
		Image bg = this.getToolkit().getImage("./Pic/login_bg.png");										//Set a JPanel with a background
		contentPane = new Background(bg);
		contentPane.setOpaque(false);
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		/* Close Button */
		ImageIcon close_button_bg = new ImageIcon("./Pic/closebutton_static.png");
		ImageIcon close_button_mouseover = new ImageIcon("./Pic/closebutton_mouseover.png");
		ImageIcon close_button_pressed = new ImageIcon("./Pic/closebutton_pressed.png");
		
		JButton Close_Button = new Button(close_button_bg, close_button_mouseover, close_button_pressed, 0);
		Close_Button.setOpaque(false);
		Close_Button.setBounds(369, 6, 28, 28);
		Close_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(0);
			}
		});
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
			}
		});
		Minimize_Button.setBounds(341, 6, 28, 28);
		contentPane.add(Minimize_Button);
		
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
		
		/* user_textbox */
		user_textbox = new JCTextField();
		user_textbox.setVisible(true);
		user_textbox.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		user_textbox.setBounds(136, 60, 233, 24);
		user_textbox.getDocument().addDocumentListener(new DocumentListener(){		//Listen to the input
        	public void insertUpdate(DocumentEvent e) 
        	{
        		SignUp_Button.setEnabled(false);
        		error_label.setVisible(Check_name_input(user_textbox_text_value = user_textbox.getText()));
        		Check_enable();
            }

            public void removeUpdate(DocumentEvent e) 
            {
            	SignUp_Button.setEnabled(false);
            	error_label.setVisible(Check_name_input(user_textbox_text_value = user_textbox.getText()));
            	Check_enable();
            }
        	
        	public void changedUpdate(DocumentEvent e)
        	{
        		SignUp_Button.setEnabled(false);
        		error_label.setVisible(Check_name_input(user_textbox_text_value = user_textbox.getText()));
        		Check_enable();
            }
        });		
		contentPane.add(user_textbox);
		
		/* ip_textbox */
		ip_textbox = new JCTextField();
		ip_textbox.setVisible(true);
		ip_textbox.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		ip_textbox.setBounds(136, 100, 233, 24);
		ip_textbox.getDocument().addDocumentListener(new DocumentListener(){		//Listen to the input
        	public void insertUpdate(DocumentEvent e) 
        	{
        		SignUp_Button.setEnabled(false);
        		error_label.setVisible(Check_IP_input(ip_textbox_text_value = ip_textbox.getText()));
        		Check_enable();
            }

            public void removeUpdate(DocumentEvent e) 
            {
            	SignUp_Button.setEnabled(false);
            	error_label.setVisible(Check_IP_input(ip_textbox_text_value = ip_textbox.getText()));
            	Check_enable();
            }
        	
        	public void changedUpdate(DocumentEvent e)
        	{
        		SignUp_Button.setEnabled(false);
        		error_label.setVisible(Check_IP_input(ip_textbox_text_value = ip_textbox.getText()));
        		Check_enable();
            }
        });		
		contentPane.add(ip_textbox);
		
		/* UserName_Label */
		JLabel UserName_Label = new JLabel();
		UserName_Label.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		UserName_Label.setForeground(Color.BLACK);
		UserName_Label.setBounds(57, 55, 69, 37);
		UserName_Label.setText("Username");
		UserName_Label.setVisible(true);
		contentPane.add(UserName_Label);
		
		/* IP_Label */
		JLabel IP_Label = new JLabel();
		IP_Label.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		IP_Label.setForeground(Color.BLACK);
		IP_Label.setBounds(105, 95, 28, 37);
		IP_Label.setText("IP");
		IP_Label.setVisible(true);
		contentPane.add(IP_Label);
		
		/* error information */ 
		error_label = new JLabel("Error: ");
		error_label.setForeground(new Color(161, 0, 0));
		error_label.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		error_label.setBounds(20, 28, 359, 22);
		error_label.setVisible(false);
		contentPane.add(error_label);
		
		/* Login_Button */
		ImageIcon loginbutton_static = new ImageIcon("./Pic/loginbutton_static.png");
		ImageIcon loginbutton_mouseover = new ImageIcon("./Pic/loginbutton_mouseover.png");
		ImageIcon loginbutton_pressed = new ImageIcon("./Pic/loginbutton_pressed.png");
		Login_Button = new Button(loginbutton_static, loginbutton_mouseover, loginbutton_pressed, 1);
		Login_Button.setOpaque(false);
		Login_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if(!db.Check_isExisted(user_textbox_text_value, ip_textbox_text_value))
				{
					error_label.setText("Error: The user with this IP is not existed, you can sign up!");
					error_label.setVisible(true);
					SignUp_Button.setEnabled(true);
					return;
				}
				if(db.Check_isOnline(user_textbox_text_value, ip_textbox_text_value))
				{
					error_label.setText("The user with this IP is already online!");
					error_label.setVisible(true);
					return;
				}
				
				/* after_login part */
				setVisible(false);
				Client frame = new Client("Client", user_textbox_text_value, ip_textbox_text_value);
				frame.setVisible(true);
				Client.port_textbox.requestFocus();
				
			}
		});
		Login_Button.setBounds(88, 160, 92, 26);
		Login_Button.setEnabled(true);
		contentPane.add(Login_Button);
		
		/* SignUp_Button */
		ImageIcon signupbutton_static = new ImageIcon("./Pic/signupbutton_static.png");
		ImageIcon signupbutton_mouseover = new ImageIcon("./Pic/signupbutton_mouseover.png");
		ImageIcon signupbutton_pressed = new ImageIcon("./Pic/signupbutton_pressed.png");
		SignUp_Button = new Button(signupbutton_static, signupbutton_mouseover, signupbutton_pressed, 1);
		SignUp_Button.setOpaque(false);
		SignUp_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				db.Insert_User(user_textbox_text_value, ip_textbox_text_value);
				SignUp_Button.setEnabled(false);
				error_label.setText("Sign up successfully!");
			}
		});
		SignUp_Button.setBounds(225, 160, 92, 26);
		SignUp_Button.setEnabled(false);
		contentPane.add(SignUp_Button);
		
		
	}
	
	private boolean Check_name_input(String input)
	{
		String regex = "[a-zA-Z]+[a-zA-Z0-9]*";
		if(user_textbox_text_value.isEmpty())
		{
			error_label.setText("Error: The username can not be empty!");
			return true;
		}	
		else if(!input.matches(regex))
		{
			error_label.setText("Error: Use alphabets or numbers with alphabets first!");
			return true;
		}
		return false;
	}
	
	private boolean Check_IP_input(String input)
	{
		// 定义正则表达式
		/* Check IP */
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        if(!input.matches(regex))
        {
        	error_label.setText("Error: The IP address is illegal!");
        	return true;
        }			
        return false;
	}
	
	private boolean Check_enable()
	{
		if(Check_name_input(user_textbox_text_value) || Check_IP_input(ip_textbox_text_value) || ip_textbox_text_value.length() == 0 || user_textbox_text_value.length() == 0)
		{
			error_label.setVisible(true);
			return false;
		}
		error_label.setVisible(false);
        return true;	
	}
	
//	private boolean Check_database()
//	{	
//		if(Check_IP_input(ip_textbox_text_value) || Check_name_input(user_textbox_text_value) || user_textbox_text_value.length() == 0 || ip_textbox_text_value.length() == 0)
//		{
//			error_label.setVisible(true);
//			return false;
//		}
//        
//        /* Check isExisted */
//        if(db.Check_isExisted())
//        {
//        	error_label.setText("Error: The user has been existed!");
//        	error_label.setVisible(true);
//        	return false;
//        }
//        error_label.setVisible(false);
//        return true;	
//	}
}
