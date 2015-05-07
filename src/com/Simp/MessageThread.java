package com.Simp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.StringTokenizer;

import javax.swing.JTextArea;

public class MessageThread extends Thread 
{
	private BufferedReader readefromserver;
	private JTextArea Content;
	private UserList userlist;
	private Client client;
	private String get_username = "";
	private String get_ip = "";
	
	MessageThread(BufferedReader reader, JTextArea Content, UserList list, Client c) 
	{
		this.readefromserver = reader;
		this.Content = Content;
		this.userlist = list;
		this.client = c;
	}
	
	public void run() 
	{
		String message = "";
		while(true)
		{
			try 
			{
				message = readefromserver.readLine();
				if(message == null)
					continue;
//				System.out.println(message);
				StringTokenizer stringTokenizer = new StringTokenizer(message, "@");
				String command = stringTokenizer.nextToken();
//				System.out.println(command);
				switch(command.toString())
				{
				case "CLOSE":																								//The server ask to close your connection
					Content.append(stringTokenizer.nextToken() + "\n");
					client.stopConnection();
					break;
					
				case "ADD":																									//a user is online
					get_username = stringTokenizer.nextToken();
					get_ip = stringTokenizer.nextToken();
					System.out.println(get_username + "\n" + get_ip);
//					System.out.println("add");
					/* update list */
					userlist.get_listItem().clear();
					for(int i = 1; i <= 2; i++)
					{
						userlist.get_listItem().addElement(" ");
				    }									
					userlist.get_listItem().addElement(get_username + "/" + get_ip);
					for(int i = 1; i <= 2; i++)
				    {
						userlist.get_listItem().addElement(" ");
				    }
					
					Content.append(Client.df.format(new Date()) + "\t" + get_username + "/" + get_ip + "is online.\n");
					break;
					
				case "DELETE":																								//a user is offline
					get_username= stringTokenizer.nextToken();
					get_ip = stringTokenizer.nextToken();
					
					/* update list */
					userlist.get_listItem().removeElement(get_username + "/" + get_ip);
					
					Content.append(Client.df.format(new Date()) + "\t" + get_username + "/" + get_ip + "is offline.\n");
					break;
				case "MESSAGE":
					Content.append(Client.df.format(new Date()) + "\t" + stringTokenizer.nextToken() + "\n");
					break;
				default:
					break;
				}

				
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			
		}
	}
}
