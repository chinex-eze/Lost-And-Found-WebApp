package com.example.lostfound.customUi;

import com.example.lostfound.database.AppTableContainers;
import com.vaadin.ui.Select;

@SuppressWarnings("serial") 
public class CitySelect extends Select {
	
	//public CitySelect() throws SQLException{ 
	public CitySelect() { 
		
		super("City", AppTableContainers.getCityTable()); 
		
		setItemCaptionMode(Select.ITEM_CAPTION_MODE_PROPERTY); 
		setItemCaptionPropertyId("Description");  
	}
}
