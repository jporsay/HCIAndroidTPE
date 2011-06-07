package com.grupo3.productConsult.services;

import java.util.LinkedList;
import java.util.List;

import com.grupo3.productConsult.Category;

public class CategoriesSearchService {

	// FIXME: No hace falta explicar que es lo que hay que arreglar...
	public static List<Category> fetchCategories() {
		List<Category> categories = new LinkedList<Category>();
		categories.add(new Category("Books", 0));
		categories.add(new Category("Movies", 1));
		return categories;
	}
}
