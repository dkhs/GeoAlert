package com.dkhs.geoalert.allert;

import java.io.Serializable;

public class Alarm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2195779385348876264L;
	
	private double latitude;
	private double longtitude;
	private String title;
	private String text;
	private int radius;
	private int uniqueID;
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(double longtitude) {
		this.longtitude = longtitude;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getUniqueID() {
		return uniqueID;
	}
	public void setUniqueID(int uniqueID) {
		this.uniqueID = uniqueID;
	}
	
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Alarm){
			if(((Alarm)obj).uniqueID == this.uniqueID) return true;
		}
		return false;
	}
	
	
}
