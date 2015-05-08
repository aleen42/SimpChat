package com.Simp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerThread extends Thread{
	private ServerSocket serverSocket;
	private int number_limit;
	private UserList userlist;
	private DataBase db = new DataBase();
	
	public ServerThread(ServerSocket serverSocket, int max, UserList list) {
		this.serverSocket = serverSocket;
		this.number_limit = max;
		this.userlist = list;
	}
	
	public void run() 
	{
		while(true)		//keep listening the connection of a client
		{
			try 
			{
				Socket socket = serverSocket.accept();									//socket for client
//				System.out.println(Server.Clients.size());
//				System.out.println(number_limit);
				if(Server.Clients.size() == number_limit)								//Clients number has arrived the number_limit
				{
//					BufferedReader read_from_client = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//					PrintWriter write_to_server = new PrintWriter(socket.getOutputStream());
					//Handle
					PrintWriter writetoclient = new PrintWriter(socket.getOutputStream());
					writetoclient.println("CLOSE@" + Server.df.format(new Date()) + "\t" + "The number of people has arrived the limit number!");
					writetoclient.flush();
					writetoclient.close();
					continue;
				}
				ClientThread client = new ClientThread(socket, userlist);
				client.start();
//				System.out.println("1");
				Server.Clients.add(client);
				Server.sendText(client.getUser() + "/" + client.getIP() + " is online!");
//				System.out.println(Server.Clients.size());
				
				
				/* update list */
				userlist.get_listItem().clear();
				for(int i = 1; i <= 2; i++)
				{
					userlist.get_listItem().addElement(" ");
			    }
				for (int i = Server.Clients.size() - 1; i >= 0; i--) {											
					userlist.get_listItem().addElement(Server.Clients.get(i).getUser() + "/" + Server.Clients.get(i).getIP());
				}
				for(int i = 1; i <= 2; i++)
			    {
					userlist.get_listItem().addElement(" ");
			    }
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void send_message(String text)
	{
		for (int i = Server.Clients.size() - 1; i >= 0; i--) 
		{											
			Server.Clients.get(i).getWriter().println("MESSAGE@" + "The server said: " + text);			//Reply online information to other clients
			Server.Clients.get(i).getWriter().flush();
		}
	}
	
	public void send_privatemessage(String text, String des_username, String des_ip)
	{
		for (int i = Server.Clients.size() - 1; i >= 0; i--) 
		{	
			System.out.println(des_username + "\n" + des_ip);
//			System.out.println(Server.Clients.get(i).getUser() + "\n" + Server.Clients.get(i).getIP());
			if(Server.Clients.get(i).getUser().equals(des_username) && Server.Clients.get(i).getIP().equals(des_ip))
			{
				Server.Clients.get(i).getWriter().println("MESSAGE@" + "The server said to you: " + text);			//Reply online information to other clients
				Server.Clients.get(i).getWriter().flush();
			}
		}
	}
}
