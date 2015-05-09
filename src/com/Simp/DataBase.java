package com.Simp;

import java.sql.*;

import javax.swing.DefaultListModel;

public class DataBase {
	
	private Connection con = null;    
    private Statement stmt = null;    
    private ResultSet res = null; 
	private String url = "jdbc:sqlserver://localhost:1433;databaseName=Chat;user=dbo;integratedSecurity=true;"; 
	private String SQL;  
	private UserList userlist;
	DataBase()
	{
		try 
		{    
	        // Establish the connection.    
			
	        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");    
//	        System.out.println("Connecting"); 
	        con = DriverManager.getConnection(url);    
//	        System.out.println("Connection Successful!"); 
		}

	    // Handle any errors that may have occurred.    
	    catch (Exception e) {    
	        e.printStackTrace();    
	    } 
	}
	
	public void get_User_name(DefaultListModel listitem)							//get User_list
	{
		try 
		{
			// Create and execute an SQL statement that returns some data.    
			SQL = "select User_Name,Ipv4_Adress,case User_Status when 0 then 'offline' when 1 then 'online' end as User_Status from Chat.dbo.Listen_User_Table where User_Status = 1;";
	        stmt = con.createStatement();    
	        res = stmt.executeQuery(SQL);  
			// Iterate through the data in the result set and display it.    
	        while (res.next()) {    
	        	listitem.addElement(res.getString("User_name") + "/" + res.getString("Ipv4_Adress"));
	        } 
	        stmt.close();
		}
		
		// Handle any errors that may have occurred.    
	    catch (Exception e) 
		{    
	    	System.out.println("cannot get username");
	        e.printStackTrace();    
	    } 	
	}
	
	public String get_status(String username, String ip)
	{
		String reserve = "null";
		
        try 
		{
        	SQL = "select case User_Status when 0 then 'offline' when 1 then 'online' end as User_Status from Chat.dbo.Listen_User_Table where User_Name = '" + username + "' and Ipv4_Adress = '" + ip + "';";
        	stmt = con.createStatement();    
	        res = stmt.executeQuery(SQL); 
        	res.next();														//next row
            reserve = res.getString("User_status");
            stmt.close();
		}
		// Handle any errors that may have occurred.    
	    catch (Exception e) 
		{    
	    	System.out.println("cannot get status");
	        e.printStackTrace();    
	    }
		return reserve;
	}
	
	public boolean Check_isOnline(String username, String ip)
	{
		boolean reserve = false;
		try 
		{
        	SQL = "select case User_Status when 0 then 'offline' when 1 then 'online' end as User_Status from Chat.dbo.Listen_User_Table where User_Name = '" + username + "' and Ipv4_Adress = '" + ip + "';";
        	stmt = con.createStatement();    
	        res = stmt.executeQuery(SQL); 
        	res.next();														//next row
            if(res.getString("User_status").equals("online"))
            	reserve = true;
            stmt.close();
		}
		// Handle any errors that may have occurred.    
	    catch (Exception e) 
		{    
	        e.printStackTrace();    
	    }
		return reserve;
	}
	
	public void Update_status(String username, String ip, String status)
	{
		int s = 0;
		if(status.equals("offline"))
			s = 0;
		else if(status.equals("online"))
			s = 1;
		
		try 
		{
        	SQL = "update Chat.dbo.Listen_User_Table set User_Status = " + s + " where User_Name = '" + username + "' and Ipv4_Adress = '" + ip + "';";
        	 
        	boolean autocommit = con.getAutoCommit();
        	con.setAutoCommit(false);
        	stmt = con.createStatement();
        	if(stmt.executeUpdate(SQL) == 1)
        	{
        		con.commit();
        	}
        	else
        	{
        		System.out.println("Update Status Failed!\n");
        		con.rollback();
        	} 	
        	con.setAutoCommit(autocommit);

		}
		// Handle any errors that may have occurred.    
	    catch (Exception e) 
		{    
	        e.printStackTrace();    
	    }
	}
	
	public void Insert_User(String username, String ip)
	{
		try 
		{
        	SQL = "insert into Chat.dbo.Listen_User_Table select '" + ip + "','" + username + "',0;";
        	boolean autocommit = con.getAutoCommit();
        	con.setAutoCommit(false);
        	stmt = con.createStatement();
        	if(stmt.executeUpdate(SQL) == 1)
        	{
        		con.commit();
        	}
        	else
        	{
        		System.out.println("Sign Up Failed!\n");
        		con.rollback();
        	} 	
        	con.setAutoCommit(autocommit);

		}
		// Handle any errors that may have occurred.    
	    catch (Exception e) 
		{    
	        e.printStackTrace();    
	    }
	}
	
	public boolean Check_isExisted(String username, String ip)
	{
		boolean reserve = false;
		try 
		{
			SQL = "select count(*) from Chat.dbo.Listen_User_Table where User_Name = '" + username + "' and Ipv4_Adress = '" + ip + "';";
	        stmt = con.createStatement();    
	        res = stmt.executeQuery(SQL);  
			// Iterate through the data in the result set and display it.    
	        res.next();														//next row
	        int count = res.getInt(1);
	        reserve = (count == 1) ? true : false;
//	        System.out.println(reserve);
	        stmt.close();
		}
		// Handle any errors that may have occurred.    
	    catch (Exception e) 
		{    
	        e.printStackTrace();    
	    }
		return reserve;
	}
	
//	public String get_ipv4(String username, String ip)							//get User_list
//	{
//		String reserve = "0.0.0.0";
//		try 
//		{
//			SQL = "select Ipv4_Adress from Chat.dbo.Listen_User_Table where User_name = '" + username + "' and Ipv4_Adress = '" + ip + "';";
//	        stmt = con.createStatement();    
//	        res = stmt.executeQuery(SQL);  
//			// Iterate through the data in the result set and display it.    
//	        res.next();														//next row
//	        reserve = res.getString("Ipv4_Adress");
////	        System.out.println(reserve);
//	        stmt.close();
//		}
//		
//		// Handle any errors that may have occurred.    
//	    catch (Exception e) 
//		{    
//	        e.printStackTrace();    
//	    }
//		return reserve;
//	}
	
	public void delet_User(int index)
	{
		try 
		{
			// Create and execute an SQL statement that returns some data.    
	        String SQL = "delete t from (select Chat.dbo.Listen_User_Table.*, row_number() over (order by User_Name) rn from Chat.dbo.Listen_User_Table) as t where rn = " + index +";";    
	        stmt = con.createStatement();    
	        stmt.executeUpdate(SQL);
	        stmt.close();
		}
		
		// Handle any errors that may have occurred.    
	    catch (Exception e)
		{    
	        e.printStackTrace();    
	    } 	
		
	}
	
}
