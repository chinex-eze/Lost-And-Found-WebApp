package com.example.lostfound.ui;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Observer;

import com.example.lostfound.customUi.CitySelect;
import com.example.lostfound.database.AppTableContainers;
import com.example.lostfound.database.RequestTableContainer;
import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate;
import com.vaadin.data.util.sqlcontainer.query.QueryDelegate.RowIdChangeEvent;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel; 
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial") 
public class ReportLostItem extends VerticalLayout {
	
	LostItemForm itemForm; 
	InstructionWindow iw = new InstructionWindow(); 
	
	private static final String COMMON_FIELD_WIDTH = "12em"; 
	
	public ReportLostItem(){ 
		Panel formPanel = new Panel("Report Lost Item"); 
		formPanel.setWidth("350px"); 
		//formPanel.setSizeFull(); 
		
		itemForm = new LostItemForm(); 
		itemForm.setSizeFull(); 
		//itemForm.setCaption("Item Details");
		itemForm.setWriteThrough(false); 
		itemForm.setInvalidCommitted(false);  
		itemForm.setValidationVisible(true); 
		
		final Object[] ORDERED_PROPERTIES = new Object[] {
				"item_name", "description", "date_of_loss", 
				"vehicle_id", "city" 
        }; 
		itemForm.setVisibleItemProperties(Arrays.asList(ORDERED_PROPERTIES)); 
		
		Button open = new Button("Instructions", new Button.ClickListener() {
			
			public void buttonClick(ClickEvent event) {
				if (iw.getParent() == null) {
					getWindow().addWindow(iw); 
				} else {
					(iw.getParent()).removeWindow(iw);
				}
			}
		}); 
		
		formPanel.addComponent(itemForm); 
		formPanel.addComponent(open); 
		
		addComponent(formPanel);  
	}
	
	///this is just a temp workaround until I figure out a better design :( 
	public void addObserver(Observer o){
		itemForm.addObserver(o); 
	}
	
	public RowId saveItem(){ 
		return itemForm.saveItem(); 
	}
	
	
	private class LostItemForm extends Form implements 
							ClickListener, QueryDelegate.RowIdChangeListener {
		
		HorizontalLayout buttons = new HorizontalLayout(); 
		
		private Button clear = new Button("Clear",  (ClickListener) this);  
		private Button proceed = new Button("Proceed",  (ClickListener) this);  
		
		ReportObservable obs = new ReportObservable(); 
		
		RequestTableContainer requestContainer = AppTableContainers.getRequestTable(); 
		RowId newRowId;  
		
		public LostItemForm(){ 
			requestContainer.addListener((QueryDelegate.RowIdChangeListener) this);
			
			setFormFieldFactory(new ReportLostItem.ItemFieldFactory()); 
			addNewLostItem(); 
			
			buttons.setSpacing(true);
			buttons.addComponent(clear); 
			buttons.addComponent(proceed); 
			
			getFooter().addComponent(buttons);  
			getFooter().setMargin(false, false, true, true); 
		}
		
		
		public void addNewLostItem(){
			try {
				requestContainer.rollback();
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			Object tempItemId = requestContainer.addItem(); 
			setItemDataSource(requestContainer.getItem(tempItemId)); 
			
			setReadOnly(false); 
		} 
		
		public void buttonClick(ClickEvent event) {
			Button source = event.getButton(); 
			
			if (source == proceed) {
				
				if (!isValid()) { 
					return; 
				} 
				//first commit to the actual item  
				commit();   
				obs.report("ITEM_PROCEED");  
			}else if (source == clear) { 
				discard(); 
				obs.report("ITEM_CANCEL"); 
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
	    	try {
	    		requestContainer.rollback();
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			setReadOnly(false); 
		}
		
		///saves the newly created item and returns its rowId object
		public RowId saveItem(){ 
			try {
				requestContainer.commit();
			} catch (UnsupportedOperationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			return newRowId; 
		}
		
		public void addObserver(Observer o){
			obs.addObserver(o); 
		} 
		
		public void rowIdChange(RowIdChangeEvent event) {
			newRowId = event.getNewRowId(); 
			//addComponent(new Label("rowIdChange: " + newRowId.getId()[0].toString())); 
		} 
	}
	
	
	///this is static cos it's also used by other classes e.g. FoundItemTable...
	public static class ItemFieldFactory extends DefaultFieldFactory { 
		
		@Override
        public Field createField(Item item, Object propertyId,
                Component uiContext) { 
			Field f = super.createField(item, propertyId, uiContext);; 
			
			if ("description".equals(propertyId)) { 
				TextArea ta = new TextArea(); 
				ta.setWordwrap(true); // The default - just making sure ;)
				ta.setCaption("Decription"); 
				ta.setMaxLength(160); 
				ta.setNullRepresentation(""); 
				//ta.setWidth(COMMON_FIELD_WIDTH);
				ta.addValidator(new StringLengthValidator(
                        "Description must be 10 to 160 characters", 10, 160, false)); 
				ta.setRequired(true); 
				ta.setRequiredError("Enter a short description for missing item."); 
				f = ta; 
			} else if("city".equals(propertyId)){ 
				CitySelect select = new CitySelect(); 
				
				 select.setNullSelectionAllowed(false); 
				 select.setRequired(true);
				 select.setRequiredError("Please select the city where item was lost."); 
				 f = select; 
			}else if ("item_name".equals(propertyId)) {
				TextField tf = new TextField("Item"); 
				tf.setNullRepresentation(""); 
				f = tf; 
                f.setRequired(true);
                f.setRequiredError("Please enter an Item Name e.g. wallet, phone..."); 
                f.setWidth(COMMON_FIELD_WIDTH);
                f.addValidator(new StringLengthValidator(
                        "Item Name must be 3 to 25 characters", 3, 25, false)); 
			}else if ("vehicle_id".equals(propertyId)) {
				TextField tf = new TextField("Train/Bus"); 
				tf.setNullRepresentation(""); 
                tf.addValidator(new StringLengthValidator(
                        "Must be 1 to 10 characters", 1, 25, false)); 
                f = tf; 
			}else if ("date_of_loss".equals(propertyId)) { 
				f.setCaption("Date of Loss"); 
				f.setRequired(true); 
                f.setRequiredError("Please enter a date when item was lost.");
			}
			else {
				f = super.createField(item, propertyId, uiContext); 
			} 
			
			return f; 
		}
	}
}
