package com.grupo3.productConsult;

import java.util.List;

import com.grupo3.productConsult.services.CategoriesSearchService;

public class CategoryManager {
	private static CategoryManager instance = null;
	private List<Category> categoryList = null;

	public static CategoryManager getInstance() {
		if (instance == null) {
			instance = new CategoryManager();
		}
		return instance;
	}

	private CategoryManager() {
	}

	public String[] getCategories() {
		if (categoryList == null) {
			categoryList = CategoriesSearchService.fetchCategories();
		}
		String[] categoryNames = new String[categoryList.size()];
		int i = 0;
		for (Category c : categoryList) {
			categoryNames[i++] = c.getName();
		}
		return categoryNames;
	}

}
