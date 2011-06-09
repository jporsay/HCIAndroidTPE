package com.grupo3.productConsult.services;

import java.util.LinkedList;
import java.util.List;

import com.grupo3.productConsult.Category;

public class CategoriesSearchService {
	// FIXME: Creo que no hace falta explicar que es lo que hay que arreglar...
	private final static int BOOKS_ID = 10;
	private final static int MOVIES_ID = 20;

	public static List<Category> fetchCategories() {
		List<Category> categories = new LinkedList<Category>();
		categories.add(new Category("Books", BOOKS_ID));
		categories.add(new Category("Movies", MOVIES_ID));
		return categories;
	}

	public static List<Category> fetchSubCategories(int categoryId) {
		List<Category> subCategories = new LinkedList<Category>();
		if (categoryId == BOOKS_ID) {
			subCategories.add(new Category("Action", 1));
			subCategories.add(new Category("Comedy", 2));
			subCategories.add(new Category("Science Fiction", 3));
		} else if (categoryId == MOVIES_ID) {
			subCategories.add(new Category("Biographies", 1));
			subCategories.add(new Category("Children", 2));
			subCategories.add(new Category("History", 3));
			subCategories.add(new Category("Mystery", 4));
		}
		return subCategories;
	}
}
