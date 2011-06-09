package com.grupo3.productConsult;

public class Product {
	public final static String CURRENCY = "$";

	private int id;
	private String imgSrc;
	private String name;
	private double price;

	// private float sales_rank;
	// private int categoryId;
	// private int subCategoryId;

	public Product(int id, String name, double price) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.imgSrc = "poner la ruta a la imagen...";
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
}
