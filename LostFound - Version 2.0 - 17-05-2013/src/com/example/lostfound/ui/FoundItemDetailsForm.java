package com.example.lostfound.ui;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.concurrent.RejectedExecutionException;

import matcher.MatchEngine;

import com.example.lostfound.LostfoundApplication;
import com.example.lostfound.customUi.CitySelect;
import com.example.lostfound.customUi.ReturnedSelect;
import com.example.lostfound.customUi.StorageSelect;
import com.example.lostfound.database.DBTableContainer;
import com.vaadin.data.Buffered;
import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial") 
public class FoundItemDetailsForm extends Form implements ClickListener {
	
	private Button save = new Button("Save",  (ClickListener) this);
    private Button cancel = new Button("Cancel",  (ClickListener) this); 
    private Button edit = new Button("Edit",  (ClickListener) this); 
    private Button newItembtn = new Button("Add New Item",  (ClickListener) this);
    
    private HorizontalLayout footer = new HorizontalLayout(); 
    
    FoundItemTable fTable; 
    private boolean newItemMode = false; 
    
    StorageSelect sSelect = new StorageSelect(); 
    CitySelect cSelect = new CitySelect(); 
    ReturnedSelect rSelect = new ReturnedSelect(); 
    
    FoundItemDetailsForm(FoundItemTable table){ 
    	fTable = table; 
    	
    	footer.setSpacing(true); 
    	footer.setMargin(true);  
    	setWriteThrough(false); 
    	setReadOnly(true); 
    	setValidationVisible(true); 
    	setValidationVisibleOnCommit(true); 

    	cancel.setIcon(new ThemeResource("icons/cancel.png")); 
    	save.setIcon(new ThemeResource("icons/ok.png")); 
    	newItembtn.setIcon(new ThemeResource("icons/document-add.png"));  
    	edit.setIcon(new ThemeResource("icons/settings.png"));  
    	
    	footer.addComponent(edit);   
    	footer.addComponent(save);      
    	footer.addComponent(cancel);   
    	footer.addComponent(newItembtn);  
    	
    	setFormFieldFactory(new ItemFieldFactory());
    	
    	setReadOnly(true); 
    	setFooter(footer); 
    	getFooter().setVisible(false); 
    }
    
    public void addFoundItem(){ 
    	try {
			((DBTableContainer) (fTable.getContainerDataSource())).rollback(); 
			((DBTableContainer) (fTable.getContainerDataSource())).removeAllContainerFilters(); 
		} catch (UnsupportedOperationException e) { 
			e.printStackTrace();
		} catch (SQLException e) { 
			e.printStackTrace(); 
			 
			getWindow().showNotification("Error: " + e.getMessage()); 
		}
    	
		Object tempItemId = ((DBTableContainer) 
						(fTable.getContainerDataSource())).addItem(); 
		
		setItemDataSource(((DBTableContainer) 
						(fTable.getContainerDataSource())).getItem(tempItemId));
		
         setReadOnly(false); 
    }
    
    public void buttonClick(ClickEvent event) {
    	Button source = event.getButton(); 

        if (source == save) {
             /* If the given input is not valid there is 
              * no point in continuing */
             if (!isValid()) {   
            	 return; 
             } 
             commit(); 
             
             if (newItemMode) { 
            	 newItemMode = false;  
             }
             /*((DBTableContainer) (fTable.getContainerDataSource())).
 	 				addContainerFilter(new Not(new IsNull("DateOfAcceptance"))); */
             setReadOnly(true);
         } else if (source == cancel) {
        	 if (newItemMode) {
        		 newItemMode = false; 
                 setItemDataSource(null); 
             } else {
                 discard();
             }
             setReadOnly(true);
             /*((DBTableContainer) (fTable.getContainerDataSource())).
 	 					addContainerFilter(new Not(new IsNull("DateOfAcceptance"))); */
         } else if (source == edit) {
             setReadOnly(false); 
         } else if (source == newItembtn) { 
        	 addFoundItem(); 
             newItembtn.setVisible(false);  
         }
    }
    
    public Button getAddButton(){ 
    	return newItembtn; 
    }
    
    @Override
     public void setReadOnly(boolean readOnly) {
         super.setReadOnly(readOnly);
         
         save.setVisible(!readOnly);
         cancel.setVisible(!readOnly);
         edit.setVisible(readOnly);
         newItembtn.setVisible(readOnly); 
         ///this field must never be edited - It should come from the database
         if(getField("FoundCode") != null){
        	 getField("FoundCode").setReadOnly(true);  
         } 
     }  
    
    @Override
     public void setItemDataSource(Item newDataSource) { 
    	
    	setReadOnly(false); 
    	newItemMode = false;  
    	
    	if (newDataSource != null) { 
    		
    		final Object[] ORDERED_PROPERTIES = new Object[] {
    				"ItemName", "Description", 
    				"City", "vehicle_id", "StorageLocation", "DateOfAcceptance", 
    				"FoundCode", "Returned"  
    			};
        	 
    		super.setItemDataSource(newDataSource, Arrays.asList (ORDERED_PROPERTIES));  
    		
    		if (newDataSource.getItemProperty("StorageLocation").getValue() != null) {
    			sSelect.select(new RowId(new Object[] { newDataSource
    	                	.getItemProperty("StorageLocation").getValue() })); 
    		} 
    		if (newDataSource.getItemProperty("City").getValue() != null) {
    			cSelect.select(new RowId(new Object[] { newDataSource
	                	.getItemProperty("City").getValue() }));  
    			
    		}  
    		if (newDataSource.getItemProperty("Returned").getValue() != null) {
    			rSelect.select(new RowId(new Object[] { newDataSource
	                	.getItemProperty("Returned").getValue() })); 
    		}  

            setReadOnly(true); 
            getFooter().setVisible(true);
         } else {
             super.setItemDataSource(null);
             getFooter().setVisible(false);
         }
     }
    
    @Override
    public void commit() throws Buffered.SourceException { 
    	//first commit to the actual item
    	super.commit(); 
    	
    	try {
			((DBTableContainer) (fTable.getContainerDataSource())).commit(); 
			
			/*after an item has been succesfully saved to database, 
			 * start the matching/search of item*/
			LostfoundApplication.MatchEngineThread.execute(new Runnable() {
				public void run() { 
					new MatchEngine().run(); 
				}
			}); 
			
		} catch (UnsupportedOperationException e) {
			e.printStackTrace();
		} catch (SQLException e) { 
			e.printStackTrace();
		} catch (RejectedExecutionException e) { //for the MatchEngineThread 
			//getWindow().showNotification(e.getMessage()); 
			System.out.println(e.getMessage()); 
		}
    	
    	setReadOnly(true); 
    }

    
    @Override
    public void discard() throws Buffered.SourceException { 
    	super.discard(); 
    	
    	try {
			((DBTableContainer) (fTable.getContainerDataSource())).rollback();
		} catch (UnsupportedOperationException e) {

			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	
    	//setItemDataSource(null);
    	setReadOnly(true);
    }
    
    
    
    public class ItemFieldFactory extends DefaultFieldFactory { 
		
    	StorageSelect sSelect = FoundItemDetailsForm.this.sSelect; 
    	CitySelect cSelect = FoundItemDetailsForm.this.cSelect; 
    	ReturnedSelect rSelect = FoundItemDetailsForm.this.rSelect; 
    	
    	
		@Override
        public Field createField(Item item, Object propertyId,
                				Component uiContext) { 
			Field f = super.createField(item, propertyId, uiContext); 

			if ("Description".equals(propertyId)) { 
				TextArea ta = new TextArea(); 
				ta.setWordwrap(true); // The default - just making sure ;)
				ta.setCaption("Decription"); 
				ta.setMaxLength(160); 
				ta.setNullRepresentation(""); 
				ta.addValidator(new StringLengthValidator(
                        "Description must be 10 to 160 characters", 10, 160, false)); 
				ta.setRequired(true); 
				ta.setRequiredError("Enter a short description for missing item."); 
				f = ta; 
			} else if ("vehicle_id".equals(propertyId)) {
				TextField tf = new TextField("Train/Bus"); 
				tf.setNullRepresentation(""); 
                tf.addValidator(new StringLengthValidator(
                        "Must be 1 to 10 characters", 1, 25, false)); 
                f = tf; 
			} else if ("ItemName".equals(propertyId)) { 
				TextField tf = new TextField("Item"); 
				tf.setNullRepresentation(""); 
				f = tf; 
                f.setRequired(true);
                f.setRequiredError("Please enter an Item Name e.g. wallet, phone..."); 
                //f.setWidth(COMMON_FIELD_WIDTH);
                f.addValidator(new StringLengthValidator(
                        "Item Name must be 3 to 25 characters", 3, 25, false)); 
			} 
			if ("DateOfAcceptance".equals(propertyId)) { 
				f.setCaption("Date Received"); 
				f.setRequired(true); 
				f.setRequiredError("Date accepted must be entered"); 
			} else if ("StorageLocation".equals(propertyId)) { 
				f = sSelect; 
				f.setRequired(true); 
				f.setRequiredError("Please enter storage location."); 
			} else if ("Returned".equals(propertyId)) { 
				rSelect.setNullSelectionAllowed(false); 
				rSelect.setRequired(true); 
				rSelect.setRequiredError("Has item been returned?");
				f = rSelect; 
			} else if ("City".equals(propertyId)) { 
				cSelect.setNullSelectionAllowed(false); 
				f = cSelect; 
				f.setRequired(true); 
				f.setRequiredError("Select a city."); 
			}
			
			return f; 
		}
	}
}
