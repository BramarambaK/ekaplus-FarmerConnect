package com.eka.farmerconnect.util;

public class FieldInfo {
	
	private FieldType fieldType;
	private String name;
	
	public FieldInfo(FieldType fieldType, String name) {
		super();
		this.fieldType = fieldType;
		this.name = name;
	}
	
	public FieldType getFieldType() {
		return fieldType;
	}
	public void setFieldType(FieldType fieldType) {
		this.fieldType = fieldType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}