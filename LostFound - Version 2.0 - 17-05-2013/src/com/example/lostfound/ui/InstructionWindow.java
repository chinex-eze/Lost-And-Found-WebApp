package com.example.lostfound.ui;

import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class InstructionWindow extends Window {
	
	public InstructionWindow(){
		VerticalLayout layout = (VerticalLayout) this.getContent();
        //layout.setMargin(true);
        layout.setSpacing(true); 
        layout.setSizeFull(); 
        
        setWidth("320px"); 
        setHeight("60%"); 
        
        Label mesg = new Label(
        		"1. Fill in the name of the item that was missing e.g. Wallet, Phone, ID card, e.t.c.<br />" + 
        		"2. Description. Give a general description of missing item. Description is limited to 160 " +
        		"characters. <br />" +
        		"4. Date when item was lost must be specified. This doesn't have to be accurate.<br /> " +
        		"The bus identification e.g. bus number or bus name can be specified. <br />" +
        		"5. The city where item was lost must be given. The city list is limited to cities where our " +
        		"service is available.",  
        		Label.CONTENT_XHTML); 
        Panel panel = new Panel("Instructions"); 
        
        //addComponent(mesg); 
        //panel.setWidth("250px"); 
        panel.setSizeFull(); 
        panel.addComponent(mesg); 
        
        addComponent(panel); 
	}
}
