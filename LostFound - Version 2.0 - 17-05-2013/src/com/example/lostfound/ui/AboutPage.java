package com.example.lostfound.ui;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial") 
public class AboutPage extends VerticalLayout {
	
	public AboutPage(){
		Panel pagePanel = new Panel(); 
		//pagePanel.setWidth("350px"); 
		pagePanel.setSizeFull(); 
		
		Label topMessage = new Label("<h2>About the application.</h2>", 
				Label.CONTENT_XHTML);  
		Label message = new Label("<em>This application is still under construction.</em> <br /> " +
				"It is a web application that was meant to manage lost items in " +
				"the buses both local and long distance. <br />" +
				"The basic idea is that when one losses an item in a bus, s/he " +
				"can make a notice to those in charge of lost item through this " +
				"applicatio. As soon as the item is found and entered into the " +
				"application database, a notice would be automatically sent to " +
				"the owner with a code that would ease finding the item at the " +
				"lost items' office.", 
				Label.CONTENT_XHTML);  
		
		Label authorHeading = new Label("<h2>About the author.</h2>", 
				Label.CONTENT_XHTML);  
		Embedded authorPic = new Embedded("", new ThemeResource("images/eze.jpg")); 
		Label authorMessage = new Label("My name is Chinedu Eze. This app is intended to " +
				"serve for my bachelor's thesis project. \nI hope it meets its intended need.", 
				Label.CONTENT_XHTML); 
		
		pagePanel.addComponent(topMessage); 
		pagePanel.addComponent(message); 
		pagePanel.addComponent(authorHeading);  
		pagePanel.addComponent(authorPic); 
		pagePanel.addComponent(authorMessage); 
		
		addComponent(pagePanel); 
	}
}
