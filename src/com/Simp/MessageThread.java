package com.Simp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

import javax.swing.JTextArea;

public class MessageThread extends Thread 
{
	private BufferedReader readefromserver;
	private JTextArea Content;
	
	public MessageThread(BufferedReader reader, JTextArea Content) 
	{
		this.readefromserver = reader;
		this.Content = Content;
	}
	
	public void run() 
	{
		String message = null;
		while(true)
		{
			
			try {
				message = readefromserver.readLine();
				if(message != null)
				{
					System.out.println(message);
					Content.append(message + "\n");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}
