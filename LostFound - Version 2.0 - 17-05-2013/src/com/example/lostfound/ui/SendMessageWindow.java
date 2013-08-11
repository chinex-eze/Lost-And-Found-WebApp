package com.example.lostfound.ui;

import java.util.concurrent.RejectedExecutionException;

import message.EmailMessenger;

import com.example.lostfound.LostfoundApplication;
import com.example.lostfound.customUi.CitySelect;
import com.example.lostfound.database.AppTableContainers;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickListener;

@SuppressWarnings("serial")
public class SendMessageWindow extends Window implements ClickListener {

	private Button send = new Button("Send", (ClickListener) this);
	private Button cancel = new Button("Cancel", (ClickListener) this);
	private Label lblMessage;
	private TextField fCode = new TextField("Item Found Code");
	private CitySelect cSelect = new CitySelect();

	private String errMessage = "";
	private String name = "";
	private String address = "";

	public SendMessageWindow(String n, String addr) {
		name = n;
		address = addr;

		VerticalLayout layout = (VerticalLayout) this.getContent();
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();

		setWidth("350px");
		setModal(true);

		lblMessage = new Label("Message: Item found message to: " + address);

		fCode.setRequired(true);
		fCode.setRequiredError("Please enter a found code for item.");

		cSelect.setRequired(true);
		cSelect.setRequiredError("Please choose a city.");
		cSelect.setNullSelectionAllowed(false);

		HorizontalLayout buttons = new HorizontalLayout();
		buttons.addComponent(send);
		buttons.addComponent(cancel);

		addComponent(lblMessage);
		addComponent(fCode);
		addComponent(cSelect);
		addComponent(buttons);
	}

	@Override
	public void buttonClick(ClickEvent event) {
		Button source = event.getButton();

		if (source == send) {
			if (isValid()) {
				sendMessage();
				getWindow().showNotification("Message sent.");
				getParent().removeWindow(this);
			} else {
				getWindow().showNotification(errMessage);
			}
		} else if (source == cancel) {
			getParent().removeWindow(this);
		}
	}

	private boolean isValid() {
		boolean bValid = false;

		if (fCode.isValid()) {
			if (cSelect.isValid()) {
				bValid = true;
			} else {
				errMessage = cSelect.getRequiredError();
			}
		} else {
			errMessage = fCode.getRequiredError();
		}

		return bValid;
	}// /is valid

	private void sendMessage() {
		final EmailMessenger messenger = new EmailMessenger();

		Item tmp = AppTableContainers.getCityTable().getItembyProperty("id",
				cSelect.getValue().toString());
		final String city = tmp.getItemProperty("Description").toString();
		// /send the email in the background
		try {
			LostfoundApplication.EmailAsyncSender.execute(new Runnable() {
				public void run() {
					messenger.sendMessage(name, address, EmailMessenger
							.getFoundMessage(name, city, fCode.getValue()
									.toString()));
				}// run
			});
		} catch (RejectedExecutionException e) {
			getWindow().showNotification(e.getMessage());
		}
	}
}
