package com.grupo3.productConsult.utilities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.grupo3.productConsult.R;

@SuppressWarnings("serial")
public class Order implements Serializable {
	private String id;
	private String latitude;
	private String longitude;
	private String status;
	private static Context context;
	private Map<String, String> statusNames;
	private String createdDate;

	public Order(Context context) {
		Order.context = context;
		setStringsNames();
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

	public String getStatusName() {
		return statusNames.get(getStatus());
	}

	public String getShippedDate() {
		return createdDate;
	}

	private void setStringsNames() {
		statusNames = new HashMap<String, String>();
		statusNames.put("1", context.getString(R.string.created));
		statusNames.put("2", context.getString(R.string.confirmed));
		statusNames.put("3", context.getString(R.string.transported));
		statusNames.put("4", context.getString(R.string.delivered));
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
}
