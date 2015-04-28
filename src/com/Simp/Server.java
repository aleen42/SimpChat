package com.Simp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
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

public class Server extends SimpChat{
	
	private JTextField input_box;
	private JTextField num_limit_textbox;
	private static JCTextField port_textbox;
	private JLabel error_Label;
	private JButton Send_Button;
	private JButton Start_Button;
	private JButton Stop_Button;
	private String port_textbox_text_value = "";
	private String num_limit_textbox_text_value = "";
	private String send_textbox_text_value = "";
	private boolean isStarted = false;
	
	private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	//set time format
	public static JTextArea Content;
	private ServerSocket serverSocket;
	private ServerThread serverThread;
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
					Server frame = new Server("Server");
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
	
	Server(String User_type) 
	{
		super(User_type);
		
		/* error information */ 
		error_Label = new JLabel("Error: ");
		error_Label.setForeground(new Color(161, 0, 0));
		error_Label.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		error_Label.setBounds(38, 518, 360, 22);
		error_Label.setVisible(false);
		getContentPane().add(error_Label);
		
		/* Start_Button */
		ImageIcon startbutton_static = new ImageIcon("./Pic/startbutton_static.png");
		ImageIcon startbutton_mouseover = new ImageIcon("./Pic/startbutton_mouseover.png");
		ImageIcon startbutton_pressed = new ImageIcon("./Pic/startbutton_pressed.png");
		Start_Button = new Button(startbutton_static, startbutton_mouseover, startbutton_pressed, 1);
		Start_Button.setOpaque(false);
		Start_Button.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				if (isStarted) 						//The server has been started
				{
					error_Label.setText("Error: The server has been started!");
					error_Label.setVisible(true);
					return;
				}
				try 
				{
					serverStart(Integer.parseInt(num_limit_textbox_text_value), Integer.parseInt(port_textbox_text_value));
					sendText("The Server has started!");
					num_limit_textbox.setEditable(false);
					num_limit_textbox.setFocusable(false);
					port_textbox.setEditable(false);
					port_textbox.setFocusable(false);
					Start_Button.setVisible(false);
					Stop_Button.setVisible(true);
				} 
				catch (BindException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				catch (NumberFormatException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		Start_Button.setBounds(224, 542, 174, 59);
		Start_Button.setEnabled(false);
		getContentPane().add(Start_Button);
		
		/* Stop_Button */
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
				if (!isStarted) {
					error_Label.setText("The Server has not been started yet!");
					error_Label.setVisible(true);
					Start_Button.setVisible(true);
					Stop_Button.setVisible(false);
					return;
				}	
				serverClose();
				sendText("The Server has stopped!");
				Start_Button.setVisible(true);
				Stop_Button.setVisible(false);
				num_limit_textbox.setEditable(true);
				num_limit_textbox.setFocusable(true);
				port_textbox.setEditable(true);
				port_textbox.setFocusable(true);
			}
		});
		Stop_Button.setBounds(224, 542, 174, 59);
		getContentPane().add(Stop_Button);
		
		/* num_limit_textbox */
		num_limit_textbox = new JCTextField();
		num_limit_textbox.setVisible(true);
//		num_limit_textbox.setEditable(false);
//		num_limit_textbox.setFocusable(false);
		num_limit_textbox.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 13));
		num_limit_textbox.setBounds(104, 575, 110, 24);
		num_limit_textbox.getDocument().addDocumentListener(new DocumentListener(){		//Listen to the input
        	public void insertUpdate(DocumentEvent e) 
        	{
        		error_Label.setVisible(Check_num_input(num_limit_textbox_text_value = num_limit_textbox.getText()));
        		Start_Button.setEnabled(Check_enable());
            }

            public void removeUpdate(DocumentEvent e) 
            {
            	error_Label.setVisible(Check_num_input(num_limit_textbox_text_value = num_limit_textbox.getText()));
            	Start_Button.setEnabled(Check_enable());
            }
        	
        	public void changedUpdate(DocumentEvent e)
        	{
        		error_Label.setVisible(Check_num_input(num_limit_textbox_text_value = num_limit_textbox.getText()));
        		Start_Button.setEnabled(Check_enable());
            }
        });
		getContentPane().add(num_limit_textbox);
		
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
        		Start_Button.setEnabled(Check_enable());
            }

            public void removeUpdate(DocumentEvent e) 
            {
            	error_Label.setVisible(Check_port_input(port_textbox_text_value = port_textbox.getText()));
            	Start_Button.setEnabled(Check_enable());
            }
        	
        	public void changedUpdate(DocumentEvent e)
        	{
        		error_Label.setVisible(Check_port_input(port_textbox_text_value = port_textbox.getText()));
        		Start_Button.setEnabled(Check_enable());
            }
        });		
		getContentPane().add(port_textbox);
		
		/* Num_limit_Label */
		JLabel Num_limit_Label = new JLabel();
		Num_limit_Label.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 12));
		Num_limit_Label.setForeground(Color.BLACK);
		Num_limit_Label.setBounds(38, 569, 76, 37);
		Num_limit_Label.setText("Num_Limit");
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
        		Send_Button.setEnabled(Check_send());
            }

            public void removeUpdate(DocumentEvent e) 
            {
            	send_textbox_text_value = input_box.getText();
            	Send_Button.setEnabled(Check_send());
            }
        	
        	public void changedUpdate(DocumentEvent e)
        	{
        		send_textbox_text_value = input_box.getText();
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
	
	private boolean Check_num_input(String input)
	{
		if(input.length() == 0)
		{
			error_Label.setText("Error: The number can not be empty!");
			return true;
		}
		if(input.substring(0, 1).compareTo("-") == 0)
		{
			error_Label.setText("Error: The number can not be minus!");
			return true;
		}
		if(input.indexOf(".") > 0)
		{
			error_Label.setText("Error: The number can not be real!");
			return true;
		}
		if(Integer.parseInt(input) > 10000)
		{
			error_Label.setText("Error: The nuber can not over 10000!");
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
		if(Check_port_input(port_textbox_text_value) || Check_num_input(num_limit_textbox_text_value) || num_limit_textbox_text_value.length() == 0 || port_textbox_text_value.length() == 0)
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
	private void serverStart(int max, int port) throws java.net.BindException {
		try 
		{
			Clients = new ArrayList<ClientThread>();
			serverSocket = new ServerSocket(port);
			serverThread = new ServerThread(serverSocket, max);
			serverThread.start();
			isStarted = true;									//start successfully
		} 
		catch (BindException be) 
		{
			isStarted = false;
			error_Label.setText("Port has been used, please try another one!");
			error_Label.setVisible(true);
			throw new BindException("Port has been used, please try another one!");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			isStarted = false;
			error_Label.setText("Port has been used, please try another one!");
			error_Label.setVisible(true);
			throw new BindException("Error Start The Server!");
		}
	}
	
	@SuppressWarnings("deprecation")
	private synchronized void serverClose()
	{
		try 
		{
			if (serverThread != null)
				serverThread.stop();	// stop the server
			
			for (int i = Clients.size() - 1; i >= 0; i--) 
			{
				
				Clients.get(i).getWriter().println("CLOSE");							//send stop information to the client
				Clients.get(i).getWriter().flush();
				
				/* Released Sources */
				Clients.get(i).getReader().close();
				Clients.get(i).getWriter().close();
				Clients.get(i).getSocket().close();
				Clients.get(i).stop();													//stop the thread which serves this client
				Clients.remove(i);														//remove it from the ArrayList Clients
			}
			if (serverSocket != null) {
				serverSocket.close();													//close Socket of the server 
			}
			isStarted = false;
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			error_Label.setText("Stop Failed!");
			error_Label.setVisible(true);
			isStarted = true;
			e.printStackTrace();
		}	
	}
	
	public void sendText(String string)
	{
		Content.append( df.format(new Date()) + "\t" + string + "\n");
	}
}
