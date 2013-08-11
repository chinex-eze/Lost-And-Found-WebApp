package com.example.lostfound.ui;

import com.vaadin.ui.TabSheet;

@SuppressWarnings("serial") 
public class MainTabSheet extends TabSheet implements 
			TabSheet.SelectedTabChangeListener{
	
	
	MainTabSheet(){ 
		setHeight("80%"); 
		//setWidth("500px"); 
		setSizeFull(); 
		
		addTab(new FrontPage(), "Home"); 
		addTab(new ReportLostItemDisplay(), "Lost Item"); 
		addTab(new AboutPage(), "About"); 
		
		addListener((TabSheet.SelectedTabChangeListener) this); 
	} 
	
	public void selectedTabChange(SelectedTabChangeEvent event) {
        TabSheet tabsheet = event.getTabSheet();
        Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
        if (tab != null) {
            getWindow().showNotification(tab.getCaption());
        }
    }
}
