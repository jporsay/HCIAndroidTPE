package com.grupo3.productConsult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private int id;
	private List<Category> subCategories;

	public Category(String name, int id) {
		this.name = name;
		this.id = id;
		subCategories = new ArrayList<Category>();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Category> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(Collection<Category> subCategories) {
		this.subCategories.clear();
		this.subCategories.addAll(subCategories);
	}

	@Override
	public String toString() {
		return "[ category: " + name + " subcategories: "
				+ subCategories.toString() + " ]";
	}
}
