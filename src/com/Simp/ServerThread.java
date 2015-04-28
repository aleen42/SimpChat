package com.Simp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerThread extends Thread{
	private ServerSocket serverSocket;
	private int number_limit;
	
	public ServerThread(ServerSocket serverSocket, int max) {
		this.serverSocket = serverSocket;
		this.number_limit = max;
	}
	
	public void run() 
	{
		while(true)		//keep listening the connection of a client
		{
			try 
			{
				Socket socket = serverSocket.accept();									//socket for client
				if(Server.Clients.size() == number_limit)								//Clients number has arrived the number_limit
				{
//					BufferedReader read_from_client = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//					PrintWriter write_to_server = new PrintWriter(socket.getOutputStream());
					//Handle
					continue;
				}
				ClientThread client = new ClientThread(socket);
				client.run();
				Server.Clients.add(client);
				//update list
				Server.Content.append(client.getUser() + "/" + client.getIP() + "\t" + "Connect Successfully!");
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
