package com.example.lostfound.ui;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial") 
public class FrontPage extends VerticalLayout {
	
	public FrontPage(){
		Panel frontPanel = new Panel(); 
		//frontPanel.setWidth("350px"); 
		frontPanel.setSizeFull(); 
		
		Label topMessage = new Label("<h1>Welcome to the Lost " +
							"but Found website.</h1>", 
				Label.CONTENT_XHTML);  
		Label message = new Label("<h2>Another Welcome.</h2>" + 
				"Please ignore this message. It's just a test string " +
				"for the application. But this app would be completed in " +
				"good time :)", 
				Label.CONTENT_XHTML); 
		
		frontPanel.addComponent(topMessage); 
		frontPanel.addComponent(message); 
		
		addComponent(frontPanel); 
	}
}
