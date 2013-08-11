package com.example.lostfound.ui;

import com.example.lostfound.database.AppTableContainers;
import com.example.lostfound.database.RequestTableContainer;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class ClientRequestDisplay extends HorizontalLayout implements 
															ClickListener {
	
	private RequestTableContainer requestDatasource = AppTableContainers.getRequestTable(); 
	
	public ClientRequestDisplay(){ 
		setSpacing(true); 
		setMargin(true); 
		
		RequestTable table = new RequestTable(requestDatasource); 
		table.setSizeFull(); 
		
		Panel tablePanel = new Panel("List of Client Requests"); 
		tablePanel.setWidth("470px"); 
		
		Panel detailsPanel = table.getDetailPanel(); 
		detailsPanel.setWidth("250px"); 
		
		///new panel to display items that match a selected item from the list 
		Panel matchPanel = table.getMatchPanel(); 
		matchPanel.setWidth("250px"); 
		
		CheckBox ckBox = new CheckBox("Show only MATCHED items"); 
		ckBox.setDescription("Show ONLY items that have been matched for return."); 
		ckBox.setImmediate(true); 
		ckBox.addListener((ClickListener) this); 
		
		SearchPanelRequest searchBar = new SearchPanelRequest(requestDatasource); 
		
		tablePanel.addComponent(ckBox); 
		tablePanel.addComponent(table); 
		tablePanel.addComponent(searchBar); 
		
		addComponent(tablePanel); 
		addComponent(detailsPanel); 
		addComponent(matchPanel); 
	}

	@Override
	public void buttonClick(ClickEvent event) {
		boolean bShow = event.getButton().booleanValue(); 
		if(bShow){ 
			requestDatasource.addContainerFilter("matched", "", true, false); 
		} 
		else { 
			requestDatasource.removeAllContainerFilters(); 
		}
	} 
}
