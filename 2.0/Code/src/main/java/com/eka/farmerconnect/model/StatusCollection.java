package com.eka.farmerconnect.model;

import java.util.List;

public class StatusCollection {


	private String collectionName;
	private List <Object> collectionData;
	private String format;

	public String getCollectionName() {
		return collectionName;
	}
	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public List<Object> getCollectionData() {
		return collectionData;
	}
	public void setCollectionData(List<Object> collectionData) {
		this.collectionData = collectionData;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}




}
