package com.grupo3.productConsult;

import java.util.ArrayList;
import java.util.List;

public class Category {
	private String name;
	private int id;
	private List<String> subCategories;

	public Category(String name, int id) {
		this.name = name;
		this.id = id;
		subCategories = new ArrayList<String>();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<String> getSubCategories() {
		return subCategories;
	}
}
