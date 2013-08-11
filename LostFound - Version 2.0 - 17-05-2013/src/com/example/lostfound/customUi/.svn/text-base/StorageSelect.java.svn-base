package com.example.lostfound.customUi;

import com.example.lostfound.database.AppTableContainers;
import com.vaadin.ui.Select;

@SuppressWarnings("serial") 
public class StorageSelect extends Select {
	
	//public StorageSelect() throws SQLException{ 
	public StorageSelect() {
		/*super("Storage Location", new DBTableContainer("TBStorage", 
				LFDatabase.GetLFDatabasePool())); */ 
		super("Storage Location", AppTableContainers.getStorageTable()); 
		
		setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY); 
		setItemCaptionPropertyId("Description"); 
	}
}
