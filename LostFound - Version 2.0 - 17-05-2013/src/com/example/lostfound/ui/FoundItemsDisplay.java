package com.example.lostfound.ui;

import com.example.lostfound.database.AppTableContainers;
import com.example.lostfound.database.ItemTableContainer;
import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;

@SuppressWarnings("serial") 
public class FoundItemsDisplay extends HorizontalLayout implements 
											ClickListener {    
	
	private ItemTableContainer itemDataSource = AppTableContainers.getItemTable(); 
	Container.Filter returnedFilter; 
	
	public FoundItemsDisplay(){
		setSpacing(true); 
		setMargin(true); 
		
		returnedFilter = new Compare.Equal("Returned", 1); 
		CheckBox ckBox = new CheckBox("Show only items NOT yet returned"); 
		ckBox.setDescription("Show ONLY items that are NOT yet RETURNED to client."); 
		ckBox.setImmediate(true); 
		ckBox.addListener((ClickListener) this); 
		
		FoundItemTable table = new FoundItemTable(itemDataSource); 
		table.setSizeFull(); 
		
		Panel tablePanel = new Panel("List of Found Items"); 
		tablePanel.setWidth("550px");  
		
		Panel detailsPanel = table.getDetailPanel(); 
		detailsPanel.setWidth("350px");  
		
		SearchPanel searchBar = new SearchPanel(itemDataSource); 
		
		tablePanel.addComponent(ckBox); 
		tablePanel.addComponent(table); 
		tablePanel.addComponent(searchBar); 
		
		addComponent(tablePanel);   
		addComponent(detailsPanel); 
	}

	@Override
	public void buttonClick(ClickEvent event) { 
		boolean bShow = event.getButton().booleanValue(); 
		if(bShow){ 
			itemDataSource.addContainerFilter(returnedFilter); 
		} 
		else { 
			//itemDataSource.removeContainerFilter(returnedFilter);  
			itemDataSource.removeAllContainerFilters(); 
		}
	}
}
