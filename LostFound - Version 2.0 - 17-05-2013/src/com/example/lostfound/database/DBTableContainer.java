package com.example.lostfound.database;

import java.sql.SQLException;

import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

@SuppressWarnings("serial") 
public class DBTableContainer 
						extends SQLContainer {
	
	public DBTableContainer(String tableName, JDBCConnectionPool pool) 
													throws SQLException{
		super(new TableQuery(tableName, pool)); 
	}
	
	public TableQuery getTableQuery(){
		return (TableQuery) super.getQueryDelegate(); 
	} 
	
	/***
	 * 	This returns the first time item that matches the value for the property. 
	 * This is intended to be used mainly for column with unique values like the 
	 * primary keys as it uses the Equal comparer and therefore should not be used 
	 * for properties that would return more than one values, in which case ONLY 
	 * the FIRST item would then be returned 
	 * @param prop
	 * @param val
	 * @return
	 */
	public Item getItembyProperty(String prop, String val){ 
		removeAllContainerFilters(); 
		addContainerFilter(new Compare.Equal(prop, val)); 
		Item item = getItem(firstItemId());
		removeAllContainerFilters(); 
		
		return item; 
	}
}
 