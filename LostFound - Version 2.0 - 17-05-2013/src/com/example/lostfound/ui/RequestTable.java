package com.example.lostfound.ui;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import com.example.lostfound.customUi.CitySelect;
import com.example.lostfound.database.AppTableContainers;
import com.example.lostfound.database.ClientTableContainer;
import com.example.lostfound.database.DBTableContainer;
import com.example.lostfound.database.ItemTableContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class RequestTable extends Table {

	private Panel detailsPanel = new Panel("Request Details");
	// panel to display details of item that match this request
	private Panel matchPanel = new Panel("Match Details");

	private RequestDetailsForm detailsForm = new RequestDetailsForm();
	private ClientDetailsForm clientDetails = new ClientDetailsForm(); 
	private MatchDetailsForm matchDetailsForm = new MatchDetailsForm(); 

	private Button btnMessage = new Button("Send Message",
			(ClickListener) clientDetails); 
	
	public RequestTable(DBTableContainer container) {
		final Object[] NATURAL_COL_ORDER = new Object[] { "item_name",
				"vehicle_id", "date_of_loss", "city", "matched" };
		final String[] COL_HEADERS_ENGLISH = new String[] { "Item", 
				"Train/Bus", "Date Lost", "City", "Matched" };

		setSizeFull();
		setSelectable(true);
		setImmediate(true);
		setNullSelectionAllowed(false);

		// sort the items in order of date reported
		container.sort(new Object[] { "date_created" }, new boolean[] { true });

		setContainerDataSource(container);

		setVisibleColumns(NATURAL_COL_ORDER);
		setColumnHeaders(COL_HEADERS_ENGLISH);

		setPageLength(15);

		detailsPanel.addComponent(detailsForm);
		detailsPanel.addComponent(clientDetails);
		detailsPanel.addComponent(btnMessage); 
		
		matchPanel.addComponent(matchDetailsForm); 
		matchPanel.addComponent(new Button("Dismiss", (ClickListener) matchDetailsForm)); 

		addListener(new RequestTableListener());
		addGeneratedColumns();
	}

	public Panel getDetailPanel() {
		return detailsPanel;
	}

	public Panel getMatchPanel() {
		return matchPanel;
	}

	@Override
	protected String formatPropertyValue(Object rowId, Object colId,
			Property property) {

		if (property.getType() == java.sql.Date.class
				&& property.getValue() != null) {
			SimpleDateFormat df = new SimpleDateFormat("MMM d, yyyy");
			return df.format((java.sql.Date) property.getValue());
		}

		return super.formatPropertyValue(rowId, colId, property);
	}

	private void addGeneratedColumns() {
		addGeneratedColumn("city", new ColumnGenerator() {
			public Component generateCell(Table source, Object itemId,
					Object columnId) {

				if (getItem(itemId).getItemProperty("city").getValue() != null) {
					Label l = new Label();
					int cityId = (Integer) getItem(itemId).getItemProperty(
							"city").getValue();

					l.setValue(AppTableContainers
							.getCityTable()
							.getColumnStringValuebyId(cityId - 1, "Description")); // /1
																					// is
																					// subtracted
																					// cos
																					// otherwise
																					// /it
																					// returns
																					// next(wrong)
																					// value
					l.setSizeUndefined();
					return l;
				}
				return null;
			}
		});
	}

	private class RequestTableListener implements Property.ValueChangeListener {

		ClientTableContainer clientCon = AppTableContainers.getClientTable(); 
		ItemTableContainer itemCon = AppTableContainers.getItemTable(); 

		@Override
		public void valueChange(Property.ValueChangeEvent event) {
			Property property = event.getProperty();
			Table temp = (Table) RequestTable.this;
			if (property == temp) {
				Item item = temp.getItem(temp.getValue()); 
				
				if (item != detailsForm.getItemDataSource()) {
					detailsForm.setItemDataSource(item);

					///for client details form
					Item cItem = clientCon.getItembyProperty("request", item
							.getItemProperty("id").toString());

					if (cItem != clientDetails.getItemDataSource()) {
						clientDetails.setItemDataSource(cItem);
					} 
					
					///for match details form 
					String matchId = (item.getItemProperty("matched")).toString(); 
					if(matchId != null){ 
						Item matchItem = itemCon.getItembyProperty("id", matchId); 
						matchDetailsForm.setItemDataSource(matchItem);
					} else { matchDetailsForm.setItemDataSource(null); } 
				}
			}
		} //valuechange 

	}// /RequestTableListener

	private class RequestDetailsForm extends Form {

		CitySelect cSelect = new CitySelect();

		public RequestDetailsForm() {
			setReadOnly(true);
			setWriteThrough(false);

			setFormFieldFactory(new RequestFieldFactory());
		}

		@Override
		public void setItemDataSource(Item newDataSource) {
			setReadOnly(false);

			if (newDataSource != null) {
				final Object[] ORDERED_PROPERTIES = new Object[] { "item_name",
						"description", "date_of_loss", "vehicle_id", "city" };

				super.setItemDataSource(newDataSource,
						Arrays.asList(ORDERED_PROPERTIES));

				if (newDataSource.getItemProperty("city").getValue() != null) {
					cSelect.select(new RowId(new Object[] { newDataSource
							.getItemProperty("city").getValue() }));
				}

			} else {
				super.setItemDataSource(null);
			}
			setReadOnly(true);
		}

		private class RequestFieldFactory extends DefaultFieldFactory {

			CitySelect cSelect = RequestDetailsForm.this.cSelect;

			@Override
			public Field createField(Item item, Object propertyId,
					Component uiContext) {

				Field f = super.createField(item, propertyId, uiContext);

				if ("description".equals(propertyId)) {
					TextArea ta = new TextArea();
					ta.setWordwrap(true); // The default - just making sure ;)
					ta.setCaption("Decription");
					f = ta;
				} else if ("vehicle_id".equals(propertyId)) {
					f.setCaption("Train/Bus");
					((AbstractTextField) f).setNullRepresentation("");
				} else if ("item_name".equals(propertyId)) {
					f.setCaption("Item");
				} else if ("date_of_loss".equals(propertyId)) {
					f.setCaption("Date of Loss");
				} else if ("city".equals(propertyId)) {
					cSelect.setNullSelectionAllowed(false);
					f = cSelect;
				}

				return f;
			}
		}
	}

	private class ClientDetailsForm extends Form implements ClickListener {

		public ClientDetailsForm() {
			setReadOnly(true);
			setWriteThrough(false);
			setCaption("Client Details");
			setFormFieldFactory(new ClientForm.ClientFieldFactory());
		}

		@Override
		public void setItemDataSource(Item newDataSource) {
			setReadOnly(false);

			if (newDataSource != null) {
				final Object[] ORDERED_PROPERTIES = new Object[] {
						"FirstNames", "LastName", "Email", "PhoneNumber" };

				super.setItemDataSource(newDataSource,
						Arrays.asList(ORDERED_PROPERTIES));
			} else {
				super.setItemDataSource(null);
			}

			setReadOnly(true);
		}

		@Override
		public void buttonClick(ClickEvent event) {
			Button source = event.getButton();

			if (source == btnMessage) {
				if (getItemDataSource() != null) {
					String name = getItemProperty("FirstNames").getValue()
							.toString()
							+ " "
							+ getItemProperty("LastName").getValue().toString();
					String email = getItemProperty("Email").getValue()
							.toString();

					getWindow().addWindow(new SendMessageWindow(name, email));
				}
			} 
		}
	} 
	
	
	private class MatchDetailsForm extends Form implements ClickListener{
		
		public MatchDetailsForm(){
			setReadOnly(true); 
			setWriteThrough(false); 
			setFormFieldFactory(new ItemFieldFactory()); 
		}
		
		
		@Override
		public void setItemDataSource(Item newDataSource) {
			setReadOnly(false);

			if (newDataSource != null) {
				final Object[] ORDERED_PROPERTIES = new Object[] {
						"ItemName", "Description", "vehicle_id", 
						"DateOfAcceptance", "FoundCode" }; 

				super.setItemDataSource(newDataSource,
						Arrays.asList(ORDERED_PROPERTIES));
			} else {
				super.setItemDataSource(null);
			}

			setReadOnly(true);
		} 
		
		
		public class ItemFieldFactory extends DefaultFieldFactory { 
			
			@Override
	        public Field createField(Item item, Object propertyId,
	                				Component uiContext) { 
				Field f = super.createField(item, propertyId, uiContext); 
				
				if ("Description".equals(propertyId)) { 
					TextArea ta = new TextArea(); 
					ta.setWordwrap(true); 
					ta.setCaption("Decription"); 
					ta.setNullRepresentation(""); 
					f = ta; 
				} else if ("vehicle_id".equals(propertyId)) {
					TextField tf = new TextField("Train/Bus"); 
					tf.setNullRepresentation(""); 
					f = tf; 
				} else if ("ItemName".equals(propertyId)) { 
					TextField tf = new TextField("Item"); 
					tf.setNullRepresentation(""); 
					f = tf; 
				}
				if ("DateOfAcceptance".equals(propertyId)) { 
					f.setCaption("Date Received"); 
				} 
				
				return f; 
			}
		}


		@Override
		public void buttonClick(ClickEvent event) {
			if(getItemDataSource() != null){
				Object rowId = RequestTable.this.getValue(); 
				RequestTable.this.getContainerProperty(rowId, "matched").setValue(null);  
			}
		}
	}
}
