package com.example.lostfound.database;

import java.sql.SQLException;

@SuppressWarnings("serial") 
public class StorageTableContainer extends DBTableContainer 
									implements IDBTable{
	
	private static String tableName = "tbl_storage"; 
	
	public StorageTableContainer() throws SQLException{
		super(tableName, LFDatabase.GetLFDatabasePool());  
	} 
	
	public String getTableName(){
		return StorageTableContainer.tableName; 
	} 
	
	public String getColumnStringValuebyId(int id, String colName){
		Object cityItemId = this.getIdByIndex(id);
		return this.getItem(cityItemId).getItemProperty(colName)
        					.getValue().toString();
	}
}
