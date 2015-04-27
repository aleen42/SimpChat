package com.Simp;

import java.awt.*;

import javax.swing.JLabel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.java.ui.componentc.JCTextField;

public class Server extends SimpChat{
	
	private JCTextField num_limit_textbox;
	private JCTextField port_textbox;
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
		/* num_limit_textbox */
		num_limit_textbox = new JCTextField();
		num_limit_textbox.setVisible(true);
		num_limit_textbox.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		num_limit_textbox.setBounds(286, 576, 110, 24);
//		num_limit_textbox.getDocument().addDocumentListener(new DocumentListener(){		//Listen to the input
//        	public void insertUpdate(DocumentEvent e) 
//        	{
//        		error_label.setVisible(Check_name_input(User_name_textbox_text_value = User_name_textbox.getText()));
//        		Add_Button.setEnabled(Check_database());
//            }
//
//            public void removeUpdate(DocumentEvent e) 
//            {
//            	error_label.setVisible(Check_name_input(User_name_textbox_text_value = User_name_textbox.getText()));
//            	Add_Button.setEnabled(Check_database());
//            }
//        	
//        	public void changedUpdate(DocumentEvent e)
//        	{
//        		error_label.setVisible(Check_name_input(User_name_textbox_text_value = User_name_textbox.getText()));
//        		Add_Button.setEnabled(Check_database());
//            }
//        });
		getContentPane().add(num_limit_textbox);
		
		/* port_textbox */
		port_textbox = new JCTextField();
		port_textbox.setVisible(true);
		port_textbox.setFont(new Font("Microsoft JhengHei UI", Font.PLAIN, 15));
		port_textbox.setBounds(286, 545, 110, 24);
//		port_textbox.getDocument().addDocumentListener(new DocumentListener(){		//Listen to the input
//        	public void insertUpdate(DocumentEvent e) 
//        	{
//        		error_label.setVisible(Check_name_input(User_name_textbox_text_value = User_name_textbox.getText()));
//        		Add_Button.setEnabled(Check_database());
//            }
//
//            public void removeUpdate(DocumentEvent e) 
//            {
//            	error_label.setVisible(Check_name_input(User_name_textbox_text_value = User_name_textbox.getText()));
//            	Add_Button.setEnabled(Check_database());
//            }
//        	
//        	public void changedUpdate(DocumentEvent e)
//        	{
//        		error_label.setVisible(Check_name_input(User_name_textbox_text_value = User_name_textbox.getText()));
//        		Add_Button.setEnabled(Check_database());
//            }
//        });
		getContentPane().add(port_textbox);
		
		/* Num_limit_Label */
		JLabel Num_limit_Label = new JLabel();
		Num_limit_Label.setFont(new Font("Microsoft JhengHei UI", Font.BOLD, 16));
		Num_limit_Label.setForeground(Color.BLACK);
		Num_limit_Label.setBounds(199, 571, 117, 37);
		Num_limit_Label.setText("Num_Limit");
		Num_limit_Label.setVisible(true);
		getContentPane().add(Num_limit_Label);
		
		/* Port_Label */
		JLabel Port_Label = new JLabel();
		Port_Label.setFont(new Font("Microsoft JhengHei UI", Font.BOLD, 16));
		Port_Label.setForeground(Color.BLACK);
		Port_Label.setBounds(247, 540, 50, 37);
		Port_Label.setText("Port");
		Port_Label.setVisible(true);
		getContentPane().add(Port_Label);
	}
}
