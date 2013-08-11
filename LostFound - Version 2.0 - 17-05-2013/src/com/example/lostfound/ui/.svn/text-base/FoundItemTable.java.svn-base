package com.example.lostfound.ui;

import java.text.SimpleDateFormat;

import com.example.lostfound.database.AppTableContainers;
import com.example.lostfound.database.DBTableContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;


@SuppressWarnings("serial") 
public class FoundItemTable  extends Table {
	
	private Panel detailsPanel = new Panel("Item Details");  
	private FoundItemDetailsForm detailsForm = new FoundItemDetailsForm(this); 
	
	
	FoundItemTable(DBTableContainer container){
		final Object[] NATURAL_COL_ORDER = new Object[] { 
			"ItemName", "vehicle_id", "City", "DateOfAcceptance", "Returned" 
		}; 
		final String[] COL_HEADERS_ENGLISH = new String[] {
			"Item", "Train/Bus", "City", "Date Received", "Returned"
		};  
		
		setSizeFull();  
		setSelectable(true); 
		setImmediate(true);  
		setNullSelectionAllowed(false); 
		 
		setContainerDataSource(container);  
		
		setVisibleColumns(NATURAL_COL_ORDER);   
		setColumnHeaders(COL_HEADERS_ENGLISH); 
		
		setPageLength(12); 
		
		detailsPanel.addComponent(detailsForm); 
		detailsPanel.addComponent(detailsForm.getAddButton()); 
		
		addListener(new FoundItemTableListener()); 
		addGeneratedColumns(); 
	}
	
	public Panel getDetailPanel(){
		return detailsPanel; 
	}  
	
	@Override
	protected String formatPropertyValue(Object rowId,
            Object colId, Property property) {
		
		//if (property.getType() == Date.class) { 
		if (property.getType() == java.sql.Date.class && 
				property.getValue() != null) { 
            SimpleDateFormat df = 
            	new SimpleDateFormat("MMM d, yyyy");  
            return df.format((java.sql.Date)property.getValue());  
        } 
		
		return super.formatPropertyValue(rowId, colId, property);
	} 
	
	
	private void addGeneratedColumns(){
		///Column generator for City column
		addGeneratedColumn("City", new ColumnGenerator(){ 
			  public Component generateCell(Table source, Object itemId,
			            Object columnId) {
				  
			      if (getItem(itemId).getItemProperty("City").getValue() != null) {
			          Label l = new Label();
			          int cityId = (Integer) getItem(itemId).getItemProperty("City").getValue();
			          
			          l.setValue(AppTableContainers.getCityTable().
			        		  getColumnStringValuebyId(cityId-1, "Description")); ///1 is subtracted cos otherwise 
			          															///it returns next(wrong) value 
			          l.setSizeUndefined();
			          return l;
			      }
			      return null;
			  }
			}); 
		///Column generator for Returned column
		addGeneratedColumn("Returned", new ColumnGenerator() {
			  public Component generateCell(Table source, Object itemId,
			            Object columnId) {
				  
			      if (getItem(itemId).getItemProperty("Returned").getValue() != null) {
			          Label l = new Label();
			          int id = (Integer) getItem(itemId).getItemProperty("Returned").getValue();
			          
			          l.setValue(AppTableContainers.getReturnedTable().
			        		  getColumnStringValuebyId(id-1, "Description")); ///1 is subtracted cos otherwise 
			          														///returns the next (wrong) value
			          l.setSizeUndefined();
			          return l;
			      }
			      return null;
			  }
			});
	}
	
	
	private class FoundItemTableListener 
			implements Property.ValueChangeListener{
		
		public void valueChange(Property.ValueChangeEvent event) {
 
			Property property = event.getProperty(); 
			Table temp = (Table) FoundItemTable.this; 
			 if (property == temp) {
				 Item item = temp.getItem(temp.getValue()); 
				 if (item != detailsForm.getItemDataSource()) {
					 detailsForm.setItemDataSource(item);
	            }
			 } 
	    }
	} 
}
