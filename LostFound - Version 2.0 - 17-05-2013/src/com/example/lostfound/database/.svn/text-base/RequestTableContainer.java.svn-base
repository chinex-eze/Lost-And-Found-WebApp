package com.example.lostfound.database;

import java.sql.SQLException;

@SuppressWarnings("serial")
public class RequestTableContainer extends DBTableContainer 
								implements IDBTable { 
	
	private static String tableName = "tbl_request"; 
	
	public RequestTableContainer() throws SQLException{
		super(tableName, LFDatabase.GetLFDatabasePool());  
	} 
	
	public String getTableName(){
		return RequestTableContainer.tableName; 
	} 
	
	public String getColumnStringValuebyId(int id, String colName){
		Object reqItemId = this.getIdByIndex(id); 
		return this.getItem(reqItemId).getItemProperty(colName)
        					.getValue().toString();
	}
}
