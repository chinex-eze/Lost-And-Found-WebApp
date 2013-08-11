package com.example.lostfound.customUi;

import com.example.lostfound.database.AppTableContainers;
import com.vaadin.ui.Select;

@SuppressWarnings("serial") 
public class ReturnedSelect extends Select {
	
	//public ReturnedSelect() throws SQLException { 
	public ReturnedSelect() {
		/*super("Returned", new DBTableContainer("tbl_Returned", 
				LFDatabase.GetLFDatabasePool()));  */
		super("Returned", AppTableContainers.getReturnedTable()); 
		
		setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY); 
		setItemCaptionPropertyId("Description"); 
	}
}
