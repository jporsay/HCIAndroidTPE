package com.grupo3.productConsult.activities;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.grupo3.productConsult.Category;
import com.grupo3.productConsult.CategoryManager;
import com.grupo3.productConsult.Product;
import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.CategoriesSearchService;

public class ProductListView extends ListActivity {

	private List<Product> currList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] products = getProductNames();
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				products));
	}

	private String[] getProductNames() {
		Bundle recdData = getIntent().getExtras();
		int catPos = Integer.parseInt(recdData.getString("categoryPos"));
		int subCatPos = Integer.parseInt(recdData.getString("categoryPos"));

		CategoryManager catManager = CategoryManager.getInstance();
		Category slectedCat = catManager.getCategoryList().get(catPos);
		int catId = slectedCat.getId();
		int subCatId = slectedCat.getSubCategories().get(subCatPos).getId();
		currList = CategoriesSearchService.fetchProductsBySubcategory(catId,
				subCatId);
		String[] names = new String[currList.size()];
		int i = 0;
		for (Product c : currList) {
			names[i++] = c.getName() + "  " + Product.CURRENCY + " "
					+ c.getPrice();
		}
		return names;
	}
}
