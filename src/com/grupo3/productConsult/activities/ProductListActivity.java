package com.grupo3.productConsult.activities;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.grupo3.productConsult.Category;
import com.grupo3.productConsult.CategoryManager;
import com.grupo3.productConsult.Product;
import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.CategoriesSearchService;

public class ProductListActivity extends ListActivity {

	private List<Product> currList;

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle recdData = getIntent().getExtras();
		currList = (List<Product>) recdData.getSerializable("products");

		String[] products = getProductNames();
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				products));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent newIntent = new Intent(ProductListActivity.this
						.getApplicationContext(), ProductDisplayActivity.class);

				Bundle bundle = new Bundle();
				bundle.putString("productId", currList.get(position).getId()
						+ "");
				newIntent.putExtras(bundle);
				startActivityForResult(newIntent, 0);
			}
		});
	}

	private String[] getProductNames() {
		String[] names = new String[currList.size()];
		int i = 0;
		for (Product c : currList) {
			names[i++] = c.getName() + "  " + Product.CURRENCY + " "
					+ c.getPrice();
		}
		return names;
	}
}
