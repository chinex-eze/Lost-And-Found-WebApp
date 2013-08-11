package com.example.lostfound;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.lostfound.data.User;
import com.example.lostfound.ui.Footer;
import com.example.lostfound.ui.HorizontalMenu;
import com.example.lostfound.ui.MainWindow;
import com.vaadin.Application; 
import com.vaadin.ui.*; 

public class LostfoundApplication extends Application {
	private static final long serialVersionUID = 1L;
	
	/**
	 * the main window 
	 */
	Window mainWindow; 
	/**
	 * keeps tract of the current user
	 */
	private User currentUser = null; 
	
	/**
	 * sends emails for the app asynchronuously 
	 */
	public static ExecutorService EmailAsyncSender  = Executors.newSingleThreadExecutor(); 
	public static ExecutorService MatchEngineThread = Executors.newSingleThreadExecutor();
	
	@Override
	public void init() {
		mainWindow = new MainWindow("Lost and Found Application", 
							(LostfoundApplication) this);
		setMainWindow(mainWindow); 
		setTheme("lostfoundtheme"); 
		
		setLogoutURL("http://localhost:8080/LostFound/?restartApplication"); 
		
		mainWindow.addComponent(new Footer()); 
	}
	
	public void changePage(Component newPage){
		mainWindow.removeAllComponents(); 
		
		mainWindow.addComponent(new HorizontalMenu(this)); 
		mainWindow.addComponent(newPage); 
		mainWindow.addComponent(new Footer()); 
	}

	public User getCurrentUser(){
		return currentUser;  
	} 
	
	public void setCurrentUser(User user){
		currentUser = user; 
	} 
	
	
	/***
	 * shutdown the ExecutorService (EmailAsyncSender) so that it doesn't 
	 * run forever.
	 */
	@Override 
	protected void finalize(){ 
		try {
			super.finalize(); 
		} catch (Throwable e) {
			e.printStackTrace();
		} 
		finally {
			EmailAsyncSender.shutdown(); 
			MatchEngineThread.shutdown();
		}
	}
}
