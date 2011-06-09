package com.grupo3.productConsult;

import java.util.List;

import com.grupo3.productConsult.services.CategoriesSearchService;

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
		categoryList = CategoriesSearchService.fetchCategories();
	}

	public String[] getCategoryNames() {
		return getNames(categoryList);
	}

	public String[] getSubCategoryNames(int index) {
		Category c = categoryList.get(index);
		if (c.getSubCategories().isEmpty()) {
			int id = c.getId();
			c.setSubCategories(CategoriesSearchService.fetchSubCategories(id));
		}
		return getNames(c.getSubCategories());
	}

	private String[] getNames(List<Category> categories) {
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

}