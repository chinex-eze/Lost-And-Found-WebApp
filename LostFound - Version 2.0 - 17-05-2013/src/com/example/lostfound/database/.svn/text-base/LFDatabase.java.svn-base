package com.example.lostfound.database;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

public class LFDatabase {
	
	private static JDBCConnectionPool pool = null; 
	
	private static String database = "lostfounddb";  
	private static String username = "root";//"lostFoundAPP";   
	private static String password = "h2so4";//"lf080";   
	private static int initConn = 2;  
	private static int maxConn = 2;  
	
	public LFDatabase(){
	} 
	
	public static JDBCConnectionPool GetLFDatabasePool(){
		
		if(pool == null){
			try {
				pool = new SimpleJDBCConnectionPool(
				        "com.mysql.jdbc.Driver",
				        "jdbc:mysql://localhost:3306/"+ database, 
				        username, password, initConn, maxConn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return pool; 
	}
}
