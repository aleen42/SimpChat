package com.Simp;

import java.sql.*;

public class DataBase {
	
	private Connection con = null;    
    private Statement stmt = null;    
    private ResultSet res = null; 
	private String url = "jdbc:sqlserver://localhost:1433;databaseName=Chat;user=dbo;integratedSecurity=true;"; 
	private String SQL;  
	DataBase()
	{
		try 
		{    
	        // Establish the connection.    
			
	        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");    
//	        System.out.println("Connecting"); 
	        con = DriverManager.getConnection(url);    
	        System.out.println("Connection Successful!"); 
		}

	    // Handle any errors that may have occurred.    
	    catch (Exception e) {    
	        e.printStackTrace();    
	    } 
	}
	
	public void get_User_name()							//get User_list
	{
		try 
		{
			// Create and execute an SQL statement that returns some data.    
			SQL = "select User_name,Ipv4_Adress,case User_status when 0 then 'offline' when 1 then 'online' end as User_status from dbo.Listen_User_Table";
	        stmt = con.createStatement();    
	        res = stmt.executeQuery(SQL);  
			// Iterate through the data in the result set and display it.    
	        while (res.next()) {    
	        	UserList.User_name_store = res.getString("User_name");
	        	UserList.add_item();
	        } 
	        stmt.close();
		}
		
		// Handle any errors that may have occurred.    
	    catch (Exception e) 
		{    
	        e.printStackTrace();    
	    } 	
	}
	
	public String get_status(int index)
	{
		String reserve = "null";
		
        try 
		{
        	SQL = "select case User_status when 0 then 'offline' when 1 then 'online' end as User_status from (select dbo.Listen_User_Table.*, row_number() over (order by User_Name) rn from dbo.Listen_User_Table) as t where rn = " + index + ";";
        	stmt = con.createStatement();    
	        res = stmt.executeQuery(SQL); 
        	res.next();														//next row
            reserve = res.getString("User_status");
            stmt.close();
		}
		// Handle any errors that may have occurred.    
	    catch (Exception e) 
		{    
	        e.printStackTrace();    
	    }
		return reserve;
	}
	
	public String get_ipv4(int index)							//get User_list
	{
		String reserve = "0.0.0.0";
		try 
		{
			SQL = "select t.* from (select dbo.Listen_User_Table.*, row_number() over (order by User_Name) rn from dbo.Listen_User_Table) as t where rn = " + index + ";";
	        stmt = con.createStatement();    
	        res = stmt.executeQuery(SQL);  
			// Iterate through the data in the result set and display it.    
	        res.next();														//next row
	        reserve = res.getString("Ipv4_Adress");
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
	
	public void delet_User(int index)
	{
		try 
		{
			// Create and execute an SQL statement that returns some data.    
	        String SQL = "delete t from (select dbo.Listen_User_Table.*, row_number() over (order by User_Name) rn from Chat.dbo.Listen_User_Table) as t where rn = " + index +";";    
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
