package com.example.lostfound.ui;

import java.util.Observable;
import java.util.Observer;

import com.example.lostfound.data.User;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;

@SuppressWarnings("serial")
public class AdminTabSheet extends HorizontalLayout 
							implements Observer, 
							TabSheet.SelectedTabChangeListener {

	TabSheet loginSheet = new TabSheet(); 
	TabSheet adminSheet = new TabSheet(); 
	private AdminLoginForm loginTab = new AdminLoginForm(); 
	
	User user; 
	
	public AdminTabSheet(User user){ 
		setHeight("80%"); 
		this.setSizeFull(); 
		this.user = user; 
		loginTab.addObserver((Observer) this); 
		
		if(user == null){ 
			loginSheet.addTab(loginTab, "Admin Login"); 
			addComponent(loginSheet); 
		} else {
			addComponent(adminSheet); 
		} 
		adminSheet.addListener((TabSheet.SelectedTabChangeListener) this); 
		loginSheet.addListener((TabSheet.SelectedTabChangeListener) this); 
	}

	public AdminLoginForm getLoginForm(){ 
		return loginTab; 
	}
	
	@Override
	public void update(Observable o, Object arg) {
		this.user  = (User) arg; 
		removeComponent(loginSheet); 
		
		Panel panel = new Panel(); 
		panel.setSizeFull(); 
		
		panel.addComponent(new AdminPage(this.user)); 
		panel.addComponent(new Button("Log Out", new Button.ClickListener(){ 	
			@Override
			public void buttonClick(ClickEvent event) { 
				getWindow().getApplication().close(); 
			}
		})); 
		
		adminSheet.addTab(panel, "Admin"); 
		adminSheet.addTab(new FoundItemsDisplay(), "Found Items"); 
		adminSheet.addTab(new ClientRequestDisplay(), "Requests"); 
		
		this.addComponent(adminSheet); 
	} 
	
	public void selectedTabChange(SelectedTabChangeEvent event) {
        TabSheet tabsheet = event.getTabSheet();
        Tab tab = tabsheet.getTab(tabsheet.getSelectedTab());
        if (tab != null) {
            getWindow().showNotification(tab.getCaption());
        }
    }
}
