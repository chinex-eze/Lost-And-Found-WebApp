package com.example.lostfound.ui;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Observer;

import com.example.lostfound.database.AppTableContainers; 
import com.example.lostfound.database.ClientTableContainer;
import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.validator.EmailValidator;
import com.vaadin.data.validator.RegexpValidator; 
import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial") 
public class ClientForm extends VerticalLayout{ 
	
	ClientContactForm clientForm; 
	
	public ClientForm(){
		Panel clientPanel = new Panel("Contact Details Form"); 
		clientPanel.setWidth("350px");
		
		clientForm = new ClientContactForm(); 
		clientForm.setSizeFull(); 

		clientForm.setWriteThrough(false); 
		clientForm.setInvalidCommitted(false); 
		clientForm.setValidationVisibleOnCommit(true); 
		clientForm.setValidationVisible(true); 
		
		final Object[] ORDERED_PROPERTIES = new Object[] {
				"FirstNames", "LastName", "Email", "PhoneNumber" 
                }; 
		clientForm.setVisibleItemProperties(Arrays.asList(ORDERED_PROPERTIES)); 
		
		clientPanel.addComponent(clientForm);  
		
		addComponent(clientPanel);    
	} 
	
	///this is just a temp workaround until I figure out a better design :( 
	public void addObserver(Observer o){
		clientForm.addObserver(o); 
	} 
	
	public RowId saveItem(RowId temp){ 
		return clientForm.saveItem(temp); 
	}
	
	
	private class ClientContactForm extends Form implements 
								ClickListener, QueryDelegate.RowIdChangeListener {
		
			HorizontalLayout buttons = new HorizontalLayout(); 
			
			private Button cancel = new Button("Cancel",  (ClickListener) this);  
			private Button submit = new Button("Submit",  (ClickListener) this);  
			
			private ReportObservable obs = new ReportObservable(); 
			
			ClientTableContainer container = AppTableContainers.getClientTable(); 
			RowId newRowId, oldRowId; 
		
		public ClientContactForm(){ 
			container.addListener((QueryDelegate.RowIdChangeListener) this); 
			setFormFieldFactory(new ClientFieldFactory()); 
			addNewClient(); 
			
			buttons.setSpacing(true); 
			buttons.addComponent(cancel); 
			buttons.addComponent(submit); 
			
			getFooter().addComponent(buttons); 
			getFooter().setMargin(false, false, true, true); 
		}
		
		private void addNewClient(){
			try {
				container.rollback();
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			Object tempItemId = container.addItem(); 
			setItemDataSource(container.getItem(tempItemId)); 
			oldRowId = (RowId) tempItemId;
			
			setReadOnly(false); 
		}  
		
		public void buttonClick(ClickEvent event) { 
			Button source = event.getButton(); 
			
			if (source == submit) {
				if (!isValid()) {
					return; 
				}
				String email = (String) ((Form)this).getField("Email").getValue();  
				String phone = (String) ((Form)this).getField("PhoneNumber").getValue();  
				if((email == null || email.trim().equals("")) && 
						(phone == null || phone.trim().equals(""))){ 
					((Form)this).setComponentError(new UserError("Please enter at least either a "+
							"phone number or an email (or both).")); 
					return; 
				} 
				commit(); 
				obs.report("CLIENT_PROCEED"); 
			}else if (source == cancel) {
				discard(); 
				try {
					container.rollback();
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				obs.report("CLIENT_CANCEL"); 
			}
		} 
		
		@Override 
		public void commit() throws Buffered.SourceException { 
			super.commit(); 
			setReadOnly(true); 
		}
		
		@Override 
		public void discard() throws Buffered.SourceException { 
	    	super.discard(); 
	    	setReadOnly(false); 
		}
		
		public void addObserver(Observer o){
			obs.addObserver(o); 
		} 
		
		/**
		 * @itemId item id assigned to the FK column of the client table 
		 * */
		public RowId saveItem(RowId temp){ 
			try {
				int itemId = Integer.parseInt(temp.getId()[0].toString()); 
				//container.getItem(oldRowId).getItemProperty("Item").setValue(itemId); 
				container.getItem(oldRowId).getItemProperty("request").setValue(itemId);
				container.commit();
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return newRowId; 
		}
		
		public void rowIdChange(RowIdChangeEvent event) {
			newRowId = event.getNewRowId(); 
		}
	}
	
	
	public static class ClientFieldFactory extends DefaultFieldFactory {
		
		@Override
        public Field createField(Item item, Object propertyId,
                Component uiContext) { 
			Field f; 
		
			f = super.createField(item, propertyId, uiContext); 
			
			if ("FirstNames".equals(propertyId)) {
				TextField tf = (TextField) f;
                tf.setRequired(true);
                tf.setNullRepresentation(""); 
                tf.setRequiredError("Please enter your first name."); 
                tf.setCaption("First Name(s)"); 
			} else if ("LastName".equals(propertyId)) {
				TextField tf = (TextField) f;
                tf.setRequired(true); 
                tf.setNullRepresentation(""); 
                tf.setRequiredError("Please enter your last name."); 
                tf.setCaption("Last Name"); 
			} else if("PhoneNumber".equals(propertyId)){
				Validator postalCodeValidator = new RegexpValidator( 
						"((\\+?358)|(00)|0)[4|5][0-9]{8}",
					  "Enter a valid (Finnish) mobile phone number.");
				f.addValidator(postalCodeValidator); 
				f.setCaption("Phone number"); 
				((AbstractTextField) f).setNullRepresentation(""); 
			}else if ("Email".equals(propertyId)) { 
				TextField tf = (TextField) f; 
				tf.setNullRepresentation(""); 
                tf.addValidator(new EmailValidator("Please enter a valid email")); 
			} 
			
			return f;
		}
	}
}
