package com.szrjk.entity;

import java.io.Serializable;

public class CircleType implements Serializable{
	private String propertyType;
	private String propertyId;
	
	public CircleType() {
		super();
	}
	public CircleType(String propertyType, String propertyId) {
		super();
		this.propertyType = propertyType;
		this.propertyId = propertyId;
	}
	public String getPropertyType() {
		return propertyType;
	}
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public String getPropertyId() {
		return propertyId;
	}
	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}
	@Override
	public String toString() {
		return "Property [propertyType=" + propertyType + ", propertyId="
				+ propertyId + "]";
	}

}
