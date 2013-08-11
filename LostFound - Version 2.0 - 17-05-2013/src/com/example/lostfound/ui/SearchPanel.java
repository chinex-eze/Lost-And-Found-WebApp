package com.example.lostfound.ui;

import com.example.lostfound.data.SearchFilter;
import com.example.lostfound.database.AppTableContainers;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextField; 

@SuppressWarnings("serial")
public class SearchPanel extends HorizontalLayout 
						implements Button.ClickListener {
	
	TextField searchTerm = new TextField("Search Term"); 
	Select searchField   = new Select("Search Field"); 
	Button search        = new Button("Search", (Button.ClickListener)this); 
	
	SQLContainer container; 
	
	public SearchPanel(SQLContainer con){
		container = con;  
		
		setSpacing(true); 
		setMargin(true); 
		
		initTextField(); 
		initSearchField(); 
		
		addComponent(searchTerm); 
		addComponent(searchField); 
		addComponent(search); 
	}

	
	@Override
	public void buttonClick(ClickEvent event) { 
		 
		String term = searchTerm.getValue().toString().trim(); 
		Object itemId = searchField.getValue(); 
		
		if(term == null || term.equals("")){ 
			getWindow().showNotification("Search term cannot be empty!"); 
			return; 
		} 
		getWindow().showNotification("Search term: " + term); 
		performSearch(new SearchFilter(itemId, term)); 
	}
	
	public void initTextField(){
		
		searchTerm.addListener(new Property.ValueChangeListener() {
			@Override
			public void valueChange(
					com.vaadin.data.Property.ValueChangeEvent event) {
				String value = (String) searchTerm.getValue(); 
				if((value.trim()).equals("")){ 
					resetContainer(); 
				}
			}
		}); 
		searchTerm.setImmediate(true); 
	}
	
	public void initSearchField(){ 
		searchField.setNullSelectionAllowed(false); 
		searchField.addItem("ItemName"); 
		searchField.addItem("vehicle_id"); 
		searchField.addItem("City"); 
		searchField.addItem("StorageLocation"); 
		searchField.addItem("FoundCode"); 
		
		searchField.setItemCaption("ItemName", "Item Name"); 
		searchField.setItemCaption("vehicle_id", "Bus/Train"); 
		searchField.setItemCaption("City", "City"); 
		searchField.setItemCaption("StorageLocation", "Storage Location"); 
		searchField.setItemCaption("FoundCode", "Code"); 
		
		searchField.setValue("ItemName"); 
	} 
	
	public void performSearch(SearchFilter filter){ 
		resetContainer(); 
		
		if("City".equals(filter.getPropertyId())){ 
			SQLContainer cityContainer = AppTableContainers.getCityTable(); 
			cityContainer.addContainerFilter("Description", filter.getTerm(), true, false); 
			
			for(Object id : cityContainer.getItemIds() ){
				container.addContainerFilter("City", id.toString(), 
														true, false); 
			} 
			cityContainer.removeAllContainerFilters();
			cityContainer = null; 
		} else if("StorageLocation".equals(filter.getPropertyId())){ 
			SQLContainer storageContainer = AppTableContainers.getStorageTable(); 
			storageContainer.addContainerFilter("Description", filter.getTerm(), true, false); 
			
			for(Object id : storageContainer.getItemIds() ){ 
				container.addContainerFilter("StorageLocation", id.toString(), 
														true, false); 
				break; 
			} 
			storageContainer.removeAllContainerFilters();
			storageContainer = null; 
		} else {
			container.addContainerFilter(filter.getPropertyId(),
					filter.getTerm(), true, false); 
		} 
	} 
	
	private void resetContainer(){ 
		container.removeAllContainerFilters(); 
		//container.addContainerFilter(new Not(new IsNull("DateOfAcceptance")));
	} 
}
