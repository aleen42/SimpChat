package com.Simp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.java.ui.componentc.JCTextArea;
import com.java.ui.componentc.JCTextField;

import javax.swing.JTextArea;

public class Client extends SimpChat{
	
	private JTextField input_box;
	private JTextField ip_textbox;
	public static JCTextField port_textbox;
	private JLabel error_Label;
	private JLabel name_Label;
	private JLabel ipv4_Label;
	private JButton Send_Button;
	private JButton Connect_Button;
	private JButton Disconnect_Button;
	private String User_IP = "0.0.0.0";
	private String User_name = "Aleen";
	private String port_textbox_text_value = "";
	private String ip_textbox_text_value = "";
	private String send_textbox_text_value = "";
	private boolean isConnected = false;
	private UserList userlist;
	private DataBase db = new DataBase();
	
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//set time format
	public static JTextArea Content;
	private Socket comsocket;
	private PrintWriter writetoserver;
	private BufferedReader readfromserver;
	private MessageThread messageThread;
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
					Login login = new Login();
					login.setVisible(true);
					
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	Client(String User_type, String username, String IP) 
	{
		super(User_type);
		super.Close_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if(isConnected == true)
				{
					boolean flag = stopConnection();
					if(flag == false)
					{
						sendText("Disconnect Failed!");
						return;
					}
				}
				System.exit(0);
			}
		});
		
		this.userlist = super.userlist;
		this.User_name = username;
		this.User_IP = IP;
		this.name_Label = super.Name_Label;
		this.ipv4_Label = super.IPV4_Label;
		
		/* error information */ 
		error_Label = new JLabel("Error: ");
		error_Label.setForeground(new Color(161, 0, 0));
		error_Label.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		error_Label.setBounds(38, 518, 360, 22);
		error_Label.setVisible(false);
		getContentPane().add(error_Label);
		
		/* Connect_Button */
		ImageIcon connectbutton_static = new ImageIcon("./Pic/connectbutton_static.png");
		ImageIcon connectbutton_mouseover = new ImageIcon("./Pic/connectbutton_mouseover.png");
		ImageIcon connectbutton_pressed = new ImageIcon("./Pic/connectbutton_pressed.png");
		Connect_Button = new Button(connectbutton_static, connectbutton_mouseover, connectbutton_pressed, 1);
		Connect_Button.setOpaque(false);
		Connect_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				try 
				{
					if (isConnected) {
						sendText("You has connected before");
						return;
					}
					boolean flag = serverConnect(ip_textbox_text_value, Integer.parseInt(port_textbox_text_value), User_name, User_IP);
					if(flag == true)
					{
//						System.out.println("Connect Succeed\n");
						sendText("Connect Succeed!");
						ip_textbox.setEditable(false);
						ip_textbox.setFocusable(false);
						port_textbox.setEditable(false);
						port_textbox.setFocusable(false);
						Connect_Button.setVisible(false);
						Disconnect_Button.setVisible(true);
//						userlist.UpdateList();
					}
					else
					{
//						System.out.println("Connect Failed\n");
						sendText("Connect Failed!");
					}
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		});
		Connect_Button.setBounds(224, 542, 174, 59);
		Connect_Button.setEnabled(false);
		getContentPane().add(Connect_Button);
		
		/* Disconnect_Button */
		ImageIcon disconnectbutton_static = new ImageIcon("./Pic/disconnectbutton_static.png");
		ImageIcon disconnectbutton_mouseover = new ImageIcon("./Pic/disconnectbutton_mouseover.png");
		ImageIcon disconnectbutton_pressed = new ImageIcon("./Pic/disconnectbutton_pressed.png");
		Disconnect_Button = new Button(disconnectbutton_static, disconnectbutton_mouseover, disconnectbutton_pressed, 1);
		Disconnect_Button.setOpaque(false);
		Disconnect_Button.setVisible(false);
		Disconnect_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				boolean flag = stopConnection();
				if(flag == true)
				{
//					System.out.println("Disconnect Succeed!\n");
				}
				else
				{
					System.out.println("Disconnect Failed!\n");
					sendText("Disconnect Failed!");
				}
			}
		});
		Disconnect_Button.setBounds(224, 542, 174, 59);
		getContentPane().add(Disconnect_Button);
		
		/* ip_textbox */
		ip_textbox = new JCTextField();
		ip_textbox.setVisible(true);
//		ip_textbox.setText("127.0.0.1");
//		num_limit_textbox.setEditable(false);
//		num_limit_textbox.setFocusable(false);
		ip_textbox.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		ip_textbox.setBounds(104, 575, 110, 24);
		ip_textbox.getDocument().addDocumentListener(new DocumentListener(){		//Listen to the input
        	public void insertUpdate(DocumentEvent e) 
        	{
        		error_Label.setVisible(Check_ip_input(ip_textbox_text_value = ip_textbox.getText()));
        		Connect_Button.setEnabled(Check_enable());
            }

            public void removeUpdate(DocumentEvent e) 
            {
            	error_Label.setVisible(Check_ip_input(ip_textbox_text_value = ip_textbox.getText()));
            	Connect_Button.setEnabled(Check_enable());
            }
        	
        	public void changedUpdate(DocumentEvent e)
        	{
        		error_Label.setVisible(Check_ip_input(ip_textbox_text_value = ip_textbox.getText()));
        		Connect_Button.setEnabled(Check_enable());
            }
        });
		getContentPane().add(ip_textbox);
		
		/* port_textbox */
		port_textbox = new JCTextField();
//		port_textbox.setText("6666");
		port_textbox.setVisible(true);
//		port_textbox.setEditable(false);
//		port_textbox.setFocusable(false);
		port_textbox.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		port_textbox.setBounds(104, 544, 110, 24);
		port_textbox.getDocument().addDocumentListener(new DocumentListener(){		//Listen to the input
        	public void insertUpdate(DocumentEvent e) 
        	{
        		error_Label.setVisible(Check_port_input(port_textbox_text_value = port_textbox.getText()));
        		Connect_Button.setEnabled(Check_enable());
            }

            public void removeUpdate(DocumentEvent e) 
            {
            	error_Label.setVisible(Check_port_input(port_textbox_text_value = port_textbox.getText()));
            	Connect_Button.setEnabled(Check_enable());
            }
        	
        	public void changedUpdate(DocumentEvent e)
        	{
        		error_Label.setVisible(Check_port_input(port_textbox_text_value = port_textbox.getText()));
        		Connect_Button.setEnabled(Check_enable());
            }
        });		
		getContentPane().add(port_textbox);
		
		/* IP_Label */
		JLabel Num_limit_Label = new JLabel();
		Num_limit_Label.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		Num_limit_Label.setForeground(Color.BLACK);
		Num_limit_Label.setBounds(87, 570, 41, 37);
		Num_limit_Label.setText("IP");
		Num_limit_Label.setVisible(true);
		getContentPane().add(Num_limit_Label);
		
		/* Port_Label */
		JLabel Port_Label = new JLabel();
		Port_Label.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		Port_Label.setForeground(Color.BLACK);
		Port_Label.setBounds(74, 538, 50, 37);
		Port_Label.setText("Port");
		Port_Label.setVisible(true);
		getContentPane().add(Port_Label);
		
		/* Content */
		Content = new JCTextArea();
//		Content.setBackground(Color.BLACK);
//		Content.setEditable(false);
//		Content.setFocusable(false);
		Content.setBorder(null);
		Content.setBounds(14, 153, 382, 303);
		Content.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		ScrollBox scroll_Content = new ScrollBox(Content);
		scroll_Content.setBounds(14, 153, 382, 303);
		getContentPane().add(scroll_Content);
		
		/* input_box */
		input_box = new JCTextField();
		input_box.setVisible(true);
		input_box.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		input_box.setBounds(14, 464, 280, 24);
		input_box.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String reserve = "IP: ";
				sendText(send_textbox_text_value);
				if(!name_Label.isVisible())
				{
					writetoserver.println("PUBLIC_MESSAGE@" + send_textbox_text_value);
					writetoserver.flush();
				}
				else
				{
					writetoserver.println("PRIVATE_MESSAGE@" + name_Label.getText() + "@" + ipv4_Label.getText().substring(reserve.length(), ipv4_Label.getText().length()) + "@" + send_textbox_text_value);
					writetoserver.flush();
				}
				input_box.setText("");														//clear input_box
				input_box.requestFocus();
			}
		});
		
		input_box.getDocument().addDocumentListener(new DocumentListener(){		//Listen to the input
        	public void insertUpdate(DocumentEvent e) 
        	{
        		send_textbox_text_value = input_box.getText();
        		if(isConnected == false)
        			return;
        		Send_Button.setEnabled(Check_send());
            }

            public void removeUpdate(DocumentEvent e) 
            {
            	send_textbox_text_value = input_box.getText();
            	if(isConnected == false)
        			return;
            	Send_Button.setEnabled(Check_send());
            }
        	
        	public void changedUpdate(DocumentEvent e)
        	{
        		send_textbox_text_value = input_box.getText();
        		if(isConnected == false)
        			return;
        		Send_Button.setEnabled(Check_send());
            }
        });	
		getContentPane().add(input_box);

		/* Send_Button */
		ImageIcon sendbutton_static = new ImageIcon("./Pic/sendbutton_static.png");
		ImageIcon sendbutton_mouseover = new ImageIcon("./Pic/sendbutton_mouseover.png");
		ImageIcon sendbutton_pressed = new ImageIcon("./Pic/sendbutton_pressed.png");
		Send_Button = new Button(sendbutton_static, sendbutton_mouseover, sendbutton_pressed, 1);
		Send_Button.setOpaque(false);
		Send_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				String reserve = "IP: ";
				sendText(send_textbox_text_value);
				if(!name_Label.isVisible())
				{
					writetoserver.println("PUBLIC_MESSAGE@" + send_textbox_text_value);
					writetoserver.flush();
				}
				else
				{
					writetoserver.println("PRIVATE_MESSAGE@" + name_Label.getText() + "@" + ipv4_Label.getText().substring(reserve.length(), ipv4_Label.getText().length()) + "@" + send_textbox_text_value);
					writetoserver.flush();
				}
				
				input_box.setText("");														//clear input_box
				input_box.requestFocus();
			}
		});
		Send_Button.setBounds(302, 463, 92, 26);
		Send_Button.setEnabled(false);
		getContentPane().add(Send_Button);
	}
	
	private boolean Check_ip_input(String input)
	{
		/* Check IP */
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        if(!input.matches(regex))
        {
        	error_Label.setText("Error: The IP address is illegal!");
        	return true;
        }			
        return false;
	}
	
	private boolean Check_port_input(String input)
	{
		if(input.length() == 0)
		{
			error_Label.setText("Error: The port can not be empty!");
			return true;
		}
		if(input.substring(0, 1).compareTo("-") == 0)
		{
			System.out.println("1");
			error_Label.setText("Error: The port can not be minus!");
			return true;
		}
		if(input.indexOf(".") > 0)
		{
			error_Label.setText("Error: The port should be an integer!");
			return true;
		}
		if(Integer.parseInt(input) < 1024 || Integer.parseInt(input) > 65535)
		{
			error_Label.setText("Error: The port should be between 1024 and 65535!");
			return true;
		}
		return false;
	}
	
	private boolean Check_enable()
	{
		if(Check_port_input(port_textbox_text_value) || Check_ip_input(ip_textbox_text_value) || ip_textbox_text_value.length() == 0 || port_textbox_text_value.length() == 0)
		{
			error_Label.setVisible(true);
			return false;
		}
        error_Label.setVisible(false);
        return true;	
	}
	
	private boolean Check_send()
	{
		if(send_textbox_text_value.length() == 0)
			return false;
		return true;
	}
	
	//Start the Server
	private synchronized boolean serverConnect(String host, int port, String name, String User_IP)
	{
		try 
		{
			comsocket = new Socket(host, port);															//Connect to host:port
			writetoserver = new PrintWriter(comsocket.getOutputStream());
			readfromserver = new BufferedReader(new InputStreamReader(comsocket.getInputStream()));
			
			
			writetoserver.println(name);
			writetoserver.flush();
			writetoserver.println(User_IP);
			writetoserver.flush();
			
			//Thread to get message
			messageThread = new MessageThread(readfromserver, Content, userlist, this);
			messageThread.start();
			
			isConnected = true;
			db.Update_status(User_name, User_IP, "online");
			return true;
//			sendText("Connect Successfully");
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			isConnected = false;
			return false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public synchronized boolean stopConnection() {
		try {
			System.out.println("close");
			writetoserver.println("CLOSE@");
			writetoserver.flush();
			readfromserver.close();
			writetoserver.close();
			comsocket.close();
			isConnected = false;
			db.Update_status(User_name, User_IP, "offline");
			userlist.get_listItem().clear();
			super.clear_user_info();
			sendText("Disconnect Succeed!");
			ip_textbox.setEditable(true);
			ip_textbox.setFocusable(true);
			port_textbox.setEditable(true);
			port_textbox.setFocusable(true);
			Disconnect_Button.setVisible(false);
			Connect_Button.setVisible(true);
			messageThread.stop();
			return true;
		} 
		catch (Exception e) 
		{
			sendText("Disconnect Failed");
			e.printStackTrace();
			isConnected = true;
			return false;
		}
	}
	
	public static void sendText(String string)
	{
		Content.append( df.format(new Date()) + "\t" + string + "\n");
	}
}
