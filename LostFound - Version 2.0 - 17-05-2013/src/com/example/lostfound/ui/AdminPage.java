package com.example.lostfound.ui;

import com.example.lostfound.data.User;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Form;

@SuppressWarnings("serial")
public class AdminPage extends Form {
	
	AdminPage(User user){ 
		setItemDataSource(new BeanItem<User>(user)); 
		setVisibleItemProperties(new Object[] {"firstName", "userName", "level"}); 
		setReadOnly(true); 
	}
}
