package com.example.lostfound.data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SearchFilter implements Serializable {

	private final String term;
    private final Object propertyId; 
    private String propertyIdDisplayName; 
     
	public SearchFilter(Object propertyId, String term){
		this.propertyId = propertyId; 
		this.term = term; 
	} 
	
	public SearchFilter(Object propertyId, String term, String propertyIdDisplayName){
		this(propertyId, term); 
		setPropertyIdDisplayName(propertyIdDisplayName); 
	} 
	
	public String getPropertyIdDisplayName() {
		return propertyIdDisplayName;
	}

	public void setPropertyIdDisplayName(String propertyIdDisplayName) {
		this.propertyIdDisplayName = propertyIdDisplayName;
	}

	public String getTerm() {
		return term;
	}

	public Object getPropertyId() {
		return propertyId;
	}
} 
