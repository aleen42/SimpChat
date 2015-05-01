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
	private static JCTextField port_textbox;
	private JLabel error_Label;
	private JButton Send_Button;
	private JButton Connect_Button;
	private JButton Stop_Button;
	private String User_IP = "0.0.0.0";
	private String User_name = "Aleen";
	private String port_textbox_text_value = "";
	private String ip_textbox_text_value = "";
	private String send_textbox_text_value = "";
	private boolean isConnected = false;
	
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//set time format
	public static JTextArea Content;
	private Socket comsocket;
	public PrintWriter writetoserver;
	private BufferedReader readfromserver;
	private MessageThread messageThread;
	public static ArrayList<ClientThread> Clients;
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
					Client frame = new Client("Client");
					frame.setVisible(true);
					port_textbox.requestFocus();
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	Client(String User_type) 
	{
		super(User_type);
		super.Close_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if(isConnected == true)
				{
					stopConnection();
				}
				System.exit(0);
			}
		});
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
					serverConnect(ip_textbox_text_value, Integer.parseInt(port_textbox_text_value), User_name, User_IP);
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
		
		/* Close_Button */
		ImageIcon stopbutton_static = new ImageIcon("./Pic/stopbutton_static.png");
		ImageIcon stoptbutton_mouseover = new ImageIcon("./Pic/stopbutton_mouseover.png");
		ImageIcon stoptbutton_pressed = new ImageIcon("./Pic/stopbutton_pressed.png");
		Stop_Button = new Button(stopbutton_static, stoptbutton_mouseover, stoptbutton_pressed, 1);
		Stop_Button.setOpaque(false);
		Stop_Button.setVisible(false);
		Stop_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				stopConnection();
			}
		});
		Stop_Button.setBounds(224, 542, 174, 59);
		getContentPane().add(Stop_Button);
		
		/* ip_textbox */
		ip_textbox = new JCTextField();
		ip_textbox.setVisible(true);
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
//		Content.setBorder(null);
		Content.setBounds(14, 153, 382, 303);
		Content.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		getContentPane().add(Content);
//		ScrollBox scroll_Content = new ScrollBox(list);
//		getContentPane().add(scroll_Content);
		
		/* input_box */
		input_box = new JCTextField();
		input_box.setVisible(true);
		input_box.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		input_box.setBounds(14, 464, 280, 24);
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
				sendText(send_textbox_text_value);
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
	private void serverConnect(String host, int port, String name, String User_IP)
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
			messageThread = new MessageThread(readfromserver, Content);
			messageThread.start();
			
			isConnected = true;
			sendText("Connect Successfully");
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			sendText("Connect Failed");
			e.printStackTrace();
			isConnected = false;
		}
	}
	
	@SuppressWarnings("deprecation")
	public synchronized void stopConnection() {
		try {
			writetoserver.println("CLOSE");
			writetoserver.flush();
			readfromserver.close();
			writetoserver.close();
			comsocket.close();
			messageThread.stop();
			isConnected = false;
		} 
		catch (Exception e) 
		{
			sendText("Stop Connect Failed");
			e.printStackTrace();
			isConnected = true;
		}
	}
	
	public static void sendText(String string)
	{
		Content.append( df.format(new Date()) + "\t" + string + "\n");
	}
}
