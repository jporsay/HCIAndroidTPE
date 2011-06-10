package com.grupo3.productConsult.activities;

import java.io.Serializable;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.grupo3.productConsult.Category;
import com.grupo3.productConsult.CategoryManager;
import com.grupo3.productConsult.R;
import com.grupo3.productConsult.services.CategoriesSearchService;

public class SubCategoryListActivity extends ListActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle recdData = getIntent().getExtras();
		int catPos = Integer.parseInt(recdData.getString("categoryPos"));

		CategoryManager catManager = CategoryManager.getInstance();
		setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,
				catManager.getSubCategoryNames(catPos)));

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CharSequence text = ((TextView) view).getText();
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
				loadProductList(position);
				/*
				 * Intent newIntent = new Intent(SubCategoryListActivity.this
				 * .getApplicationContext(), ProductListActivity.class); Bundle
				 * bundle = new Bundle(); bundle.putString("categoryPos", catPos
				 * + ""); bundle.putString("subCategoryPos", position + "");
				 * newIntent.putExtras(bundle);
				 * startActivityForResult(newIntent, 0);
				 */
			}
		});
	}

	private void loadProductList(final int subCatIndex) {
		Bundle data = getIntent().getExtras();
		final int catIndex = Integer.parseInt(data.getString("categoryPos"));
		Intent intent = new Intent(Intent.ACTION_SYNC, null, this,
				CategoriesSearchService.class);
		intent.putExtra("command",
				CategoriesSearchService.LOAD_PRODUCTS_BY_SUBCATEGORY);
		Category category = CategoryManager.getInstance().getCategoryList()
				.get(catIndex);
		int catId = category.getId();
		int subCatId = category.getSubCategories().get(subCatIndex).getId();
		intent.putExtra("categoryId", catId + "");
		intent.putExtra("subCategoryId", subCatId + "");
		intent.putExtra("receiver", new ResultReceiver(new Handler()) {
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				switch (resultCode) {
				case CategoriesSearchService.STATUS_SUCCESS:
					Serializable productList = resultData
							.getSerializable("products");
					Intent intent = new Intent(SubCategoryListActivity.this,
							ProductListActivity.class);
					Bundle b = new Bundle();
					b.putSerializable("products", productList);
					intent.putExtras(b);
					startActivity(intent);
					break;
				case CategoriesSearchService.STATUS_ERROR:
					break;
				}
			}
		});
		startService(intent);
	}
}
