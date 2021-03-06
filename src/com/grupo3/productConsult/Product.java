package com.grupo3.productConsult;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	public final static String CURRENCY = "$";

	private int id;
	private String imgSrc;
	private String name;
	private double price;
	private float salesRank;
	private Map<String, String> properties;
	private int categoryId;

	public Product(int id, String name, double price) {
		properties = new HashMap<String, String>();
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public double getPrice() {
		return price;
	}

	public String getImgSrc() {
		return imgSrc;
	}

	public int getId() {
		return id;
	}

	public void setInformation(String key, String value) {
		properties.put(key, value);
	}

	public String getProperty(String key) {
		return properties.get(key);
	}

	public void setSaleRank(float salesRank) {
		this.salesRank = salesRank;
	}

	public float getSalesRank() {
		return salesRank;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	@Override
	public String toString() {
		return name + " -- Prop: " + properties.toString();
	}
}
