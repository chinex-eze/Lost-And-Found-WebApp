package com.example.lostfound.ui;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.RejectedExecutionException;

import matcher.MatchEngine;
import message.EmailMessenger;

import com.example.lostfound.LostfoundApplication;
import com.example.lostfound.database.AppTableContainers;
import com.vaadin.data.Item;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class ReportLostItemDisplay extends HorizontalLayout implements Observer {

	ReportLostItem lost;
	ClientForm client;

	public ReportLostItemDisplay() {
		setSpacing(true);
		setMargin(true);

		lost = new ReportLostItem();
		client = new ClientForm();
		lost.addObserver((Observer) this);
		client.addObserver((Observer) this);
		addComponent(lost);
	}

	@Override
	public void update(Observable o, Object arg) {

		if ("ITEM_CANCEL".equals(arg)) {
			removeComponent(client);
		} else if ("ITEM_PROCEED".equals(arg)) {
			addComponent(client);
		} else if ("CLIENT_PROCEED".equals(arg)) {
			RowId temp = lost.saveItem();
			RowId temp1 = client.saveItem(temp);

			final Item item = AppTableContainers.getClientTable()
					.getItembyProperty("id", temp1.toString());

			final EmailMessenger messenger = new EmailMessenger();
			messenger.setName(item.getItemProperty("FirstNames").toString());
			messenger.setToAddress(item.getItemProperty("Email").toString());
			messenger.setMessage(EmailMessenger.getConfirmationMessage(item
					.getItemProperty("FirstNames").toString()));
			try {
				// /send a confirmation email
				LostfoundApplication.EmailAsyncSender.execute(new Runnable() {
					public void run() {
						messenger.sendMessage();
					}// run
				});
				// /start the matching/search of item
				LostfoundApplication.MatchEngineThread.execute(new Runnable() {
					public void run() {
						new MatchEngine().run();  
					}
				});
			} catch (RejectedExecutionException e) {
				getWindow().showNotification(e.getMessage());
			}
			removeComponent(client);
			removeComponent(lost);
			addComponent(new ConfirmationPage(item.getItemProperty("Email")
					.toString()));
		}
	}
}
