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

	// private int categoryId;
	// private int subCategoryId;

	public Product(int id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imgSrc = "http://www.moviewallpaper.net/wpp/David_Hasselhoff_in_Baywatch_Wallpaper_1_1024.jpg";
		properties = new HashMap<String, String>();
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

	@Override
	public String toString() {
		return name + " -- Prop: " + properties.toString();
	}
}
