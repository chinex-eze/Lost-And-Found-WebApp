package com.example.lostfound.database;

import java.sql.SQLException;

@SuppressWarnings("serial") 
public class ClientTableContainer extends DBTableContainer 
						implements IDBTable{
	
	private static String tableName = "tbl_client"; 
	
	public ClientTableContainer() throws SQLException{
		super(tableName, LFDatabase.GetLFDatabasePool());  
	} 
	
	public String getTableName(){
		return ClientTableContainer.tableName; 
	} 
	
	public String getColumnStringValuebyId(int id, String colName){
		Object cityItemId = this.getIdByIndex(id);
		return this.getItem(cityItemId).getItemProperty(colName)
        					.getValue().toString();
	}
}
