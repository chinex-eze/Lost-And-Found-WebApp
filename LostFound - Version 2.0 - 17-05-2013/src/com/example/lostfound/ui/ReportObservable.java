package com.example.lostfound.ui;

import java.io.Serializable;
import java.util.Observable;

///implements Serializable in order to prevent NotSerializableException 
@SuppressWarnings("serial")
public class ReportObservable extends Observable implements Serializable{ 
	
	public void report(Object arg){
		setChanged(); 
		notifyObservers(arg); 
	}
}
