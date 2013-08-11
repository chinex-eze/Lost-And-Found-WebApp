package com.example.lostfound.ui; 

import java.sql.SQLException;
import java.util.Observer;

import com.example.lostfound.data.User;
import com.example.lostfound.database.DBFreeformContainer;
import com.example.lostfound.database.LFDatabase;
import com.vaadin.ui.LoginForm;
import com.vaadin.ui.LoginForm.LoginEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial") 
public class AdminLoginForm extends VerticalLayout { 
	
	private ReportObservable obs = new ReportObservable(); 
	DBFreeformContainer userData = null;  
	
	public AdminLoginForm() {  
		Panel loginPanel = new Panel("Login");
		//loginPanel.setWidth("250px"); 

		
		LoginForm login = new LoginForm(); 
		login.setSizeFull();  
		
		loginPanel.addComponent(login); 
        
        login.addListener(new LoginForm.LoginListener() {
            public void onLogin(LoginEvent event) { 
            	String query = prepareQuery(event.getLoginParameter("username"), 
            					event.getLoginParameter("password")); 
            	
            	try {
					userData = new DBFreeformContainer(query, 
													"id", 
													LFDatabase.GetLFDatabasePool());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				if(userData.size() == 1){
					getWindow().showNotification("Login Successful. " + userData.size()); 
					obs.report(initUser(userData)); 
				} else { 
					getWindow().showNotification("Login failed. Invalid credentials." + userData.size()); 
				}
            }
        }); 
        
        addComponent(loginPanel);  
	} 
	
	public String prepareQuery(String username, String pword){ 
		
		return "Select * from tbl_admin_user where " +
				"UserName='" + username + "' AND " +
				"Password='" + pword + "'"; 
	} 
	
	public User initUser(DBFreeformContainer data){ 
		User user = new User(); 
		Object id = userData.getIdByIndex(0); 
		String f = userData.getItem(id).getItemProperty("FirstNames").getValue().toString(); 
		String u = userData.getItem(id).getItemProperty("UserName").getValue().toString(); 
		String l = userData.getItem(id).getItemProperty("AdminLevel").getValue().toString(); 
		user.setFirstName(f); 
		user.setUserName(u); 
		user.setLevel(l); 
		 
		return user; 
	} 
	
	public void addObserver(Observer o){
		obs.addObserver(o); 
	} 
}
