package com.example.lostfound.ui;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial") 
public class ConfirmationPage extends VerticalLayout {
	
	public ConfirmationPage(String contact){ 
		Panel confirmationPanel = new Panel(); 
		confirmationPanel.setWidth("350px");  
		
		Label topMessage = new Label("<h1>Confirmation</h1>", 
				Label.CONTENT_XHTML);  
		Label message = new Label("<h2>Report confirmed</h2>" +
					"<p>Thanks for using the lost and found web app.</p>" + 
					"<p>A confirmatory message has been sent to '" +
					contact + "'" +
					"<p>A message would be sent to you as soon as " + 
					"we have information about your missing item " +
					"in our database.</p>", 
					Label.CONTENT_XHTML); 
		
		confirmationPanel.addComponent(topMessage);  
		confirmationPanel.addComponent(message); 
		
		addComponent(confirmationPanel); 
	} 
	
}
