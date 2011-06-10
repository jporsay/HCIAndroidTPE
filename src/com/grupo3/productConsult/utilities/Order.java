package com.grupo3.productConsult.utilities;

public class Order {
	private String id;
	private String latitude;
	private String longitude;
	private String status;
	
	public Order() {
	}
	
	public String getId() {
		return id;
	}
	
	public String getLatitude() {
		return latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
}
