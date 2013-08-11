package com.example.lostfound.database;

public interface IDBTable {
	
	public String getTableName(); 
	
	public String getColumnStringValuebyId(int id, String colName); 
}
