package com.grupo3.productConsult.utilities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.app.Application;

import com.grupo3.productConsult.R;

@SuppressWarnings("serial")
public class Order implements Serializable {
	private String id;
	private String latitude;
	private String longitude;
	private String status;

	private Map<String, String> statusNames;

	public Order() {
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

	private void setStringsNames() {
		Application a = new Application();
		// Hack to getString without being Activity
		statusNames = new HashMap<String, String>();
		statusNames.put("1", a.getString(R.string.created));
		statusNames.put("2", a.getString(R.string.confirmed));
		statusNames.put("3", a.getString(R.string.transported));
		statusNames.put("4", a.getString(R.string.delivered));
	}
}
