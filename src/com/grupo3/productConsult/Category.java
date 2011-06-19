package com.grupo3.productConsult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.grupo3.productConsult.utilities.PhoneUtils;

public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private int id;
	private List<Category> subCategories;
	private String locale;

	public Category(String name, int id) {
		this.name = name;
		this.id = id;
		subCategories = new ArrayList<Category>();
		locale = PhoneUtils.getLanguageId();
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

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	@Override
	public String toString() {
		return "[ category: " + name + " subcategories: "
				+ subCategories.toString() + " ]";
	}
}
