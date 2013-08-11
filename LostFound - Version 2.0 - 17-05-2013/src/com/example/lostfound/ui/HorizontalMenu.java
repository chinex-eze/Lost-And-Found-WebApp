package com.example.lostfound.ui;

import com.example.lostfound.LostfoundApplication;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout; 
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial") 
public class HorizontalMenu extends HorizontalLayout 
								implements ClickListener {
	
	 private Button home = new Button("Home", (ClickListener)this);  
	 private Button about = new Button("About", (ClickListener)this); 
	 private Button reportBtn = new Button("Lost Item", (ClickListener)this); 
	 private Button login = new Button("Login", (ClickListener)this); 
	 
	 private LostfoundApplication app; 
	 
	public HorizontalMenu(LostfoundApplication app){ 
		this.app = app; 
		
		setSpacing(true); 
		setMargin(true); 
		
		about.setIcon(new ThemeResource("icons/help.png"));  
		home.setIcon(new ThemeResource("icons/home.png")); 
		reportBtn.setIcon(new ThemeResource("icons/note.png")); 
		login.setIcon(new ThemeResource("icons/user.png")); 
		
		addComponent(home); 
		addComponent(reportBtn); 
		addComponent(about); 
		addComponent(login); 
	}
	
	
	public void buttonClick(ClickEvent event) {
    	Button source = event.getButton();

        if (source == home) {
        	app.changePage(new FrontPage()); 
         } else if (source == about) {
            app.changePage(new AboutPage()); 
         } else if (source == reportBtn) {
            app.changePage(new ReportLostItemDisplay()); 
         } else if (source == login) {
            app.changePage(new AdminLoginForm()); 
         }
    } 
}
