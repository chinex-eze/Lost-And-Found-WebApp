package com.example.lostfound.ui;

import com.example.lostfound.LostfoundApplication;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class MainWindow extends Window 
				implements Button.ClickListener {

	private static final long serialVersionUID = 1L;
	
	private LostfoundApplication app; 
	
	VerticalLayout main = new VerticalLayout();  
	HorizontalLayout bottom = new HorizontalLayout(); 
	
	private MainTabSheet  tabSheet   = new MainTabSheet(); 
	private AdminTabSheet adminSheet; // = new AdminTabSheet(app.getCurrentUser()); 
	
	private Button btnHome = new Button("Home", (ClickListener)this);  
	private Button btnLogin = new Button("Login", (ClickListener)this);
	
	public MainWindow(String title, LostfoundApplication app) { 
		super(title); 
		this.app = app; 
		
		main.setMargin(true);
        main.setSpacing(true); 
        
        bottom.setSpacing(true); 
		bottom.setMargin(true); 
		setContent(main); 
		
		btnHome.setVisible(false); 
		
		bottom.addComponent(btnHome); 
		bottom.addComponent(btnLogin); 
		main.addComponent(tabSheet); 
		main.addComponent(bottom); 
	} 
	
	
	public void buttonClick(ClickEvent event) { 
    	Button source = event.getButton(); 
    	
    	if (source == btnHome) {
    		//this basically logs current user out 
    		//and create a new instance of the app 
    		app.setCurrentUser(null); //NOT necessary though ;) 
    		getWindow().getApplication().close(); 
    	} else if (source == btnLogin) {
    		btnLogin.setVisible(false); 
    		switchToAdmin(); 
    	}
	}  
	
	private void switchToAdmin(){ 
		main.removeAllComponents(); 
		adminSheet = new AdminTabSheet(app.getCurrentUser());
		main.addComponent(adminSheet); 
		btnHome.setVisible(true);  
		main.addComponent(bottom); 
	}
}
