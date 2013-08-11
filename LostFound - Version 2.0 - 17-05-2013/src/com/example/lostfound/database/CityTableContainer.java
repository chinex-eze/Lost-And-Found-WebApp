package com.example.lostfound.database;

import java.sql.SQLException;


@SuppressWarnings("serial") 
public class CityTableContainer extends DBTableContainer 
			implements IDBTable{
	
	private static String tableName = "tbl_city"; 
	
	public CityTableContainer() 
				throws SQLException{
		super(tableName, LFDatabase.GetLFDatabasePool());  
	} 
	
	public String getTableName(){
		return CityTableContainer.tableName; 
	}
	
	public String getColumnStringValuebyId(int id, String colName){
		Object cityItemId = this.getIdByIndex(id);
		return this.getItem(cityItemId).getItemProperty(colName)
        					.getValue().toString();
	}
}
