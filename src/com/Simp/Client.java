package com.Simp;

import java.awt.*;

public class Client extends SimpChat{
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
	}
}
