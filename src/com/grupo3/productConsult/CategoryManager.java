package com.grupo3.productConsult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.grupo3.productConsult.utilities.PhoneUtils;

public class CategoryManager {
	private static CategoryManager instance = null;
	private List<Category> categoryList;

	public static CategoryManager getInstance() {
		if (instance == null) {
			instance = new CategoryManager();
		}
		return instance;
	}

	private CategoryManager() {
		categoryList = new ArrayList<Category>();
	}

	public String[] getCategoryNames() {
		return getNames(categoryList);
	}

	public String[] getSubCategoryNames(int index) {
		Category c = categoryList.get(index);
		return getNames(c.getSubCategories());
	}

	private String[] getNames(List<Category> categories) {
		if (categories == null) {
			return new String[] {};
		}
		String[] categoryNames = new String[categories.size()];
		int i = 0;
		for (Category subCat : categories) {
			categoryNames[i++] = subCat.getName();
		}
		return categoryNames;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public void saveCategoryList(Collection<Category> categories) {
		this.categoryList.clear();
		this.categoryList.addAll(categories);
	}

	public void saveSubCategories(int index, List<Category> subCategories) {
		categoryList.get(index).setSubCategories(subCategories);
	}

	public boolean categoriesLoaded() {
		return !this.categoryList.isEmpty()
				&& categoryList.get(0).getLocale().equals(
						PhoneUtils.getLanguageId());
	}

	public boolean subCategoryLoaded(int index) {
		Category c = this.categoryList.get(index);
		return c != null && !c.getSubCategories().isEmpty()
				&& c.getLocale().equals(PhoneUtils.getLanguageId());
	}
}