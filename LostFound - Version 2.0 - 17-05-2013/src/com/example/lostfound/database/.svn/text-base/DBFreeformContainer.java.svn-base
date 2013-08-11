package com.example.lostfound.database;

import java.sql.SQLException;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;

@SuppressWarnings("serial") 
public class DBFreeformContainer extends SQLContainer{
	
	public DBFreeformContainer(String query, String id, //id = primary key of DB table 
					JDBCConnectionPool pool) throws SQLException{
		super(new FreeformQuery(query, pool,id)); 
	}
	
	public FreeformQuery getTableQuery(){
		return (FreeformQuery) super.getQueryDelegate(); 
	}
}
