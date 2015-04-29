package com.Simp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

//a thread used to serve the client
public class ClientThread extends Thread {
	
	private Socket socket;
	private BufferedReader readfromclient;
	private PrintWriter writetoclient;
	private String User_name = "";
	private String IP = "";
	
	public Socket getSocket()
	{
		return socket;
	}
	
	public PrintWriter getWriter() {
		return writetoclient;
	}
	
	public BufferedReader getReader() {
		return readfromclient;
	}
	
	public String getUser()
	{
		return User_name;
	}
	
	public String getIP()
	{
		return IP;
	}
	
	ClientThread(Socket socket)
	{
		try 
		{
			this.socket = socket;
			readfromclient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			User_name = readfromclient.readLine();
			IP = readfromclient.readLine();
			writetoclient = new PrintWriter(socket.getOutputStream());
			writetoclient.println(User_name + "/" + IP + "\t" + "Connect Successfully!");					//Reply connect successfully
			writetoclient.flush();
			
			if (Server.Clients.size() > 0) {
				writetoclient.println("There are "+ (Server.Clients.size() - 1) +" online users.");			//Reply a number of online users
				writetoclient.flush();
			}
			
			for (int i = Server.Clients.size() - 1; i >= 0; i--) {											
				Server.Clients.get(i).getWriter().println(User_name + "/" + IP + " is online!");			//Reply online information to other clients
				Server.Clients.get(i).getWriter().flush();
			}
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	public void run()
	{
		String message = null;
		while (true) 																						//always listen to the client message
		{
			try 
			{
				message = readfromclient.readLine();
				if(message.equals("CLOSE"))
				{
					Server.sendText(User_name + "/" + IP + " is offline!");			
					/* Release Sources */
					readfromclient.close();
					writetoclient.close();
					socket.close();
					for(int i = Server.Clients.size() - 1; i >= 0; i--) 
					{											
						Server.Clients.get(i).getWriter().println(User_name + "/" + IP + " is offline!");			//Reply offline information to other clients
						Server.Clients.get(i).getWriter().flush();
					}
					
					/* delete the thread */
					for(int i = Server.Clients.size() - 1; i >= 0; i--) 
					{											
						if(Server.Clients.get(i).getUser() == User_name)
						{
							ClientThread temp = Server.Clients.get(i);
							Server.Clients.remove(i);
							temp.stop();
							return;
						}
					}
					
				}
				else
				{
					for(int i = Server.Clients.size() - 1; i >= 0; i--) 
					{											
						Server.Clients.get(i).getWriter().println(User_name + "/" + IP + " said:\t" + message);			//Reply offline information to other clients
						Server.Clients.get(i).getWriter().flush();
					}
				}
			} 
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (SecurityException e)
			{
				e.printStackTrace();
			}
		}
	}
}
